package proj1;
/* Author: Esther Ho
 * Due Date: 11/5/2017
 * CMSC 335 Project 1
 * 
 * 
 * File Name: MyPerson.java
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
import java.util.Scanner;

class Person extends Thing {
   static int indexNew = 50000;
   static ArrayList <String> words;
  
   String skill = "";
  
   public Person (SeaPort msp) {
      if (words == null) readNamesFile ();
      name = words.get (rn.nextInt (words.size()));
      index = indexNew++;
      parent = msp.index;
      /*
      if (skillNames == null) readSkillsFile ();
      skill = skillNames.get (rn.nextInt (skillNames.size()));
      */
   } // create a random person
   
   // Person scanner constructor
   public Person(Scanner sc){
	   
	   super(sc);
	   if (sc.hasNext()) skill = sc.next();
   }
   
   void readNamesFile () {
      try {
         words = new ArrayList <> ();
         Scanner sp = new Scanner (new File ("personNames.txt"));
         while (sp.hasNext()) words.add (sp.next());
         System.out.println ("Names File size: " + words.size());  
         sp.close();
      } 
      catch (java.io.FileNotFoundException e) {System.out.println ("bad file");}
   } // end readWordsFile

   public String toString () {
      return "Person: " + super.toString();
   } // end toString

   public String getOutputFormat () {
      String st = "";
      st += String.format ("    person %20s %d %d %s\n", 
             name, index, parent, skill);
      return st;
   } // end method getOutputFormat
   

} // end MyPerson