/**
 * An abstract class with methods and variables for a hash set.
 *
 * @author jherskow
 */
public abstract class SimpleHashSet implements SimpleSet{

// -- Variables. -----------------------------------------------

	int capacity, numberOfElements;
	protected static final int INITIAL_CAPACITY = 16, REHASH_FACTOR = 2, MINIMUM_CAPACITY = 1;
	protected static final boolean LARGER = true, SMALLER = false;
	protected static final float
			DEFAULT_UPPER_LOAD_FACTOR = 0.75f , //todo
			DEFAULT_LOWER_LOAD_FACTOR = 0.25f;
	float upperLoadFactor, lowerLoadFactor;

// ------ API METHODS -------------------------------------------

	abstract public boolean delete(String toDelete);
	abstract public boolean contains(String searchVal);
	abstract public boolean add(String newValue);

	// javadoc in interface file
	public int size(){
		return numberOfElements;
	}

	/**
	 * @return The lower load factor.
	 */
	public float getLowerLoadFactor(){
		return lowerLoadFactor;
	}

	/**
	 * @return The upper load factor.
	 */
	public float getUpperLoadFactor(){
		return upperLoadFactor;
	}

	/**
	 * @return The current capacity (number of cells) of the table.
	 */
	public int capacity(){
		return this.capacity;
	}
}
