import java.util.Objects;

/**
 * Implements a hash set using Closed hashing.
 * @author jherskow
 */
public class ClosedHashSet extends SimpleHashSet implements SimpleSet {

// ---- VARIABLES ---------------------------------------------------------------

	String[] hashTable;
	static final String DELETED = "random pointer";
	int capacityMinusOne;

// ---- CONSTRUCTORS -------------------------------------------------------------

	/**
	 * Constructs a new, empty table with the specified load factors,
	 * and the default initial capacity (16).
	 *
	 * @param upperLoadFactor desired upper load factor
	 * @param lowerLoadFactor desired lower load factor
	 */
	public ClosedHashSet(float upperLoadFactor, float lowerLoadFactor){
		this.capacity = INITIAL_CAPACITY;
		this.capacityMinusOne = capacity - 1;
		this.numberOfElements = 0;
		this.upperLoadFactor = upperLoadFactor;
		this.lowerLoadFactor = lowerLoadFactor;
		this.hashTable = new String[this.capacity];
	}

	/**
	 * A default constructor.
	 * Constructs a new, empty table with default initial capacity (16),
	 * upper load factor (0.75) and lower load factor (0.25).
	 */
	public ClosedHashSet(){
		this(DEFAULT_UPPER_LOAD_FACTOR, DEFAULT_LOWER_LOAD_FACTOR);
	}

	/**
	 * Data constructor - builds the hash set by adding the elements one by one.
	 * @param data string array to add
	 */
	public ClosedHashSet(java.lang.String[] data){
		this();
		for (java.lang.String string: data) {
			this.add(string);
		}
	}

// ---- API METHODS -------------------------------------------------------------

	public boolean add(String newValue){
		int hash = newValue.hashCode();
		int deletedIndex = -1;
		boolean wasDeleted = false;
		for (int i = 0; i < capacity ; i++) {
			int index = clamp(i,hash);
			// check equality by reference, to allow use of the DELETED string.
			if (hashTable[index] == DELETED){
				wasDeleted = true;
				deletedIndex = index;
			}else if (Objects.equals(hashTable[index], null)){
				// insert into deleted spot if one was encountered.
				if (wasDeleted) index = deletedIndex;
				hashTable[index] = newValue;
				numberOfElements++;
				if (((float)numberOfElements/capacity) > upperLoadFactor) rehash(LARGER);
				return true;
			}else if (Objects.equals(hashTable[index], newValue)) {
				return false;
			}
		}
		return false;
	}

	public boolean contains(String searchVal){
		int hash = searchVal.hashCode();
		for (int i = 0; i < capacity ;i++) {
			int index = clamp(i, hash);
			if (Objects.equals(hashTable[index], null)) return false;
			if (Objects.equals(hashTable[index], searchVal)) return true;
		}
		return false;
	}

	public boolean delete(String toDelete){
		int hash = toDelete.hashCode();
		for (int i = 0; i < capacity ;i++) {
			int index = clamp(i, hash);
			if (Objects.equals(hashTable[index], null)) return false;
			if (Objects.equals(hashTable[index], toDelete)){
				hashTable[index] = DELETED;
				numberOfElements--;
				if (((float)numberOfElements/capacity) < lowerLoadFactor && capacity > MINIMUM_CAPACITY){
					rehash(SMALLER);
				}
				return true;
			}
		}
		return false;
	}

// ---- HELPER METHODS -------------------------------------------------------------

	int clamp(int i, int hash){
		return (hash+(i+i*i)/2)&(capacityMinusOne);
	}

	void rehash(boolean needLarger){
		int oldCapacity = capacity;
		String[] oldHashTable = hashTable;

		// increase or decrease the capacity, as necessary.
		capacity = needLarger ? capacity*REHASH_FACTOR: capacity/REHASH_FACTOR;
		hashTable = new String[capacity];
		capacityMinusOne = capacity -1;
		numberOfElements = 0;

		// add all elements to the new
		for (int i = 0; i < oldCapacity; i++) {
			if (oldHashTable[i] != null && oldHashTable[i] != DELETED) {
				this.add(oldHashTable[i]);
			}
		}
	}
}
