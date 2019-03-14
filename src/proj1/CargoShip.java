package proj1;
import java.util.Scanner;

/* Author: Esther Ho
 * Due Date: 11/5/2017
 * CMSC 335 Project 1
 * 
 * 
 * File Name: MyCargoShip.java
 * 
 * Description:
 * 
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */

class CargoShip extends Ship {
   static int indexNew = 40000;
   
   double cargoWeight = 0;
   double cargoVolume = 0;
   double cargoValue  = 0;
   
   public CargoShip (boolean f, int n) {
      super (f, n);
      index = indexNew++;
      cargoWeight =  20 + rn.nextDouble() *  200;
      cargoVolume = 100 + rn.nextDouble() *  100;
      cargoValue  =  10 + rn.nextDouble() * 1000;
      for (int i = 0; i < rn.nextInt (n); i++)
         jobs.add (new Job (this));
   } // end random ship constructor

   // CargoShip Scanner constructor
   public CargoShip(Scanner sc){
	   
	   super(sc);
	   if (sc.hasNextDouble()) cargoWeight = sc.nextDouble();
	   if (sc.hasNextDouble()) cargoVolume = sc.nextDouble();
	   if (sc.hasNextDouble()) cargoValue = sc.nextDouble();
   }
   
   public String getOutputFormat () {
      String st = "";
      st += String.format ("    cship %20s %d %d %.2f %.2f %.2f %.2f %.2f %.2f %.2f\n", 
             name, index, parent, weight, length, width, draft,
             cargoWeight, cargoVolume, cargoValue);
      return st;
   } // end method getOutputFormat
   
   /* ADDED
    * toString()
    */
   public String toString(){
	   
	   return "Cargo Ship: " + super.toString();
   }
   
} // end class MyCargoShip