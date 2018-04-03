package bn.core;

import java.awt.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import bn.inference.*;
import bn.parser.*;

public class Main {
	/**
	 * This is the main class which is intended to be used to test the project. 
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println("Arugment: ");
		//for(String arg: args) {System.out.println(arg);}


		String lang = args[0];			// Language
		String inference = args[1];		// Take the value of Inference
		String[] parameters;
		String filename = "";

		if (!lang.equals("java")){ //Check if language is applicable
			System.out.println("Just to be clear this is java code"); 
			return;
		}  

		int samples = 0;
		
		switch (inference){
		case "MyBNInferencer":
			System.out.println("Exact Inference using Inference Enumeration!");
			filename = args[2];		//XML or BIF file
			parameters = Arrays.copyOfRange(args, 3, args.length);  //Take the parameters as separate
			break;
		case "MyBNApproxInferencer":
			System.out.println("Approximation Inference!");
			samples = Integer.parseInt(args[2]); // Get sample
			filename = args[3];		//XML or BIF file
			parameters = Arrays.copyOfRange(args, 4, args.length);
			System.out.println("Sample: " + samples); 
			break;
//		case "MyBNGibbsInferencer":
//			System.out.println("Gibbs Inference!");
//			samples = Integer.parseInt(args[2]); // Get sample
//
//			filename = args[3];		//XML or BIF file
//			parameters = Arrays.copyOfRange(args, 4, args.length);
//			System.out.println("Sample: " + samples); 
//			break;

		default:
			System.out.println("We only support MyBNInferencer,MyBNApproxInferencer and MyBNGibbsInferencer");
			return;

		}
		
		
		String type = "XML";
		if (filename.toLowerCase().endsWith(".bif")) { 
			System.out.println("BIF file."); type = "BIF";
		}
		else if (filename.toLowerCase().endsWith(".xml")) { 
			System.out.println("XML file."); type = "XML";
		}
		else { 
			System.out.println("We only support .XML and .BIF files!");  
			return;
		}
		
		System.out.println("\n\nParameters:");
		String[] evidenceRandomVariable = new String[parameters.length/2]; 	// 5 parameters means 2 are evidence,
		Object[] evidenceValues = new Object[parameters.length/2]; 	// values (2);
		
		int index = 0;
		for(int i = 0; i < parameters.length; i++) {
			String P = parameters[i];
			System.out.print(P + " ");		
			if (i == 0) continue; 	// Skip ahead after query
			if (i % 2 == 1) { 				// Every other is a variable name
				//System.out.print("<< ");
				evidenceRandomVariable[index] = P;
				index ++;
			} else {						// The next item is always the value object
				evidenceValues[index-1] = P;
			}
		}
		
		System.out.println("\n\nEvidence: ");
		for (String e: evidenceRandomVariable) System.out.print(e + "\t"); System.out.println("");
		for (Object e: evidenceValues) System.out.print(e + "\t");

		String queryRandomVariable = parameters[0];
		System.out.println("\n\nPrinting distribution for: " + queryRandomVariable);
		
		/* Time to actually do things! */
		try {
			System.out.println("\nAttempting to read file: " + filename + " at location");
			String newpath = "src/bn/examples/" + filename; System.out.println(newpath); // Correct the path to /examples/
			
			BayesianNetwork BN = new BayesianNetwork();
			
			if (type.equals("BIF")) {
				BIFParser parser = new BIFParser(new FileInputStream(newpath));

				BN = parser.parseNetwork();
			}
			else {
				XMLBIFParser parser = new XMLBIFParser();
				
				BN = parser.readNetworkFromFile(newpath);
			}
			
			RandomVariable query = BN.getVariableByName(queryRandomVariable);
			
			Assignment A = new Assignment();
			RandomVariable[] evidence = new RandomVariable[evidenceValues.length];
			for (int i = 0; i < evidence.length; i++) {	
				evidence[i] = BN.getVariableByName(evidenceRandomVariable[i]);
				A.set(evidence[i], evidenceValues[i]);		// evidence are added to assignment
			}
			
			
			if (inference.equals("MyBNInferencer")) {
				System.out.println("\n\nEnumeration Inferencing.... \n");
				ExactInference inf = new ExactInference(BN);
				
				final long startTime = System.currentTimeMillis();
				Distribution dist = inf.ENUMERATIONASK(query,A, BN); //ENUMERATIONASK(RandomVariable X, Assignment e, BayesianNetwork bn)
				final long endTime = System.currentTimeMillis();
				
				System.out.println("Completed in " + (endTime-startTime) + " ms.");  //Ferguson suggestion of keeping track of time
				System.out.println("\n\nProbabilities:" + dist.toString());
			}
			
			if (inference.equals("MyBNApproxInferencer")) {
				System.out.println("\n\nRejection Inferencing.... \n");
				RejectionInference inf = new RejectionInference();
				
				
				final long startTime = System.currentTimeMillis();
				Distribution dist = inf.RejectAsk(query,BN,A, samples); //RejectAsk(RandomVariable X, BayesianNetwork bn, Assignment e, int N)
				final long endTime = System.currentTimeMillis();
				
				System.out.println("\nCompleted in " + (endTime-startTime) + " ms."); //Ferguson suggestion of keeping track of time
				System.out.println("\n\nProbabilities:" + dist.toString());
			}
			

			
		} catch (IOException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
