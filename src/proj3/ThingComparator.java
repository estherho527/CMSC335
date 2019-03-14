package proj3;
/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 3
 * 
 * File Name: Time.java
 * 
 * Description:
 * 
 * A ThingComparator implements the Comparator interface to allow for comparisons of Thing objects
 * -includes different ways to compare Things based on what they are
 * 
 * 
 */

import java.util.Comparator;

public class ThingComparator implements Comparator<Thing>{
	
	private String req;// requirement by which to compare by

	/*
	 * 
	 */
	protected ThingComparator(String target) {
		this.setReq(target.toLowerCase());
	}

	// setter
	private void setReq(String target) {
		req = target.toLowerCase();
	}

	// getter
	protected String getReq() {
		return req;
	}

	/**
	 * @inheritdoc
	 * @param thing1 <code>Thing</code> instance 1
	 * @param thing2 <code>Thing</code> instance 2
	 * @return<code>int</code>, either 0, 1, -1
	 */
	public int compare(Thing thing1, Thing thing2) {

		switch (this.getReq()) {
		
		// docks
		case "ship":
			return (((Dock) thing1).getShip().getName().compareTo(((Dock) thing2).getShip().getName()));
				
		// ships
		case "draft":
			if (((Ship) thing1).getDraft() == ((Ship) thing2).getDraft()) {
				return 0;
			} else if (((Ship) thing1).getDraft() > ((Ship) thing2).getDraft()) {
				return 1;
			} else {
				return -1;
			}
		case "length":
			if (((Ship) thing1).getLength() == ((Ship) thing2).getLength()) {
				return 0;
			} else if (((Ship) thing1).getLength() > ((Ship) thing2).getLength()) {
				return 1;
			} else {
				return -1;
			}
		case "weight":
			if (((Ship) thing1).getWeight() == ((Ship) thing2).getWeight()) {
				return 0;
			} else if (((Ship) thing1).getWeight() > ((Ship) thing2).getWeight()) {
				return 1;
			} else {
				return -1;
			}
		case "width":
			if (((Ship) thing1).getWidth() == ((Ship) thing2).getWidth()) {
				return 0;
			} else if (((Ship) thing1).getWidth() > ((Ship) thing2).getWidth()) {
				return 1;
			} else {
				return -1;
			}
		
		// persons
		case "skill":
			return ((Person)thing1).getSkill().compareTo(((Person)thing2).getSkill());
			
		// jobs
		case "duration":
			if (((Job) thing1).getDuration() == ((Job) thing2).getDuration()) {
				return 0;
			} else if (((Job) thing1).getDuration() > ((Job) thing2).getDuration()) {
				return 1;
			} else {
				return -1;
			}
			
		// any Thing
		case "name":
			return thing1.getName().compareTo(thing2.getName());
			
		// not able to compare by this requirement	
		default:
			return -1;
		}
	}

}// end ThingComparator class