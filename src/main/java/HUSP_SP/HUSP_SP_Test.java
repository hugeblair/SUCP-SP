package HUSP_SP;

import java.io.IOException;

/**
 * This is an implementation of the HUSP-SP algorithm as presented in this paper: <br/><br/>
 * 
 *    Yuting Yang, Wensheng Gan et al. "HUSP-SP: Faster Utility Mining on Sequence Data"
 *
 * 
 * @author Yuting Yang & Wensheng Gan, HITsz, China.
 * 
 */
public class HUSP_SP_Test {
    public static void main(String[] args) throws IOException {

//    	String input = "./dataFile/SIGN.txt";
//        double minUtilityRatio = 0.0169;

       // String dataset = "Crime_N";0.084
        double minUtilityRatio = 0.53;
//        String dataset = args[0];
//        double minUtilityRatio = Double.parseDouble(args[1]);

//        String dataset = "tst";
//        String input = "../dataFile/" + dataset + ".txt";
        String input = "../dataFile/" + "Crime_18.txt";
        String name = "crime_HUSP";
        String output = name + "_P_50.txt";

        // run the algorithm
        HUSP_SP_Algo huspMiner = new HUSP_SP_Algo(input, minUtilityRatio, output);
        huspMiner.runAlgo();

        // print statistics
        huspMiner.printStatistics();
//        System.out.println("minUtility: " +  huspMiner.minUtility);
    }
}
