package bn.inference;

import bn.core.*;
import java.util.*;


public class LikelihoodInference {
	private static RandomSample rand = new RandomSample();
	
//	function LIKELIHOOD-WEIGHTING(X , e, bn, N ) returns an estimate of P(X|e) inputs: X , the query variable
//	e, observed values for variables E
//	bn, a Bayesian network specifying joint distribution P(X1, . . . , Xn) N , the total number of samples to be generated
//	local variables: W, a vector of weighted counts for each value of X , initially zero
//	forj =1toN do
//	x, w ← WEIGHTED-SAMPLE(bn, e) W[x]←W[x]+w wherex isthevalueofX inx
//	return NORMALIZE(W)
	public Distribution LIKELIHOODWEIGHTING(RandomVariable X, BayesianNetwork bn, Assignment e, int N){
		Distribution W = new Distribution();
		for(Object x : X.getDomain()) {
			W.put(x, 0);
		}
		
		for(int i=1;i<=N;i++) {
			Pair xw = WEIGHTEDSAMPLE(bn,e); //x, w ← WEIGHTED-SAMPLE(bn, e)  
			double w = xw.weight;
			Object x = xw.event.get(X);		// RandomVariable X value in the generated event
			W.put(x,  W.get(x)+ w);		//W[x]←W[x]+w where x is the valueof X; 
		}

		W.normalize();					//As always insure they sum up to one
		return W;
	}

	
//	function WEIGHTED-SAMPLE(bn,e) returns an event and a weight
//	w ← 1; x ← an event with n elements initialized from e foreachvariableXi inX1,...,Xn do
//	if Xi is an evidence variable with value xi in e
//	then w ←w × P(Xi = xi | parents(Xi))
//	else x[i] ← a random sample from P(Xi | parents(Xi ))
//	return x, w
	
	public static Pair  WEIGHTEDSAMPLE(BayesianNetwork bn,Assignment e) { 	//returns a pair of event and weight
		
		Assignment a = e.copy();  							//copy so you don't lose the original
		List<RandomVariable> Y = bn.getVariableListTopologicallySorted();
		double w = 1.0;								// weight is starting at 1

		for (RandomVariable Xi: Y) { 		// foreach variable Xi in X1,...,Xn do
			if (a.containsKey(Xi)) {		// if Xi is an evidence variable with value xi in e
				w = w * bn.getProb(Xi, a);		// then w ←w × P(Xi = xi | parents(Xi))
			} 
			else {  						//x[i] ← a random sample from P(Xi | parents(Xi ))
				
				Domain XiDomain = Xi.getDomain();				// Domain of Xi Variable
				Distribution DistributionXi = new Distribution(Xi);			// new Distribution


				for (Object xi: XiDomain) {				
					Assignment k = a.copy();			
					k.put(Xi, xi);					
					DistributionXi.put(xi, bn.getProb(Xi, k));		
				}

				Object random = rand.RandomSample(Xi,DistributionXi); //a random sample from P(Xi | parents(Xi )
				a.set(Xi, random);							

			}
		}


		return new Pair(a, w);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}