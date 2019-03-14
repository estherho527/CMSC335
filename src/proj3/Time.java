package proj3;
/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 3
 * 
 * File Name: Time.java
 * 
 * Description:
 * A Time object represents time passed
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
