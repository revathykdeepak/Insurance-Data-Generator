package com.riskyinsurance.insurancedatacalculator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

/**
 * @author dell
 * 
 * Distance Speeding, meaning the total distance that Peter has driven at a speed exceeding the speed limit
Duration Speeding, meaning the total time that Peter has driven at a speed exceeding the speed limit
Total Distance, meaning the total driving distance
Total Duration, meaning the total driving time
 */

public class InsuranceData {
	

	private BigDecimal speedingDist;
	private Duration speedingDuration;
	private BigDecimal totalDist;
	private Duration totalDuration;
	
	
	
	public InsuranceData() {
		super();
		speedingDist = BigDecimal.ZERO;
		totalDist = BigDecimal.ZERO;
		speedingDuration = Duration.ofSeconds(0);
		totalDuration = Duration.ofSeconds(0);
	}
	public BigDecimal getSpeedingDist() {
		return speedingDist;
	}
	public void setSpeedingDist(BigDecimal speedingDist) {
		this.speedingDist = speedingDist;
	}
	public Duration getSpeedingDuration() {
		return speedingDuration;
	}
	public void setSpeedingDuration(Duration speedingDuration) {
		this.speedingDuration = speedingDuration;
	}
	public BigDecimal getTotalDist() {
		return totalDist;
	}
	public void setTotalDist(BigDecimal totalDist) {
		this.totalDist = totalDist;
	}
	public Duration getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(Duration totalDuration) {
		this.totalDuration = totalDuration;
	}

	@Override
	public String toString() {
		return "InsuranceData [speedingDist=" + speedingDist + ", speedingDuration=" + speedingDuration + ", totalDist="
				+ totalDist + ", totalDuration=" + totalDuration + "]";
	}

}
