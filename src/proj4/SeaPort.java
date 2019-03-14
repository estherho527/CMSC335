package proj4;
/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 4
 * 
 * 
 * File Name: SeaPort.java
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
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.JTextArea;

class SeaPort extends Thing {
   static int indexNew = 10000;
   static ArrayList <String> portNames = null;
   
   double latitude  = 0;
   double longitude = 0;
   ArrayList <Dock>   docks   = new ArrayList <> (); 
   ArrayList <Ship>   que     = new ArrayList <> (); // all ships waiting
   ArrayList <Ship>   ships   = new ArrayList <> (); // all ships total, including those at docks
   ArrayList <Person> persons = new ArrayList <> ();
   ArrayList <Job> 	  jobs 	  = new ArrayList <> ();
   
   HashMap<String, SkillPool> skillPools = new HashMap<>(); // collection of SkillPools, indexed by skill
     
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
   
   protected void readPortsFile () {
      try {
         portNames = new ArrayList <> ();
         Scanner sp = new Scanner (new File ("portNames.txt"));
         while (sp.hasNext()) portNames.add (sp.next());
         System.out.println ("Ports file size: " + portNames.size());  
         sp.close();
      } 
      catch (java.io.FileNotFoundException e) {System.out.println ("bad file");}
   } // end method readPortsFile
   
   /*
    * getNeededWorkers()
    * 
    * -gathers all needed workers for Job by required skills
    * -if we cannot gather all required workers, 
    *   make sure to return them and not hold them to prevent deadlock
    */
   protected synchronized ArrayList<Person> getNeededWorkers(Job job){
	   
	   System.out.println("looking for " + job.name + "'s requirements");
	   
	   SkillPool skillPool;
	   ArrayList<Person> possibleWorkers = new ArrayList<Person>();
	   boolean gotAllNeededSkills = true;
	   String workerLog = "";
	   Person worker;
	   JTextArea poolLog;
	   
	   // HashMap tracking requirements:
	   // Skill, # needed for job
	   HashMap<String, Integer> neededSkillsMap = new HashMap<String, Integer>(); 
	   
	   // HashMap tracking requirements filled:
	   // skill, filled = true || notFilled = false
	   HashMap<String, Boolean> reqFilledMap = new HashMap<String, Boolean>();
	   
	   // fill up hashMap
	   for (String skill : job.requirements){
		   reqFilledMap.put(skill, false);
	   }
	   
	   // go through all requirements of a Job
	   everyReq:
	   for (String skill : job.requirements){
		   
		   System.out.println(job.name + "checking for skill: " + skill + " @" + getName());
		   
		   // if skill doesn't exist, adds skill to hashmap, with needed count set to 1
		   // if it already exists, increment needed count
		   neededSkillsMap.merge(skill, 1, Integer::sum);
		   
		   // get corresponding skillPool
		   skillPool = skillPools.get(skill);
		   
		   // no workers for skill --> kill job
		   if(skillPool == null || skillPool.getSkillPoolSize() == 0){
			   
			   System.out.println("no workers with needed skill exist");
			   
			   // output message to job logs
			   job.getJobLog().append("CANCEL: No workers with needed skill: [" + skill + "] found for " + job.getName()
					   + " (Ship: " + job.getParentShip().getName() + ")\n");
			   
			   // release all resources
			   releaseWorkers(possibleWorkers);
			   job.killJobThread();
			   return possibleWorkers; //  empty
		   }
		   
		   // not enough workers with particular skill
		   else if (skillPool.getSkillPoolSize() < neededSkillsMap.get(skill)){
			   
			   System.out.println("not enough workers with skill");
			   
			   // output message to job logs
			   job.getJobLog().append("CANCEL: Not enough workers with needed skill [" + skill + "] found for " + job.getName()
			   		+ " (Ship: " + job.getParentShip().getName() + ")\n");
			   
			   releaseWorkers(possibleWorkers);
			   job.killJobThread();
			   return possibleWorkers; // empty 
		   }
		   
		   // 
		   else{
			   
			   // find free worker with required skill in pool
			   for (Person possible: skillPool.getPersonPool()){
				   
				   // found free worker for this one requirement
				   if (possible.isFreeToWork){
					   
					   System.out.println("person with skill found!");
					   
					   skillPool.assignPersonForJob(possible);
					   possibleWorkers.add(possible);
					   
					   reqFilledMap.put(skill, true);
					   
					   continue everyReq; // jump to next iter of outer for loop
				   }
			   }
			   
			   // no workers are free, job cannot start
			   gotAllNeededSkills = false;
			   break; // no need to look more
		   }
		   
	   }// end for loop
	   
	   // double check all requirements filled (all true values)
	   for(Boolean bool : reqFilledMap.values()){
		   if (!bool){
			   gotAllNeededSkills = false;
			   break;
		   }
	   }
	   
	   // if requirements all met, pass workers to Job thread through return
	   if(gotAllNeededSkills){
  
		   // update logs for worker or jobs
		   workerLog += "GO: Workers on " + job.getName()
		   		+ " (Ship: " + job.getParentShip().getName() + "):";
		   
		   int size = possibleWorkers.size();
		   
		   for(int i = 0; i < size; i++) {
			   
			   worker = possibleWorkers.get(i);
			   poolLog = worker.getPool().getSkillPoolLog();
			   
			   workerLog += worker.getName();
			   
			   if (size >= 3 && i <= size - 3)
				   workerLog += ", ";
			   
			   else if (i == size - 2)
				   workerLog += " & ";

			   poolLog.append("Skill: " + worker.getSkill() + " (worker: " + " Worker: " + worker.getName() + ") assigned to " + job.getName() + "\n");
		   }
		   
		   job.getJobLog().append(workerLog + "\n");
	   }
	   
	   // release all possible workers if not all requirements met
	   else{
		   
		   ArrayList<String> reqNeeded = new ArrayList<String>();
		   
		   // format req into a string to write in Job Log
		   for(String skill : job.requirements){
			   
			   if (reqFilledMap.get(skill) == false)
				   reqNeeded.add(skill);
			   
		   }
		   
		   releaseWorkers(possibleWorkers);
		   job.getJobLog().append("WAIT: " + job.getName() + " doesn't have enough workers available for reqs:" + reqNeeded.toString() + ". Relasing workers.\n");
	   }
		  
	   return possibleWorkers;
	   
   }// end getNeededWorkers
   
   /*
    * returnWorkers
    * 
    * -returns workers to original SkillPools after Job
    */
   protected synchronized void returnWorkers(ArrayList<Person> resources){
	   
	   SkillPool skPool; 
	   //JTextArea poolLog;
	   
	   for(Person worker : resources){
		   
		   skPool = skillPools.get(worker.getSkill());
		   skPool.returnPersonAfterJobToPool(worker);
		   
	   }
   }// end returnWorkers
   
   /*
    * releaseWorkers()
    * 
    * -release workers to original SkillPools who are unable to work on Job
    */
   protected synchronized void releaseWorkers(ArrayList<Person> resources){
	   
	   SkillPool skPool; 
	   
	   for(Person worker : resources){
		   
		   skPool = skillPools.get(worker.getSkill());
		   skPool.releasePersonToPool(worker);   	  
	   }
	   
	   resources.clear();
   }// end returnWorkers
   
   /*
    * sortWorkersIntoPools()
    * 
    * -called after file is completely read
    * -sorts workers by their skill into SkillPools that 
    *   act as resource pools for Job threads
    */
   protected void sortWorkersIntoPools(JTextArea poolLog){
	   
	   SkillPool skillPool;
	   
	   for (Person worker: persons){
		   
		   String skill = worker.getSkill();
		   
		   // if not, make a new resource pool and add that to HashMap of SkillPools
		   if (!skillPools.containsKey(skill)){
			   
			   skillPool = new SkillPool(new ArrayList<Person>(), skill, name);
			   skillPools.put(skill, skillPool);
			   skillPool.setPoolLog(poolLog);
		   }
		   
		   skillPool = skillPools.get(skill);
		   
		   // add worker to respective pool
		   skillPool.addPerson(worker);
		   worker.setSkillPool(skillPool);
	   }
	   
	   System.out.println("All created skill pools:");
	   for(SkillPool skp: skillPools.values()){
		   
		   if (skp.getSkillPoolLog() == null)
			   skp.setPoolLog(poolLog);
		   System.out.println(skp.getSharedSkill() + ": # total = " + skp.getSkillPoolSize());
	   }
	   
   }// end sortWorkersIntoPools
   
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
	   st += "\n\n --- List of all jobs:";
	   for (Job mj: jobs) st += "\n   > " + mj;
      return st;
   } // end toString()
   
} // end class SeaPort