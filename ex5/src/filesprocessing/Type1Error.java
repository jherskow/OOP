package filesprocessing;

/**
 * A type 1 error is used to indicate an error in a specific filter or order argument
 * in order to inform the user that the default is being used instead.
 *
 * @author jherskow
 * @author felber
 */
class Type1Error extends Exception {
	private static final String format = "Warning in line %d\n";

	/** The constructor takes an error message, and formats it into the error format. */
	Type1Error(int lineNum){
	    super(String.format(format, lineNum));
    }
}
