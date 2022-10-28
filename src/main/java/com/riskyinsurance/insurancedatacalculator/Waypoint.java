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


		double latitude;
		double longitude;
		
		public Position(double latitude, double longitude) {
			super();
			this.latitude = latitude;
			this.longitude = longitude;
		}
		public Position() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public String toString() {
			return "Position [latitude=" + latitude + ", longitude=" + longitude + "]";
		}
		
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
	
	
	
	public Waypoint() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Waypoint(LocalDateTime timestamp, double latitude, double longitude,
			double speed, double speed_limit) {
		super();
		this.timestamp = timestamp;
		this.position = new Position(latitude,longitude);
		this.speed = speed;
		this.speed_limit = speed_limit;
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
