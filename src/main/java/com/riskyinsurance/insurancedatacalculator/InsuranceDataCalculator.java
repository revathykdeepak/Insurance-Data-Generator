package com.riskyinsurance.insurancedatacalculator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
		
		List<Waypoint> waypointList = Utils.getWaypointListFromPath(waypointJsonPath);
		calculateInsuranceData(waypointList);
		
	}
	
	InsuranceData calculateInsuranceData(List<Waypoint> waypointList) {
		InsuranceData insuranceData = new InsuranceData();
		if (!waypointList.isEmpty()) {
			
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
	
		InsuranceDataCalculator dataCalculator = new InsuranceDataCalculator();
		dataCalculator.saveInsuranceDataFromWaypoint(waypointJsonPath, insuranceDataPath);
		
	}
	
	
	
	
	
	
	
}
