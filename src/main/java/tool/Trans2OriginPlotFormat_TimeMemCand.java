package tool;


import java.io.*;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yuting Yang
 * @create 2021-08-03-10:18
 */
public class Trans2OriginPlotFormat_TimeMemCand {
    static String getNum(String string){
        String regEx="(\\d+(\\.\\d+)?)";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        if(m.find())
            return m.group();
        return null;
    }
    public static void main(String[] args) throws Exception {
        String inputFilePath = "exp_Jump_uRatio.txt";
        String outputFilePath = "formattedExp_JumpURatio.txt";
//        String inputFilePath = "exp_HighScala.txt";
//        String outputFilePath = "formattedExp_HighScala.txt";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath)));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFilePath));

        DecimalFormat df = new DecimalFormat("0.000%");

        LinkedHashMap<String, LinkedHashMap<String, String>> resultMap = new LinkedHashMap<>();
        LinkedHashMap<String, String> dataset = null;
        int count = 0;
        String datasetKey = "";
        String entry = null;

        String thisLine;
        while ((thisLine = bufferedReader.readLine()) != null) {
            // if the line is a comment, is  empty or is a kind of metadata, skip it
            if (thisLine.isEmpty() == true || thisLine.charAt(0) == '=' || thisLine.charAt(0) == '%' || thisLine.charAt(0) == '@') {
                count = 0;
                continue;
            }
            String[] strings = thisLine.split(":|~");
            if(strings.length == 1){
                dataset = resultMap.getOrDefault(strings[0], new LinkedHashMap<>());
                if(resultMap.containsKey(strings[0])) {
                    continue;
                }
                resultMap.put(strings[0], dataset);
            }
            else {
                count++;
                String trim = strings[1].trim();
                String num = getNum(trim);
                if(count == 1){
//                    datasetKey = df.format(Double.parseDouble(num));
                    datasetKey = (int)Double.parseDouble(num) + "";
                    entry = dataset.getOrDefault(datasetKey, "");
                }
                else {
                    entry += ",";
                    switch (count) {
                        case 2:
                            entry += num; break;
                        case 3:
                            entry += String.format("%.3f", Double.parseDouble(num));break;
                        default:
                            entry += num;
                    }

                    if (count == 6)//此处数字代表算法运行一次输出的结果的行数，如下输出6行
//                        =============  JUCP-SP - STATS ============
//                    utilityThreshold: 0.00010
//                    time: 4.64 s
//                    Max memory: 209.93553924560547  MB
//                    HUSPs: 5
//                    Candidates: 1451
//                    halfQualifiedNum: 129
                        dataset.put(datasetKey, entry);
                }
            }
        }

        for (Map.Entry<String, LinkedHashMap<String, String>> stringLinkedHashMapEntry : resultMap.entrySet()) {
            bufferedWriter.write(stringLinkedHashMapEntry.getKey());
            bufferedWriter.newLine();
            for (Map.Entry<String, String> stringEntry : stringLinkedHashMapEntry.getValue().entrySet()) {
                bufferedWriter.write(stringEntry.getKey() + stringEntry.getValue());
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.close();
    }
}
