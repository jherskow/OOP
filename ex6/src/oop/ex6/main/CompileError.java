package oop.ex6.main;

/**
 * A CompileError is an exception designed to be thrown when the code tester encounters an illegal
 * piece of code.
 *
 * CompileError is initialised with a Detailed and helpful error message, as well as the line number of the
 * offending code.
 *
 * Although the cause and message may differ, the
 */
public class CompileError extends Exception{

	// handy format to give line number and message all at once.
	private static final String format = "Error in line: %d.\n%s";

	/**
	 *
	 * @param explanation
	 * @param linenumber
	 */
	public CompileError(String explanation, int linenumber){

		// add +1 to change 0-index to 1-index
		super(String.format(format, linenumber+1, explanation));
	}
}
