package com.riskyinsurance.insurancedatacalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public class Utils {
	
	public enum CalcMethod{
		U_ACC,
		HAVERSINE
	}

    public static double durationToSecs(Duration duration) {
    	return duration.toMillis() * 0.001;
    }
	
    public static Duration secsToDuration(double secs) {
    	return Duration.ofMillis(Math.round(secs * 1000)) ;
    }
    
    static double getDistance(Waypoint prevWp, Waypoint curWp, CalcMethod method) {
    	Duration duration;
    	if (method == CalcMethod.HAVERSINE) {
    		return getDistanceHaversine(prevWp.getPosition().getLatitude(),
			        prevWp.getPosition().getLongitude(),
					curWp.getPosition().getLatitude(),
					curWp.getPosition().getLongitude());
    	}
    	else {
    		duration = Duration.between(prevWp.getTimestamp(), curWp.getTimestamp());
    		return getDistanceUAcc(prevWp.getSpeed(),curWp.getSpeed(), 
    				durationToSecs(duration));
    	}
    }
	static double getDistanceUAcc(double initialVel, double finalVel, double timeSec) {
		return initialVel*timeSec + (finalVel-initialVel)*0.5*timeSec;
	}
	
	static Duration getSpeedingDuration (double initialVel, double finalVel, double speedLimit, Duration duration ) {
		//Assumes constant acceleration
		double timeSecTotal = durationToSecs(duration);
		double timeSec = (finalVel - speedLimit)* timeSecTotal / (finalVel - initialVel);
		return secsToDuration(timeSec);
	}

    
	public static BigDecimal addValuesWithScale( BigDecimal bigD, double doubleVal) {
		return BigDecimal.valueOf(doubleVal).setScale(3, RoundingMode.HALF_DOWN).add(bigD);
	}
	
	 static double getDistanceHaversine(double lat1, double lon1, double lat2, double lon2) {
		 	final double R = 6371 * 1000; // In meters
	        double dLat = Math.toRadians(lat2 - lat1);
	        double dLon = Math.toRadians(lon2 - lon1);
	        lat1 = Math.toRadians(lat1);
	        lat2 = Math.toRadians(lat2);

	        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
	        double c = 2 * Math.asin(Math.sqrt(a));
	        return R * c;
	    }	
}
