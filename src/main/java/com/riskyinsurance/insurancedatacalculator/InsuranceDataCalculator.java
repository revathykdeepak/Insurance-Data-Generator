package com.riskyinsurance.insurancedatacalculator;

import static com.riskyinsurance.insurancedatacalculator.Utils.addValuesWithScale;
import static com.riskyinsurance.insurancedatacalculator.Utils.durationToSecs;
import static com.riskyinsurance.insurancedatacalculator.Utils.getDistance;
import static com.riskyinsurance.insurancedatacalculator.Utils.getDistanceUAcc;
import static com.riskyinsurance.insurancedatacalculator.Utils.getSpeedingDuration;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.riskyinsurance.insurancedatacalculator.Utils.CalcMethod;

public class InsuranceDataCalculator {
	
	static Logger logger = Logger.getLogger(InsuranceDataCalculator.class.getName());
	

	//save default value for waypoint JSON file
	private static final String DEFAULT_SOURCE_PATH  = "../resourcepaths/waypoints.json"; 
	//save default value for destination JSON file
	private static final String DEFAULT_DEST_PATH  = "../resourcepaths/result.json"; 
	//store calculation method to be used in distance calculation
	public CalcMethod calcMethod = CalcMethod.U_ACC;
	//store number of invalid waypoints in data
    private long droppedWaypoints = 0;
    
	public InsuranceDataCalculator(CalcMethod calcMethod) {
		super();
		this.calcMethod = calcMethod;
	}
	
  /**
    * Gets insurance data from waypoint JSON file and save results in a JSON file
    *
    * @param  waypointJsonPath   JSON file containing waypoint data
    * @param  insuranceDataPath   Path to JSON in which insurance data is to be stored
    * @return true for success and false for failure
    */	
	public boolean saveInsuranceDataFromWaypoint (Path waypointJsonPath , 
		Path insuranceDataPath) {
		InsuranceData insuranceData = getInsuranceDataFromWaypoint(waypointJsonPath);
		writeDataToJsonPath(insuranceData, insuranceDataPath);
		return true;
	}

   /**
    * Gets insurance data from waypoint JSON file
    *
    * @param  waypointJsonPath   JSON file containing waypoint data
    * @return insurance data calculated from waypoints
    */		
	public InsuranceData getInsuranceDataFromWaypoint (Path waypointJsonPath) {
		InsuranceData insuranceData;
		ArrayList<Waypoint> waypointList;
		waypointList = getWaypointListFromPath(waypointJsonPath);
		insuranceData = getInsuranceDataFromWaypoint(waypointList);
		return insuranceData;
	}
	
   /**
    * Checks whether values in given waypoint is valid
    * 
    * Current implementation checks:
    *  -90 <= latitude <= 90
    *  -180 <= longitude <= 180
    *  0 <= speed <= 123 m/s (400 kmph)
    *  0 <= speed_limit <= 123 m/s 
    *
    * @param  wp   JWaypoint to be tested
    * @return  boolean representing whether waypoint is valid or not
    */	
	
	boolean isValidWaypoint(Waypoint wp) {
		boolean flag = true;
		if (wp.getPosition().getLatitude() < -90.0 ||
				wp.getPosition().getLatitude() > 90.0	) {
			flag = false;
		}
		if (wp.getPosition().getLongitude() < -180.0 ||
				wp.getPosition().getLongitude() > 180.0	) {
			flag = false;
		}
		if(wp.getSpeed() < 0.0 || wp.getSpeed() > 123.0) {
			flag = false;
		}
		if(wp.getSpeed_limit() < 0.0 || wp.getSpeed_limit() > 123.0) {
			flag = false;
		}
		return flag;
	}
	
   /**
    * Gets next valid waypoint in the given waypoint list iterator
    *  and drops invalid waypoints in between. Number of dropped values
    *  is being kept track using class variable "droppedWaypoints"

    * @param  iterator   pointing to current waypoint being analyzed
    * @return next valid waypoint or null with iterator modified to point towards this value
    */	
	Waypoint getnextValidWaypoint ( Iterator<Waypoint> iterator) {
		Waypoint validWp = null, curWp; 
		while (iterator.hasNext()) {
			curWp = iterator.next();
			if( isValidWaypoint(curWp)){
				return curWp;
			}
			else {
				droppedWaypoints ++;
				iterator.remove();
			}
		}
		return validWp;
		
	}
	
  /**
    * Gets insurance data from waypoint list
    *  
    *  Description: divides trip into different intervals which each interval
    *  representing the time between two consecutive valid waypoints. For each interval, 
    *  duration, distance, speeding duration , speeding distance are calculated.
    *  These values are added to get final insurance data.
    *  
	*
    * @param  waypointList   list of waypoints
    * @return insurance data calculated from waypoints
    */		
	public InsuranceData getInsuranceDataFromWaypoint(ArrayList<Waypoint> waypointList) {
		InsuranceData insuranceData = new InsuranceData();
		droppedWaypoints = 0;
		try {
			Waypoint curWp, prevWp;
			double distance, vlnDistance, speedLimit ;
			Duration duration, vlnDuration;
			double minVelocity,maxVelocity;
			if (!waypointList.isEmpty()) {
				Iterator<Waypoint> iterator = waypointList.iterator();		
				prevWp = getnextValidWaypoint(iterator); // get first valid waypoint
				//iterate through remaining waypoints
				while(iterator.hasNext()) {
					curWp = getnextValidWaypoint(iterator);
					//if only single waypoint is present, no insurance data
					//can be calculated. So exit
					if(curWp == null) break;
					
					//calculate insurance data values for this interval [prevWp,curWp]
					// prevWp -> previous waypoint
					//curWp -> current waypoint
					
					duration = Duration.between(prevWp.getTimestamp(), curWp.getTimestamp());
					distance = getDistance(prevWp, curWp, calcMethod);
					//Assuming max of both speed limits if speed limits of 2 points doesn't match
					speedLimit = Math.max(curWp.getSpeed_limit(), prevWp.getSpeed_limit());
					minVelocity = Math.min(curWp.getSpeed(), prevWp.getSpeed());
					maxVelocity = Math.max(curWp.getSpeed(), prevWp.getSpeed());
					
					//if min velocity of both waypoints is greater than speed limit
					// speed limit is broken during entire interval.
					// speeding distance = total distance in interval
					//speeding time = time of interval
					if (minVelocity > speedLimit) {
						  vlnDuration = duration;
						  vlnDistance = distance;
					}						  
					//If minvelocity <= speedlimit < maxvelocity
					//speed limit is broken in some parts of the interval
					// assume uniformely accelerated motion between the waypoints
					// calculate speeding duration -> duration when speed was b/w (speedlimit, maxvelocity)
					// speeding distance -> distance travelled during speed limit violation. use getDistanceUAcc()
					else if (maxVelocity > speedLimit && 
							speedLimit >= minVelocity ){
				          vlnDuration = getSpeedingDuration(minVelocity, maxVelocity, curWp.getSpeed_limit(), duration);
				          vlnDistance = getDistanceUAcc (curWp.getSpeed_limit(), maxVelocity,
				        		  durationToSecs(vlnDuration));	

					}
					
					// minvelocity <= maxvelocity <= speed_limit
					//the vehicle was within speed limit in the interval
					// speeding distance = speeding time = 0
					else {
						vlnDuration = Duration.ofSeconds(0);
						vlnDistance = 0.0;
					}
					// add this interval's insurance data values to insurance data object
					insuranceData.setSpeedingDist(
						 addValuesWithScale(insuranceData.getSpeedingDist(), vlnDistance));
					insuranceData.setSpeedingDuration
					     (insuranceData.getSpeedingDuration().plus(vlnDuration));
					
					insuranceData.setTotalDuration(
							insuranceData.getTotalDuration().plus(duration));
					insuranceData.setTotalDist(
							addValuesWithScale(insuranceData.getTotalDist(), distance));
					prevWp = curWp;
				}
			}


		} catch (Exception ex) {
		    ex.printStackTrace();
		    logger.log(Level.WARNING, ex.getMessage());
		}

		if(droppedWaypoints > 0) {
			logger.log(Level.WARNING, "Dropped waypoints "+ droppedWaypoints);
		}
		else {
			logger.log(Level.INFO, "Dropped waypoints "+ droppedWaypoints);
		}
		return insuranceData;
		
	}

   /**
    * Reads waypoints from given path and convert them 
    *  into ArrayList of waypoint objects
    *
    * @param  waypointJsonPath   JSON file which stores waypoints
    * @return  Read waypoints as an ArrayList
    */	
	public ArrayList<Waypoint> getWaypointListFromPath (Path waypointJsonPath) 
			 {
		ArrayList<Waypoint> waypointList = new ArrayList<>();
		    // create object mapper instance
		    ObjectMapper mapper =  JsonMapper.builder()
		    		   .addModule(new JavaTimeModule())
		    		   .build();
		    Waypoint[] wpArr;
			try {
				wpArr = mapper.readValue(waypointJsonPath.toFile(), Waypoint[].class);
			    for(Waypoint wp : wpArr) {
			    	waypointList.add(wp);
			    }	
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Reading waypoints from file failed !!!");
			    logger.log(Level.SEVERE, e.getMessage());
			} 

		return waypointList;
		
	}
	
   /**
    * Writes calculated insurance data into given file 
    *
    * @param  data   Calculated InsuranceData statistics to be written
    * @param  resultJsonPath   Json file path to write the data into
    * @return  boolean representing function success/failure
    */
	public boolean writeDataToJsonPath (InsuranceData data,  Path resultJsonPath) {
		    
	    ObjectMapper mapper =  JsonMapper.builder()
	    		   .addModule(new JavaTimeModule())
	    		   .build();

	    try {
			mapper.writeValue(resultJsonPath.toFile(), data);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Writing Insurance Data to file failed !!!");
		    logger.log(Level.SEVERE, e.getMessage());
		}
		
	    return false;
	}
	
	/**
	 * Function to print how to run Insurance data calculator jar
	 * */
	private static void printInputFormat() {
	    System.out.println("\nProper Usage is: \n\n\tjava -jar insurance-data-calculator-0.0.1-SNAPSHOT.jar"
	    		+ " <waypoint json path> <result path> <distance cal method>");
	    System.out.println("\nTo use default values (" + DEFAULT_SOURCE_PATH + " ,  " +
	    		DEFAULT_DEST_PATH+ " , U ) :\n\n\tjava -jar insurance-data-calculator-0.0.1-SNAPSHOT.jar");
	    System.out.println("\nWhere "
	    		+"\n <waypoint json path> : Full path of waypoint json file"
	    		+"\n <result path> : Full path of result file where insurance data should be saved"
	    		+"\n <distance cal method> : Possibe values H and U"
	    		+ "\n\t H : Haversine method using longitude and latitudes"
	    		+"\n\t U: Calculate distance assuming uniform acceleration between points");
		
	}
	
	/**
	 * Main function that accepts JSON path and saves resultant insurance data 
	 * in JSON format
	 * @param args[0] : Path to JSON file containing waypoints. 
	 * 					Default value : DEFAULT_SOURCE_PATH
	 * @param args[1] : Path where to store Insurance Data as JSON. 
	 * 					Default value : DEFAULT_DEST_PATH
	 * @param args[1] : Distance calculation  method to be used
	 * 					 : Possibe values H and U"
	 *   				H : Haversine method using longitude and latitudes"
	 *   				U: Calculate distance assuming uniform acceleration between points
	 *   				Default value : U
	 */
	public static void main(String[] args) {
		
		Path waypointJsonPath= null;
		Path insuranceDataPath = null;
		CalcMethod calcMethod= CalcMethod.U_ACC;

		if (args.length == 0) {
			
			waypointJsonPath  = Paths.get(DEFAULT_SOURCE_PATH ); 
			insuranceDataPath  = Paths.get(DEFAULT_DEST_PATH);
			calcMethod = CalcMethod.U_ACC;
       			
		}
		else if(args.length == 2 || args.length == 3) {
			if( args.length == 3) {
				System.out.println("calc method : "+ args[2] );
				if(args[2].equals("H")) {
					calcMethod = CalcMethod.HAVERSINE;
				}
				else if (args[2].equals("U")) {
					calcMethod = CalcMethod.U_ACC;
				}
				else {
					System.out.println("\nThe given calculation method is invalid!");
					printInputFormat();
					System.exit(0);					
				}
			}
			waypointJsonPath  = Paths.get(args[0]);
			insuranceDataPath = Paths.get(args[1]);
			
			if(!Files.isRegularFile(waypointJsonPath) ) {
				System.out.println("\nGiven waypoint path is invalid or not accssible!");
				printInputFormat();
				System.exit(0);
			}
			try {
				insuranceDataPath.toFile();
			}
			catch(Exception ex) {
				System.out.println("\nGiven output path is invalid or not accssible!");
				printInputFormat();
				System.exit(0);
			}
		}
		else {
			printInputFormat();
		    System.exit(0);
		}
	
		InsuranceDataCalculator dataCalculator = new InsuranceDataCalculator(calcMethod);
		dataCalculator.saveInsuranceDataFromWaypoint(waypointJsonPath, insuranceDataPath);
		System.out.println("\nSuccess!!! Result available in " + insuranceDataPath.toString());
	}
	
	
	
	
	
	
	
}
