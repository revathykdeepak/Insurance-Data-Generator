/**
 * 
 */
package com.riskyinsurance.insurancedatacalculator;

import java.time.LocalDateTime;

/**
 * @author dell
 * 
 * latitude (degrees)
longitude (degrees)
speed (meters per second)
speed_limit (meters per second)
timestamp (ISO 8601 date-time)
 *
 */
public class Waypoint {
	
	LocalDateTime timestamp;
	Position position;
	Double speed;
	Double speed_limit;
	
	public class Position{
		@Override
		public String toString() {
			return "Position [latitude=" + latitude + ", longitude=" + longitude + "]";
		}
		Double latitude;
		Double longitude;
		
		public Double getLatitude() {
			return latitude;
		}
		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}
		public Double getLongitude() {
			return longitude;
		}
		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}
				
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	public Double getSpeed_limit() {
		return speed_limit;
	}
	public void setSpeed_limit(Double speed_limit) {
		this.speed_limit = speed_limit;
	}

	@Override
	public String toString() {
		return "Waypoint [timestamp=" + timestamp + "\nposition=" + position + "\nspeed=" + speed + ", speed_limit="
				+ speed_limit + "]";
	}

	

	
}
