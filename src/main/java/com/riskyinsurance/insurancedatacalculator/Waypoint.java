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
	
	private LocalDateTime timestamp;
	private Position position;
	private double speed;
	private double speed_limit;
	
	public class Position{
		@Override
		public String toString() {
			return "Position [latitude=" + latitude + ", longitude=" + longitude + "]";
		}
		double latitude;
		double longitude;
		
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
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
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getSpeed_limit() {
		return speed_limit;
	}
	public void setSpeed_limit(double speed_limit) {
		this.speed_limit = speed_limit;
	}

	@Override
	public String toString() {
		return "Waypoint [timestamp=" + timestamp + "\nposition=" + position + "\nspeed=" + speed + ", speed_limit="
				+ speed_limit + "]";
	}

	

	
}
