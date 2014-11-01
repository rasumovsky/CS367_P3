////////////////////////////////////////////////////////////////////////////////
// 
// Title:            TrainSimulator
// Files:            TrainSimulator.java, Station.java, PlatformADT.java,
//                   Platform.java, Train.java, StackADT.java, 
//                   SimpleStack.java, QueueADT.java, SimpleQueue.java
//                   FullStackException.java, EmptyStackException.java,
//                   FullQueueException.java, EmptyQueueException.java,
//                   FullStationException.java, EmptyStationException.java
//
// Semester:         CS367 Fall 2014
//
// Author:           Andrew Hard
// Email:            hard@wisc.edu
// CS Login:         hard
// Lecturer's Name:  Jim Skrentny
// Lab Section:      LEC-002 (77632)
//
///////////////////////////////////////////////////////////////////////////////
//
// Pair Partner:     Wayne Chew
// Email:            mchew2@wisc.edu
// CS Login:         mchew
// Lecturer's Name:  Jim Skrentny
// Lab Section:      LEC-001 (77631)
//
///////////////////////////////////////////////////////////////////////////////

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * The TrainSimulator class simulates a series of trains moving from station
 * to station in order to calculate the ATA and ATD of the trains from each
 * station in the simulation. Station capacity data and train ETD data are
 * stored in separate input files to be read in the main method.
 */

public class TrainSimulator{
    
    /**
     * The main method reads station and train input files, stores data,
     * and simulates the moving of trains in order to calculate the 
     * actual time of arrival (ATA) and the actual time of departure (ATD)
     * of each train for each station.
     */
    
    // Question: shouldn't the Platform, Queue, and Stack exceptions be caught in the program?
    public static void main(String[] args) throws FileNotFoundException,
    FullPlatformException, EmptyPlatformException, FullQueueException, EmptyQueueException {
	
	// Check that an attempt has been made to provide all arguments:
	if (args.length != 3) {
	    System.out
.println("Usage: java TrainSimulator N Stations_File_Name Trains_File_Name");
	    System.exit(0);
	}
	
	// Value of args[0] integer determines the print command:
	int infoCommand = Integer.parseInt(args[0]);
	if (infoCommand < 0 || infoCommand > 2) {
	    throw new IllegalArgumentException();
	}
	// Assign booleans based on the input command:
	boolean print = (infoCommand == 0);
	boolean printATD = (infoCommand == 1);
	boolean printATA = (infoCommand == 2);
	

	// Data storage variables:
	int nTrains = 0;// number of trains in simulation
	int nStations = 0;// number of stations in simulation
	List<Train> allTrains = new ArrayList<Train>();// Store Trains
	List<Station> allStations = new ArrayList<Station>();// Store Stations
	List<SimpleQueue<Train>> allTracks
	    = new ArrayList<SimpleQueue<Train>>();// Store tracks
	SimpleQueue<Train> track;
	
	
	// Load file for station data:
	File stationFile = new File(args[1]);
	if (!stationFile.exists()) {
	    throw new FileNotFoundException();
	}
	
	boolean is1stLine = true;
	Scanner stationInput = new Scanner(stationFile);
	while (stationInput.hasNext()) {
	    
	    String currLine = stationInput.nextLine();
	    
	    // First line gives the number of stations:
	    if (is1stLine) {
		is1stLine = false;
		nStations = Integer.parseInt(currLine);
	    }
	    
	    // The remaning lines give the station ID and capacity:
	    else {
		String delims = ",";
		String[] splitLine = currLine.split(delims);
		
		// Make a new Station object and add to array:
		allStations.add(new Station(Integer.parseInt(splitLine[0]),
					    Integer.parseInt(splitLine[1])));
	    }
	}
	stationInput.close();// Finished loading station data
	
	
	// Load a new file for train data:
	File trainFile = new File(args[2]);
	if (!trainFile.exists()) {
	    throw new FileNotFoundException();
	}
	
	is1stLine = true;
	Scanner trainInput = new Scanner(trainFile);
	while (trainInput.hasNext()) {
	    
	    String currLine = trainInput.nextLine();
	    
	    // First line gives the number of trains:
	    if (is1stLine) {
		is1stLine = false;
		// read the number of trains:
		nTrains = Integer.parseInt(currLine);
	    }
	    // The remaining lines give the train IDs and ETDs:
	    else {
		String delims = ",";
		String[] splitLine = currLine.split(delims);
		
		// Make a new Train object to add to array eventually:
		Train currTrain = new Train(Integer.parseInt(splitLine[0]));
		List<Integer> currArray = currTrain.getETD();
		for (int i = 1; i < splitLine.length; i++) {
		    currArray.add(Integer.parseInt(splitLine[i]));
		}
		allTrains.add(currTrain);
	    }
	}
	trainInput.close();// Finished loading train data
	
	
	// Initialize allTracks (having FIFO structures):
	for (int i = 0; i < nStations-1; i++) {
	    // Each track has capacity to carry all simulated trains:
	    track = new SimpleQueue<Train>(nTrains);
	    allTracks.add(track);
	}
	
	
	// Train movement variables:
	int time = 0;
	boolean movement = true;
	boolean done = false;
	
	// Variables to be assigned as needed:
	Train train;
	Platform currPlatform;
	
	// Set up the simulation at time = 0 by adding trains 
	// to 1st station platform:
	for (int i = nTrains-1; i >= 0; i--) {
	    allStations.get(0).getPlatform().put(allTrains.get(i));
	    allTrains.get(i).getATD().add(0);//set actual departure time = 0
	}
	
	

	// Begin simulation of movement:
	while (movement) {
	    
	    // Loop over stations, move from platform to tracks:
	    for (int i = 0; i < nStations-1; i++) {
		currPlatform = allStations.get(i).getPlatform();//get platform
		
		done = false;
		// The ith track follows the ith station:
		track = allTracks.get(i);//get tracks
		
		// Check to see whether train can depart in that station:
		while (!done) {
		    if (!currPlatform.isEmpty()) {
			train = currPlatform.check();//get train
			
			//check ATD and ETD to see whether the train can depart
			if (train.getETD().get(i) <= time 
			    && train.getATD().get(i) <= time) {
			    
			    track.enqueue(currPlatform.get());
			    if (print) {
				System.out.printf(
"%d:\tTrain %d has exited from station %d.\n",time,train.getId(),i+1);
			    }
			    train.getATD().remove(i);
			    train.getATD().add(time);//set ATD
			    train.getATA().add(time+10);//set ATA
			}
			else {
			    done = true;
			}
		    }
		    else {
			done = true;
		    }
		}// end while loop
	    }//end 1st loop over stations
	    
	    // Loop over stations again, move from tracks to platforms:
	    for (int i = 0; i < nStations-1; i++) {
		
		currPlatform = allStations.get(i+1).getPlatform();//get platform
		
		track = allTracks.get(i);//get track
		done = false;
	
		while (!done) {
		
		    if (!track.isEmpty() && !currPlatform.isFull()) {
			train = track.peek();//get train
			
			//check ATA to see whether train can arrive at platform:
			if (train.getATA().get(i) <= time) {
			    currPlatform.put(track.dequeue());
			    
			    if (print) {
				System.out.printf(
"%d:\tTrain %d has been parked at station %d.\n",time,train.getId(),i+2);
			    }
			    train.getATA().remove(i);
			    train.getATA().add(i,time);//set ATA
			    //stay at platfrom for at least +1s:
			    train.getATD().add(time+1);
			}
			else {
			    done = true;
			}
		    }
		    else {
			done = true;
		    }
		}
		
	    }// end 2nd loop over stations
	    
	    // Question: Is the last station guaranteed to have the same capacity as the number of trains? What if there are fewer trains?
	    if (allStations.get(nStations-1).getPlatform().isFull()) {
		movement = false;
	    }//check whehter last platform is full
	    
	    // Increment the time each iteration.
	    time++;

	}// End of simulation
	
	
	
	List<Integer> tempList;// Store ATA, ATD info temporarily
	
	// Lastly, loop over trains to print ATA/ATD info as asked:
	if (printATD || printATA) {
	    for (int i = 0; i < nTrains; i++) {
		
		// Option 1: Print out actual departure times:
		if (printATD) { tempList = allTrains.get(i).getATD(); }
	
		// Option 2: Print out actual arrival times:
		else { tempList = allTrains.get(i).getATA(); }//printATA
		
		System.out.print("[");
		for (int j = 0; j < nStations-1; j++) {
		    if (j == nStations-2) {
			System.out.println(tempList.get(j) + "]");
		    }
		    else {
			System.out.print(tempList.get(j) + ", ");
		    }
		}
	    }
	}// end printATD and printATA sections.
	
    }
}
