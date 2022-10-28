package com.riskyinsurance.insurancedatacalculator;

import java.math.BigDecimal;
import java.time.Duration;

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
	public InsuranceData(BigDecimal speedingDist, Duration speedingDuration, BigDecimal totalDist,
			Duration totalDuration) {
		super();
		this.speedingDist = speedingDist;
		this.speedingDuration = speedingDuration;
		this.totalDist = totalDist;
		this.totalDuration = totalDuration;
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
