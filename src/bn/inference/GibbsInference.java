package bn.inference;

import bn.core.*;
import java.util.*;
//function GIBBS-ASK(X , e, bn, N ) returns an estimate of P(X|e)
//local variables: N, a vector of counts for each value of X , initially zero
//Z, the nonevidence variables in bn
//x, the current state of the network, initially copied from e
//initialize x with random values for the variables in Z forj =1toN do
//for each Zi in Z do
//set the value of Zi in x by sampling from P(Zi|mb(Zi)) N[x]←N[x]+1wherex isthevalueofX inx
//return NORMALIZE(N)
public class GibbsInference {
	private static RandomSample rand = new RandomSample();
	public Distribution GIBBSASK(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {
		Distribution Q = new Distribution();
		for(Object x : X.getDomain()) {  		//local variables: N, a vector of counts for each value of X , initially zero
			Q.put(x, 0);
		}
		
		List<RandomVariable> Z = new ArrayList<>();
		for(RandomVariable Xi : bn.getVariableListTopologicallySorted()) { 	//Z, the nonevidence variables in bn
			//System.out.println(Xi.getName());
			if(!e.containsKey(Xi)) Z.add(Xi);
		}
		Assignment x = e.copy();  		 //x, the current state of the network, initially copied from e	
		for(RandomVariable Zi : Z) {	//initialize x with random values for the variables in Z
			Object randomValue = Zi.getDomain().get(new Random().nextInt(Zi.getDomain().size()));
            x.set(Zi, randomValue);
		}
		for(int i=1;i<=N;i++) { 		//for j =1 to N do
			for(RandomVariable Zi : Z) { //for each Zi in Z do
				x.put(Zi, mb(Zi,x,bn));			//set the value of Zi in x by sampling from P(Zi|mb(Zi))-Markov Blanket
				Q.put(x.get(Zi), Q.get(x.get(Zi))+1);			//N[x]←N[x]+1wherex isthevalueofX inx
			}
		}
		Q.normalize();
		return Q;
	}
	public Object mb(RandomVariable Zi, Assignment x, BayesianNetwork bn) { //Markov Blanket
		Distribution Q = new Distribution(Zi);
		Assignment k = x.copy();	
		for (Object z: Zi.getDomain()) {					// For each value in the domain of Yv:
					
			k.put(Zi, z);			
			double prob= bn.getProb(Zi, k);         //Zi parent 
			Set <RandomVariable> ZiChildren = bn.getChildren(Zi);
			//System.out.println(ZiChildren.toString());
			for (RandomVariable child : ZiChildren) {
				//System.out.println(Zi.getName()+" Child "+child.getName());
				prob *= bn.getProb(child, k);
			}
			
			Q.put(z, prob);			// Update the distribution
		}
		Q.normalize();
		Object random = rand.RandomSample(Zi,Q);
		x.set(Zi, random);						// Update the state

		return random;
	}

}
