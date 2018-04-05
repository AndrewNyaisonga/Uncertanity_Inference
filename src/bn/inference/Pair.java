package bn.inference;
import bn.core.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Stores an event and associated weight
 * @author Seth Schaffer
 *
 */

public class Pair {

	protected Assignment event;
	protected double weight = 1.0;
	
	public Pair(Assignment event, double weight) {
		super();
		this.event = event;
		this.weight = weight;
	}

}