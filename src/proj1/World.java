package proj1;
/* Author: Esther Ho
 * Due Date: 11/5/2017
 * CMSC 335 Project 1
 * 
 * 
 * File Name: MyWorld.java
 * 
 * Description:
 * 
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class World extends Thing {

   ArrayList <SeaPort> ports = new ArrayList <SeaPort> ();
   ArrayList <Thing> everyThing = new ArrayList<Thing>();
   Time time = new Time (0);
      
   public World(){ }
   
   public World (Scanner scan){
	   
	   super(scan); // parse out name, index, parent
	   System.out.println("scanning to make world!");
	   processFile(scan);
	   scan.close();
   }
   
   // getters
   public ArrayList<SeaPort> getPorts(){
	   return ports;
   }
   
   public ArrayList<Thing> getEveryThing(){
	   return everyThing;
   }
   
   public Time getTime(){
	   return time;
   }
   
   // used by CreateSeaPortDataFile
   public void createRandomPorts (int n, int numDocks, int numPass, int numCargo, int numJobs, int numPersons) {
      for (int i = 0; i < n; i++)
         ports.add (new SeaPort (numDocks, numPass, numCargo, numJobs, numPersons));
   } // end method createRandomPorts
   
   public void toArray (List <Thing> ma) {
      for (SeaPort mp: ports) mp.toArray (ma);
   } // end toArray
   
   // process whole file
   private void processFile(Scanner sc){

	   String line = "";
	   
	   while (sc.hasNextLine()){

		   line = sc.nextLine();
		   processLine(line);
	   }
   }
   
   // processes one line
   private void processLine (String st) { 
   //       System.out.println ("Processing >" + st + "<");
	   
	  String line = st;
      Scanner sc = new Scanner (st);
      if (!sc.hasNext()) {
    	  sc.close();
    	  return;
      }
         
      line = sc.next().trim();
      
      switch (line) {
      
         case "port": 
        	 addPort(sc); 
        	 break;

         case "dock": 
        	 addDock(sc);
        	 break;

         case "pship":
        	 addPShip(sc);
        	 break;

         case "cship":
        	 addCShip(sc);
        	 break;

         case "person":
        	 addPerson(sc);	
        	 break;

         case "job":
        	 addJob(sc);
        	 break;

         // no default
         default:
        	 break;
      }
    }// end processLine

   void addPort(Scanner sc){
	   
	   System.out.println("new port");
	   SeaPort nPort = new SeaPort(sc);
	   everyThing.add(nPort);
	   ports.add(nPort);
   }
   
   void addDock(Scanner sc){
	   
	   System.out.println("new dock");
	   Dock nDock = new Dock(sc);
	   everyThing.add(nDock);
	   
	   // add dock to parent sea port
	   getSeaPortByIndex(nDock.getParent()).docks.add(nDock);
   }
   
   void addPShip(Scanner sc){
       
	   System.out.println("new pship");
	   PassengerShip pship = new PassengerShip(sc);
	   everyThing.add(pship);
	   
	   assignShip(pship);
   }
   
   void addCShip(Scanner sc){
	   
	   System.out.println("new cship");
       CargoShip cship = new CargoShip(sc);
       everyThing.add(cship);

       assignShip(cship);
   }
   
   void addPerson(Scanner sc){
	   
	   System.out.println("new person");
	   Person nperson = new Person(sc);
       everyThing.add(nperson);
       
       // add person to parent sea port
	   getSeaPortByIndex(nperson.getParent()).persons.add(nperson);
   }
   
   void addJob(Scanner sc){
       
	   System.out.println("new job");
	   Job njob = new Job(sc);
       everyThing.add(njob);
   }
   
    Ship getShipByIndex (int x) {
    	for (SeaPort msp: ports)
    		for (Ship ms: msp.ships)
    			if (ms.index == x) 
    				return ms;
    	return null;
    } // end getShipByIndex
      
    void assignShip (Ship ms) {
        Dock md = getDockByIndex (ms.parent);
        if (md == null) {
           getSeaPortByIndex (ms.parent).ships.add (ms);
           getSeaPortByIndex (ms.parent).que.add (ms);
           return;
        }
        md.ship = ms;
        getSeaPortByIndex (md.parent).ships.add (ms);
     } // end method assignShip  
    

    
    Dock getDockByIndex(int x){
    	
        for (SeaPort sp: ports){
        	for (Dock d: sp.docks){
        		if (d.index == x)
        			return d;
        	}
        }
    	return null;
    }// end getDockByIndex
    
    SeaPort getSeaPortByIndex(int x){
    	
        for (SeaPort sp: ports){	
        	if (sp.index == x)
        		return sp;
        }
    	return null;
    }// end getSeaPortByIndex()
    
    /*
     * searchByIndex()
     * - returns search results by Index as a String
     */
    protected String searchByIndex(int toFind){
    
    	String result = "";
    	
    	
    	for (Thing t : everyThing){
    		
    		if (t.getIndex() == toFind)
    			result += t.getOutputFormat() + "\n";
    	}
    
    	return result;
    }
    
    /*
     * searchByType()
     * - returns search results by Type as a String
     */
    protected String searchByType(String toFind){

    	// standardize search term to find class easier
    	String target = toFind.toLowerCase().replace(" ", "");  
    	
    	String result = "";
    	
    	for (SeaPort sp : ports){
    		
    		switch (target){
    		
    			case "seaport":
    				result = traverseType(ports);
    				break;
    			
    			case "dock":
    				result = traverseType(sp.docks);
    				break;
    				
    			case "ship":
    				result = traverseType(sp.ships);
    				break;
    				
    			case "pship":
    			case "passengership":
    				result = traverseType(sp.ships, PassengerShip.class);
    				break;
    				
    			case "cship":
    			case "cargoship":
    				result = traverseType(sp.ships, CargoShip.class);
    				break;
    				
    			case "person":
    				result = traverseType(sp.persons);
    				break;
    				
    			default:
    				break;
    		}// end switch
    	}
    	
    	return result;
    } // end searchByType
    
    /*
     * traverseIndex()
     * - finds all instances of the type and returns as a String
     * - called when searching through ships, to differentiate if it's a passenger ship or cargo ship
     */
    private <T> String traverseType(ArrayList<T> list, Class<?> check){
    	
    	String result = "";
    	
    	for (T t: list){
    		
    		if(t.getClass() == check)
    		result += ((Thing)t).getOutputFormat() + "\n";
    	}
    	
    	return result;
    }// end traverseType() version 1
    
    /*
     * traverseIndex()
     * -finds all instances of the type and returns as a String
     */
    private <T> String traverseType(ArrayList<T> list){
    	
    	String result = "";
    	
    	for (T t: list){
    		
    		result += t + "\n";
    	}
    	
    	return result;
    }// end traverseType() version 2
    
    /*
     * searchByName()
     * - combo box index = 2
     */
    protected String searchByName(String toFind){

    	String result = "";
    	
    	for (Thing t : everyThing){
    		
    		if (t.getName() == toFind)
    			result += t.getOutputFormat() + "\n";
    	}
    	return result;
    }

    protected String searchBySkill(String toFind){
    	
    	String result = "";
    	
    	for (SeaPort sp : ports){
    		for(Person p: sp.persons){
    			if (p.skill == toFind)
    				result += p.getOutputFormat() + "\n";
    		}
    	}
    	
    	return result;
    }
    
    public void clear(){
    	
    	if (ports.size() > 0)
    		ports.clear();
    }// end clear
    
    /*
    public String getOutputFormat(){
    	return "";
    }
    */
    
   /* 
    * toString()
    */
   public String toString(){
	   
	   String st = ">>>>> The World: \n\n";
	   
	   for (SeaPort sp: ports)
		   st += sp + "\n";
	   
	   return st + "\n";
   }// end toString()
   
} // end class World