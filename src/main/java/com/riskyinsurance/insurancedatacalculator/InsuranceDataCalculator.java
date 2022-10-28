package com.riskyinsurance.insurancedatacalculator;

import static com.riskyinsurance.insurancedatacalculator.Utils.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.riskyinsurance.insurancedatacalculator.Utils.CalcMethod;

public class InsuranceDataCalculator {
	
	private static final String DEFAULT_SOURCE_PATH  = "../resourcepaths/waypoints.json"; 
	private static final String DEFAULT_DEST_PATH  = "../resourcepaths/wp1new.json"; 
	public static CalcMethod calcMethod = CalcMethod.U_ACC;

	
	public void saveInsuranceDataFromWaypoint (Path waypointJsonPath , 
			Path insuranceDataPath) {
		InsuranceData insuranceData = getInsuranceDataFromWaypoint(waypointJsonPath);
		writeDataToJsonPath(insuranceData, insuranceDataPath);
		
	}
	
	public InsuranceData getInsuranceDataFromWaypoint (Path waypointJsonPath) {
		List<Waypoint> waypointList = getWaypointListFromPath(waypointJsonPath);
		InsuranceData insuranceData = getInsuranceDataFromWaypoint(waypointList);
		return insuranceData;
	}
	

	public InsuranceData getInsuranceDataFromWaypoint(List<Waypoint> waypointList) {
		InsuranceData insuranceData = new InsuranceData();
		try {
			Waypoint curWp, prevWp;
			double distance, vlnDistance, speedLimit ;
			Duration duration, vlnDuration;
			double minVelocity,maxVelocity;
			if (!waypointList.isEmpty()) {
				Iterator<Waypoint> iterator = waypointList.iterator();
				prevWp = iterator.next();
				while(iterator.hasNext()) {
					curWp = iterator.next();
					duration = Duration.between(prevWp.getTimestamp(), curWp.getTimestamp());
					distance = getDistance(prevWp, curWp, calcMethod);
					//Assuming max of both speed limits if speed limits of 2 points doesn't match
					speedLimit = Math.max(curWp.getSpeed_limit(), prevWp.getSpeed_limit());
					minVelocity = Math.min(curWp.getSpeed(), prevWp.getSpeed());
					maxVelocity = Math.max(curWp.getSpeed(), prevWp.getSpeed());
					if (minVelocity > speedLimit) {
						  vlnDuration = duration;
						  vlnDistance = distance;
					}else if (maxVelocity > speedLimit && 
							speedLimit >= minVelocity ){
				          vlnDuration = getSpeedingDuration(minVelocity, maxVelocity, curWp.getSpeed_limit(), duration);
				          vlnDistance = getDistanceUAcc (curWp.getSpeed_limit(), maxVelocity,
				        		  durationToSecs(vlnDuration));
				          
					}else {
						vlnDuration = Duration.ofSeconds(0);
						vlnDistance = 0.0;
					}
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
		}


		return insuranceData;
	}
	
	public List<Waypoint> getWaypointListFromPath (Path waypointJsonPath) {
		List<Waypoint> waypointList = new ArrayList<>();
		try {
		    // create object mapper instance
		    ObjectMapper mapper =  JsonMapper.builder()
		    		   .addModule(new JavaTimeModule())
		    		   .build();
	    
		    waypointList =
		    		Arrays.asList(mapper.readValue(waypointJsonPath.toFile(), Waypoint[].class));
		    
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		
		return waypointList;
		
	}
	
	public void writeDataToJsonPath (InsuranceData data,  Path resultJsonPath) {
		try {
		    
		    ObjectMapper mapper =  JsonMapper.builder()
		    		   .addModule(new JavaTimeModule())
		    		   .build();

		    mapper.writeValue(resultJsonPath.toFile(), data);

		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
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
	

	public static void main(String[] args) {
		
		Path waypointJsonPath= null;
		Path insuranceDataPath = null;

		if (args.length == 0) {
			
			waypointJsonPath  = Paths.get(DEFAULT_SOURCE_PATH ); 
			insuranceDataPath  = Paths.get(DEFAULT_DEST_PATH);
       			
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
	
		InsuranceDataCalculator dataCalculator = new InsuranceDataCalculator();
		dataCalculator.saveInsuranceDataFromWaypoint(waypointJsonPath, insuranceDataPath);
		
	}
	
	
	
	
	
	
	
}
