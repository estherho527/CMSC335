package proj1;
/* Author: Esther Ho
 * Due Date: 11/5/2017
 * CMSC 335 Project 1
 * 
 * 
 * File Name: MyTime.java
 * 
 * Description:
 * 
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */


class Time {
   int time = 0; // measured in seconds
   
   public Time (int t) {time = t;}
   
   public String toString () {
      return String.format ("%d %d:%d:%d", time/60/60/24, (time/60/60)%24, (time/60)%60, time%60);
   } // end method toString

} // end class MyTime
