package bn.inference;

import bn.core.*;
import java.util.*;




public class ExactInference {
	
	private BayesianNetwork B;
	
	public ExactInference(BayesianNetwork bn){
		this.B= bn;
	}
	public Distribution ENUMERATIONASK(RandomVariable X, Assignment e, BayesianNetwork bn){
		Distribution Q = new Distribution();   //Distribution to be returned  
		
		for(Object x: X.getDomain()) {     //get all domain of X
			e.set(X, x);
			Q.put(x,ENUMERATEALL(bn.getVariableListTopologicallySorted(),e));  //Enumerator all in topological order
		}
		Q.normalize();
		return Q;
	}
	
	public double ENUMERATEALL(List<RandomVariable> vars, Assignment e){
		if(vars.isEmpty()) return 1;
		RandomVariable Y = vars.get(0);
		List<RandomVariable> rest = vars.subList(1, vars.size());
		if(e.containsKey(Y)) return B.getProb(Y, e)*ENUMERATEALL(rest,e);
		double totalProbability = 0;
		for(Object y: Y.getDomain()) {
			Assignment evidence = e.copy(); //Make a copy to not alter the original
			evidence.set(Y,y);  //Setting the value of y on variable Y
			
			try {
				totalProbability += B.getProb(Y, evidence) * ENUMERATEALL(rest,evidence);
			}catch(NoSuchElementException event) {  //Just in case we don't find the values after topological sort
					 System.out.println("There was an error");
			}
		}
		return totalProbability;
	}
}
