package filesprocessing;

import java.util.*;

/**
 * An Order takes care of ordering a directory.
 *
 * An Order has methods which compare two Explorables,
 * and which sort an array of Explorables by the same comparator.
 *
 * @author jherskow
 * @author felber
 */
class Order implements Comparator<Explorable> {
	private OrderLambda currentOrder;
	private Order secondaryOrder;
	private int reverse = 1;

	// An error message, empty by default,
	// enables main to print the error message after parsing is complete.
	private String errorMessage = "";

	// The default filter function
	private OrderLambda absolute = (dick, jane) -> dick.getAbsolutePath().compareTo(jane.getAbsolutePath());

// ============================= Constructors =================================

	/**
	 * Creates an Order from a correctly formatted string.
	 * Otherwise, creates the default order and stores an error code.
	 *
	 * @param orderText Formatted String.
	 * @param lineNum The line for the error code.
	 */
	Order(String orderText, int lineNum) {

		String[] args = orderText.split("#");
		String orderName = args[0];
	    if (args[args.length-1].equals("REVERSE")) reverse = -1;
		currentOrder = null;
		secondaryOrder = new Order(reverse);

		try {
			if ((reverse == 1 && args.length != 1) || (reverse == -1 && args.length != 2)){
				throw new Type1Error(lineNum);
			}
			if (orderName.equals("abs") || orderName.equals("")){
				currentOrder = absolute;
			}
			if (orderName.equals("type")){
				currentOrder = (dick, jane) -> dick.getType().compareTo(jane.getType());
			}
			if (orderName.equals("size")){
				currentOrder = (dick, jane) -> Double.compare(dick.getSize(), jane.getSize());
			}
			if (currentOrder == null) throw new Type1Error(lineNum);
		} catch (Type1Error e){
			currentOrder = absolute;
			errorMessage = e.getMessage();
		}

	}

	/**
	 * Alternative constructor which uses the default filter, and REVERSE, if necessary.
	 * (Used since in the REVERSE case, we need our fallback order to reverse as well.)
	 *
	 * @param reverse 1 or -1 for ordering.
	 */
	private Order(int reverse){
		currentOrder = absolute;
		this.reverse = reverse;
	}

// ============================= Methods =================================

	/**
	 * Sorts a given array, first by secondarySort then by the desired sort.
	 * (if REVERSE is given, BOTH orders will be reversed)
	 *
	 * @param files Explorable array to be sorted.
	 */
	void sort(Explorable[] files){
		Arrays.sort(files, secondaryOrder);
		Arrays.sort(files, this);
	}

	@Override
	// Implement the compare method for Comparator using the result of operate
	public int compare(Explorable dick, Explorable jane){
		return operate(dick, jane, currentOrder);
	}

	/**
	 * Generates a Comparator-compatible result from a given lambda function.
	 * @param dick an Explorable object.
	 * @param jane an Explorable object.
	 * @param orderLambda an OrderLambda object.
	 * @return the result of the lambda's comparison.
	 */
	private int operate(Explorable dick, Explorable jane, OrderLambda orderLambda){
		return reverse*orderLambda.order(dick, jane);
	}

	/**
	 * @return the errorMessage of this filter.
	 * - This will be empty string if no error has occurred.
	 */
	String getErrorMessage(){return errorMessage;}

}

