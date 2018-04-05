package bn.inference;

import java.util.*;
import bn.core.*;

public class RandomSample {
	
	RandomSample(){}
	
	private Random rand = new Random();
	
	public Object RandomSample( RandomVariable Xi,Distribution dist) {

		double random = rand.nextDouble();

		Domain XiDomain = Xi.getDomain();
		int size = XiDomain.size();

		double[] DistributionValues = new double[size]; for (double d: DistributionValues) { d = 0; } 	//initialize
		double[] runningSum =  new double[size+1]; 
		runningSum[0] = 0;		//start with zero					

	
		
		int index = 0;
		for (Object xi: XiDomain) {	// For each object in the domain of Y: 
			DistributionValues[index] = dist.get(xi); 
			index ++;													// Store the next distribution value
			runningSum[index] = summation(DistributionValues);				// Store the sum of dist. values as the sample probability
			//System.out.print(" Sample: " + samples[index]);		
		}
		
		Object w = null;
		
		for (int i = 0; i < runningSum.length-1; i++) { 		//find the random elemnt depending on runningSum
			if (random > runningSum[i]) {
				if (random < runningSum[i+1]) {
					w = XiDomain.get(i); 						// w is the Object from the domain at the index 
					return w;
				}
			}
		}

		return w;

	}
	
	public static double summation(double[] DistributionValues) {
		double summation = 0;
		for (int i=0;i<DistributionValues.length;i++) { summation += DistributionValues[i]; }

		return summation;
	}
}
