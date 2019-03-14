package proj1;
/* Author: Esther Ho
 * Due Date: 11/5/2017
 * CMSC 335 Project 1
 * 
 * 
 * File Name: MyShip.java
 * 
 * Description:
 * 
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Ship extends Thing {
   static ArrayList <String> words = null;

   double weight = 0;
   double length = 0;
   double width  = 0;
   double draft  = 0;
   Time arrivalTime = null;
   Time dockTime    = null;
   ArrayList <Job> jobs = new ArrayList <> ();

   public Ship () {} // end no-parameter constructor
   
   public Ship (boolean t, int n) {
      if (words == null) readWordsFile ();
      name = words.get (rn.nextInt (words.size()));
      weight =  50 + rn.nextDouble () * 200;
      length = 100 + rn.nextDouble () * 400;
      width  =  30 + rn.nextDouble () * 100;
      draft  =  15 + rn.nextDouble () *  30;
   } // create a random ship
   
   public Ship (Scanner sc) {
	   super(sc);
	   if (sc.hasNextDouble()) weight = sc.nextDouble();
	   if (sc.hasNextDouble()) length = sc.nextDouble();
	   if (sc.hasNextDouble()) width = sc.nextDouble();
	   if (sc.hasNextDouble()) draft = sc.nextDouble();
   } // end end Scanner constructor
   
   void readWordsFile () {
      try {
         words = new ArrayList <> ();
         Scanner sp = new Scanner (new File ("shipNames.txt"));
         while (sp.hasNext()) words.add (sp.next());
         System.out.println ("Words File size: " + words.size());  
         sp.close();
      } 
      catch (java.io.FileNotFoundException e) {System.out.println ("bad file");}
   } // end readWordsFile

   void toArray (List <Thing> mta) {
      mta.add (this);
      for (Job mj: jobs) mj.toArray (mta);
   } // end method toList
   
   public String getOutputFormat () {
      String st = "";
      st += String.format ("    ship %20s %d %d %.2f %.2f %.2f %.2f\n", 
             name, index, parent, weight, length, width, draft);
      return st;
   } // end method getOutputFormat
   
   /* ADDED
    * toString()
    */
   public String toString(){
	   
	  // String st = "Ship: " + name;
	   return super.toString();
   }

} // end class MyShip