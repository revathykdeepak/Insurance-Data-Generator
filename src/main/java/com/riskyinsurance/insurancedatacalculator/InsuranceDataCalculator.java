package com.riskyinsurance.insurancedatacalculator;

import java.net.SocketOption;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

import static com.riskyinsurance.insurancedatacalculator.Utils.*;

public class InsuranceDataCalculator {
	

	
	public InsuranceData getInsuranceDataFromWaypoint (Path waypointJsonPath) {
		//TO-DO
		return new InsuranceData();
	}
	
	public InsuranceData getInsuranceDataFromWaypoint (List<Waypoint> waypointList) {
		//TO-DO
		return new InsuranceData();
	}
	
	public void saveInsuranceDataFromWaypoint (Path waypointJsonPath , 
			Path insuranceDataPath) {
		//TO-DO explore streaming
		List<Waypoint> waypointList = Utils.getWaypointListFromPath(waypointJsonPath);
		InsuranceData insuranceData = calculateInsuranceData(waypointList);
		System.out.println(insuranceData);
		
	}
	
	InsuranceData calculateInsuranceData(List<Waypoint> waypointList) {
		InsuranceData insuranceData = new InsuranceData();
		Waypoint curWp, prevWp;
		double totDistance, speedDistance, speedLimit ;
		Duration totDuration, speedDuration;
		double minVelocity,maxVelocity;
		if (!waypointList.isEmpty()) {
			Iterator<Waypoint> iterator = waypointList.iterator();
			prevWp = iterator.next();
			while(iterator.hasNext()) {
				curWp = iterator.next();
				totDuration = Duration.between(prevWp.getTimestamp(), curWp.getTimestamp());
				totDistance = getDistance(prevWp.getSpeed(),curWp.getSpeed(), durationToSecs(totDuration));
				//Assuming max of both speed limits if speed limits of 2 points doesn't match
				speedLimit = Math.max(curWp.getSpeed_limit(), prevWp.getSpeed_limit());
				minVelocity = Math.min(curWp.getSpeed(), prevWp.getSpeed());
				maxVelocity = Math.max(curWp.getSpeed(), prevWp.getSpeed());
				if (minVelocity > speedLimit) {
					  speedDuration = totDuration;
					  speedDistance = totDistance;
				}else if (maxVelocity > speedLimit && 
						speedLimit >= minVelocity ){
			          speedDuration = getSpeedingDuration(maxVelocity, minVelocity, curWp.getSpeed_limit(), totDuration);
			          speedDistance = getDistance(curWp.getSpeed_limit(), maxVelocity,
			        		  durationToSecs(speedDuration));
			          
				}else {
					speedDuration = Duration.ofSeconds(0);
					speedDistance = 0.0;
				}
				insuranceData.setSpeedingDist(speedDistance + insuranceData.getSpeedingDist());;
				insuranceData.setSpeedingDuration
				     (insuranceData.getSpeedingDuration().plus(speedDuration));
				
				insuranceData.setTotalDuration(
						insuranceData.getTotalDuration().plus(totDuration));
				insuranceData.setTotalDist(totDistance + insuranceData.getTotalDist());
				prevWp = curWp;
			}
		}
		return insuranceData;
	}
	


	
	

	
	public static void main(String[] args) {
		
		Path waypointJsonPath= null;
		Path insuranceDataPath = null;
		//TO-DO
		if (args.length == 0) {
			
			waypointJsonPath  = 
					Paths.get("../resourcepaths/waypoints.json");
			insuranceDataPath  = 
					Paths.get("../resourcepaths/result.json");
       			
		}
		else if (args.length != 2){
			    System.out.println("Proper Usage is: java program InsuranceDataCalculator"
			    		+ " waypointpath savepath");
			    System.exit(0);
		}
		else {
			waypointJsonPath  = 
					Paths.get(args[0]);
			insuranceDataPath  = 
					Paths.get(args[1]);
       			
		}
	
		InsuranceDataCalculator dataCalculator = new InsuranceDataCalculator();
		dataCalculator.saveInsuranceDataFromWaypoint(waypointJsonPath, insuranceDataPath);
		
	}
	
	
	
	
	
	
	
}
