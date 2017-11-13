/**
 * Implements a hash set using open hashing.
 * Makes use of the LinkedListFacadeSet class to overcome native java limitations.
 * @author jherskow
 */
public class OpenHashSet extends SimpleHashSet implements SimpleSet {

// ---- VARIABLES ---------------------------------------------------------------

	LinkedListFacadeSet[] hashTable;

// ---- CONSTRUCTORS -------------------------------------------------------------

	/**
	 * Constructs a new, empty table with the specified load factors,
	 * and the default initial capacity (16).
	 *
	 * @param upperLoadFactor desired upper load factor
	 * @param lowerLoadFactor desired lower load factor
	 */
	public OpenHashSet(float upperLoadFactor, float lowerLoadFactor){
		this.capacity = INITIAL_CAPACITY;
		this.numberOfElements = 0;
		this.upperLoadFactor = upperLoadFactor;
		this.lowerLoadFactor = lowerLoadFactor;
		this.hashTable = new LinkedListFacadeSet[this.capacity];
		for (int i = 0; i < capacity; i++) {
			hashTable[i] = new LinkedListFacadeSet();
		}
	}

	/**
	 * A default constructor.
	 * Constructs a new, empty table with default initial capacity (16),
	 * upper load factor (0.75) and lower load factor (0.25).
	 */
	public OpenHashSet(){
		this(DEFAULT_UPPER_LOAD_FACTOR, DEFAULT_LOWER_LOAD_FACTOR);
	}

	/**
	 * Data constructor - builds the hash set by adding the elements one by one.
	 * @param data string array to add
	 */
	public OpenHashSet(java.lang.String[] data){
		this();
		for (java.lang.String string: data) {
			this.add(string);
		}
	}

// ---- API METHODS -------------------------------------------------------------

	public boolean add(String newValue){
		if (hashTable[hashAndClamp(newValue)].add(newValue)){
			numberOfElements++;
			// check for rehashing upwards
			if (((float)numberOfElements/capacity) > upperLoadFactor) rehash(LARGER);
			return true;
		}
		return false;
	}

	public boolean contains(String searchVal){
		return hashTable[hashAndClamp(searchVal)].contains(searchVal);
	}

	public boolean delete(String toDelete){
		if (hashTable[hashAndClamp(toDelete)].delete(toDelete)){
			numberOfElements--;
			// check for rehashing downwards
			if (((float)numberOfElements/capacity) < lowerLoadFactor && capacity > MINIMUM_CAPACITY){
				rehash(SMALLER);
			}
			return true;
		}
		return false;
	}

// ---- HELPER METHODS -------------------------------------------------------------

	int hashAndClamp(String value){
		return (Math.abs(value.hashCode())%capacity);
	}

	void rehash(boolean needLarger){
		int oldCapacity = capacity;

		// increase or decrease the capacity, as necessary.
		capacity = needLarger ? capacity*REHASH_FACTOR: capacity/REHASH_FACTOR;
		LinkedListFacadeSet[] newHashTable = new LinkedListFacadeSet[capacity];

		// Initialise all linked lists in the new array.
		for (int i = 0; i <capacity ; i++) {
			newHashTable[i] = new LinkedListFacadeSet();
		}
		numberOfElements = 0;
		// add all elements to the new
		for (int i = 0; i < oldCapacity; i++) {
			for (String value: hashTable[i].iterable()) {
				newHashTable[hashAndClamp(value)].add(value);
			}
		}

		// dereference the old table
		this.hashTable = newHashTable;
	}
}
