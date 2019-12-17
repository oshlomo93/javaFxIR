package Indexer;

import Parse.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Indexer {

    private Map<String, String> sortedDict;
    private HashMap<String, List<String[]>> pointers;
    private HashMap<String, List<String>> posting;
    private int fileCounter;

    public Indexer() {
        sortedDict = new TreeMap<>();
        pointers = new HashMap<>(1000);
        posting = new HashMap<>(1000);
        fileCounter = 0;
    }

    public Map<String, String> getSortedDict() {
        return sortedDict;
    }

    public void setDictionary(Map<String, String> sortedDict) {
        this.sortedDict = sortedDict;
    }

    public void setPointers(HashMap<String, List<String[]>> pointers) {
        this.pointers = pointers;
    }

    public void updateAll(List<String[]> allTerms, LinkedList<Document> allDocs, String path) throws IOException {
        updateDict(allTerms);
        updatePosting(allDocs, path);
        updateNumOfDocs();
    }

    private void updateNumOfDocs() {
        for (Map.Entry<String, String> term: sortedDict.entrySet()) {
            String name = term.getKey();
            if (pointers.containsKey(name)) {
                String numOfDocs = String.valueOf(pointers.get(name).size());
                sortedDict.replace(name , numOfDocs);
            }
        }
    }

    private void updatePosting(LinkedList<Document> allDocs, String path) throws IOException {
        for (Document doc : allDocs) {
            if (doc != null) {
                Hashtable<String, ArrayList<Integer>> allTermInDoc = doc.listOfWord.getTermAndAllPos();
                for(String term : allTermInDoc.keySet()){
                    List<String> termDetails;
                    if(posting.containsKey(term)){
                        termDetails = posting.get(term);
                        termDetails.add(doc.getDetails(term));
                        posting.replace(term ,termDetails);
                    }
                    else{
                        termDetails = new ArrayList<>();
                        termDetails.add(doc.getDetails(term));
                        posting.put(term ,termDetails);
                    }
                }
            }
        }
        writePostingToDisk(path);
    }

    public void deleteFromDict(Set<String> entityList ){
        if(entityList != null){
            for (String entityString : entityList) {
                if(sortedDict.containsKey(entityString)){
                    String val = sortedDict.get(entityString);
                    int valInt = Integer.parseInt(val);
                    if(valInt==1){
                        sortedDict.remove(entityString);
                    }
                }
            }
        }
    }

    private void updatePointers(String termName, String path, String line) {
        String[] newNode = new String[2];
        newNode[0] = path;
        newNode[1] = line;
        String lowTermName = termName.toLowerCase();
        String upTermName = termName.toUpperCase();
        List<String[]> newList = new ArrayList<>();
        boolean isLow = false;
        if (termName.charAt(0) >= 'a' && termName.charAt(0) <= 'z')
            isLow = true;
        if (isLow) {
            if (pointers.containsKey(lowTermName)) {
                pointers.get(lowTermName).add(newNode);
            }
            else if (pointers.containsKey(upTermName)){
                newList = pointers.get(upTermName);
                newList.add(newNode);
                pointers.put(lowTermName, newList);
                pointers.remove(upTermName);
            }
            else {
                newList.add(newNode);
                pointers.put(lowTermName, newList);
            }
        }
        else {
            if (pointers.containsKey(lowTermName)) {
                pointers.get(lowTermName).add(newNode);
            }
            else if (pointers.containsKey(upTermName)){
                pointers.get(upTermName).add(newNode);
            }
            else {
                newList.add(newNode);
                pointers.put(upTermName, newList);
            }
        }
    }

    private void updateDict(List<String[]> allTerms) {
        Iterator<String[]> iter = allTerms.iterator();
        String[] currentTerm;
        String termName;
        String lowTermName;
        String upTermName;
        boolean isTermLow;
        while (iter.hasNext()) {
            currentTerm = iter.next();
            isTermLow = false;
            termName = currentTerm[0];
            lowTermName = termName.toLowerCase();
            upTermName = termName.toUpperCase();
            if ((termName.charAt(0)) >= 'a' && (termName.charAt(0)) <= 'z')
                isTermLow = true;
            if (!sortedDict.containsKey(upTermName) && !sortedDict.containsKey(lowTermName)) {
                if (isTermLow)
                    sortedDict.put(lowTermName , "1");
                else
                    sortedDict.put(upTermName, "1");
            }
            else {
                if (sortedDict.containsKey(lowTermName))
                    sortedDict.replace(lowTermName, "1");
                else {
                    if (!isTermLow)
                        sortedDict.replace(upTermName, "1");
                    else {
                        sortedDict.put(lowTermName, "1");
                        sortedDict.remove(upTermName);
                    }
                }
            }
        }
    }

    private void writePostingToDisk(String path) throws IOException {
        fileCounter++;
        String newPath = path + "\\postingFile" + fileCounter + ".txt";
        File file = new File(newPath);
        if (file.createNewFile()) {
            int line = 1;
            FileWriter writer = new FileWriter(file);
            for (String termName: posting.keySet()) {
                for (String termList: posting.get(termName))
                {
                    writer.write(termList);
                }
                writer.write("\n");
                updatePointers(termName, newPath, String.valueOf(line));
                line++;
            }

            writer.close();
            posting.clear();
        }
    }

    public void writeDictToDisk(String path) throws IOException {
        File file = new File(path + "\\Dictionary.txt");
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file);
            for (Map.Entry<String, String> termName: sortedDict.entrySet()) {
                writer.write(termName.getKey() + ", " + termName.getValue());
                writer.write("\n");
            }
            writer.close();
        }
        File Pointers = new File(path + "\\Pointers.txt");
        if (Pointers.createNewFile()) {
            FileWriter writer = new FileWriter(Pointers);
            for (Map.Entry<String, List<String[]>> termName: pointers.entrySet()) {
                String name = termName.getKey() + "; ";
                writer.write(name);
                for (String[] str: termName.getValue()) {
                    for (String s: str)
                        writer.write(s + "; ");
                }
                writer.write("\n");
            }
            writer.close();
        }
    }

    public void printDict() {
        int i = 1;
        for (Map.Entry<String, String> entry : sortedDict.entrySet()) {
            System.out.println(i + ": " + entry.getKey() +
                    ", " + entry.getValue());
            i++;
        }
    }
}
