package bn.inference;

import bn.core.*;
import java.util.*;
import java.util.Map.Entry;


public class RejectionInference {
	private BayesianNetwork B = new BayesianNetwork();
	private Random random = new Random();
	public RejectionInference(){
		
	}
	
	public Distribution RejectAsk(RandomVariable X, BayesianNetwork bn, Assignment e, int N) {
		return RejectingSampling(X,bn,e,N);
	}
	
	public Distribution RejectingSampling(RandomVariable X, BayesianNetwork bn, Assignment e, int N) {
		
		Distribution Q = new Distribution();
		for(Object x : X.getDomain()) {
			Q.put(x, 0);
		}
		
		for(int i=1;i<=N;i++) {
			Assignment x = PriorSample(bn);
			if(isConsistent(x,e)) {
				Q.put(x.get(X), Q.get(x.get(X))+1);  //get the value and add one to it
				System.out.println("Consistent count:" + Q.toString());
				
			}
		}
		Q.normalize();
		return Q; 
		
	}
	
	public Assignment PriorSample(BayesianNetwork bn) {
		Assignment x = new Assignment();
		List<RandomVariable> sortedList = bn.getVariableListTopologicallySorted();
		
		for(int i = 0; i < bn.size(); i++){ //for each random variable in bn, in topological order
        //so we grabbed a random variable X[i], set its value weighted randomly given parents 
			RandomVariable Xi = sortedList.get(i);
			ArrayList<Double> weights = new ArrayList<Double>(); 
			for(int j = 0; j < Xi.getDomain().size(); j++){
				x.set(Xi, Xi.getDomain().get(j));
				weights.add(j, bn.getProb(Xi, x)); //getCPTForVariable(Xi).get(x)
			}
			double r = Math.random();
			double sum = 0;
			for(int k = 0; k < weights.size(); k++){
				sum += weights.get(k);
				if(r <= sum){
					x.put(Xi, Xi.getDomain().get(k));
					break;
				}
			} 
		}
		
		return x;
	}
	
	private boolean isConsistent(Assignment x, Assignment e) {
		for(Entry<RandomVariable, Object> a: e.entrySet()) {
			if(!a.getValue().equals(x.get(a.getKey()))) return false;
			
		}
		
		return true;
	}
	

}
