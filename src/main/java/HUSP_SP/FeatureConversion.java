package HUSP_SP;

import java.io.*;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class FeatureConversion {

    private ArrayList<ArrayList<Integer>> patternList;
    private ArrayList<ArrayList<Integer>> allFeature;
    private String patternFileName_P;
    private String patternFileName_N;
    private String patternFileName_J;
    private String dbFilename_P;
    private String dbFilename_N;
    Boolean  ifRand;
    private String output;
    private int totalNum;
    int JNum;
    double minUtility;
    boolean isRandDb;
    boolean fillZero;
    int  zeroNum;
    int dbsize;

    public static void main(String[] args) throws IOException {
//        for(int i = 0; i < 4; i++)
//            run(i);
    run(3);
    }

    static void run(int i) throws IOException {
        FeatureConversion fc = new FeatureConversion(i);

//        fc.ConvertFeature();
//        fc.ConvertFeatureSingle();
        fc.ConvertFeature3();
    }




    public FeatureConversion(int i) {
        this.patternList = new ArrayList<ArrayList<Integer>>();
        this.allFeature = new ArrayList<ArrayList<Integer>>();
        String[] patternFile_index = {"50","100","150","200"};
        String[] outputFile_index = {"100","200","300","400"};
        String[] patternName_index = {"HUSP","SUCP","CP","JP"};
        int patternName_i =1;
        int[] numIndex = {100,200,300,400};
        String patternFileName = "crime_" + patternName_index[patternName_i];
        this.patternFileName_P = patternFileName + "_P_" + patternFile_index[i] +".txt";
        this.patternFileName_N = patternFileName + "_N_" + patternFile_index[i] +".txt";
//        this.patternFileName_P = patternFileName + "_P.txt";
        this.patternFileName_J = "human_FP_P_80.txt";
        String dataset = "human";
        minUtility = 0;
        this.dbFilename_P = "../dataFile/" +"human_P.txt";
        this.dbFilename_N = "../dataFile/" + "human_N.txt";
//        this.output = "student_"+patternName_index[patternName_i]+"_"+outputFile_index[i] + "_feature.csv";
        this.output = "human_FP_80_feature.csv";
//        totalNum = numIndex[i];
        totalNum = 320;
        JNum =80;
        ifRand = true;

        isRandDb = true;
        dbsize = 300;

        fillZero = false;
        zeroNum = 837;
    }


    void ConvertFeature() throws IOException {
        readPattern(patternFileName_P, totalNum /2);
        readPattern(patternFileName_N, totalNum /2);

        ULinkList[] uLinkListDB_P = getDBFromTxt_Reduce(dbFilename_P);
        ULinkList[] uLinkListDB_N = getDBFromTxt_Reduce(dbFilename_N);

        addFeature(uLinkListDB_P);
        int Pnum = uLinkListDB_P.length;
        addFeature(uLinkListDB_N);
        totalNum = Pnum + uLinkListDB_N.length;

        printFeature(Pnum);

        return;




    }

    void ConvertFeature3() throws IOException {


        readPattern(patternFileName_P, totalNum /2);
        readPattern(patternFileName_N, totalNum /2);
        readPattern(patternFileName_J, JNum);

        ULinkList[] uLinkListDB_P = getDBFromTxt_Reduce(dbFilename_P);
        ULinkList[] uLinkListDB_N = getDBFromTxt_Reduce(dbFilename_N);
        addFeature(uLinkListDB_P);
        int Pnum = uLinkListDB_P.length;
        addFeature(uLinkListDB_N);
        totalNum = Pnum + uLinkListDB_N.length;

        printFeature(Pnum);

        return;




    }

    void ConvertFeatureSingle() throws IOException {


        readPattern(patternFileName_P, totalNum );

        ULinkList[] uLinkListDB_P = getDBFromTxt_Reduce(dbFilename_P);
        ULinkList[] uLinkListDB_N = getDBFromTxt_Reduce(dbFilename_N);
        addFeature(uLinkListDB_P);
        int Pnum = uLinkListDB_P.length;
        addFeature(uLinkListDB_N);
        totalNum = Pnum + uLinkListDB_N.length;

        printFeature(Pnum);

        return;




    }

    void printFeature(int Pnum){
        try {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(output)));
            ArrayList<String> header = new ArrayList<>();
            for (int i = 0; i < allFeature.get(0).size(); i++) {
                header.add(Integer.toString(i));
            }
            if(fillZero){
                for(int j =allFeature.get(0).size();j<zeroNum;j++)
                    header.add(Integer.toString(j));
            }
            header.add("Class");
            String[] headerS = new String[allFeature.get(0).size()];
            headerS = header.toArray(headerS);
            CSVFormat csvFormat = CSVFormat.EXCEL.withHeader(headerS);
            CSVPrinter printer = csvFormat.print(writer);
            for (int i = 0; i < Pnum; i++) {
                ArrayList<Integer> feature = allFeature.get(i);
                Iterator var2 = feature.iterator();

                while(var2.hasNext()) {
                    Object value = var2.next();
                    printer.print(value);
                }
                if(fillZero){
                    for(int j =feature.size();j<zeroNum;j++)
                        printer.print("0");
                }
                printer.print("Positive");
                printer.println();
            }
            for (int i = Pnum; i < allFeature.size(); i++) {
                ArrayList<Integer> feature = allFeature.get(i);
                Iterator var2 = feature.iterator();

                while(var2.hasNext()) {
                    Object value = var2.next();
                    printer.print(value);
                }
                if(fillZero){
                    for(int j =feature.size();j<zeroNum;j++)
                        printer.print("0");
                }
                printer.print("Negative");
                printer.println();
            }
            printer.flush();
            printer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addFeature(ULinkList[] uLinkListDB){
        for (ULinkList uLinkList : uLinkListDB) {
            ArrayList<Integer> featureList = new ArrayList<>(patternList.size());
            for(int m = 0; m < patternList.size(); m++){
                ArrayList<Integer> pattern = patternList.get(m);

                if(ifExist(uLinkList, pattern, 0,0)){
                    featureList.add(1);
                }else{
                    featureList.add(0);
                }

            }
            allFeature.add(featureList);
        }
    }


    void readPattern(String patternFileName, int featureNum) throws IOException{
        BufferedReader myInput = null;
        String thisLine;
        ArrayList<ArrayList<Integer>> oripatternList = new ArrayList<>();
        try {
            // prepare the object for reading the file
            myInput = new BufferedReader(new InputStreamReader(new FileInputStream(new File(patternFileName))));
            // for each line (transaction) until the end of file
            while ((thisLine = myInput.readLine()) != null) {
                // if the line is a comment, is  empty or is a kind of metadata, skip it
                if (thisLine.isEmpty() == true || thisLine.charAt(0) == '#' || thisLine.charAt(0) == '%' || thisLine.charAt(0) == '@') {
                    continue;
                }

                HashSet<Integer> consideredItems = new HashSet<>();
                // split the transaction according to the " " separator
                String tokens[] = thisLine.split(" ");
                int seqLen = tokens.length;
                ArrayList<Integer> pattern = new ArrayList<>(seqLen);

                // Then read each token from this sequence (except the last three tokens
                // which are -1 -2 and the sequence utility)
                for (int i = 0; i <= seqLen - 1; i++) {
                    String currentToken = tokens[i];
                    // if the current token is not -1
                    if (currentToken.length() == 0)
                        continue;
                    int item = Integer.parseInt(currentToken);
                    pattern.add(item);

                }

                oripatternList.add(pattern);
            }

            if(ifRand){
                Random random = new Random();
                Set<Integer> uniqueNumbers = new HashSet<>();

                int desiredCount = featureNum;  // 指定要生成的不重复随机数的个数

                while (uniqueNumbers.size() < desiredCount) {
                    int randomNumber = random.nextInt(oripatternList.size());  // 生成0到99之间的随机数
                    uniqueNumbers.add(randomNumber);
                }
                for (int i : uniqueNumbers) {
                    patternList.add(oripatternList.get(i));
                }
            }else{
                patternList.addAll(oripatternList);
            }

        } catch (Exception e) {
            // catches exception if error while reading the input file
            e.printStackTrace();
        } finally {
            if (myInput != null) {
                myInput.close();
            }
        }
    }

    Boolean ifExist(ULinkList uLinkList, List<Integer> pattern, int startPos,int utility){
        if(pattern.isEmpty()){
            if((double) utility / uLinkList.remainUtility(0) > minUtility)
                return true;
            else
                return false;
        };
        int item = pattern.get(0);
        if(item != -1){
            Integer[] itemIndices = uLinkList.getItemIndices(item);
            if (itemIndices == null)
                return false;
            for(int i = 0; i < itemIndices.length;i++){
                int itemInd = itemIndices[i];
                if(itemInd < startPos)  continue;
                if(ifExist(uLinkList, pattern.subList(1, pattern.size()),itemInd+1,utility+uLinkList.utility(itemInd))){
                    return true;
                }
            }
            return false;
        }else{
            int nextPos = uLinkList.nextItemsetPos(startPos);
            return ifExist(uLinkList, pattern.subList(1, pattern.size()),nextPos,utility);
        }
    }

    ULinkList[] getDBFromTxt_Reduce(String fileName) throws IOException {
        Map<Integer, Integer> mapItemToSWU = new HashMap<>();
        ArrayList<UItem[]> rawDB = new ArrayList<>();
        BufferedReader myInput = null;
        String thisLine;
        int totalUtility = 0;
        int minUtility = 0;
        try {
            // prepare the object for reading the file
            myInput = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            // for each line (transaction) until the end of file
            while ((thisLine = myInput.readLine()) != null) {
                // if the line is a comment, is  empty or is a kind of metadata, skip it
                if (thisLine.isEmpty() == true || thisLine.charAt(0) == '#' || thisLine.charAt(0) == '%' || thisLine.charAt(0) == '@') {
                    continue;
                }

                HashSet<Integer> consideredItems = new HashSet<>();
                // split the transaction according to the " " separator
                String tokens[] = thisLine.split(" ");
                int seqLen;
                if (tokens[tokens.length - 3].equals("-2")) {
                    seqLen = tokens.length - 4;
                }
                else seqLen = tokens.length - 3;
                UItem[] uItems = new UItem[seqLen];

                String sequenceUtilityString = tokens[tokens.length - 1];
                int positionColons = sequenceUtilityString.indexOf(':');
                int sequenceUtility = Integer.parseInt(sequenceUtilityString.substring(positionColons + 1));
                totalUtility += sequenceUtility;

                // Then read each token from this sequence (except the last three tokens
                // which are -1 -2 and the sequence utility)
                for (int i = 0; i <= seqLen - 1; i++) {
                    String currentToken = tokens[i];
                    // if the current token is not -1
                    if (currentToken.length() == 0)
                        continue;
                    if (currentToken.charAt(0) != '-') {
                        // find the left brack
                        int positionLeftBracketString = currentToken.indexOf('[');
                        // get the item
                        String itemString = currentToken.substring(0, positionLeftBracketString);
                        int item = Integer.parseInt(itemString);

                        String itemUtility = currentToken.substring(positionLeftBracketString + 1, currentToken.length() - 1);
                        int utility = Integer.parseInt(itemUtility);

                        uItems[i] = new UItem(item, utility);

                        if (!consideredItems.contains(item)) {
                            consideredItems.add(item);
                            Integer swu = mapItemToSWU.get(item);

                            // add the utility of sequence utility to the swu of this item
                            swu = (swu == null) ? sequenceUtility : swu + sequenceUtility;
                            mapItemToSWU.put(item, swu);
                        }
                    } else {
                        uItems[i] = new UItem(-1, -1);
                    }
                }

                rawDB.add(uItems);
            }
        } catch (Exception e) {
            // catches exception if error while reading the input file
            e.printStackTrace();
        } finally {
            if (myInput != null) { myInput.close();
            }
        }

        /***********trans rawDB to designed seq data structure**********/
        ArrayList<ULinkList> uLinkListDBs = new ArrayList<ULinkList>();
        int maxItemName = 0;
        int maxSequenceLength = 0;
        List<UItem[]> randomList = new ArrayList<>();
        Random random = new Random();

        if(isRandDb){
            int originalSize = rawDB.size();
            for (int i = 0; i < dbsize; i++) {
                int randomIndex = random.nextInt(originalSize);
                UItem[] element = rawDB.get(randomIndex);
                randomList.add(element);
            }
            rawDB = (ArrayList<UItem[]>) randomList;
        }

        Iterator<UItem[]> listIterator = rawDB.iterator();
        while (listIterator.hasNext()) {
            UItem[] uItems = listIterator.next();
            ArrayList<UItem> newItems = new ArrayList<>();
            int seqIndex = 0;
            HashMap<Integer, ArrayList<Integer>> tempHeader = new HashMap<>();
            BitSet tempItemSetIndices = new BitSet(uItems.length);
            for (UItem uItem : uItems) {
                if(uItem == null)
                    continue;
                int item = uItem.itemName();
                if (item != -1) {
                    if (mapItemToSWU.get(item) >= minUtility) {
                        newItems.add(uItem);
                        if (item > maxItemName)
                            maxItemName = item;
                        if (tempHeader.containsKey(item))
                            tempHeader.get(item).add(seqIndex++);
                        else {
                            ArrayList<Integer> list = new ArrayList<>();
                            list.add(seqIndex++);
                            tempHeader.put(item, list);
                        }
                    }
                }
                else
                    tempItemSetIndices.set(seqIndex);
            }
            int size = newItems.size();
            if (size > 0) {
                if (size > maxSequenceLength)
                    maxSequenceLength = size;
                ULinkList uLinkList = new ULinkList();
                uLinkList.seq = newItems.toArray(new UItem[size]);
                uLinkList.remainingUtility = new int[size];
                uLinkList.itemSetIndex = tempItemSetIndices;


//                uLinkList.headTable = new itemAndIndices[tempHeader.size()];
                uLinkList.header = new int[tempHeader.size()];
                uLinkList.headerIndices = new Integer[tempHeader.size()][];
                int hIndex = 0;
                for (Map.Entry<Integer, ArrayList<Integer>> entry : tempHeader.entrySet()) {
                    uLinkList.header[hIndex++] = entry.getKey();
//                    uLinkList.headTable.put(entry.getKey(), entry.getValue().toArray(new Integer[entry.getValue().size()]));
                }
                Arrays.sort(uLinkList.header);
                for (int i = 0; i < uLinkList.header.length; i++) {
                    int cItem = uLinkList.header[i];
                    ArrayList<Integer> indices = tempHeader.get(cItem);
                    uLinkList.headerIndices[i] = indices.toArray(new Integer[indices.size()]);
                }


                int remainingUtility = 0;
                for (int i = uLinkList.length() - 1; i >= 0; --i) {
                    int item = uLinkList.itemName(i);


                    uLinkList.setRemainUtility(i, remainingUtility);
                    remainingUtility += uLinkList.utility(i);

                }

                uLinkListDBs.add(uLinkList);
            }
//            listIterator.remove();
        }


        ULinkList[] uLinkListDB =  uLinkListDBs.toArray(new ULinkList[uLinkListDBs.size()]);

        return uLinkListDB;
    }

}
