package filesprocessing;

/**
 * OrderLambda is the interface for the lambda functions
 * used as the comparator for Filter objects.
 *
 * @author jherskow
 * @author felber
 */
interface OrderLambda {
	int order(Explorable dick, Explorable jane);
}
