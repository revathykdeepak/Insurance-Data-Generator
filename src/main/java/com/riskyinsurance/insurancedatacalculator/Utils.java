package com.riskyinsurance.insurancedatacalculator;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Utils {
	
	public static List<Waypoint> getWaypointListFromPath (Path waypointJsonPath) {
		List<Waypoint> waypointList = new ArrayList();
		try {
		    // create object mapper instance
		    ObjectMapper mapper =  JsonMapper.builder()
		    		   .addModule(new JavaTimeModule())
		    		   .build();
	    
		    waypointList =
		    		Arrays.asList(mapper.readValue(waypointJsonPath.toFile(), Waypoint[].class));
		    
		    for (Waypoint wPoint : waypointList) {
		    	System.out.println(wPoint);
		    }

		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		
		return waypointList;
		
	}

    public static double durationToSecs(Duration duration) {
    	return duration.toMillis() * 0.001;
    }
	
    public static Duration secsToDuration(double secs) {
    	return Duration.ofMillis(Math.round(secs * 1000)) ;
    }
	public static double getDistance(double initialVel, double finalVel, double timeSec) {
		return initialVel*timeSec + (finalVel-initialVel)*0.5*timeSec;
	}
	
	public static Duration getSpeedingDuration (double initialVel, double finalVel, double speedLimit, Duration duration ) {
		//Assumes constant acceleration
		double timeSecTotal = durationToSecs(duration);
		double timeSec = (finalVel - speedLimit)* timeSecTotal / (finalVel - initialVel);
		return secsToDuration(timeSec);
	}
	

	
}
