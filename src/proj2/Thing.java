package proj2;
/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 2
 * 
 * File Name: Thing.java
 * 
 * Description:
 * A Thing has a name, index, and parent. 
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Thing implements Comparable <Thing> {
   static java.util.Random rn = new java.util.Random ();
   static ArrayList <String> skillNames = null; // to read file only once
   
   String name = "";
   int index = 0;
   int parent = 0;
   
   
   public Thing () {}
   
   public Thing (Scanner sc) {
	   if (sc.hasNext()) name = sc.next();
	   if (sc.hasNextInt()) index = sc.nextInt();
	   if (sc.hasNextInt()) parent = sc.nextInt();
   } // end Scanner constructor
   
   // setters
   public void setName(String n){
	   this.name = n;
   }
   
   public void setIndes(int i){
	   this.index = i;
   }
   
   public void setParent(int p){
	   this.parent = p;
   }
   
   // getters
   public String getName(){
	   return name;
   }
   
   public int getIndex(){
	   return index;
   }
   
   public int getParent(){
	   return parent;
   }
   
   void toArray (List <Thing> mta) {
      mta.toArray ();
   } // end method add
   
   public int compareTo (Thing m) {
	   
	   // can only return true/1 if index, parent, and name match
	   if (name.equals(m.name) && (index == m.index) && (parent == m.parent))
		   return 1;

	   return 0;
   } // end method compareTo > Comparable
   
   
   public String getOutputFormat () {
      return toString();
   } // default toFileString method in MyThing
   

   public String toString(){
	   
	   return name + " " + index;
   }// end toString()
   
} // end class Thing