package com.riskyinsurance.insurancedatacalculator;


/**
 * @author dell
 * 
 * Distance Speeding, meaning the total distance that Peter has driven at a speed exceeding the speed limit
Duration Speeding, meaning the total time that Peter has driven at a speed exceeding the speed limit
Total Distance, meaning the total driving distance
Total Duration, meaning the total driving time
 */

public class InsuranceData {
	
	Double speedingDist;
	Double speedingDuration;
	Double totalDist;
	Double totalDuration;
	
	
	
	public Double getSpeedingDist() {
		return speedingDist;
	}
	public void setSpeedingDist(Double speedingDist) {
		this.speedingDist = speedingDist;
	}
	public Double getSpeedingDuration() {
		return speedingDuration;
	}
	public void setSpeedingDuration(Double speedingDuration) {
		this.speedingDuration = speedingDuration;
	}
	public Double getTotalDist() {
		return totalDist;
	}
	public void setTotalDist(Double totalDist) {
		this.totalDist = totalDist;
	}
	public Double getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(Double totalDuration) {
		this.totalDuration = totalDuration;
	}


}
