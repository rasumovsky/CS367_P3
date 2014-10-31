///////////////////////////////////////////////////////////////////////////////
// 
// Main Class File:  TrainSimulator.java 
// File:             Platform.java
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

/**
 * An ordered collection of trains. Implement with  a stack (LIFO)
 * data structure, since "the stations are built into the hillsides
 * with only a single access that is both the entrance and the exit.
 */
public class Platform implements PlatformADT {	
    
    // private stack to store the trains
    private StackADT<Train> trainStack;
    
    /**
     * Platform class constructor. 
     * @param inputCap: the train-holding capacity of the plaform
     * @throws IllegalArgumentException if capacity <=0
     */
    public Platform(int capacity) {
	
	// Check that realistic capacity argument provided:
	if (capacity <= 0) {
	    throw new IllegalArgumentException();
	}
	
	// initialize private class variables:
	trainStack = new SimpleStack<Train>(capacity);
    }
    
    
    /**
     * Adds the given train to the platform.
     *
     * @param train: The train to be added to the platform
     * @throws FullPlatformException If the platform is full
     * @throws IllegalArgumentException if item is null
     */
    public void put(Train item) throws FullPlatformException {
	
	if (item == null) {
	    throw new IllegalArgumentException();
	}
	
	try {
	    trainStack.push(item);
	}
	catch (FullStackException fse) {
	    throw new FullPlatformException();
	}
    }
    
    
    /**
     * Removes a train from the platform.
     *
     * @return The train removed from the platform
     * @throws EmptyPlatformException If the platform is empty
     */
    public Train get() throws EmptyPlatformException {
	
	Train result;
	try {
	    result = trainStack.pop();
	}
	catch (EmptyStackException ese) {
	    throw new EmptyPlatformException();
	}
	
	return result;
    }
    
    
    /**
     * Returns the train which may exit first without removing it from the
     * platform.
     *
     * @return The train removed from the platform
     * @throws EmptyPlatformException If the platform is empty
     */
    public Train check() throws EmptyPlatformException {
	
	Train result;
	try {
	    result = trainStack.peek();
	}
	catch (EmptyStackException ese) {
	    throw new EmptyPlatformException();
	}
	
	return result;
    }
    
    
    /**
     * Checks if the platform is empty.
     * 
     * @return True if the platform is empty; else false
     */
    public boolean isEmpty() {
	return trainStack.isEmpty();
    }
  
    
    /**
     * Checks if the platform is full.
     * 
     * @return True if the platform is full; else false
     */
    public boolean isFull() {
	return trainStack.isFull();
    }

}
