package proj1;
import java.util.Scanner;

/* Author: Esther Ho
 * Due Date: 11/5/2017
 * CMSC 335 Project 1
 * 
 * 
 * File Name: MyDock.java
 * 
 * Description:
 * 
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */

class Dock extends Thing {
   static int indexNew = 20000;
   
   Ship ship = null;

   public Dock (boolean f) {
      index = indexNew++;
      name = "Pier_"  + index%2000;
   } // end random ship constructor

   // Dock Scanner Constructor
   public Dock(Scanner sc){
	   
	   super(sc);
	   
   }
   
   public String getOutputFormat () {
      String st = "";
      st += String.format ("  dock %s %d %d %d\n", name, index, parent, ship.index);
      return st;
   } // end method getOutputFormat

   /* ADDED
    * toString()
    */
   public String toString(){
	   
	   String st = "Dock: " + name + "\n\t" + ship;
	   
	   return st;
   }// end toString
   
} // end class MyDock