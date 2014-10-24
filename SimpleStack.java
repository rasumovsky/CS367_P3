///////////////////////////////////////////////////////////////////////////////
// 
// Main Class File:  TrainSimulator.java 
// File:             SimpleStack.java
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
 * The SimpleStack class implements the StackADT using an array.
 * It is a LIFO data structure.
 * @author Ming Chew
 * @author Andrew Hard 
 */
public class SimpleStack<E> implements StackADT<E> {
    
    private E items[];// Stores the stack data (add to end)
    private int numItems;// the current number of stack items
    private int capacity;// the max capacity of the stack
    
    /**
     * Constructor for the SimpleStack class
     * @param inputCap - The capacity of the stack
     */
    public SimpleStack(int inputCap) {
	if (inputCap <= 0) {
	    throw new IllegalArgumentException();
	}
	
	numItems = 0;
	capacity = inputCap;
	items = (E[])(new Object[capacity]);
    }
    
    
    /**
     * Adds an item to the top of the stack.
     *
     * @param item The item to add to the stack
     */
    public void push(E item) throws FullStackException {
	
	if (item == null) {
	    throw new IllegalArgumentException();
	}
	else if (this.isFull()) {
	    throw new FullStackException();
	}
	
	items[numItems] = item;
	numItems++;
    }
    
    
    /**
     * Removes and returns the top item of the stack.
     *
     * @return The top item of the stack
     * @throws EmptyStackException If the stack is empty
     */
    public E pop() throws EmptyStackException {
	
	if (this.isEmpty()) {
	    throw new EmptyStackException();
	}
	
	// Stop counting the element at the top.
	// Don't delete it, just forget about it.
	numItems--;//first decrease the number of items.
	return items[numItems];// intentionally not numItems-1
    }
    
    
    /**
     * Returns the top item of the stack without removing it.
     *
     * @return The top item of the stack
     * @throws EmptyStackException If the stack is empty
     */
    public E peek() throws EmptyStackException {
	
	if (isEmpty()) {
	    throw new EmptyStackException();
	}
	
	return items[numItems-1];
    }
    
    
    /**
     * Checks if the stack is empty.
     * 
     * @return True if stack is empty; else false
     */
    public boolean isEmpty() {
	return (numItems == 0);
    }
    
    
    /**
     * Checks if the stack is full.
     * 
     * @return True if stack is full; else false
     */
    public boolean isFull() {
	return (numItems == capacity);
    }
    
}
