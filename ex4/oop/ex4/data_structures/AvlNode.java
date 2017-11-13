package oop.ex4.data_structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Implements a node of an avl tree.
 *
 * Includes most methods for the tree, since a node
 * is itself a binary tree.
 *
 * @author jherskow
 */
public class AvlNode implements Iterable<Integer>,Comparable<AvlNode>{
	int height;
	int value;
	static final int NOT_FOUND = -1, ROOT_DEPTH =0;
	AvlNode right = null, left = null;

	/* Makes a new node with desired value. */
	AvlNode(int value){
		this.value = value;
		this.height = 1;
	}

	/* Makes a new node from an existing node. */
	AvlNode(AvlNode node){
		this.value = node.value;
		this.height = node.height;
		this.left = node.left;
		this.right = node.right;
	}

	/**
	 * Add a new node with the given key to the tree rooted at this node.
	 * @param newValue the value of the new node to add.
	 * @return true if the value to add is not already in the tree and it was successfully added,
	∗ false otherwise.
	 */
	public boolean add(int newValue) {
		/*
		if node is equal return false
		if node is null
			place
			do values all way up
			add to thingy all way up
			rebalance all way up
			return true
		otherwise:
			if greater add right
			if lesser add left
		 */
		boolean result = false;
		if (value == newValue)
			result = false;
		else if (newValue < value) {
			if (left != null) result = left.add(newValue);
			else{
				left = new AvlNode(newValue);
				result = true;
			}
		}else if (newValue > value) {
			if (right != null) result = right.add(newValue);
			else{
				right = new AvlNode(newValue);
				result = true;
			}
		}
		//if (result){
		refreshHeight();
		rebalance();
		//refreshHeight();
		//}
		return result;
	}


	/**
	 * Check whether the tree rooted here contains the given input value.
	 * @param searchVal the value to search for.
	 * @return the depth of the node (0 for the root) with the given value if it was found in
	∗ the tree, −1 otherwise.
	 */
	public int contains(int searchVal) {
		return containsWithDepth(searchVal, ROOT_DEPTH);
	}


	/*
	 * Recursive contains finder
	 */
	int containsWithDepth(int searchVal, int i) {
		if (Objects.equals(value, searchVal)) return i;
		if (left != null && searchVal < value) return left.containsWithDepth(searchVal, i + 1);
		if (right != null && searchVal > value) return right.containsWithDepth(searchVal, i + 1);
		return NOT_FOUND;
	}


	/**
	 * Removes the node with the given value from the tree, if it exists.
	 * @param toDelete the value to remove from the tree.
	 * @return true if the given value was found and deleted, false otherwise.
	 */
	public boolean delete(int toDelete) {
		 /*
		find node
		do stuff
		check balance
		 */
		boolean result;
		boolean needLeft = (value > toDelete);
		AvlNode next = needLeft? left: right;
		if (next == null) return false;
		if (next.value == toDelete){
			result = true;

			// if there is at least one null
			if ((next.left == null) || (next.right == null)) {

				// try and make swap the non-null node.
				AvlNode swap = (next.left == null) ? next.right: next.left;

				// if swap is still null, there are no children
				// so we simply nullify the node
				if (swap == null) {
					if(needLeft) left = null;
					else right = null;

				//otherwise make the node equal to it's swap child.
				}else{
					next.clone(swap);
				}

			// if there are two children, replace node with its smallest right child.
			}else{
				AvlNode parentOfRightMinimum = next.parentOfRightMinimum();
				if (parentOfRightMinimum.left == null) next.value = parentOfRightMinimum.value ;
				else next.value = parentOfRightMinimum.left.value ;
				next.delete(parentOfRightMinimum.value);
			}

		}else result = next.delete(toDelete);

		//if (result){
		refreshHeight();
		rebalance();
		//refreshHeight();
		//}
		return result;
	}

	@Override
	public Iterator<Integer> iterator() {
		return this.listmaker().iterator();
	}

	/* return a list of tree's elements*/
	ArrayList<Integer> listmaker(){
		ArrayList<Integer> list = new ArrayList<>();
		if (left != null) list.addAll(this.left.listmaker());
		list.add(value);
		if (right != null) list.addAll(this.right.listmaker());
		return list;
	}

	/* rotates right */
	void rotateRight(){
		AvlNode b = new AvlNode(this);
		AvlNode a = new AvlNode(b.left);
		b.left = a.right;
		a.right = b;
		a.refreshHeight();
		b.refreshHeight();
		clone(a);
	}

	/* rotates left */
	void rotateLeft(){
		AvlNode a = new AvlNode(this);
		AvlNode b = new AvlNode(a.right);
		a.right = b.left;
		b.left = a;
		a.refreshHeight();
		b.refreshHeight();
		clone(b);
	}

	/* re-balances tree rooted at node */
	void rebalance() {
		int factor = balanceFactor();
		if (Math.abs(factor) <= 1){ return;
		}else if (factor == 2 ) {
			//if (right.balanceFactor() == 1) rotateLeft();
			if (right.balanceFactor() == -1) {
				right.rotateRight();
				rotateLeft();
			}else rotateLeft();
		}
		else if (factor == -2 ) {
			//if (left.balanceFactor() == -1) rotateRight();
			if (left.balanceFactor() == 1) {
				left.rotateLeft();
				rotateRight();
			}else rotateRight();
		}else if (factor > 2){
			this.right.rebalance();
		}else if (factor < 2){
			this.left.rebalance();
		}
	}

	/* Changes this node to be identical to an existing node */
	void clone(AvlNode node){
		this.value = node.value;
		this.height = node.height;
		this.left = node.left;
		this.right = node.right;
	}

	/* returns Minimum element of right sub-tree. */
	AvlNode parentOfRightMinimum(){
		AvlNode next = this.right;
		while (next.left != null && next.left.left != null) next = next.left;
		return next;
	}

	int balanceFactor(){
		return (rightHeight() - leftHeight());
	}
	int rightHeight(){
		return (right == null)? 0: right.height;
	}

	int leftHeight(){
		return (left == null)? 0: left.height;
	}

	int height(){
		return  (left == null && right ==null) ? 1: Math.max(rightHeight(), leftHeight()) +1 ;
	}

	void refreshHeight(){
		this.height = height();
	}

	boolean isBalanced(){
		return Math.abs(balanceFactor())<= 1;
	}

	@Override ///////////////////////////////////////////////////////////////////// todo debug////////////
	public int compareTo(AvlNode o) {
		if(value > o.value)
			return 1;
		if(value < o.value)
			return -1;
		return 0;
	}


}
