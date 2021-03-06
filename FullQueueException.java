///////////////////////////////////////////////////////////////////////////////
// 
// Main Class File:  TrainSimulator.java 
// File:             FullQueueException.java
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

/**
 * The FullQueueException class extends the Exception class, which means
 * is is a checked exception. 
 * @author Ming Chew
 * @author Andrew Hard 
 */
public class FullQueueException extends Exception {
 
    public FullQueueException() {
	super();
    }
    
    public FullQueueException(String message) {
	super(message);
    }
    
}
