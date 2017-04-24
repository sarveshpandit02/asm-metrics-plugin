package edu.utdallas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MetricsManager {
    private static Map<String,Map<String,ArrayList<String>>> methodResults = new HashMap<String,Map<String,ArrayList<String>>>();
    private static Set<String> arraySizeMetrics = new HashSet<String>(Arrays.asList("Number of Statements:","Number of expressions:","Number of casts:" ));
    private static Set<String> arrayAndSetSizeMetrics = new HashSet<String>(Arrays.asList("Number of operators:"));
    private static Set<String> setSizeMetrics = new HashSet<String>(Arrays.asList("Number of local methods:",
            "Variable Declarations:","Exceptions Referenced:","Number of external methods:","Variables Referenced:","Class References:"));


    public static void print(){
        PrintWriter writer = null;
        String file = System.getProperty("user.dir") + "/method-metrics.txt";

        File report = new File(file);
        try {
            writer = new PrintWriter(report);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Map<String, ArrayList<String>>> resultEntry :methodResults.entrySet()) {
            writer.println("------------------------------------------------------------------");
            writer.println("Method Name: "+resultEntry.getKey());
            for (Map.Entry<String, ArrayList<String>> resEntry :resultEntry.getValue().entrySet()) {

                if(arraySizeMetrics.contains(resEntry.getKey())){
                    writer.println(resEntry.getKey());
                    writer.println(resEntry.getValue().size());
                }else if(setSizeMetrics.contains(resEntry.getKey())){
                    writer.println(resEntry.getKey());
                    writer.println(new HashSet<>(resEntry.getValue()).size());
                }else if (arrayAndSetSizeMetrics.contains(resEntry.getKey())){
                    int allCount =  resEntry.getValue().size();
                    int uniqueCount = new HashSet<>(resEntry.getValue()).size();

                    writer.println("Unique "+resEntry.getKey());
                    writer.println(uniqueCount);
                    writer.println("");
                    writer.println("All "+resEntry.getKey());
                    writer.println(allCount);

                }else {
                    writer.println(resEntry.getKey());
                    for (String resultLines : new HashSet<>(resEntry.getValue())) {
                        writer.println(resultLines);
                    }
                }
                writer.println("");
            }

        }
        writer.close();
    }

    public static void addResultMessage(String res, String method, String resultType){

        if(methodResults.containsKey(method)){
            Map<String,ArrayList<String>> results = methodResults.get(method);
            if(results.containsKey(resultType)){
                results.get(resultType).add(res);
            }else{
                ArrayList<String> s = new ArrayList<String>();
                s.add(res);
                results.put(resultType,s);
            }
        }else {
            Map<String,ArrayList<String>> resMap = new HashMap<String,ArrayList<String>>();
            ArrayList<String> s = new ArrayList<String>();
            s.add(res);
            resMap.put(resultType,s);
            methodResults.put(method,resMap);
        }
    }

}


