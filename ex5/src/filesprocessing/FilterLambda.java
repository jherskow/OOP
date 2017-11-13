package filesprocessing;

/**
 * FilterLambda is the interface for the lambda functions
 * used as the predicate test for Filter objects.
 *
 * @author jherskow
 * @author felber
 */
interface FilterLambda {
	boolean operation(Explorable thing);
}
