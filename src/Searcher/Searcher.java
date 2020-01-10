package Searcher;

import Parse.Document;
import Parse.Parse;
import Ranker.Ranker;

import javax.print.Doc;
import java.io.*;
import java.net.InetAddress;
import java.util.*;

public class Searcher {

    private String query;
    private HashMap<String, String> queries;
    private Ranker  ranker;
    private Document parsedQuery;
    private Parse parser;
    private ArrayList<Document> allRelevantDocs;
    private HashMap<String, Integer> termTF;
    private ReadQueries reader;
    private int size;
    private TreeMap<String, ArrayList<String>> results;
    String saveQueryPath;

    public TreeMap<String, ArrayList<String>> getResults() {
        return results;
    }

    public Searcher(String query, Parse parser, boolean isSemantic,String saveQueryPath) {
        ranker = new Ranker(isSemantic);
        this.parser = parser;
        this.query = query;
        termTF = new HashMap<>();
        allRelevantDocs = new ArrayList<>();
        size = 50;
        results = new TreeMap<>();
        this.saveQueryPath = saveQueryPath;
    }

    public Searcher(String path, boolean isSemantic, Parse parse, boolean isFile,String saveQueryPath) {
        parser = parse;
        ranker = new Ranker(isSemantic);
        termTF = new HashMap<>();
        reader = new ReadQueries(path);
        allRelevantDocs = new ArrayList<>();
        size = 50;
        results = new TreeMap<>();
        this.saveQueryPath = saveQueryPath;
    }

    public void start() {
        try {
            if (reader != null) {
                this.reader.resetReader();
                getAllQueries();
            }
            parser.allQueries.clear();
            parseQueries();
            for (Document doc : parser.allQueries) {
                parsedQuery = doc;
                HashMap<String, ArrayList<String>> allPath = getAllPath();
                ArrayList<String> allDocsAsString = getAllRelevantDocsAndTermTF(allPath);
                allRelevantDocs = getAllRelevantDocs(allDocsAsString);
                updateReleventDocs();
                ArrayList<String> relevantDocs = ranker.rank(parsedQuery, allRelevantDocs, size, termTF);
                results.put(parsedQuery.getId(), relevantDocs);
                //System.out.println("All relevant docs for query " + doc.getId() + ":");
                //for (String d : relevantDocs) {
                //    System.out.println(d);
                //}
            }
            writeResults(saveQueryPath);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("something went wrong, happy debugging! :)");
        }
    }

    private void updateReleventDocs() {
        ArrayList<String> docsAndTerms= read(parser.getPostingPath()+ "\\documentsTerms.txt");
        for (String line : docsAndTerms) {
            String[] lineSplite = line.split(";");
            Document document = getDocumentById(lineSplite[0]);
            for (int i=1 ; i< lineSplite.length ; i++){
                document.addTermByName(lineSplite[i]);
            }
        }
    }

    Document getDocumentById(String docId){
        for ( Document document : allRelevantDocs ) {
            if(document.getId().equals(docId)){
                return document;
            }
        }
        return null;
    }



    private ArrayList<Document> getAllRelevantDocs(ArrayList<String> allDocsAsString) {
        ArrayList<Document> allRelevantDocs = new ArrayList<>();
        HashMap<String, int[]> allDocsDetails = readFile(parser.getPostingPath()+ "\\documentsDetails.txt");
        for (String docName : allDocsAsString) {
            int [] allData = allDocsDetails.get(docName);
            Document newDoc = new Document(docName, ""+allData[0],""+allData[1],""+allData[2]);
            allRelevantDocs.add(newDoc);
        }
        return allRelevantDocs;
    }


    private void writeResults(String saveQueryPath) throws IOException {
        File resultsFile = new File(saveQueryPath + "\\results.txt");
        FileWriter writer = new FileWriter(resultsFile);
        for (String query : results.keySet()) {
            for (String docName : results.get(query)) {
                String line = query + " 0 " + docName + " 1 " + 0 + " void";
                writer.write(line + "\n");
            }
        }
        writer.close();
    }

    private void getAllQueries() {
        try {
            reader.readQueries();
            queries = reader.getQueries();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetResults() {
        this.results.clear();
    }

    private void parseQueries() {
        if (query != null){
            parser.startParseDocument("query", query, true);
        }
        else if (queries != null) {
            for (Map.Entry<String,String> query : queries.entrySet()) {
                parser.startParseDocument(query.getKey(), query.getValue(), true);
            }
        }
    }


    private void getAllRelevantDocs() {
        ArrayList<String> allQueryTerms = parsedQuery.getAllTerms();
        if (allQueryTerms.size() > 0) {
            for (String term : allQueryTerms) {
                if (!termTF.containsKey(term))
                    getDocsWithTerm(term);
            }
        }
    }

    private void getDocsWithTerm(String term) {
        int tfCounter = 0;
        List<String[]> allDocs;
        HashMap<String, List<String[]>> pointers = parser.indexer.getPointers();
        if (pointers.containsKey(term)) {
            allDocs = pointers.get(term);
            for (String[] path : allDocs) {
                String details = read(path[0], Integer.parseInt(path[1]));
                tfCounter += update(term, details, tfCounter);
            }
        }
        termTF.put(term, 1);
    }

    private int update(String term, String details, int tfCounter) {
        int counter = tfCounter;
        String[] allDetails = details.split(";");
        for (int i=0; i<allDetails.length; i=i+3) {
            String[] docDetails = new String[3];
            docDetails[0] = allDetails[i];
            docDetails[1] = allDetails[i+1];
            docDetails[2] = allDetails[i+2];
            String docID = docDetails[0];
            int tf = Integer.parseInt(docDetails[1]);
            String[] positions = docDetails[2].split(",");
            if (isExists(docID)) {
                Document doc = getDoc(docID);
                if (doc != null) {
                    doc.addTermByName(term);
                }
            }
            else {
                Document newDoc = getDocDetails(docID);
                assert newDoc != null;
                newDoc.addTermByName(term);
                allRelevantDocs.add(newDoc);
            }
            counter += tf;
        }
        return counter;
    }

    private Document getDocDetails(String docID) {
        try {
            Document newDoc = new Document(docID);
            File file = new File(parser.getPostingPath() + "\\documentsDetails.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(";");
                if (words[0].equals(docID)) {
                    newDoc.setMaxTf(Integer.valueOf(words[1]));
                    newDoc.setNumOfTerms(Integer.valueOf(words[2]));
                    newDoc.setNumOfWords(Integer.valueOf(words[3]));
                    return newDoc;
                }
            }
            return newDoc;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Document getDoc(String docID) {
        Document doc = null;
        for (Document d : allRelevantDocs) {
            if (d.getId().equals(docID))
                doc = d;
            break;
        }
        return doc;
    }

    private boolean isExists(String docID) {
        try {
            if (allRelevantDocs != null) {
                for (Document doc : allRelevantDocs) {
                    if (doc.getId().equals(docID))
                        return true;
                }
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String read(String fileName, int lineNumber) {
        try {
            String ans = "";
            int lineCounter = 0;
            File file = new File(parser.getPostingPath() + "\\" + fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            while (lineCounter != lineNumber) {
                ans = reader.readLine();
                lineCounter++;
            }
            return ans;
        }
        catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }





    public  HashMap<String, ArrayList<String>> getAllPath() {
        HashMap<String, ArrayList<String>> allPostingFilesAndLines = new HashMap<>();
        ArrayList<String> allQueryTerms = parsedQuery.getAllTerms();
        if (allQueryTerms.size() > 0) {
            HashMap<String, List<String[]>> pointers = parser.indexer.getPointers();
            for (String term : allQueryTerms) {
                if (pointers.containsKey(term)) {
                    for (String[] posting : pointers.get(term)) {
                        String file = posting[0];
                        String line = posting[1];
                        if (allPostingFilesAndLines.containsKey(file)) {

                            allPostingFilesAndLines.get(file).add(line);
                        }
                        else {
                            ArrayList<String> lines = new ArrayList<>();
                            lines.add(line);
                            allPostingFilesAndLines.put(file, lines);
                        }
                    }
                }
            }
        }
        return allPostingFilesAndLines;
    }

    private ArrayList<String> getAllRelevantDocsAndTermTF(HashMap<String, ArrayList<String>> allPath) {
        ArrayList<String> allDocs = new ArrayList<>();
        for (String file : allPath.keySet()) {
            ArrayList<String> allFileLines = read(file);
            for (String line : allFileLines) {
                String[] words = line.split(";");
                String docName;
                String termName = words[0];
                for (int i=1; i<words.length; i++) {
                    if (i%2 == 1) {
                        docName = words[i];
                        if (!allDocs.contains(docName))
                            allDocs.add(docName);
                    }
                    else {
                        int size = Integer.valueOf(words[i]);
                        if (termTF.containsKey(termName)) {
                            int newVal = termTF.get(termName) + size;
                            termTF.replace(termName, newVal);
                        }
                        else {
                            termTF.put(termName, size);
                        }
                    }
                }
            }
        }
        return allDocs;
    }

    private ArrayList<String> read(String file) {
        ArrayList<String> allLines = new ArrayList<>();
        try {
            File postingFile = new File(parser.getPostingPath() + "\\" + file + ".txt");
            FileReader reader = new FileReader(postingFile);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                allLines.add(line);
            }
            br.close();
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return allLines;
    }

    private HashMap<String, int[]> readFile(String file) {
        HashMap<String, int[]> allDocsDetails = new HashMap<>();
        try {
            File detailsFile = new File(file);
            FileReader reader = new FileReader(detailsFile);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(";");
                int[] details = new int[3];
                details[0] = Integer.valueOf(words[1]);
                details[1] = Integer.valueOf(words[2]);
                details[2] = Integer.valueOf(words[3]);
                allDocsDetails.put(words[0], details);
            }
            br.close();
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return allDocsDetails;
    }


}
