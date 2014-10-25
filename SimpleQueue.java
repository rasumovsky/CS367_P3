///////////////////////////////////////////////////////////////////////////////
// 
// Main Class File:  TrainSimulator.java 
// File:             SimpleQueue.java
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
 * An ordered collection of items, where items are added to the back and
 * removed from the front.
 */
public class SimpleQueue<E> implements QueueADT<E> {
    
    private E items[];// stores the queue data
    private int numItems;// the number of queued items
    private int capacity;// the number of items that can be stored
    private int shift;// front position of the queue in items[] array
    
    
    /** 
     * Constructor for the SimpleQueue class
     * @param inputCap - The capacity of the queue.

     */
    public SimpleQueue(int inputCap) {
	if (inputCap <= 0) {//assume capacity of at least 1
	    throw new IllegalArgumentException();
	}
	
	numItems = 0;
	shift = 0;
	capacity = inputCap;
	items = (E[])(new Object[capacity]);
    }
    
    
    /**
     * Adds an item to the rear of the queue.
     * @param item The item to be added to the queue
     */
    public void enqueue(E item) throws FullQueueException {
	
	if (item == null) {
	    throw new IllegalArgumentException();
	}
	
	else if (this.isFull()) {
	    throw new FullQueueException();
	}
	
	items[getIndex(numItems)] = item;
	numItems++;
    }
    
    
    
    /**
     * Removes and returns the front item of the queue.
     *
     * @return The front item of the queue
     * @throws EmptyQueueException If the queue is empty
     */
    public E dequeue() throws EmptyQueueException {
	
	if (this.isEmpty()) {
	    throw new EmptyQueueException();
	}
	
	E firstInLine = items[getIndex(0)];
	numItems--;
	shift++;
	return firstInLine;
    }
    
    
    /**
     * Returns the front item of the queue without removing it.
     *
     * @return The front item of the queue
     * @throws EmptyQueueException If the queue is empty
     */
    public E peek() throws EmptyQueueException {
	
	if (this.isEmpty()) {
	    throw new EmptyQueueException();
	}
	
	return items[getIndex(0)];
    }
    
    
    /**
     * Checks if the queue is empty.
     * 
     * @return True if queue is empty; else false
     */
    public boolean isEmpty() {
	return (numItems == 0);
    }
    
    
    /**
     * Checks if the queue is full.
     * 
     * @return True if queue is full; else false
     */
    public boolean isFull() {
	return (numItems == capacity);
    }
    
    
    /**
     * Private method to account for the shifting of array indices as a 
     * result of calls to the dequeue method. This allows the program to 
     * avoid O(N) shifts of all array elements.
     * @param unshiftedIdx: The unshifted array index, where 0 is the
     * front of queue.
     * @param shiftedIdx: The shifted array index, where the front of the
     * queue increases by 1 after a call to dequeue().
     */
    
    private int getIndex(int unshiftedIdx) {
	
	// Shifted index accounts for the changing array location of front
	int shiftedIndex = unshiftedIdx + shift;
	
	// Index must be modulo the array capacity:
	shiftedIndex = shiftedIndex % capacity;
	
	return shiftedIndex;
    }
}
