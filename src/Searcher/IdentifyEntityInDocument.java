package Searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class IdentifyEntityInDocument {

    private HashMap<String, Integer> allDocEntities;
    private String path;
    private String docName;
    private int max;

    public IdentifyEntityInDocument(String path) {
        this.path = path;
        allDocEntities = new HashMap<>();
    }

    public ArrayList<String[]> getAllEntities(String docName) {
        this.docName = docName;
        File entities = new File(path + "\\documentsEntities.txt");
        try {
            FileReader fileReader = new FileReader(entities);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while((line = reader.readLine()) != null) {
                String[] words = line.split(";");
                if (words[0].equals(docName)) {
                    createAllDocEntities(words);
                }
            }
        }
        catch (Exception e) {
            System.out.println("problem with reading the queries in ReadQueries class");
        }
        return getTopEntities();
    }

    private void createAllDocEntities(String[] words) {
        for (int i=1; i<words.length; i++) {
            String[] entity = words[i].split(",");
            allDocEntities.put(entity[0], Integer.valueOf(entity[1]));
        }
        sortEntities();
    }

    private void sortEntities() {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new ArrayList<>(allDocEntities.entrySet());

        // Sort the list
        Collections.sort(list, (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

        // put data from sorted list to hashmap
        HashMap<String, Integer> sorted = new LinkedHashMap<>();
        for (int i=list.size()-1; i>=0; i--) {
            if (i == list.size()-1)
                max = list.get(i).getValue();
            sorted.put(list.get(i).getKey(), list.get(i).getValue());
        }
        allDocEntities = sorted;
    }


    private ArrayList<String[]> getTopEntities() {
        ArrayList<String[]> ans = new ArrayList<>();
        int count = 0;
        for (String entity : allDocEntities.keySet()) {
            String[] str = new String[2];
            str[0] = entity;
            str[1] = String.valueOf(getScore(allDocEntities.get(entity)));
            ans.add(str);
            //System.out.println(str[0] + ", " + str[1]);
            count++;
            if (count == 5)
                break;
        }
        return ans;
    }

    private float getScore(int count) {
        if ( max > 0) {
            return (float)count/max;
        }
        return count;
    }
}
