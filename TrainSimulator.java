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

/*
 */

public class TrainSimulator{
    
    /**
     * The main method reads station and train input files, stores data,
     * and simulates the moving of trains in order to calculate the 
     * actual time of arrival (ATA) and the actual time of departure (ATD)
     * of each train for each station.
     */
    public static void main(String[] args) throws FileNotFoundException,
    FullPlatformException, EmptyPlatformException, FullQueueException, EmptyQueueException {
	
	// Check that an attempt has been made to provide all arguments:
	if (args.length != 3) {
	    System.out
.println("Usage: java TrainSimulator N Stations_File_Name Trains_File_Name");
	    System.exit(0);
	}
	
	// Assign value to the args[0] integer
	int infoCommand = Integer.parseInt(args[0]);
	
	if (infoCommand < 0 || infoCommand > 2) {
	    throw new IllegalArgumentException();
	}
	
	
	// Data storage variables:
	int nTrains = 0;// number of trains in simulation
	int nStations = 0;// number of stations in simulation
	List<Train> allTrains = new ArrayList<Train>();// Store all Trains
	List<Station> allStations = new ArrayList<Station>();// Store all Stations
	
	
	// Load file for station data
	File stationFile = new File(args[1]);
	if (!stationFile.exists()) {
	    throw new FileNotFoundException();
	}
	
	boolean is1stLine = true;
	Scanner stationInput = new Scanner(stationFile);
	while (stationInput.hasNext()) {
	    
	    String currLine = stationInput.nextLine();
	    
	    // first line gives the number of stations:
	    if (is1stLine) {
		is1stLine = false;
		// read the number of stations:
		nStations = Integer.parseInt(currLine);
	    }
	    // following lines give the station ID and capacity:
	    else {
		String delims = ",";
		String[] splitLine = currLine.split(delims);
		
		// Make a new Station object and add to array:
		allStations.add(new Station(Integer.parseInt(splitLine[0]),
					    Integer.parseInt(splitLine[1])));
	    }
	}
	stationInput.close();// finished loading station data
	
	
	// Load file 2 for train data
	File trainFile = new File(args[2]);
	if (!trainFile.exists()) {
	    throw new FileNotFoundException();
	}
	
	is1stLine = true;
	Scanner trainInput = new Scanner(trainFile);
	while (trainInput.hasNext()) {
	    
	    String currLine = trainInput.nextLine();
	    
	    // first line gives the number of trains:
	    if (is1stLine) {
		is1stLine = false;
		// read the number of trains:
		nTrains = Integer.parseInt(currLine);
	    }
	    // following lines give the train ID and ETDs:
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
	trainInput.close();// finished loading train data

	boolean print = false;
	boolean printATD = false;
	boolean printATA = false;

		switch(infoCommand){
		case 0:
			print = true;
		break;
		case 1:
			printATD = true;
		break;
		case 2:
			printATA = true;
		break;
		default:
		System.out.println("Error");
		break;
	}//user command
	
	//Train movement
	int time = 0;
	boolean movement = true;
	boolean done = false;
	
	//Initialize train tracks(nStations -1), time = 0
	List<SimpleQueue<Train>> allTracks = new ArrayList<SimpleQueue<Train>>();
	SimpleQueue<Train> track;
	Train train;
	Platform currPlatform;
	
	
	for(int i = 0; i < nStations-1; i++){
		track = new SimpleQueue<Train>(100);
		allTracks.add(track);
	}

	//set up the simulation, time = 0
	for(int i = nTrains-1; i >= 0; i--){
		allStations.get(0).getPlatform().put(allTrains.get(i));
		allTrains.get(i).getATD().add(0);//set departure time to 0
	}//add trains to platform of first station

	//movement begins
	while(movement){
		for(int i = 0; i < nStations-1; i++){
			currPlatform = allStations.get(i).getPlatform();//get platform

			done = false;
			track = allTracks.get(i);//get tracks

			while(!done){
				if(!currPlatform.isEmpty()){
				train = currPlatform.check();//get train
				
				if(train.getETD().get(i) <= time && train.getATD().get(i) <= time){
					track.enqueue(currPlatform.get());
					if(print){
					System.out.printf("%d:\tTrain %d has exited from station %d.\n",time,train.getId(),i+1);
				}
					train.getATD().remove(i);
					train.getATD().add(time);//set actual departure time
					train.getATA().add(time+10);//set arrival time
				}//check ATD and ETD to see whether the train can depart
				else{
					done = true;
				}
				}
				else{
					done = true;
				}
			}//check to see whether train can depart in that station
		}//from platform to tracks

		for(int i=0; i< nStations-1; i++){
			currPlatform = allStations.get(i+1).getPlatform();//get platform
			
			track = allTracks.get(i);//get track
			done = false;
			while(!done){
				if(!track.isEmpty() && !currPlatform.isFull()){
				train = track.peek();//get train
				if(train.getATA().get(i) <= time){
					currPlatform.put(track.dequeue());
				if(print){
					System.out.printf("%d:\tTrain %d has been parked at station %d.\n",time,train.getId(),i+2);
				}
					train.getATA().remove(i);
					train.getATA().add(i,time);//set actual time of arrival
					train.getATD().add(time+1);//stay at platfrom for at least 1s
				}//check ATA to see whether train can arrive at platform
				else{
					done = true;
				}
				}
				else{
					done = true;
				}
			}
			
		}
		
		if(allStations.get(nStations-1).getPlatform().isFull()){
			movement = false;
		}//check whehter last platform is full

		time ++;
	}

	List<Integer> temp;

	if(printATD){
		for(int i = 0; i < nTrains; i++){
			temp = allTrains.get(i).getATD();

			System.out.print("[");
			for(int j = 0; j < nStations-1; j++){
				if(j == nStations-2){
					System.out.println(temp.get(j) + "]");
				}
				else{
					System.out.print(temp.get(j) + ", ");
				}
			}
		}		
	}

	if(printATA){
		for(int i = 0; i < nTrains; i++){
			temp = allTrains.get(i).getATA();

			System.out.print("[");
			for(int j = 0; j < nStations-1; j++){
				if(j == nStations-2){
					System.out.println(temp.get(j) + "]");
				}
				else{
					System.out.print(temp.get(j) + ", ");
				}
			}
		}	
	
    }
}
}
