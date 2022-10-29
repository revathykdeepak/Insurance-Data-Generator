# Insurance-Data-Generator

## Problem

Peter's car has reported Waypoints from a trip (e.g. where it has driven). The following data is known for each Waypoint:

- latitude (degrees)
- longitude (degrees)
- speed (meters per second)
- speed_limit (meters per second)
- timestamp (ISO 8601 date-time)

The data for Peter's Waypoints needs to be organized in these categories, to make it possible to decide his insurance fee:

- Distance Speeding, meaning the total distance that Peter has driven at a speed exceeding the speed limit
- Duration Speeding, meaning the total time that Peter has driven at a speed exceeding the speed limit
- Total Distance, meaning the total driving distance
- Total Duration, meaning the total driving time

## Solution

To calculate Insurance data categories, the entire trip is broken down into several intervals with each interval representing the travel between two consecutive 
waypoints. Insurance data is calculated for each intervals and these values are added together to get insurance data for the entire trip. 


For example, if three waypoints A,B and C are given,


Insurance  data for trip (ABC)  =   Insurance data (AB) + Insurance data (BC)


For a single interval, value of Insurance data fields are calculated as:
- **Total Duration (seconds)** : Difference between time stamp of the two waypoints. 


- **Total Distance (meters)** : Two methods can be used to get total distance between two waypoints
  - **Calculate distance by assuming uniformely accelerated motion** : In this method, we assume that in an interval, the vehicle travels with
    a constant acceleration. So distance can be calculated from the formula
    
     $${s}={ut}+\frac{v-u}{2} t$$
     
     where <br /> 
     &emsp;s -> distance <br />
     &emsp;u -> speed at first waypoint <br />
     &emsp;v -> speed at sevond waypoint <br />
     &emsp;t -> duration between waypoints <br />
     
  - **Haversine Formula** : [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula)  is used to approximately calculate distance between two points given their latitude and
  logitude. Thus we can use latitude and longitude of way points to calculate the distance. But for using this, we need to assume that the vehicle is travelling in a straight
  line in the shortest distance between the two waypoints.


- **Speeding Duration (seconds) & Speeding Distance (meters)**  : There are 3 ways in which a vehicle can travel between 2 waypoints with respect to speed limit:
  - Case 1 : Vehicle speed is within the speed limit at both points. In this case, both speeding distance and speeding duration are considered as 0.
  - Case 2 : Vehicle speed is greater than speed limit at both points. In this case, speeding distance is equal to total distance and speeding duration is equal to total duration
  - Case 3 : speed is within limit at one waypoint and grater than speed limit at the other point. To calculate speeding distance and duration, a uniformly
  accelerated motion is assumed between the two points.  speeding duration is calculated from formula 
  
  $$ {t_2} = {t_1} \frac{v-v_l}{v-u} $$
     
     where <br /> 
     &emsp;t2 -> speeding duration <br />
     &emsp;t1 -> total duration <br />
     &emsp;v -> speed at the waypoint where speed limit is exceeding <br />
     &emsp;u -> speed at the waypoint where vehicle is within speed limit <br />
     &emsp;vl -> speed limit <br />
     
     Speeding distance is calculated as <br />
     
  $${s}={ut}+\frac{v-u}{2} t$$
     
     where <br /> 
     &emsp;s -> speeding distance <br />
     &emsp;u -> speed limit <br />
     &emsp;v -> speed at the waypoint where speed limit is exceeding <br />
     &emsp;t -> speeding duration <br />
**Assumptions**


