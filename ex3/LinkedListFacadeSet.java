import java.util.LinkedList;

/**
 * A Wrapper to enable creation of an array of linked lists in OpenHashSet.
 *
 * @author jherskow
 */
public class LinkedListFacadeSet implements SimpleSet{
	private LinkedList<String> linkedList;

	/**
	 *
	 */
	public LinkedListFacadeSet(){
		this.linkedList = new LinkedList<String>();
	}

	public boolean add(String newValue){
		return (!linkedList.contains(newValue) && linkedList.add(newValue));
	}

	public boolean contains(String searchVal){
		return linkedList.contains(searchVal);
	}

	public boolean delete(String toDelete){
		return linkedList.remove(toDelete);
	}

	public LinkedList<String> iterable(){
		return this.linkedList;
	}


	public int size(){
		return linkedList.size();
	}
}
