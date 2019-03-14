package proj2;
/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 2
 * 
 * 
 * File Name: Ship.java
 * 
 * Description:
 * A Ship can have weight, length, width, and draft.  Each Ship can have a list of jobs, and an ArrivalTime and dockTime
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
   
   /*
    * Ship Scanner constructor
    */
   public Ship (Scanner sc) {
	   super(sc);
	   if (sc.hasNextDouble()) weight = sc.nextDouble();
	   if (sc.hasNextDouble()) length = sc.nextDouble();
	   if (sc.hasNextDouble()) width = sc.nextDouble();
	   if (sc.hasNextDouble()) draft = sc.nextDouble();
   } // end end Scanner constructor
   
   // for CreateSeaPortDataFile.java
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

   // getters
   public double getWeight(){
	   return weight;
   }
   
   public double getLength(){
	   return length;
   }
   
   public double getWidth(){
	   return width;
   }
   
   public double getDraft(){
	   return width;
   }
   
   public Time getArrivalTime(){
	   return arrivalTime;
   }
   
   public Time getDockTime(){
	   return dockTime;
   }
   
   public ArrayList<Job> getJobs(){
	   return jobs;
   }
   
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