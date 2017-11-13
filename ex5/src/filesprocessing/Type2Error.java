package filesprocessing;

/**
 * A type 2 error is used in cases where the main program should stop running,
 * and print the cause to the user.
 *
 * @author jherskow
 * @author felber
 */
class Type2Error extends Exception {
	private static final String format = "ERROR: %s";

	/** The constructor takes an error message, and formats it into the error format. */
	Type2Error(String message){
	    super(String.format(format, message));
    }
}
