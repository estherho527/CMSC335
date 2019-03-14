package proj2;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;

/* Author: Esther Ho
 * CMSC 335 Summer Session 1 2018
 * Project 2
 * 
 * File Name: SeaPortProgramGUI.java
 * 
 * Description:
 * Create a GUI to display the World of the SeaPorts
 * 
 * 
 * DISCLAIMER: based off of code fragments, and with strategy written by Nicholas Duchon (2016) 
 *    for this CMSC 335 project
 */
public class SeaPortProgramGUI extends JFrame{

	static final long serialVersionUID = 123L;

	// variables
	private World world;
	File file = new File(""); 
	FileReader reader;
	Scanner sc;
	HashMap<Integer, Thing> masterList;

	// GUI variables
	private String guiTitle;
	private JPanel jbp, tab1, tab2, tab2Buttons, tab3, tab4;
	private JTextArea jtaT1 = new JTextArea();
	private JTextArea jtaT2 = new JTextArea();
	private JTextArea jtaT3 = new JTextArea();
	private JTextArea jtaT4 = new JTextArea();
	private JScrollPane jsp1, jsp2, jsp3, jsp4;
	private JTree tree;
	private JButton readButton;
	private JButton displayButton;
	private JButton searchButton;
	private JButton expandButton;
	private JButton collapseButton;
	private JButton sortButton;
	private JLabel searchLabel, sortLabel, sortByLabel;
	private JTextField searchTF;
	private JComboBox<String> searchCB, sortCB, sortByCB;
	private JFileChooser jfc;
	private JTabbedPane jtp;


	/*
	 * Constructor
	 */
	public SeaPortProgramGUI(){

		guiTitle = "SeaPort Program";
		
		// set up filechooser
		jfc = new JFileChooser(".");
		jfc.setDialogTitle("Choose File to Read");

		// sets up main GUI
		setTitle(guiTitle);
		setLayout(new BorderLayout());
		setSize(1100,600);
		
		// make tabbed pane
		jtp = new JTabbedPane();
		tab1 = new JPanel(); // display toString() output after reading in data
		tab2 = new JPanel(); // show Jtree after reading in data
		tab3 = new JPanel(); // show search results
		tab4 = new JPanel(); // show sort results
		tab2Buttons = new JPanel();

		tab2.setLayout(new BorderLayout());
		tab2Buttons.setLayout(new FlowLayout());
		
		// add scroll bar to text area for all tabs
		jtaT1.setEditable(false);
		jtaT2.setEditable(false);
		jtaT3.setEditable(false);
		jtaT4.setEditable(false);
		jtaT1.setLineWrap(true);
		jtaT2.setLineWrap(true);
		jtaT3.setLineWrap(true);
		jtaT4.setLineWrap(true);
		jsp1 = new JScrollPane(jtaT1);
		tab1.add (jsp1, BorderLayout.CENTER); 
		jsp2 = new JScrollPane(jtaT2);
		jsp3 = new JScrollPane(jtaT3);
		tab3.add (jsp3, BorderLayout.CENTER); 
		jsp4 = new JScrollPane(jtaT4);
		tab4.add(jsp4, BorderLayout.CENTER);
		
		// set font
		jtaT1.setFont(new java.awt.Font("Monospaced", 0, 12));
		jtaT2.setFont(new java.awt.Font("Monospaced", 0, 12));
		jtaT3.setFont(new java.awt.Font("Monospaced", 0, 12));
		jtaT4.setFont(new java.awt.Font("Monospaced", 0, 12));
		
		// default text shown
		jtaT1.setText("Click \"Read\" Button to choose a file to scan and read");
		jtaT2.setText("Click \"Display\" Button AFTER reading a file show the data Tree");
		jtaT3.setText("Choose a search target and click \"Search\" Button AFTER reading a file show the data Tree");
		jtaT4.setText("Choose what to sort and how to sort it and click \"Sort\" Button AFTER reading a file show the data Tree");
		
		// organize panel 2 for JTree
		expandButton = new JButton("Expand All");
		collapseButton = new JButton ("Collapse All");
		tab2Buttons.add(expandButton);
		tab2Buttons.add(collapseButton);
		tab2.add(tab2Buttons, BorderLayout.PAGE_START);
		tab2.add (jsp2, BorderLayout.CENTER); 
		
		// add panels to tabbed pane
		jtp.add("Output", jsp1);
		jtp.add("Tree", tab2);
		jtp.add("Search Result", jsp3);
		jtp.addTab("Sort Result", jsp4);
	
		// make buttons for top panel
		readButton = new JButton("Select File");
		displayButton = new JButton("Display Tree");
		searchButton = new JButton("Search");
		sortButton = new JButton("Sort");

		searchLabel = new JLabel ("Search Target");
		searchTF = new JTextField(10);
		sortLabel = new JLabel("Sort all:");
		sortByLabel = new JLabel("By:");

		// add options to Search combo box in top panel
		String[] searchCBOptions = {"Index", "Class", "Name", "Skill"};
		searchCB = new JComboBox<String>(searchCBOptions);
		
		// options for Sort combo box
		String[] sortCBOptions = {"SeaPort", "Dock", "Ship", "Person", "Job"};
		sortCB = new JComboBox<String>(sortCBOptions);
		
		String[] sortByOptions = {"Name"};
		sortByCB = new JComboBox<String>(sortByOptions);

		// add buttons to top panel
		jbp = new JPanel();
		jbp.add(readButton);
		jbp.add(displayButton);
		jbp.add(searchLabel); // search label
		jbp.add(searchTF); // search text field
		jbp.add(searchCB); // combo box
		jbp.add(searchButton); // search button
		jbp.add(sortLabel);
		jbp.add(sortCB);
		jbp.add(sortByLabel);
		jbp.add(sortByCB);
		jbp.add(sortButton); 

		// add panes to main gui
		add(jbp, BorderLayout.PAGE_START); // top panel
		add(jtp, BorderLayout.CENTER); // tabbedpane panel

		validate();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // GUI appear in center
		setVisible(true);

		// various action listeners for buttons
		readButton.addActionListener (e -> readFile());
		displayButton.addActionListener (e -> displaySeaPort());
		searchButton.addActionListener (e -> search ((String)(searchCB.getSelectedItem()), searchTF.getText()));
		expandButton.addActionListener (e -> expandAllNodes());
		collapseButton.addActionListener (e -> collapseAllNodes());
		sortCB.addActionListener(e -> updateSortByCB());
		sortButton.addActionListener(e -> {
			try {
				sort();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
		});
		
	}// end constructor

	/*
	 * readFile()
	 * - called when "Select File" Button is pressed
	 * - allows user to select a file to be read and processed
	 * - if the file is not found or corrupt or not correct format, an error dialog window will be shown
	 * - world is displayed to "Output" Tab in tabbed pane
	 */
	private void readFile () {

		// set filters to only allow user to pick text files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
	    jfc.setFileFilter(filter);
	    
		int result = jfc.showSaveDialog(null);
		
		// after user chooses a file
		if (result == JFileChooser.APPROVE_OPTION) {

			try{
				file = jfc.getSelectedFile();
				jtaT1.setText(""); // reset JTextArea
				jtaT1.setText("File: \"" + file.getName() + "\" \n________________________\n\n");
				reader = new FileReader(file);
				sc = new Scanner(reader);
				world = new World(sc);
				
				displayMessage("World created! The full world will be shown in Output Tab", "File read and World created");
				jtaT1.append("" + world);
				
				// close readers and scanners
				reader.close();
				sc.close();
				
			} catch (FileNotFoundException e) {
                this.displayError("Error: No such file found. Please try again.", "Error");
                System.out.println(e.getMessage());
            } catch (IOException e){
            	this.displayError("Error closing FileReader", "Error");
                System.out.println(e.getMessage());
            }
		}
	} // end method readFile

	/*
	 * displaySeaPort() is called when "Display Tree" button is pressed
	 * - tests if file has been read
	 * - if yes, creates a JTree to be displayed in the "Tree" Tab of tabbed pane
	 */
	private void displaySeaPort () {

		// if no file chosen, show error dialog
		if (world.getEveryThing().isEmpty()){
			
			displayError("No world to generate Tree! File may be improper format, or no file read in yet", "Error");
			return;
		}

		// only create tree if file was read in
		else{
			displayMessage("Tree for " + file.getName() + " will be shown in Tree Tab", "Tree made");
			tab2.remove(jsp2);
			DefaultMutableTreeNode root = createNodes("World");
			tree = new JTree(root);
			jsp2 = new JScrollPane(tree);
			tab2.add(jsp2, BorderLayout.CENTER);
		}
	} // end method displaySeaPort

	/*
	 * search() 
	 * - called when "Search" button is pressed AND target and sortBy criteria is chosen
	 * - if requirements met, will display the search results in the "Search Result" tab of tabbed pane
	 */
	private void search (String category, String target) {
		
		// cannot search if world is not created
		if (world == null || sc == null){
			
			displayError("No world to search! First select a file to read.", "Error");
			return;
		}
		
		// no search target entered
		if (target.equals("")){
			
			displayError("No search target entered.", "Error");
			return;
		}
		
		String toFind; // what to find
		String input = searchTF.getText(); // what user types in
		String searchResults; // results to display back to GUI
		int comboBoxSelection; // criteria to sort by
		
		
		toFind = input.trim();
		searchResults = "";
		comboBoxSelection = searchCB.getSelectedIndex();
		
		switch (comboBoxSelection){
			
			// index
			case 0: 
				try{
					int index = -1;
					index = Integer.parseInt(toFind);
					
					if(index > -1)
						searchResults = world.searchByIndex(index);
				}catch (NumberFormatException e){
					System.out.println(e.getMessage());
					displayError("index error", "Error");
				}	
				break;
				
			// class
			case 1:
				searchResults = world.searchByClass(toFind);
				break;
				
			// name
			case 2:
				searchResults = world.searchByName(toFind);
				break;
				
			// skill
			case 3:
				searchResults = world.searchBySkill(toFind);
				break;
				
			default:
				break;
		}// end switch
		
		// no results
		if (searchResults.equals("")){
			jtaT3.setText("No results found for:" + toFind);
			displayMessage("No results found", "Notice!");
		}
		
		// display results
		jtaT3.setText("Search Results for " + input + ":\n" +  searchResults);
		displayMessage("Search Done for " + input + " done! Search Results will be shown in \"Search Result\" Tab", "Search Finished");
		
	} // end method search

	/*
	 * createNodes()
	 * - called to process through world and create a JTree
	 * - returns the root node when done
	 */
	private DefaultMutableTreeNode createNodes(String title) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(title);
		DefaultMutableTreeNode spn, dn, qn, sn, psn, csn, jn, pn, skn;
		
		// seaports
		for(SeaPort sp: world.ports) {       
			
			spn = new DefaultMutableTreeNode("Port: " + sp.name);
			top.add(spn);              
			
			// docks
			for(Dock d: sp.docks) { 
				
				dn = new DefaultMutableTreeNode("Dock: " + d.name);
				spn.add(dn);
				
				if (d.ship instanceof PassengerShip){
					
					psn = new DefaultMutableTreeNode("PShip: " + d.ship.name );
					dn.add(psn);
					
					for(Job j: d.ship.jobs){
						
						jn = new DefaultMutableTreeNode("Job: " + j.name);
						psn.add(jn);
						
						for(String sk: j.requirements){
							
							skn = new DefaultMutableTreeNode("Skill: " + sk);
							jn.add(skn);
						}
					} // end of jobs at passenger ship
				} // if passenger ship
			
				else{
					
					csn = new DefaultMutableTreeNode("CShip: " + d.ship.name );
					dn.add(csn);
					
					for(Job j: d.ship.jobs){
						
						jn = new DefaultMutableTreeNode("Job: " + j.name);
						csn.add(jn);
						
						for(String sk: j.requirements){
							
							skn = new DefaultMutableTreeNode("Skill: " + sk);
							jn.add(skn);
						}
					}// end of jobs at cargo ship
				} // if cargo ship				
			} // end for each dock

			qn = new DefaultMutableTreeNode("In Queue");
			spn.add(qn);
			
			// ships in que
			for(Ship q: sp.que){
				if (q instanceof PassengerShip){
					
					psn = new DefaultMutableTreeNode("PShip: " + q.name );
					qn.add(psn);
					
					for(Job j: q.jobs){
						
						jn = new DefaultMutableTreeNode("Job: " + j.name);
						psn.add(jn);
						
						for(String sk: j.requirements){
							
							skn = new DefaultMutableTreeNode("Skill: " + sk);
							jn.add(skn);
						}
					} // end of jobs at passenger ship
				} // if passenger ship
			
				else{
					
					csn = new DefaultMutableTreeNode("CShip: " + q.name );
					qn.add(csn);
					
					for(Job j: q.jobs){
						
						jn = new DefaultMutableTreeNode("Job: " + j.name);
						csn.add(jn);
						
						for(String sk: j.requirements){
							
							skn = new DefaultMutableTreeNode("Skill: " + sk);
							jn.add(skn);
						}
					}// end of jobs at cargo ship
				} // if cargo ship		
			}// end for waiting ships
			
			sn = new DefaultMutableTreeNode("At Port");
			spn.add(sn);
			
			// ships at sea port
			for(Ship s: sp.ships){
				
				if (s instanceof PassengerShip){
					
					psn = new DefaultMutableTreeNode("PShip: " + s.name );
					sn.add(psn);
					
					for(Job j: s.jobs){
						
						jn = new DefaultMutableTreeNode("Job: " + j.name);
						psn.add(jn);
						
						for(String sk: j.requirements){
							
							skn = new DefaultMutableTreeNode("Skill: " + sk);
							jn.add(skn);
						}
					} // end of jobs at passenger ship
				} // if passenger ship
			
				else{
					
					csn = new DefaultMutableTreeNode("CShip: " + s.name );
					sn.add(csn);
					
					for(Job j: s.jobs){
						
						jn = new DefaultMutableTreeNode("Job: " + j.name);
						csn.add(jn);
						
						for(String sk: j.requirements){
							
							skn = new DefaultMutableTreeNode("Skill: " + sk);
							jn.add(skn);
						}
					}// end of jobs at cargo ship
				} // if cargo ship		
			}// end for ship at port
			
			// persons
			for(Person p: sp.persons){
				pn = new DefaultMutableTreeNode("Person: " + p.name);
				spn.add(pn);
				
				// skill
				skn = new DefaultMutableTreeNode("Skill: " + p.skill);
				pn.add(skn);
				
			} // end for persons at port
		} // end for each sea port
		return top;
	} // end method createNodes
	
	/*
	 * collapseAllNodes()
	 * - called when "Collapse All" Button is pressed inside "Tree" tab
	 * - visually collapses all nodes of JTree
	 */
    private void collapseAllNodes(){
        for(int i = 0; i < tree.getRowCount()-1; i++)
            tree.collapseRow(i);

    }// end collapseAllNodes
    
    /*
     * expandAllNodes()
     * -called when "Expand All" button is pressed inside "Tree" tab
     * - visually expands all nodes of JTree
     */
    private void expandAllNodes(){
        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);
         
    }// end expandAllNodes

    /*
     * updateSortByCB()
     * - updates sort by options in JComboBox based on what Class is being chosen to sort
     */
	private void updateSortByCB() {
		
		String chosen = (String) sortCB.getSelectedItem();
		System.out.println("chosen to sort: " + chosen);

		// possible options
		String[] shipSortByOptions = {"Name", "Draft", "Length", "Weight", "Width"};
		String[] dockSortByOptions = {"Name", "Ship"};
		String[] personSortByOptions = {"Name", "Skill"};
		String[] jobSortByOptions = {"Name", "Duration"};
		
		int length; // length of sortByCB currently
		
		// remove all options but Name
		while((length = sortByCB.getItemCount()) > 1){
			sortByCB.removeItemAt(length-1);
		}
		
		if (chosen.equals("Ship")){
			for (int i = 1; i < shipSortByOptions.length; i++){
				sortByCB.addItem(shipSortByOptions[i]);
			}
		}
		
		else if (chosen.equals("Dock")){
			sortByCB.addItem(dockSortByOptions[1]);
		}
		
		else if (chosen.equals("Person")){
			
			sortByCB.addItem(personSortByOptions[1]);
		}
		
		else if (chosen.equals("Job")){
			sortByCB.addItem(jobSortByOptions[1]);
			
		}
		
		validate();
	}

	/*
	 * sort()
	 * -allows user to sort based on what type of Thing, and how to sort
	 */
	private void sort() throws NoSuchMethodException, SecurityException {
		
		String sortFor = (String)sortCB.getSelectedItem(); // what is being sorted
		String sortBy = (String)sortByCB.getSelectedItem(); // how it is being sorted
		String sortResults = ""; // holder for results
		
		//String toSort = sortFor.toLowerCase();
		//String howSort = sortBy.toLowerCase();
		
		// String[] sortField = {"name", "ship", "draft", "length", "weight", "width", "duration"}; // fields to sort by	
		// Method[] corrMethod = {"getName", ""}; // corresponding getters
		
		// cannot world if world is not created
		if (world == null || sc == null){

			displayError("No world to sort! First select a file to read.", "Error");
			return;
		}
		
		// perform sort
		@SuppressWarnings({ "rawtypes", "unused" })
		Class classType;
		ArrayList<Thing> toSort;
		
		ThingComparator compare = new ThingComparator(sortBy);
		
		// determine which class is to be sorted
		switch(sortFor){
		
		case "SeaPort":
			classType = SeaPort.class;
			toSort = new ArrayList<Thing>(world.getPorts());
			break;
		case "Dock":
			classType = Dock.class;
			toSort = new ArrayList<Thing>(world.getDocks());
			break;
		case "Ship":
			classType = Ship.class;
			toSort = new ArrayList<Thing>(world.getShips());
			break;
		case "Person":
			classType = Person.class;
			toSort = new ArrayList<Thing>(world.getPersons());
			break;
		case "Job":
			classType = Job.class;
			toSort = new ArrayList<Thing>(world.getJobs());
			break;
		default:
			classType = Thing.class;
			toSort = new ArrayList<Thing>(world.getEveryThing());
			break;
		}// end class switch
		
		// sort and spit into a String
		Collections.sort(toSort, compare);
		
		for(int i = 0; i < toSort.size(); i++){
			
			sortResults +=  toSort.get(i).getOutputFormat() + "\n";
			
			// for sorting docks by their ship's name, display ship too  
			if (sortBy.equals("Ship")){
				sortResults += "\t" +((Dock)toSort.get(i)).getShip().getOutputFormat() + "\n";
			}
		}
		
		// display result to GUI
		displayMessage("Sort Results will be shown in \"Sort Result\" Tab", "Sort Finished");
		jtaT4.setText("Results for " + sortFor + ", sorted by " + sortBy + ":\n" + sortResults);
	}
    
    /*
     * displayMessage()
     * - used to output regular dialog message windows to the user
     */
    private void displayMessage(String toDisplay, String title){
    	
    	JOptionPane.showMessageDialog(this, toDisplay, title, JOptionPane.DEFAULT_OPTION);
    }// end displayMessage
    
    /*
     * displayError()
     * - used to output error dialog message windows to the user
     */
    private void displayError(String toDisplay, String title){
    	
    	JOptionPane.showMessageDialog(this, toDisplay, title, JOptionPane.ERROR_MESSAGE);
    }// end displayError

	/*
	 * main()
	 * -creates GUI from constructor
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		//World world = new World();
		SeaPortProgramGUI gui = new SeaPortProgramGUI();

	}// end main
}// end SeaPortGUI class