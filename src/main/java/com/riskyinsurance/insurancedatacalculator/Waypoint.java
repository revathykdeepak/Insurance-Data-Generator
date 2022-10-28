/**
 * 
 */
package com.riskyinsurance.insurancedatacalculator;

import java.time.LocalDateTime;
import java.util.Objects;

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
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(latitude, longitude);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Position other = (Position) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
					&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude);
		}
		private Waypoint getEnclosingInstance() {
			return Waypoint.this;
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
	@Override
	public int hashCode() {
		return Objects.hash(position, speed, speed_limit, timestamp);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Waypoint other = (Waypoint) obj;
		return Objects.equals(position, other.position)
				&& Double.doubleToLongBits(speed) == Double.doubleToLongBits(other.speed)
				&& Double.doubleToLongBits(speed_limit) == Double.doubleToLongBits(other.speed_limit)
				&& Objects.equals(timestamp, other.timestamp);
	}

	

	
}
