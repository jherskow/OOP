package filesprocessing;

/**
 * The explorable interface specifies the methods required for an object
 * to be compatible with our file browsing system.
 *
 * Using this interface, the functionality is modular and extensible.
 *
 * @author jherskow
 * @author felber
 */
interface Explorable {

	/**
	 * @return the size of the explorable
	 */
	double getSize();

	/**
	 * @return the name of the explorable
	 */
	String getName();

	/**
	 * @return true iff explorable is writeable
	 */
	boolean canWrite();

	/**
	 * @return true iff explorable can kill.
	 */
	boolean canExecute();

	/**
	 * @return true iff explorable is hidden
	 */
	boolean isHidden();

	/**
	 * @return the full path of the explorable
	 */
	String getAbsolutePath();

	/**
	 * @return the type name of the explorable
	 */
	String getType();
}
