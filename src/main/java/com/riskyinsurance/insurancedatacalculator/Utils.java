package com.riskyinsurance.insurancedatacalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

/**
 * The Utils class contains functions and variables that help with calculating 
 * Insurance data values.
 */
public class Utils {
	
	/**
	* enum to keep track of distance calculation method.
	* Consists of two values:
	*  U_ACC : Distance calculation from speed assuming uniformly accelerated motion between waypoints.
	*  HAVERSINE : Distance calculation from latitudes and longitudes using haversine formula 
	*/
	public enum CalcMethod{
		U_ACC,
		HAVERSINE
	}
	
    /**
    * Calculate seconds in a duration. Calculates seconds from number of milliseconds.
    *  This is function is used to avoid precision loss that happens when 
    *  using Duration.getSeconds()
    *
    * @param  duration   Duration object for which number of seconds is to be calculated
    * @return  double value representing duration in seconds
    */

    public static double durationToSecs(Duration duration) {
    	return duration.toMillis() * 0.001;
    }
	
    /**
    * Convert time measurement in seconds into Duration object.
    *  This is function is used to avoid precision loss that happens when 
    *  using Duration.ofSeconds(long seconds)
    *
    * @param  secs  double value representing duration in seconds 
    * @return  Duration object with specified number of seconds
    */
    public static Duration secsToDuration(double secs) {
    	return Duration.ofMillis(Math.round(secs * 1000)) ;
    }
    
    /**
    * This is function is used to add a double value to a BigDecimal
    * while retaining scale for the BigDecimal object
    *
    * @param  bigD  BigDecimal object to be added
    * @param  doubleVal  double value to be added
    * @return  sum of inputs as a BigDecimal object
    */   
	public static BigDecimal addValuesWithScale( BigDecimal bigD, double doubleVal) {
		return BigDecimal.valueOf(doubleVal).setScale(3, RoundingMode.HALF_DOWN).add(bigD);
	}
	
    /**
    * Calculate distance between two waypoints.
    *
    * @param  prevWp previous waypoint
    * @param  curWp current waypoint
    * @param  method calculation method - haversine or uniform acceleration
    * @return  distance between points in meters
    */
    
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
    
    /**
    * Calculate distance between two points given their latitude and longitude using 
    * haversine formula
    *
    * @param  lat1 latitude of first point in degrees
    * @param  lon1 longitude of first point in degrees
    * @param  lat2 latitude of second point in degrees
    * @param  lon2 longitude of second point in degrees
    * @return  distance between points in meters
    */
	 static double getDistanceHaversine(double lat1, double lon1, double lat2, double lon2) {
		 	final double R = 6371 * 1000; // radius of earth in meters
	        double dLat = Math.toRadians(lat2 - lat1);
	        double dLon = Math.toRadians(lon2 - lon1);
	        lat1 = Math.toRadians(lat1);
	        lat2 = Math.toRadians(lat2);

	        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
	        double c = 2 * Math.asin(Math.sqrt(a));
	        return R * c;
	    }	
    
    /**
    * Calculate distance between two points assuming an object with uniform acceleration 
    * moving between the points. Distance is calculated using formula
    * 
    * 	s = ut + 1/2 * (v-u) *t
    *
    * @param  initialVel velocity at point 1 in m/s
    * @param  finalVel velocity at point 2 in m/s
    * @param  timeSec time taken to travel from point 1 to 2 in seconds
    * @return  distance between points in meters
    */
	static double getDistanceUAcc(double initialVel, double finalVel, double timeSec) {
		return initialVel*timeSec + (finalVel-initialVel)*0.5*timeSec;
	}
	
	static Duration getSpeedingDuration (double initialVel, double finalVel, double speedLimit, Duration duration ) {
		//Assumes constant acceleration
		double timeSecTotal = durationToSecs(duration);
		double timeSec = (finalVel - speedLimit)* timeSecTotal / (finalVel - initialVel);
		return secsToDuration(timeSec);
	}




}
