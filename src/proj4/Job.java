package proj4;
/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 4
 * 
 * File Name: Job.java
 * 
 * Description:
 * A Job is represented by a list of required skills (requirements), and its duration
 * -Each Job is associated with a Thread object
 * -the Thread can only be started and run when its parent Ship is docked
 * -this class handles docking/undocking of ships
 * 
 * 
 * DISCLAIMER: referred to Cave project by Nicholas Duchon (2016) 
 *    for this SeaPort project
 */

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

class Job extends Thing implements Runnable {
	static int indexNew = 60000;
	static ArrayList <String> words;
	
	double duration = 0;

	ArrayList <String> requirements = new ArrayList <> (); // skills required for job to go
	//ArrayList <String> reqStillNeeded = new ArrayList<> (); // still need to find
	
	// project 3 -thread related
	private Ship ship = null;
	
	private boolean goFlag = true; // f when suspended
	private boolean noKillFlag = true; // f when job cancelled
	private boolean done = false; // t when job 100% finished
	private Status status = Status.SUSPENDED;
	Thread thread; 

	private ArrayList<Person> workers; // for proj 4
	
	enum Status {RUNNING, SUSPENDED, WAITING_FOR_DOCK, GETTING_WORKERS, WAITING_FOR_WORKERS,
		DONE, CANCELLED};

	// GUI 
	private JPanel oneJobPanel; 
	private JLabel shipJobLabel, dockPortLabel, reqLeftLabel;
	private JProgressBar progressBar = new JProgressBar();
	private JButton jbGo = new JButton ("Pause");
	private JButton jbKill = new JButton("Cancel");
	private JTextArea log; 
	
	// constructor for CreateSeaProjectDataFile.java
	public Job (Ship ms) {

		name = String.format ("Job_%d_%d_%d", 
				rn.nextInt(90)+10, rn.nextInt(90)+10, rn.nextInt(90)+10);
		index = indexNew++;
		parent = ms.index;
		duration = rn.nextDouble () * 100 + 20;
	} // create a random job

	// Scanner constructor for Project 1 & 2
	public Job (Scanner sc){

		super(sc);

		// there can be zero or more requirements/skills
		while (sc.hasNext()) requirements.add(sc.next());
	}

	// Scanner constructor for Proj 3 & 4
	public Job (Scanner sc, HashMap <Integer, Ship> shipList) {
		
		// name, index, parent index
		super(sc);
		
		ship = (Ship) (shipList.get (getParent()));
				
		// parse duration and requirement list
		if (sc.hasNextDouble())
			duration = sc.nextDouble ();
		
		while (sc.hasNext()) 
			requirements.add(sc.next());
		
		// set GUI elements
		
		// each job is one row with 5 sections:
		// shipName:jobName  |  dockName @ portName |  ProgressBar | PauseButton | KillButton
		oneJobPanel = new JPanel(new GridLayout(1,5));
		
		shipJobLabel = new JLabel(ship.getName() + ":" + getName(), JLabel.CENTER);
		shipJobLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
		shipJobLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		dockPortLabel = new JLabel("In queue", JLabel.CENTER);
		dockPortLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
		dockPortLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		progressBar = new JProgressBar ();
		progressBar.setStringPainted (true);
		progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		jbGo.setFont(new java.awt.Font("Monospaced", 0, 12));
		jbGo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		jbKill.setFont(new java.awt.Font("Monospaced", 0, 12));
		jbKill.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		oneJobPanel.add(shipJobLabel);
		oneJobPanel.add(dockPortLabel);
		oneJobPanel.add(progressBar);
		oneJobPanel.add(jbGo);
		oneJobPanel.add(jbKill);

		jbGo.addActionListener (new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				toggleGoFlag ();
			}
		});

		jbKill.addActionListener (new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				killJobThread();
			}
		});

		log = new JTextArea();
		thread = new Thread (this, name); // don't start job till whole file read
	} // end constructor

	// getters
	public double getDuration(){
		return duration;
	}
	
	public ArrayList<String> getReq(){
		return requirements;
	}
	
	public Ship getParentShip(){
		return ship;
	}
	
	public SeaPort getParentPort(){
		return ship.getPort();
	}

	public JTextArea getJobLog(){
		return log;
	}
	
	protected void setJobLog(JTextArea toSet){
		log = toSet;
	}
	
	protected void setReqLeftLabel(String toSet){
		reqLeftLabel.setText(toSet);
	}

	public String getOutputFormat () {
		String st = "";
		st += String.format ("    job %20s %d %d %.2f", 
				name, index, parent, duration);
		for (String sr: requirements) st += " " + sr;
		return st + "\n";
	} // end method getOutputFormat

	/* ADDED
	 * toString()
	 */
	public String toString(){

		String st = "Job: " + super.toString() + ", Duration: " + duration + "\n\tRequirements:";
		
		if (requirements.isEmpty())
			st+= " none";

		else
			for (String sr: requirements)
				st+= "" +  sr;

		return st + "\n";
	}

	public void toggleGoFlag () {
		goFlag = !goFlag;
		oneJobPanel.revalidate();
	} // end method toggleRunFlag
	
	public void startJobThread(){
		
		thread.start();
	} // end startJobThread
	
	public void killJobThread(){
		noKillFlag = false;
		oneJobPanel.revalidate();
	}// end killJobThread
	
	/*
	 * showStatus
	 * -updates the text of buttons depending on the job's status
	 */
	void showStatus (Status st) {
		status = st;
		switch (status) {
		case RUNNING:
			jbGo.setBackground (Color.green);
			jbGo.setText ("Running");
			break;
		case SUSPENDED:
			jbGo.setBackground (Color.yellow);
			jbGo.setText ("Suspended");
			break;
		case WAITING_FOR_DOCK:
			jbGo.setBackground (Color.orange);
			jbGo.setText ("Wait: Dock");
			break;
		case GETTING_WORKERS:
			jbGo.setBackground(Color.orange);
			jbGo.setText("Get: workers");
			break;
		case WAITING_FOR_WORKERS:
			jbGo.setBackground(Color.orange);
			jbGo.setText("Wait: workers");
			break;
		case DONE:
			jbGo.setBackground (Color.cyan);
			jbGo.setText ("Done");
			break;
		case CANCELLED:
			jbKill.setBackground(Color.red);
			jbGo.setBackground(Color.red);
			jbGo.setText("CANCELLED");
		} // end switch on status
		
		// make sure color shows
		jbGo.setOpaque(true);
		jbGo.setBorderPainted(true);
		jbKill.setOpaque(true);
		jbKill.setBorderPainted(true);
		
		oneJobPanel.revalidate();
		
	} // end showStatus
	
	// return single Job progress GUI panel
	public JPanel getJobPanel(){

		return oneJobPanel;
	}
	
	// check if ship is waiting in queue
	private synchronized boolean isWaiting(){
		
		// get parent ship's port's workers
		ArrayList<Person> jobWorkers; 
		
		SeaPort port = ship.getPort();
		
		// return true if job's ship is in port queue
		// ship still waiting
		
		if (port.que.contains(ship)){
			showStatus(Status.WAITING_FOR_DOCK);
			return true;
		}
		
		// ship is docked
		// proj 4: start job if workers with required skills are available
		else{
			
			// try to get needed workers
			showStatus(Status.GETTING_WORKERS);
			dockPortLabel.setText(ship.getDock().getName() + " @ "+ port.getName());
			oneJobPanel.revalidate();
			
			// only get requirements if Job has requirements
			if (!getReq().isEmpty()){
				
				// attempt to get workers
				jobWorkers = getParentPort().getNeededWorkers(this);
				
				// job was cancelled due to resource problem
				if (!noKillFlag){
					showStatus(Status.CANCELLED);
					return false;
				}
				
				// not enough workers --> wait
				if (jobWorkers == null || jobWorkers.size() == 0){
					
					showStatus(Status.WAITING_FOR_WORKERS);
					return true;
				}
				
				// got all workers --> go!
				else{
					
					System.out.println("got all workers");
					workers = jobWorkers;
					return false;
				}
			}
			
			// no requirements --> go!
			System.out.println("no req needed.");
			return false;
		}
	}// end isWaiting
	
	@Override
	public void run () {
		
		System.out.println(name + " starting");
		
		double time = System.currentTimeMillis();
		double startTime = time;
		double stopTime = time + 1000 * duration;
		double totalDuration = stopTime - time;
		String workerLog = "";
		
		/* 
		 * synchronized block 1
		 * wait until ship is in a dock with resources to do a job
		 */
		synchronized (ship.getPort()) { 
			
			// job waits until it can get to a dock
			while (isWaiting()) {
			
				try {
					ship.getPort().wait();
				}
				catch (InterruptedException e) {
				} // end try/catch block
				
			} // end while waiting for worker to be free
		} // end sychronized on worker
		
		/*
		 * ADD check for skill requirements here?
		 */
		
		/*
		 * update the progress bar and status while progressing or suspended
		 */
		while (time < stopTime && noKillFlag) {
			
			try {
				Thread.sleep (100);
			} catch (InterruptedException e) {}
			
			// update progress bar while running
			if (goFlag) {
				
				showStatus (Status.RUNNING);
				time += 100;
				progressBar.setValue ((int)(((time - startTime) / totalDuration) * 100));
			} 
			
			// pause button was pressed
			else {
				
				System.out.println(name + " paused");
				showStatus (Status.SUSPENDED);
			} // end if stepping
			
		} // end running
		
		// job was cancelled
		if(!noKillFlag){
			
			progressBar.setValue(0);
			done = true; // not going to finish the job, so consider it done
			showStatus(Status.CANCELLED);	
		}
		
		// update when job finishes AND job is not suspended
		else if(goFlag && noKillFlag){
			
			progressBar.setValue (100);
			done = true;
			showStatus (Status.DONE);
			
			System.out.println(name + " finished");
		}
		
		/*
		 * move docked ship if no other jobs,
		 * and get a new ship from the port's queue
		 * 
		 */
		synchronized(ship.getPort()){
			
			// return resources and update logs
			
			// only return if Job had any requirements in the first place
			if (noKillFlag && !requirements.isEmpty() && !workers.isEmpty()){
				
				//update logs
				workerLog += name + " (FINISH 1 JOB & RETURN: Ship: " + ship.getName() + ") returning: ";
				int size = workers.size();
				
				for (int i = 0; i < size; i++){
					
					workerLog += workers.get(i).getName();
					
					if (size >= 3 && i <= size - 3)
						workerLog += ", ";
					
					else if (i == size - 2)
						workerLog += " & ";
				}
				
				log.append(workerLog + "\n");
				
				// acutally return workers
				ship.getPort().returnWorkers(workers);
				
			}// done returning
			
			boolean jobsNotDone = false;
			Ship newShiptoDock;
			SeaPort parentPort = ship.getPort();
			Dock dock = ship.getDock(); 
			
			// check if jobs for this ship are finished
			for (Job j: ship.jobs){
				
				if (!(j.done)){
						jobsNotDone = true;
						break;
				}
			}
			
			// if finished, undock the ship and get a new one
			if (!jobsNotDone){
				
				log.append("FINISHED ALL JOB: Ship " + ship.getName() + " leaving " + dock.getName() + " in " + parentPort.getName() + "\n");

				// continue this for all ships in que 
				// until a ship with jobs is found
				while (!parentPort.que.isEmpty()){
					
					// grab new ship
					newShiptoDock = parentPort.que.remove(0);
					
					// only dock ship that has jobs to finish
					if (!newShiptoDock.jobs.isEmpty()){
						
						dock.ship = newShiptoDock;
						newShiptoDock.dock = dock;
	
						log.append("DOCKED: Ship " + newShiptoDock.getName() + " docking at " + dock.getName() 
								+ " in " + parentPort.getName() + "\n");
						break;
					}
				}
			}
		// done undocking and getting new ships from que
		
			
		// notify other jobs and free up workers
			
			ship.busyFlag = false;
			ship.getPort().notifyAll ();	
		}
		
	} // end method run - implements runnable

} // end Job class