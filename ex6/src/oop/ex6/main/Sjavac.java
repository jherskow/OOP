package oop.ex6.main;

import oop.ex6.scopes.Global;
import oop.ex6.scopes.Scope;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Runs the code. Is run by adding to the argument the location of the sjava code to be checked. The exact
 * specifications are in the Readme.
 */
public class Sjavac {
	private static final String
			GOOD_CODE = "0",
			BAD_CODE = "1",
			IO_FAIL = "2",

			IO_FAIL_ERROR = "A problem occurred while trying to read the file";

    /**
     * The main, it runs the parser and prints 0 if the code is good, 1 if it is bad, and 2 if there is an
     * IO error.
     * @param args the file name of what should be parsed.
     */
	public static void main(String[] args) {

		// Reset variables just in case someone tries to run main multiple times from the same object
		Scope global = null;
		ArrayList<String> file = null;
		boolean okSoFar = true;

		// try and open the file.
		try {
			file = FileReader.processInput(args[0]);
		// otherwise print 2
		}catch (FileNotFoundException e){
			okSoFar = false;
			System.out.println(IO_FAIL);
			System.err.println(IO_FAIL_ERROR);
		}

		// if file loaded successfully, create global and begin testing
		if (okSoFar){

			try{
				global = new Global(file);
			// otherwise print 1, and the error message.
			}catch (CompileError e) {
				okSoFar = false;
				System.out.println(BAD_CODE);
				System.err.println(e.getMessage());
			}
		}

		// if code is successful, print 0.
		if (okSoFar){
			System.out.println(GOOD_CODE);
		}

	}
}
