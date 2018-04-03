package bn.inference;

import bn.core.*;
import java.util.*;

public class LikelihoodInference {
	
	
//	function LIKELIHOOD-WEIGHTING(X , e, bn, N ) returns an estimate of P(X|e) inputs: X , the query variable
//	e, observed values for variables E
//	bn, a Bayesian network specifying joint distribution P(X1, . . . , Xn) N , the total number of samples to be generated
//	local variables: W, a vector of weighted counts for each value of X , initially zero
//	forj =1toN do
//	x, w ← WEIGHTED-SAMPLE(bn, e) W[x]←W[x]+w wherex isthevalueofX inx
//	return NORMALIZE(W)
	public double LIKELIHOODWEIGHTING(RandomVariable X, BayesianNetwork bn, Assignment e, int N){
		Distribution W = new Distribution();
		for(Object x : X.getDomain()) {
			W.put(x, 0);
		}
		
		for(int i=1;i<=N;i++) {
			HashMap<RandomVariable,Double> x = WEIGHTEDSAMPLE(bn,e);
			//W[x]←W[x]+w wherex isthevalueofX
			
			System.out.println("Consistent count:" + W.toString());
		}
		return 5.5;
	}
	
//	function WEIGHTED-SAMPLE(bn,e) returns an event and a weight
//	w ← 1; x ← an event with n elements initialized from e foreachvariableXi inX1,...,Xn do
//	if Xi is an evidence variable with value xi in e
//	then w ←w × P(Xi = xi | parents(Xi))
//	else x[i] ← a random sample from P(Xi | parents(Xi ))
//	return x, w
	
	public HashMap<RandomVariable,Double> WEIGHTEDSAMPLE(BayesianNetwork bn, Assignment e) {
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}