/**
 * Test com.riskyinsurance.insurancedatacalculator.Utils class
 */
package com.riskyinsurance.insurancedatacalculator;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import com.riskyinsurance.insurancedatacalculator.Utils.CalcMethod;


public class UtilsTest {

	@Test
	public void testGetDistanceHaversine() {
		double[] p1_lat = {36.12,59.334};
		double[] p1_long = {-86.67,18.0667};
		double[] p2_lat = {33.94,59.3337};
		double[] p2_long = {-118.40,18.0662};
		double[] hav_dist = {2886444.44,43.78};
		for(int i=0; i<p1_lat.length ;i++) {
			double res = Utils.getDistanceHaversine(p1_lat[i], p1_long[i], 
					p2_lat[i], p2_long[i]);
			assertEquals(res, hav_dist[i], 0.01);
		}
	}
	
	@Test
	public void testGetDistanceUAcc() {
		double[] u = {5f, 30f, 20f, 15f};
		double[] v = {15f, 10f, 20f, 20f};
		double[] t = {5f, 5f, 5f, 5f};
		double[] s = {50.0, 100.0, 100.0, 87.5};
		for(int i=0; i<u.length ;i++) {
			double res = Utils.getDistanceUAcc(u[i], v[i], t[i]);
			assertEquals(res, s[i], 0.01);
		}
	}
	
	@Test
	public void testGetDistance() {
		Waypoint wp1 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:00.000") , 59.334, 18.0667, 
				 6.3889, 8.33 );
		Waypoint wp2 = new Waypoint(LocalDateTime.parse("2016-06-21T12:00:05.000") , 59.3337, 18.0662, 
				 9.4, 8.33 );
		double havDist = Utils.getDistance(wp1, wp2, CalcMethod.HAVERSINE);
		double uaDist = Utils.getDistance(wp1, wp2, CalcMethod.U_ACC);
		assertEquals(uaDist, 39.47225, 0.01);
		assertEquals(havDist, 43.7822, 0.01);
		
	}
		
	

}
