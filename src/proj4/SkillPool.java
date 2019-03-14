package proj4;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 4
 * 
 * File Name: SkillPool.java
 * 
 * Description:
 * A SkillPool object represents a resource pool for Job threads, consisting of a 
 * collection of Person objects with the same skill.
 * Created to help control access to the resource pool
 * 
 * 
 * 
 * DISCLAIMER: based off of file written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */

public class SkillPool {

	// Resource pool variables
	private ArrayList<Person> persons; // collection of Persons
	private int numFree; // number of Persons free to work
	private int totalNumPersons; // total number of Person with the skill
	private String skill; // skill shared by Persons
	private String seaPort; // port where persons are at
	
	// GUI variables
	private JPanel skillRowPanel; // row for resource "table"
	private JLabel seaPortLabel, skillLabel, numFreeLabel, totalLabel;
	private JTextArea log; 
	
	public SkillPool(ArrayList<Person> people, String sharedSkill, String parentPort) {

		persons = people;
		totalNumPersons = persons.size();
		numFree = totalNumPersons;
		skill = sharedSkill;
		seaPort = parentPort;
		
		// row format:
		// skill	seaPort		#Free	Total#
		skillRowPanel = new JPanel(new GridLayout(1,4));
		skillLabel = new JLabel(skill, JLabel.CENTER);
		seaPortLabel = new JLabel(seaPort, JLabel.CENTER);
		numFreeLabel = new JLabel("" + numFree, JLabel.CENTER);
		totalLabel = new JLabel("" + totalNumPersons, JLabel.CENTER);
	
		//skillRowPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		skillLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		skillLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
		seaPortLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		seaPortLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
		numFreeLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		numFreeLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
		totalLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		totalLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
		
		skillRowPanel.add(skillLabel);
		skillRowPanel.add(seaPortLabel);
		skillRowPanel.add(numFreeLabel);
		skillRowPanel.add(totalLabel);
		
		// used for ResourceLogTextArea in main GUI, not part of skillRowPanel
		log = new JTextArea();
		
	}// end constructor
	
	// getters
	public ArrayList<Person> getPersonPool(){
		return persons;
	}
	
	public int getFreeWorkers(){
		return numFree;
	}
	
	public int getSkillPoolSize(){
		return totalNumPersons;
	}
	
	public String getSharedSkill(){
		return skill;
	}
	
	public String getPortName(){
		return seaPort;
	}
	
	public JTextArea getSkillPoolLog(){
		return log;
	}
	
	protected void setPoolLog(JTextArea toSet){
		log = toSet;
	}

	/* 
	 * addPerson()
	 * 
	 * adds a Person with the skill to the pool, and updates the counts
	 */
	protected synchronized void addPerson(Person toAdd){
		
		persons.add(toAdd);
		totalNumPersons++;
		numFree++;
		
		numFreeLabel.setText("" + numFree);
		totalLabel.setText("" + totalNumPersons);
		skillRowPanel.revalidate();
		
	}// end addPerson
	
	/*
	 * assignPersonForJob()
	 * 
	 * Person's skill matches Job requirement
	 * -sets Person as busy and updates count
	 */
	protected synchronized void assignPersonForJob(Person worker){
		
		System.out.println(worker.getName() + " (skill: " + skill + " assigned!");
		worker.setIsFreeToWork(false);
		numFree--;
		
		numFreeLabel.setText("" + numFree);
		log.append("Skill: " + worker.getSkill() + " (worker: " + worker.getName() + ") assigned to a job" + "\n");
		skillRowPanel.revalidate();
		
	}// end assignPersonForJob
	
	/*
	 * returnPersonToPool()
	 * 
	 * -set Person as free and updates count
	 */
	protected synchronized void returnPersonAfterJobToPool(Person worker){
		
		System.out.println(worker.getName() + " (skill: " + skill + ") released");
		worker.setIsFreeToWork(true);
		numFree++;
		
		numFreeLabel.setText("" + numFree);
		log.append("Skill: " + worker.getSkill() + " (worker: " + worker.getName() + ") returned after job" + "\n");
		skillRowPanel.revalidate();
		
	}// end returnPersonToPool
	
	/*
	 * releasePersonToPool()
	 * 
	 * -releases Person who did not end up assigned to Job
	 */
	protected synchronized void releasePersonToPool(Person worker){
		
		System.out.println(worker.getName() + " (skill: " + skill + ") didn't get Job");
		worker.setIsFreeToWork(true);
		numFree++;
		
		numFreeLabel.setText("" + numFree);
		log.append("Skill: " + worker.getSkill() + " (worker: " + worker.getName() + ") could not work on job" + "\n");
		skillRowPanel.revalidate();
		
	}// end returnPersonToPool
	
	/*
	 * getSkillPoolRow()
	 * 
	 * returns JPanel row to build a "table"
	 */
	protected JPanel getSkillPoolRow(){
		
		return skillRowPanel;
	}
	
	public String toString(){
		
		String st = "SkillPool skill: " + skill + ", # total = " + totalNumPersons + ", port: " + seaPort + "\n";
				
		return st;
	}
}// end SkillPool class
