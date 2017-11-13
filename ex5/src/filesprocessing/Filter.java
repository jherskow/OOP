package filesprocessing;

import java.util.*;
import java.util.function.*;

/**
 * A Filter takes care of filtering a directory.
 *
 * A filter has methods which test a single Explorable,
 * and which reduce an array of Explorables by the same test.
 *
 * @author jherskow
 * @author felber
 */
class Filter implements Predicate<Explorable>{
	private FilterLambda myFilter;
	private double floor, ceiling;
	private boolean not;
	private int notInt, numArgs, lineNum;
	private static final String
			NOT = "NOT",
			SPLIT ="#",
			YES ="YES",
			NO ="NO";

	// An error message, empty by default,
	// enables main to print the error message after parsing is complete.
	private String errorMessage = "";



// ============================= Constructors =================================

	/**
	 * Creates a filter from a correctly formatted string.
	 * Otherwise, creates the default filter and stores an error code.
	 *
	 * @param filterText Formatted String.
	 * @param lineNum The line for the error code.
	 */
	Filter(String filterText, int lineNum) {
		this.lineNum = lineNum;
		
		String[] parse = filterText.split(SPLIT);
		numArgs = parse.length;
		
		not = (parse[numArgs-1].equals(NOT));
		notInt = not? 1: 0;
		
		String filterName = parse[0];
		
		myFilter = null;
		// default filter
		FilterLambda all = (thing) -> true;

		try {
			switch (filterName) {
				case "greater_than":
					expectedArgs(2);
					floor = Double.parseDouble(parse[1]);
					myFilter = (thing) -> thing.getSize() > floor;
					if (floor < 0) throw new Type1Error(lineNum);
					break;
				case "smaller_than":
					expectedArgs(2);
					ceiling = Double.parseDouble(parse[1]);
					myFilter = (thing) -> thing.getSize() < ceiling;
					if (ceiling < 0) throw new Type1Error(lineNum);
					break;
				case "between":
					expectedArgs(3);
					floor = Double.parseDouble(parse[1]);
					ceiling = Double.parseDouble(parse[2]);
					myFilter = (thing) -> thing.getSize() >= floor && thing.getSize() <= ceiling;
					if (floor < 0 || ceiling < 0 || ceiling < floor)
						throw new Type1Error(lineNum);
					break;
				case "file":
					expectedArgs(2);
					myFilter = (thing) -> thing.getName().equals(parse[1]);
					break;
				case "contains":
					expectedArgs(2);
					myFilter = (thing) -> thing.getName().contains(parse[1]);
					break;
				case "prefix":
					expectedArgs(2);
					myFilter = (thing) -> thing.getName().startsWith(parse[1]);
					break;
				case "suffix":
					expectedArgs(2);
					myFilter = (thing) -> thing.getName().endsWith(parse[1]);
					break;
				case "writable":
					expectedArgs(2);
					myFilter = Explorable::canWrite;
					badBooleanParameter(parse[1]);
					checkNo(parse[1]);
					break;
				case "executable":
					expectedArgs(2);
					myFilter = Explorable::canExecute;
					badBooleanParameter(parse[1]);
					checkNo(parse[1]);
					break;
				case "hidden":
					expectedArgs(2);
					myFilter = Explorable::isHidden;
					badBooleanParameter(parse[1]);
					checkNo(parse[1]);
					break;
				case "all":
					expectedArgs(1);
					myFilter = all;
					break;

				// if the first argument is not a recognized filter
				default:
					myFilter = all;
					throw new Type1Error(lineNum);
			}
		} catch (Type1Error e){
			myFilter = all;
			not = false;
			errorMessage = e.getMessage();
		}
	}

// ============================= Methods =================================


	/**
	 * Checks if the number of arguments is consistent with the given number and the status of #NOT
	 * @param expectedArgs Desired number of arguments.
	 * @throws Type1Error  If the number of arguments is not consistent.
	 */
	private void expectedArgs(int expectedArgs) throws Type1Error{
		if(numArgs != expectedArgs + notInt) throw new Type1Error(lineNum);
	}

	/**
	 * Ensures the 2nd String for YES/NO filters is either YES or NO
	 * @param query String to check
	 */
	private void badBooleanParameter(String query) throws Type1Error{
		if (!query.equals(YES) && !query.equals(NO)) throw new Type1Error(lineNum);
	}

	/**
	 * If the query is NO - flip the boolean not.
	 * @param query String to check
	 */
	private void checkNo(String query){
		if (query.equals(NO)) not = !not;
	}

	/**
	 * @return An array of the Explorable Objects which are consistent with this filter
	 */
	Explorable[] filtered(Explorable[] files){
		return Arrays.stream(files).filter(this).toArray(Explorable[]::new);
	}

	@Override
	// Implement the test method for Predicate using the result of operate
	public boolean test(Explorable thing){
		return not != operate(thing, myFilter);
	}

	/**
	 * Generates a Predicate-compatible result from a given lambda function.
	 * @param file an Explorable object.
	 * @param filter a FilterLambda object.
	 * @return the result of the lambda's test.
	 */
	private boolean operate(Explorable file, FilterLambda filter){
		return filter.operation(file);
	}

	/**
	 * @return the errorMessage of this filter.
	 * - This will be empty string if no error has occurred.
	 */
	String getErrorMessage(){return errorMessage;}
}
