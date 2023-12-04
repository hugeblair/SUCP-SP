package DataProcesing; /**
 * This class divide an original dataset to two same size datasets for contrast mining
 * @author .Yuting Yang, 2020.11
 */
import java.io.*;

public class MainDataPreprocessing {
    public static void main(String [] arg) throws IOException {
        String[] datasets = {"Yoochoose", "SIGN", "Leviathan", "Kosarak10k", "BIBLE"};   //BIBLE, Scalability_160K"};
        for (String dataset : datasets){

            String input = "../dataFile/" + dataset + ".txt";

            String output1 = "../dataFile/" + dataset +"_P.txt";
            String output2 = "../dataFile/" + dataset +"_N.txt";

            // create a writer object to write results to file
            BufferedWriter writer1 = new BufferedWriter(new FileWriter(output1));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(output2));

            BufferedReader myInput = null;
            String thisLine;

            /********************Count the num of lines********************/
            myInput = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input))));
            int lineNum = 0;
            try{
                while (myInput.readLine() != null) {
                    lineNum++;
                }
            }catch (Exception e) {
                // catches exception if error while reading the input file
                e.printStackTrace();
            }finally {
                myInput.close();
            }
            lineNum /= 2;

            /******************equally divide the file***********************/
            int num = 0;
            try {
                // prepare the object for reading the file
                myInput = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input))));
                // for each line (transaction) until the end of file
                while ((thisLine = myInput.readLine()) != null) {
                    if (num <= lineNum){
                        writer1.write(thisLine);
                        writer1.newLine();
                    }else {
                        writer2.write(thisLine);
                        writer2.newLine();
                    }
                    num++;

                }

                writer1.close();
                writer2.close();

            }catch (Exception e) {
                // catches exception if error while reading the input file
                e.printStackTrace();
            }finally {
                if(myInput != null){
                    // close the input file
                    myInput.close();
                }
            }
        }

    }

}
