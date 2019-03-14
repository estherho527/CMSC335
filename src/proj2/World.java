package proj2;
/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 2
 * 
 * File Name: World.java
 * 
 * Description: A World is a Thing that can hold all other Things in the SeaPort project
 * -This class is where process() is called from SeaPortProgramGUI.java, and where the local HashMaps are
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


class World extends Thing {

	ArrayList <SeaPort> ports = new ArrayList <SeaPort> ();
	ArrayList <Thing> everyThing = new ArrayList<Thing>();
	ArrayList <Dock> docks = new ArrayList<Dock>();
	ArrayList <Ship> ships = new ArrayList<Ship>();
	ArrayList <Person> persons = new ArrayList<Person>();
	ArrayList <Job> jobs = new ArrayList<Job>();
	Time time = new Time (0);

	public World(){ }

	public World (Scanner scan){

		super(scan); // parse out name, index, parent
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

	public ArrayList<Dock> getDocks(){
		return docks;
	}

	public ArrayList<Ship> getShips(){
		return ships;
	}

	public ArrayList<Person> getPersons(){
		return persons;
	}

	public ArrayList<Job> getJobs(){
		return jobs;
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
		String token = "";

		// local hash maps where processing happens
		HashMap<Integer, SeaPort> portMap = new HashMap<Integer, SeaPort>();
		HashMap<Integer, Dock> dockMap = new HashMap<Integer, Dock>();
		HashMap<Integer, Ship> shipMap = new HashMap<Integer, Ship>();
		HashMap<Integer, Person> personMap = new HashMap<Integer, Person>();

		// scan every line in the file
		while (sc.hasNextLine()){

			line = sc.nextLine();
			
			// skip blank lines
			if (line.length() == 0)
				continue;
			
			Scanner tScan = new Scanner(line);
			
			// grab next token
		      token = tScan.next().trim();
		      
		    // add Thing based on what Class it is
			switch (token) {

			case "port": 
				SeaPort nPort = new SeaPort(tScan);
				everyThing.add(nPort);
				ports.add(nPort);
				portMap.put(nPort.getIndex(), nPort);
				break;

			case "dock": 
				Dock nDock = new Dock(tScan);
				everyThing.add(nDock);

				// add dock to parent sea port
				(portMap.get(nDock.getParent())).docks.add(nDock);

				docks.add(nDock);
				dockMap.put(nDock.getIndex(), nDock);
				break;

			case "pship":
				PassengerShip pship = new PassengerShip(tScan);
				everyThing.add(pship);

				assignShip(pship, portMap, dockMap);

				ships.add(pship);
				shipMap.put(pship.getIndex(), pship);        	 
				break;

			case "cship":
				CargoShip cship = new CargoShip(tScan);
				everyThing.add(cship);

				assignShip(cship);

				ships.add(cship);
				shipMap.put(cship.getIndex(), cship);        	 
				break;

			case "person":
				Person nperson = new Person(tScan);
				everyThing.add(nperson);

				// add person to parent sea port
				getSeaPortByIndex(nperson.getParent()).persons.add(nperson);

				persons.add(nperson);
				personMap.put(nperson.getIndex(), nperson);
				break;

			case "job":
				Job njob = new Job(tScan);
				everyThing.add(njob);
				jobs.add(njob);        	
				break;

				// no default
			default:
				break;
			}// end switch
		} // end while scanning
		
	}// end process
	
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

	// proj 2 assignShip version using HashMap
	void assignShip (Ship ms, HashMap<Integer, SeaPort> spMap, HashMap<Integer, Dock> dMap) {
		Dock md = dMap.get(ms.getParent());
		if (md == null) {
			spMap.get(ms.getParent()).ships.add (ms);
			spMap.get(ms.getParent()).que.add (ms);
			return;
		}
		md.ship = ms;
		spMap.get(md.getParent()).ships.add (ms);
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
	protected String searchByClass(String toFind){

		// standardize search term to find class easier
		String target = toFind.toLowerCase().replace(" ", "");  

		String result = "";

		for (SeaPort sp : ports){

			switch (target){

			case "seaport":
				result += traverseType(ports);
				break;

			case "dock":
				result += traverseType(sp.docks);
				break;

			case "ship":
				result += traverseType(sp.ships);
				break;

			case "pship":
			case "passengership":
				result += traverseType(sp.ships, PassengerShip.class);
				break;

			case "cship":
			case "cargoship":
				result += traverseType(sp.ships, CargoShip.class);
				break;

			case "person":
				result += traverseType(sp.persons);
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

			result += ((Thing)t).getOutputFormat() + "\n";
		}

		return result;
	}// end traverseType() version 2

	/*
	 * searchByName()
	 * - returns result as String
	 */
	protected String searchByName(String toFind){

		String result = "";

		for (Thing t : everyThing){

			if (t.getName().equals(toFind))
				result += t.getOutputFormat() + "\n";
		}
		return result;
	}

	/*
	 * searchBySkill()
	 * -returns result as String
	 */
	protected String searchBySkill(String toFind){

		String result = "";

		for (SeaPort sp : ports){
			for(Person p: sp.persons){
				if (p.skill.equals(toFind))
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
	 * getOutputFormat()
	 * -String version of this object alone
	 */
	public String getOutputFormat(){
		return "";
	}


	/* 
	 * toString()
	 * - used to display whole world, with other Things
	 */
	public String toString(){

		String st = ">>>>> The World: \n\n";

		for (SeaPort sp: ports)
			st += sp + "\n";

		return st + "\n";
	}// end toString()

} // end class World