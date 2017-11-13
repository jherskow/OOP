package oop.ex4.data_structures;


import java.util.Iterator;
import java.lang.Math;

/**
 * Created by jherskow on 5/9/17.
 */
public class AvlTree implements Iterable<Integer>, BinarySearchTree {
	AvlNode root;
	int size;

// ========================= CONSTRUCTORS ==========================================================
	/**
	 * The default constructor.
	 */
	public AvlTree(){
		//default constructor goes here
		size =0;
	}

	/**
	 *
	 * @param data the values to add to tree
	 */
	public AvlTree(int[] data){
		// add one by one, possibly sort first
		this();
		for (int value : data) {
			add(value);
		}
	}

	/**
	 * A copy constructor that creates a deep copy of the given AvlTree. The new tree
	 ∗ containsWithDepth all the values of the given tree, but not necessarily in the same structure.
	 *
	 * @param avlTree an AVL tree.
	 */
	public AvlTree(AvlTree avlTree){
		// get int[] from iterateor
		// send to new tree constructor
		// todo
		this();
		for(int value: avlTree){
			add(value);
		}
	}

// ========================= API METHODS ==============================================================


	@Override
	public boolean add(int newValue){
		if (root == null){
			root = new AvlNode(newValue);
			size++;
			return true;
		}
		return (root.add(newValue) && (0<size++));
	}

	@Override
	public int contains(int searchVal){
		if (root == null) return AvlNode.NOT_FOUND;
		return root.contains(searchVal);
	}

	@Override
	public boolean delete(int toDelete){
		if (root == null) return false;
		if (root.value == toDelete){

			// if there is at least one null
			if ((root.left == null) || (root.right == null)) {

				// try and make swap the non-null node.
				AvlNode swap = (root.left == null) ? root.right: root.left;

				// if swap is still null, there are no children
				// so we simply nullify the node
				if (swap == null) {
					root = null;

					//otherwise make the node equal to it's swap child.
				}else{
					root.clone(swap);
				}

				// if there are two children, replace node with its smallest right child.
			}else{
				AvlNode parentOfRightMinimum = root.parentOfRightMinimum();
				if (parentOfRightMinimum.left == null) root.value = parentOfRightMinimum.value ;
				else root.value = parentOfRightMinimum.left.value ;
				root.delete(parentOfRightMinimum.value);
			}


			size--;
			return true;
		}else if (root.delete(toDelete)){
			size--;
			//root.refreshHeight();
			root.rebalance();
			return true;
		}
		return false;
	}

	@Override
	public int size(){
		return size;
	}

	@Override
	public Iterator<Integer> iterator(){
		return root.iterator();
	}

	/**
	 * Calculates the minimum number of nodes in an AVL tree of height h.
	 * @param h the height of the tree (a non−negative number) in question.
	 * @return the minimum number of nodes in an AVL tree of the given height.
	 */
	public static int findMinNodes(int h){
		if (h==0) return 1;
		if (h==1) return 2;
		else return 1 + findMinNodes(h-1) + findMinNodes(h-2);
	}

	/**
	 * Calculates the maximum number of nodes in an AVL tree of height h.
	 * @param h the height of the tree (a non−negative number) in question.
	 * @return the maximum number of nodes in an AVL tree of the given height.
	 */
	public static int findMaxNodes(int h){
		return (int) Math.pow(2,h) -1;
	}

	public void print() { /////////////////////////////////todo debug////////////////////////////////////////////
		BTreePrinter.printAvlNode(root);
		System.out.println(size);
	}
}
