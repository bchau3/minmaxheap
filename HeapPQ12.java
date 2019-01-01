/*
NAME:<Brandon Chau>
LOGIN:<cs12fas>
ID:<A15719874>
EMAIL:<bchau@ucsd.edu>
 */

/*
 * This file creates a Heap that could be a Min or Max Heap based on a the
 * value of a boolean
 * There is also an iterator to traverse the heap
 */
import java.util.ArrayList;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * HeapPQ12 class that implements an unbounded array-backed heap structure and is
 * an extension of the Java Collections AbstractQueue class
 * <p>
 * The elements of the heap are ordered according to their natural
 * ordering,  HeapPQ12 does not permit null elements.
 * The top of this heap is the minimal or maximal element (called min/max)
 * with respect to the specified natural ordering.
 * If multiple elements are tied for min/max value, the top is one of
 * those elements -- ties are broken arbitrarily.
 * The queue retrieval operations poll and  peek
 * access the element at the top of the heap.
 * <p>
 * A HeapPQ12 is unbounded, but has an internal capacity governing the size of
 * an array used to store the elements on the queue. It is always at least as
 * large as the queue size. As elements are added to a HeapPQ12, its capacity
 * grows automatically. The details of the growth policy are not specified.
 * <p>
 * This class and its iterator implements the optional methods of the
 * Iterator interface (including remove()). The Iterator provided in method
 * iterator() is not guaranteed to traverse the elements of the HeapPQ12 in
 * any particular order.
 * <p>
 * Note that this implementation is not synchronized. Multiple threads
 * should not access a HeapPQ12 instance concurrently if any of the
 * threads modifies the HeapPQ12.
 * @author Brandon Chau
 * @date 12/1/2018
 * instance variables: capacity - the max defined elements that can be in
 * the heap
 * nelems - number of defined elements in the heap
 * isMaxHeap - boolean to check if Heap is min or max heap
 * iterator - iterator for the heap
 */
public class HeapPQ12<E extends Comparable<? super E>> extends AbstractQueue<E> {

  private ArrayList<E> heap; //list to hold elements of the heap
  //variable set to initial and can be changed to hold max # of defined elements
  private int capacity = 5;
  //variable to hold number of elements current in the heap
  private int nelems;
  //boolean to check if heap is min or max
  private boolean isMaxHeap;
  //iterator to traverse the heap
  private Iterator<E> iterator;
  //boolean value to help update heap iterator
  private boolean resize = false;

  /**
   * HeapPQ12() - min Heap constructor with initial capacity 5
   */
  public HeapPQ12() {
    //create new array list and initialize with null values
    heap = new ArrayList<E>();
    for (int i = 0; i <= capacity; i++) {
      //add null values
      heap.add(null);
    }
    //set isMaxHeap to false to indicate a min heap
    this.isMaxHeap = false;
    //initialize iterator
    this.iterator = iterator();
  }

  /**
   * HeapPQ12 - constructor to define min or max heap of initial capacity 5
   * @param isMaxHeap - boolean to set if min or max heap
   */
  public HeapPQ12(boolean isMaxHeap) {
    //call HeapPQ12 constructor that makes min heap
    this();
    //check if boolean is true or false
    if (isMaxHeap) {
      //if true, set to true to indicate a max heap
      this.isMaxHeap = true;
    } else {
      //otherwise, it is a min heap
      this.isMaxHeap = false;
    }
  }

  /**
   * HeapPQ12() - constructor for min or max heap with a set capacity
   * @param capacity - the set capacity of the heap
   * @param isMaxHeap - boolean to set if heap is min or max
   */
  public HeapPQ12(int capacity, boolean isMaxHeap) {
    //initialize defined capacity
    this.capacity = capacity;
    //create new heap of defined capacity and add initialize with null values
    heap = new ArrayList<E>();
    for (int i = 0; i <= capacity; i++) {
      //add null values
      heap.add(null);
    }
    //if isMaxHeap is true, set define as max heap
    if (isMaxHeap) {
      this.isMaxHeap = true;
    }
    else {
      //else, it is a min heap, so set to false
      this.isMaxHeap = false;
    }
    //instantiate iterator
    this.iterator = iterator();
  }

  /**
   * HeapPQ12 - Constructor that makes a heap based on the copy of the Heap
   * passed in the argument, makes deep copy of the elements
   * @param toCopy - the heap to copy from
   */
  public HeapPQ12(HeapPQ12<E> toCopy) {
    //create a deep copy of elements from heap to copy from
    ArrayList<E> copy = new ArrayList<E>();
    //add all elements from copy heap over to new heap and set to this heap
    copy.addAll(toCopy.heap);
    heap = copy;
    //define all other instance variables from the copy
    capacity = toCopy.capacity;
    nelems = toCopy.nelems;
    isMaxHeap = toCopy.isMaxHeap;
    this.iterator = toCopy.iterator;
  }

  /**
   * size() - returns the number of defined elements in the heap
   * @return nelems - the variable containing the number of elements
   */
  public int size() {
  //return number of elements
    return nelems;
  }

  /**
   * iterator() - constructs a new iterator
   * @return the new iterator
   */
  public Iterator<E> iterator() {
  //return a new iterator for the heap
    return new HeapPQ12Iterator();
  }

  /**
   * peek() - returns the value of the element at the root
   * @return - the first element in the root, or null if no element exists
   */
  public E peek() {
    //check if heap is empty
    if (!isEmpty()) {
      //if not return the first element
      return heap.get(1);
    }
    //else, return null
    return null;
  }

  /**
   * poll() - returns and removes the first element in the heap and
   * reorganizes the heap
   * @return the element that was removed or null if list was empty
   */
  public E poll() {
    E toRemove = null;
    if (!isEmpty()) {
      //if list is not empty, swap with last element
      swap(1, nelems);
      //remove the first element which is now at the end of the heap
      toRemove = heap.remove(nelems);
      //decrement number of elements
      nelems--;
      //use trickle down to reorganize the tree
      trickleDown(1);
    }
  //return removed element
    return toRemove;
  }

  /**
   * offer() - adds an element to the heap and reorganizes the heap
   * @param e - the value of the element to add
   * @return true if element was added successfully, false otherwise
   */
  public boolean offer(E e) {
    //throw exception of value of e is null
    if (e == null) {
      throw new NullPointerException();
    } else {
      //if size of list was zero, set first index to element
      if (size() == 0) {
        heap.set(1, e);
        //if size equal to capacity, double arraylist, then add new element
      } else if (size() == capacity) {
        doubleArrayList(heap);
        heap.set(size()+1,e);
        //if size less than capacity, simply add to next index after size
      } else if (size() < capacity) {
        heap.set(size()+1, e);
      }
      //increment number of elements
      nelems++;
      //bubble up to reorganize
      bubbleUp(nelems);
    }
    //if algorithm reaches this point, element was added successfully
    //so return true
    return true;
  }

  /**
   * isEmpty() - checks if heap is empty
   * @return a boolean true if size is equal to 0, false if not equal to 0
   */
  public boolean isEmpty() {
    //compares size to 0
    return size() == 0;
  }

  /**
   * clear() - clears the heap so it becomes empty
   */
  public void clear() {
    //check if we can still remove
    while (poll() != null) {
      //if so, remove
      this.poll();
    }
  }

  /* ------ Private Helper Methods ----
   *  DEFINE YOUR HELPER METHODS HERE
   */

  /**
   * parent() - returns the parent of the index passed
   * @param index - the index of the current child (if a parent exists)
   * @return the current index divided by 2, which should be the parent
   * otherwise return -1 if parent does not exists, or 1 if index already at 1
   */
  private int parent(int index) {
    //check if parent is at 0, meaning does not exists
    if (index == 0) {
      return -1;
      //if index currently at 1, stay at 1 because that is the root
    } else if (index == 1) {
      return 1;
    }
    //otherwise, divide by 2 to get parent
    return index / 2;
  }

  /**
   * LeftChild() - gets the left child of current index
   * @param index - the index of the supposed parent
   * @return the expected index of left child or -1 if no child
   */
  private int LeftChild(int index) {
    //multiply by to get expected index of left child
    int indexOfLeftChild = index * 2;
    //check if index of child is out of bounds, if so, return -1
    if (indexOfLeftChild > size()) {
      return -1;
    }
    //otherwise, return that index
    return indexOfLeftChild;
  }

  /**
   * RightChild() - returns the right child of specified index
   * @param index - the index of the supposed parent
   * @return the expected index of the right child or -1 if no child
   */
  private int RightChild(int index) {
    //get expected index of right child
    int indexOfRightChild = index * 2 + 1;
    //if out of bounds, return -1
    if (indexOfRightChild > size()) {
      return -1;
    }
    //else, return that index
    return indexOfRightChild;
  }

  /**
   * compare() - compares to elements in relation to if the heap is min
   * or max
   * @param first - one element to compare
   * @param second - second element to compare
   * @return an integer < 0, equal to 0, or greater than 0
   */
  private int compare(E first, E second) {
    //check if a max heap
    if (isMaxHeap) {
      //if those, compare second to first
      return second.compareTo(first);
      }
      //if min, compare first to second
      return first.compareTo(second);
  }

  /**
   * trickleDown() - reorganizes the heap after a poll
   * @param index - the index to start the trickle down process
   */
  private void trickleDown(int index) {
    //create variables to keep track of current index, and smallest or largest
    //starting at the index passed
    int current = index;
    int smallestOrLargest = index;
    //make sure current index never goes greater than the number of elements
    //or there will be a null pointer exception
    while (current <= size()) {
      //get indexes of left and right child
      int iOfLC = LeftChild(current);
      int iOfRC = RightChild(current);
      //check if left child is valid, and see if value is smaller or bigger
      //depending on min or max heap, if so, set left child to smallestOrLargest
      if (iOfLC != -1 && compare(heap.get(iOfLC),heap.get(smallestOrLargest)) < 0) {
        smallestOrLargest = iOfLC;
      }
      //check if right child is valid, and see if value is smaller or bigger
      //depending on min or max heap, if so, set right child to smallestOrLargest
      if (iOfRC != -1 && compare(heap.get(iOfRC), heap.get(smallestOrLargest)) < 0) {
        smallestOrLargest = iOfRC;
      }
      //if smallestOrLargest is equal to current, this means we should stop
      //trickling down as elements are in their correct positions
      if (smallestOrLargest == current) {
        break;
      }
      //swap smallestOrLargest and current so elements go in their correct
      //positions
      swap(smallestOrLargest, current);
      //set current to smallestOrLargest and loop again until while loop breaks
      current = smallestOrLargest;
    }
  }

  /**
   * buubleUp() - organizes the heap after using offer function
   * @param index - the index to start the bubble up process
   */
  private void bubbleUp(int index) {
    //get parent of index passed
    int iOfP = parent(index);
    //set current index to keep track of element being compared
    int current = index;
    //make sure index of parent stays greater than or equal to 1
    while (iOfP >= 1) {
      //compare element at current index to the parent of the current index
      if (compare(heap.get(current), heap.get(iOfP)) < 0) {
        //swap if parent is bigger or smaller than current,depending
        // on min or max heap
        swap(iOfP, current);
        //change current to what index of parent was
        current = iOfP;
        //if parent is the opposite of what the min or max heap should be,
        //elements should be in desired positions
      } else {
        break;
      }
      //otherwise, get parent of current which was the old index of the
      //past parent
      iOfP = parent(current);
    }
  }

  /**
   * doubleArrayList() - doubles the heap being passed
   * @param oldHeap - the heap to increase the capacity of
   * @return the new heap with double the capacity
   */
  private ArrayList<E> doubleArrayList(ArrayList<E> oldHeap) {
    //create copy of old heap being passed
    resize = true;
    ArrayList<E> copyOfOldHeap = oldHeap;
    //double the capacity
    capacity = 2*capacity;
    //create a new array list for the heap
    ArrayList<E> newHeap = new ArrayList<E>();
    //initialize new heap with null values of new capacity
    for (int i = 0; i <= capacity; i++) {
      newHeap.add(null);
    }
    //copy defined elements from old heap to new heap
    for (int i = 1; i <= copyOfOldHeap.size()-1; i++ ) {
      newHeap.set(i, copyOfOldHeap.get(i));
    }
    //set instance variable to new heap just created, and return it
    heap = newHeap;
    return heap;
  }

  /**
   * swap() - swaps the elements between two specified indexes
   * @param index1 - the index of first element to swap
   * @param index2 - the index of second element to swap
   */
  private void swap(int index1, int index2) {
    //set element of first index as tempporary
    E tmp = heap.get(index1);
    //set index1 to be element at index 2
    heap.set(index1, heap.get(index2));
    //set index 2 to be element of the temporary of index 1
    heap.set(index2, tmp);
  }


  /**
   * Inner Class for an Iterator
   * Allows traversal of the Heap
   * has methods to iterate through such as next and hasNext
   **/
  private class HeapPQ12Iterator implements Iterator<E> {
    //array list to iterate through the heap
    private ArrayList<E> list;
    //current index to keep track of location in the heap
    private int currentIndex;

    /**
     * HeapPQ12Iterator() - constructor for the iterator
     */
    private HeapPQ12Iterator() {
      //set list equal to the heap from HeapPQ12
      list = heap;
      //start current index at 0
      currentIndex = 0;
    }

    /**
     * hasNext() - checks if there are more elements that can be traversed
     * through
     * @return true if there is more elements, false if there is not
     */
    public boolean hasNext() {
      //if currentIndex is less than the number of elements,
      //traversal is still possible
      if (currentIndex < nelems) {
        //return true
        return true;
      }
      //else, return false
      return false;
    }

    /**
     * next() - returns the next element of the heap
     * @return the element of the next index if possible
     * otherwise, throw an exception
     * @throws NoSuchElementException if there is no next element
     */
    public E next() throws NoSuchElementException {
      //check if heap has been resized
      if (resize == true) {
        //update list
        list = heap;
        //set back to false;
        resize = false;
      }
      //if there is no next element, throw an exception
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      //otherwise, increment the index, and get the element at that index
      currentIndex++;
      return (list.get(currentIndex));
    }
  }
} 
