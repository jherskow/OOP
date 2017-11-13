/**
 * A Facade to allow use of SimpleSet's API on Java.Collection-compatible classes.
 *
 * @author jherskow
 */
public class CollectionFacadeSet implements SimpleSet {
	java.util.Collection<String> collection;

	public CollectionFacadeSet(java.util.Collection<String> collection){
		this.collection = collection;
	}

	public boolean add(String newValue){
		return (!collection.contains(newValue) && collection.add(newValue));
	}
	public int size(){
		return collection.size();
	}

	public boolean contains(String searchVal){
		return collection.contains(searchVal);
	}

	public boolean delete(String toDelete){
		return collection.remove(toDelete);
	}
}

