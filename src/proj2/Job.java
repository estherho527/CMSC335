package proj2;
/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 2
 * 
 * File Name: Job.java
 * 
 * Description:
 * A Job is represented by a list of required skills (requirements), and its duration
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */

import java.util.ArrayList;
import java.util.Scanner;

class Job extends Thing {
   static int indexNew = 60000;
   static ArrayList <String> words;
  
   double duration = 0;
   ArrayList <String> requirements = new ArrayList <> (); // skills
  // eg {"painter", "painter", "painter", "carpenter"};

   public Job (Ship ms) {
	   
      name = String.format ("Job_%d_%d_%d", 
                rn.nextInt(90)+10, rn.nextInt(90)+10, rn.nextInt(90)+10);
      index = indexNew++;
      parent = ms.index;
      duration = rn.nextDouble () * 100 + 20;
   } // create a random job
   
   // Job Scanner constructor
   public Job (Scanner sc){
	   
	   super(sc);
	   
	   // there can be zero or more requirements/skills
	   while (sc.hasNext()) requirements.add(sc.next());
	  
   }
   
   // getter
   public double getDuration(){
	   return duration;
   }
   
   public String getOutputFormat () {
      String st = "";
      st += String.format ("    job %20s %d %d %.2f", 
             name, index, parent, duration);
      for (String sr: requirements) st += " " + sr;
      return st + "\n";
   } // end method getOutputFormat
   
   /* ADDED
    * toString()
    */
   public String toString(){
	   
	   String st = "Job: " + name;
	   for (String sr: requirements)
		   st+= sr;
		    
	   return st + "\n";
   }
   
} // end MyThing