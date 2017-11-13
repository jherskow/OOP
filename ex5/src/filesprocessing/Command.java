package filesprocessing;

/**
 * A Command represents the instructions for a single display of a directory.
 * It has a single Order and a single Filter.
 *
 * @author jherskow
 * @author felber
 */
class Command {
	Filter filter;
	Order order;

	Command(String filter, int filterLine, String order, int orderLine){
		this.filter = new Filter(filter, filterLine);
		this.order = new Order(order, orderLine);
	}
}
