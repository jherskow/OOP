package oop.ex4.data_structures;

import java.util.Iterator;

/**
 * Represents the required methods of a binary tree.
 * @author jherskow
 */
public interface BinarySearchTree {

	/**
	 * Add a new node with the given key to the tree.
	 * @param newValue the value of the new node to add.
	 * @return true if the value to add is not already in the tree and it was successfully added,
	∗ false otherwise.
	 */
	boolean add(int newValue);

	/**
	 * Check whether the tree contains the given input value.
	 * @param searchVal the value to search for.
	 * @return the depth of the node (0 for the root) with the given value if it was found in
	∗ the tree, −1 otherwise.
	 */
	int contains(int searchVal);

	/**
	 * Removes the node with the given value from the tree, if it exists.
	 * @param toDelete the value to remove from the tree.
	 * @return true if the given value was found and deleted, false otherwise.
	 */
	boolean delete(int toDelete);

	/**
	 * @return the number of nodes in the tree.
	 */
	int size();

	/**
	 * @return an iterator for the Avl Tree. The returned iterator iterates over the tree nodes
	∗ in an ascending order, and does NOT implement the remove() method.
	 */
	Iterator<Integer> iterator();
}
