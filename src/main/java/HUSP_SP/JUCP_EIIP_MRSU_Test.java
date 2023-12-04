package HUSP_SP;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
public class JUCP_EIIP_MRSU_Test {
    public static void main(String[] args) throws IOException {

//        String dataset = "Yoochoose";
//        String input1 = "../dataFile/" + dataset + "_N.txt";
//        String input2 = "../dataFile/" + dataset + "_P.txt";
//        int minUtility1 = 84;
//        int minUtility2 = 104;
//        int minContrastUtility = 10;
////        String input1 = "../dataFile/" + dataset + ".txt";
////        String input2 = "../dataFile/" + dataset + ".txt";
////        int minUtility1 = 463076;
////        int minUtility2 = 463076;
////        int minContrastUtility = 1;
//        String output = "huspminer_Baseline.txt";
//
//        // run the algorithm
//        HUSP_SP_Contrast_minUtilityPruning huspMiner = new HUSP_SP_Contrast_minUtilityPruning(input1, input2, minUtility1, minUtility2, minContrastUtility);
//        huspMiner.runAlgo();

//        String dataset = "Yoochoose";
//        String dataset = args[0];
//        int minContrastUtility = Integer.parseInt(args[1]);
//        String dataset = "Kosarak10k";
//        double minUtility1 = 0.0169;
//        double minContrastUtility = 0;

//        double minUtility1 =  Double.parseDouble(args[1]);
//        String dataset = args[0];
//        double minContrastUtility = Double.parseDouble(args[2]);

        String dataset = "Crime";
        double minUtility1 = 0.0580;
        double minContrastUtility = 2;

        String input1 = "../dataFile/" + "Crime_47.txt";
        String input2 = "../dataFile/" + "Crime_74.txt";
        String output = "crime_FP_P_20.txt";

        // run the algorithm
        JUCP_EIIP_MRSU huspMiner = new JUCP_EIIP_MRSU(input1, input2, minUtility1, minContrastUtility,output);
//        huspMiner.setMaxLen(5);
        huspMiner.runAlgo();

        // print statistics
        huspMiner.printStatistics();
        System.out.println("halfQualifiedNum: " + huspMiner.halfQualifiedNum);

//        System.out.println("minUtility1: " + huspMiner.minUtility1);
//        System.out.println("minUtility2: " + huspMiner.minUtility2);
//        System.out.println(dataset);

//        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
//        for (String pattern : huspMiner.patterns) {
//            writer.write(pattern);
//            writer.newLine();
//        }
//        writer.close();
    }
}
