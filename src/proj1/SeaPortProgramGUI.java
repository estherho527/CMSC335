package proj1;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;

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
import java.util.Scanner;

/* Author: Esther Ho
 * CMSC 335 Summer Session 1
 * Project 1
 * 
 * 
 * File Name: SeaPortGUI.java
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

	// GUI variables
	private String guiTitle;
	private JPanel jbp, tab1, tab2, tab2Buttons, tab3;
	private JTextArea jtaT1 = new JTextArea();
	private JTextArea jtaT2 = new JTextArea();
	private JTextArea jtaT3 = new JTextArea();
	private JScrollPane jsp1, jsp2, jsp3;
	private JTree tree;
	private JButton jbr;
	private JButton jbd;
	private JButton jbs;
	private JButton expand;
	private JButton collapse;
	private JLabel jls;
	private JTextField jtf;
	private JComboBox<String> jcb;
	private JFileChooser jfc;
	private JTabbedPane jtp;


	/*
	 * Constructor
	 */
	public SeaPortProgramGUI(){

		//world = new World();

		guiTitle = "SeaPort Program";
		
		// set up filechooser
		jfc = new JFileChooser(".");
		jfc.setDialogTitle("Choose File to Read");

		// sets up main GUI
		setTitle(guiTitle);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setSize(700,600);
		setVisible(true);

		// make tabbed pane
		jtp = new JTabbedPane();
		tab1 = new JPanel(); // display toString() output after reading in data
		tab2 = new JPanel(); // show Jtree after reading in data
		tab3 = new JPanel(); // show search results
		tab2Buttons = new JPanel();

		tab2.setLayout(new BorderLayout());
		tab2Buttons.setLayout(new FlowLayout());
		
		// add scroll bar to text area for all tabs
		jtaT1.setEditable(false);
		jtaT2.setEditable(false);
		jtaT3.setEditable(false);
		jtaT1.setLineWrap(true);
		jtaT2.setLineWrap(true);
		jtaT3.setLineWrap(true);
		jsp1 = new JScrollPane(jtaT1);
		tab1.add (jsp1, BorderLayout.CENTER); 
		jsp2 = new JScrollPane(jtaT2);
		jsp3 = new JScrollPane(jtaT3);
		tab3.add (jsp3, BorderLayout.CENTER); 
		
		// set font
		jtaT1.setFont(new java.awt.Font("Monospaced", 0, 12));
		jtaT2.setFont(new java.awt.Font("Monospaced", 0, 12));
		jtaT3.setFont(new java.awt.Font("Monospaced", 0, 12));
		
		// default text shown
		jtaT1.setText("Click \"Read\" Button to choose a file to scan and read");
		jtaT2.setText("Click \"Display\" Button AFTER reading a file show the data Tree");
		jtaT3.setText("Choose a search target and click \"Search\" Button to choose a file to scan and read");

		// organize panel 2 for JTree
		expand = new JButton("Expand All");
		collapse = new JButton ("Collapse All");
		tab2Buttons.add(expand);
		tab2Buttons.add(collapse);
		tab2.add(tab2Buttons, BorderLayout.PAGE_START);
		tab2.add (jsp2, BorderLayout.CENTER); 
		
		// add panels to tabbed pane
		jtp.add("Output", jsp1);
		jtp.add("Tree", tab2);
		jtp.add("Search Result", jsp3);
	
		
		// make buttons for top panel
		jbr = new JButton("Select File");
		jbd = new JButton("Display Tree");
		jbs = new JButton("Search");

		jls = new JLabel ("Search Target");
		jtf = new JTextField(10);

		// add options to combo box in top panel
		jcb = new JComboBox<String>();
		jcb.addItem("Index"); 
		jcb.addItem("Type");
		jcb.addItem("Name");
		jcb.addItem("Skill");

		// add buttons to top panel
		jbp = new JPanel();
		jbp.add(jbr); // read button
		jbp.add(jbd); // display button
		jbp.add(jls); // search label
		jbp.add(jtf); // search text field
		jbp.add(jcb); // combo box
		jbp.add(jbs); // search button

		// add panes to main gui
		add(jbp, BorderLayout.PAGE_START); // top panel
		add(jtp, BorderLayout.CENTER); // tabbedpane panel

		validate();

		// various action listeners for buttons
		jbr.addActionListener (e -> readFile());
		jbd.addActionListener (e -> displaySeaPort());
		jbs.addActionListener (e -> search ((String)(jcb.getSelectedItem()), jtf.getText()));
		expand.addActionListener (e -> expandAllNodes());
		collapse.addActionListener (e -> collapseAllNodes());
		
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
		//stab2.remove(jtaT2);
		else{
			displayMessage("Tree for " + file.getName() + " will be shown", "Notification");
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
	private void search (String type, String target) {
		jtaT1.append (String.format ("Search button pressed, type: >%s<, target: >%s<\n", type, target));
		
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
		
		String toFind; //user types in what to search through 
		String searchResults; // results to display back to GUI
		int comboBoxSelection; // criteria to sort by
		
		toFind = jtf.getText().trim();
		searchResults = "";
		comboBoxSelection = jcb.getSelectedIndex();
		
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
				
			// type
			case 1:
				searchResults = world.searchByType(toFind);
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
		jtaT3.setText(searchResults);
		
	} // end method search

	/*
	 * createNodes()
	 * - called to process through world and create a JTree
	 * - returns the root node when done
	 */
	private DefaultMutableTreeNode createNodes(String title) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(title);
		DefaultMutableTreeNode spn, dn, qn, sn, psn, csn, jn, pn, skn;
		for(SeaPort sp: world.ports) {       
			
			spn = new DefaultMutableTreeNode("Port: " + sp.name);
			top.add(spn);              
			
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
	public static void main(String[] args) {

		//World world = new World();
		SeaPortProgramGUI gui = new SeaPortProgramGUI();

	}// end main
}// end SeaPortGUI class