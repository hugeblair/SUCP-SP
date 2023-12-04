package tool;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class DatasetStatics {

    public static class Item implements Comparable<Item>{

        int item;
        int utility;

        public Item(int item,int utility){
            this.item = item;
            this.utility = utility;
        }
        public int compareTo(Item i){
            if (this.item > i.item)
                return 1;
            else if(this.item < i.item)
                return -1;
            else
                return 0;
        }

    }

    public static void main(String [] arg) throws IOException {
//        String latexChar = "\\textit";
        String latexChar = "";
//        String latexLineEnd = "\\\\ \\hline";
        String latexLineEnd = "\\\\ ";
//        String[] datasets = {"SIGN", "BIBLE", "Scalability_160K", "Kosarak10k", "Leviathan" , "Yoochoose"};//, "Kosarak10k", "Leviathan", "MSNBC", "SIGN"};
        String[] datasets = {"Scalability_10K", "Scalability_80K", "Scalability_160K", "Scalability_240K", "Scalability_320K" , "Scalability_400K"};//, "Kosarak10k", "Leviathan", "MSNBC", "SIGN"};
//        String[] NP = {"_P","_N"};
        String[] NP = {""};
        for (String s : datasets) {
            for (String s1 : NP) {
            // the input database
            String input = "../../dataFile/" + s + s1 + ".txt";
            BufferedReader myInput = null;
            String thisLine;

            // the number of q-sequences input
            int num_S = 0;
            Set Item = new HashSet();
            int total_utility = 0;
            int num_item = 0;
            int max_item_utility = 0;
            int totalLengthS = 0;
            int maxLengthS = 0;
            int NumItemset = 0;
            Map<Integer, Integer> Time = new HashMap<Integer, Integer>();

            try {
                // prepare the object for reading the file
                myInput = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input))));

                // for each line (transaction) until the end of file
                while ((thisLine = myInput.readLine()) != null) {
                    int current_lengthS = 0;
                    List<Item> list = new ArrayList<Item>();
                    num_S++;

                    // if the line is  a comment, is  empty or is a kind of metadata
                    if (thisLine.isEmpty() == true || thisLine.charAt(0) == '#' || thisLine.charAt(0) == '%' || thisLine.charAt(0) == '@') {
                        continue;
                    }
                    // update the input number

//                    StringBuilder buffer = new StringBuilder();

                    // split the sequence according to the " " separator
                    String tokens[] = thisLine.split(" ");

                    // get the sequence utility (the last token on the line)
                    for (int i = 0; i < tokens.length - 1; i++) {

                        String currentToken = tokens[i];

                        // if empty, continue to next token
                        if (currentToken.length() == 0) {
                            continue;
                        }

                        // if the current token is -1
                        // the ending sign of an itemset
                        if (currentToken.equals("-1")) {
                            NumItemset++;
                            Collections.sort(list);
//                            for (Item item:list){
//                                buffer.append(item.item);
//                                buffer.append("[");
//                                buffer.append(item.utility);
//                                buffer.append("] ");
//                            }
                            list.clear();
//                            buffer.append("-1 ");
                        } else if (currentToken.equals("-2")) {
//                            buffer.append("-2 ");
                        } else {

                            int positionLeftBracketString = currentToken.indexOf('[');
                            int positionRightBracketString = currentToken.indexOf(']');
                            String itemString = currentToken.substring(0, positionLeftBracketString);
                            Integer item = Integer.parseInt(itemString);

                            // We also extract the utility from the string:
                            String utilityString = currentToken.substring(positionLeftBracketString + 1, positionRightBracketString);
                            Integer itemUtility = Integer.parseInt(utilityString);


                            Item.add(item);
                            total_utility += itemUtility;
                            num_item++;
                            if (itemUtility > max_item_utility)
                                max_item_utility = itemUtility;
                            current_lengthS++;
                            totalLengthS++;

                            list.add(new Item(item, itemUtility));
                        }


                    }

                    if (current_lengthS > maxLengthS)
                        maxLengthS = current_lengthS;

                }

                System.out.print(latexChar);
//                System.out.print("{" + s + "}");
                System.out.print(s );
                System.out.print(" & ");

                System.out.print(toWestNumFormat(num_S));
                System.out.print(" & ");

                System.out.print(toWestNumFormat(Item.size()));
                System.out.print(" & ");

                System.out.printf("%.2f", totalLengthS * 1.0 / num_S);
                System.out.print(" & ");

                System.out.print(maxLengthS);
                System.out.print(" & ");

                System.out.printf("%.2f", NumItemset * 1.0 / num_S);
                System.out.print(" & ");

                System.out.printf("%.2f", num_item * 1.0 / NumItemset);
                System.out.print(" & ");

                System.out.printf("%.2f", (total_utility * 1.0) / num_item);
                System.out.print(" & ");

                System.out.print(toWestNumFormat(max_item_utility));

                System.out.print(latexLineEnd + "\n");
//                System.out.print("\n");

            } catch (Exception e) {
                // catches exception if error while reading the input file
                e.printStackTrace();
            } finally {
                if (myInput != null) {
                    // close the input file
                    myInput.close();
                }
            }
        }
        }

    }

    /**
     *    将整数在千分位以逗号分隔
     * @param number
     * @return
     */
    public static String toWestNumFormat(int number) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(number);
    }
}

