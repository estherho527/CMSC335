package proj3;
/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 3
 * 
 * 
 * File Name: PassengerShip.java
 * 
 * Description:
 * A PassengerShip can track how many passengers, rooms, and occupied rooms there are.
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */

import java.util.HashMap;
import java.util.Scanner;

class PassengerShip extends Ship {
   static int indexNew = 30000;
   
   int numberOfPassengers    = 0;
   int numberOfRooms         = 0;
   int numberOfOccupiedRooms = 0;

   public PassengerShip (boolean f, int n) {
      super (f, n);
      index = indexNew++;
      numberOfRooms         = 100 + rn.nextInt (1000);
      numberOfPassengers    = Math.round(numberOfRooms * rn.nextFloat() * 4);
      numberOfOccupiedRooms = Math.min (numberOfRooms, numberOfPassengers/2);
      for (int i = 0; i < rn.nextInt (n); i++)
         jobs.add (new Job (this));
   } // end random ship constructor
   
   public PassengerShip (Scanner sc, HashMap<Integer, Dock> docks, HashMap<Integer, SeaPort> ports) {
	   super(sc, docks, ports);
	   if (sc.hasNextInt()) numberOfPassengers = sc.nextInt();
	   if (sc.hasNextInt()) numberOfRooms = sc.nextInt();
	   if (sc.hasNextInt()) numberOfOccupiedRooms = sc.nextInt();
   } // end end Scanner constructor
   
   public String getOutputFormat () {
      return String.format ("    pship %20s %d %d %.2f %.2f %.2f %.2f %d %d %d\n", 
             name, index, parent, weight, length, width, draft,
             numberOfPassengers, numberOfRooms, numberOfOccupiedRooms);
   } // end method getOutputFormat
   
   /* ADDED
    * toString() method
    */
   public String toString () {
      String st = "Passenger ship: " + super.toString();
      if (jobs.size() == 0) 
         return st;
     // for (Job mj: jobs) st += "\n       - " + mj;
      return st;
   } // end method toString
   
} // end class MyPassengerShip