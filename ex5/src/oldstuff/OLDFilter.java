package oldstuff;

import com.sun.scenario.effect.FilterContext;
import filesprocessing.Explorable;

import java.util.function.Function;

/**
 * Created by jherskow on 5/24/17.
 */
public class OLDFilter {
	String query, name;
	double ceiling, floor, value;
	boolean not, reverse;
	int type;

	public OLDFilter(String filter){
		String[] parse = filter.split("#");
		query = parse[0];
		if (query.equals("greater_than") || query.equals("smaller_than") ){
			value = Double.parseDouble(parse[1]);
			return;
		}if (query.equals("between")){                      // todo exceptions
			floor = Double.parseDouble(parse[1]);
			ceiling = Double.parseDouble(parse[2]);
			return;
		}if (query.equals("file")){
			name = parse[1];
		}
		not = (parse[parse.length-1].equals("NOT"));
	}

	boolean greaterThan(Explorable thing){
		return not != (thing.getSize() > value);
	}

	boolean smallerThan(Explorable thing){
		return not != (thing.getSize() < value);
	}

	boolean between(Explorable thing){
		double size = thing.getSize();
		return not != (size <= ceiling && size >= floor);
	}

	boolean isName(Explorable thing){
		return not != (thing.getName().equals(query));
	}

	boolean doesNameContain(Explorable thing){
		return not != (thing.getName().contains(query));
	}

	boolean isNamePrefix(Explorable thing){
		return not != (thing.getName().startsWith(query));
	}

	boolean isNameSuffix(Explorable thing){
		return not != (thing.getName().startsWith(query));
	}

	boolean canWrite(Explorable thing){
		return not != (thing.canWrite());
	}

	boolean canExecute(Explorable thing){
		return not != (thing.canExecute());
	}

	boolean isHidden(Explorable thing){
		return not != (thing.isHidden());
	}

}
