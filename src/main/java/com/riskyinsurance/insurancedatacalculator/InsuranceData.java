package com.riskyinsurance.insurancedatacalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Objects;

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
	
	public InsuranceData(double speedingDist, Duration speedingDuration, double totalDist,
			Duration totalDuration) {
		super();
		this.speedingDist = BigDecimal.valueOf(speedingDist).setScale(3, RoundingMode.HALF_DOWN);
		this.speedingDuration = speedingDuration;
		this.totalDist = BigDecimal.valueOf(totalDist).setScale(3, RoundingMode.HALF_DOWN);
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
	@Override
	public int hashCode() {
		return Objects.hash(speedingDist, speedingDuration, totalDist, totalDuration);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InsuranceData other = (InsuranceData) obj;
		return Objects.equals(speedingDist, other.speedingDist)
				&& Objects.equals(speedingDuration, other.speedingDuration)
				&& Objects.equals(totalDist, other.totalDist) && Objects.equals(totalDuration, other.totalDuration);
	}



}
