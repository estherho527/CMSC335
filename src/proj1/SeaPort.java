package proj1;
/* Author: Esther Ho
 * Due Date: 
 * CMSC 335 Project 1
 * 
 * 
 * File Name: MySeaPort.java
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

class SeaPort extends Thing {
   static int indexNew = 10000;
   static ArrayList <String> portNames = null;
   
   double latitude  = 0;
   double longitude = 0;
   ArrayList <Dock>   docks   = new ArrayList <> ();
   ArrayList <Ship>   que     = new ArrayList <> ();
   ArrayList <Ship>   ships   = new ArrayList <> ();
   ArrayList <Person> persons = new ArrayList <> ();
     
   // SeaPort Scanner Constructor
   public SeaPort(Scanner sc){
	   
	   super(sc);
	   parent = 0; // null parent
   }
   
   public SeaPort (int numDocks, int numPass, int numCargo, int numJobs, int numPersons) {
      if (portNames == null) readPortsFile ();
      name = portNames.get (rn.nextInt (portNames.size()));
      index = indexNew++;
      parent = 0;
      int remainingDocks = rn.nextInt (numDocks) + 5;
      while (remainingDocks > 0 && numPass > 0) {
         PassengerShip mps = new PassengerShip (true, numJobs); // random ship
         ships.add (mps);
         Dock md = new Dock (true); // random dock
         docks.add (md);
         md.ship = mps;
         md.parent = index;
         mps.parent = md.index;
         numPass --;
         remainingDocks--;
      } // end adding passenger ships first
      while (numPass > 0) {
         PassengerShip mps = new PassengerShip (true, numJobs); // random ship
         ships.add (mps);
         que.add (mps);
         mps.parent = index;
         numPass --;
      } // end remaining passenger ships
      while (remainingDocks > 0 && numCargo > 0) {
         CargoShip mpc = new CargoShip (true, numJobs); // random ship
         ships.add (mpc);
         Dock md = new Dock (true); // random dock
         docks.add (md);
         md.ship = mpc;
         md.parent = index;
         mpc.parent = md.index;
         numCargo --;
         remainingDocks--;
      } // end adding passenger ships first
      while (numCargo > 0) {
         CargoShip mpc = new CargoShip (true, numJobs); // random ship
         ships.add (mpc);
         que.add (mpc);
         mpc.parent = index;
         numCargo --;
      } // end remaining passenger ships
      
      for (int i = 0; i < rn.nextInt (numPersons) + 5; i++) {
         persons.add (new Person (this));
      }
   } // end list of port names constructor - creates a random port
   
   void readPortsFile () {
      try {
         portNames = new ArrayList <> ();
         Scanner sp = new Scanner (new File ("portNames.txt"));
         while (sp.hasNext()) portNames.add (sp.next());
         System.out.println ("Ports file size: " + portNames.size());  
         sp.close();
      } 
      catch (java.io.FileNotFoundException e) {System.out.println ("bad file");}
   } // end method readPortsFile
   
   void toArray (List <Thing> ma) {
      ma.add (this);
      for (Dock   md: docks  ) md.toArray (ma);
      for (Ship   ms: ships  ) ms.toArray (ma);
      for (Person mp: persons) mp.toArray (ma);
   } // end toArray in SeaPort

   public String getOutputFormat () {
      return String.format ("port %s %d %d\n", name, index, 0);
   } // end method getOutputFormat

   //toString
   public String toString(){
	   String st = "\n\nSeaPort: " + super.toString() + "\n";
	   for (Dock md: docks) st += "\n" + md + "\n";
	   st += "\n\n --- List of all ships in que:";
	   for (Ship ms: que ) st += "\n   > " + ms;
	   st += "\n\n --- List of all ships:";
	   for (Ship ms: ships) st += "\n   > " + ms;
	   st += "\n\n --- List of all persons:";
	   for (Person mp: persons) st += "\n   > " + mp;
      return st;
   } // end toString()
   
} // end class SeaPort