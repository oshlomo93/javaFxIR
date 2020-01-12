package Searcher;

import Parse.Document;
import Parse.Parse;
import Ranker.Ranker;
import java.io.*;
import java.util.*;

public class Searcher {

    private String query;
    private HashMap<String, String> queries;
    private Ranker  ranker;
    private Document parsedQuery;
    private Parse parser;
    private List<Document> allRelevantDocs;
    private HashMap<String, Integer> termTF;
    private ReadQueries reader;
    private int size;
    private IdentifyEntityInDocument allEntities;
    private TreeMap<String, ArrayList<String>> results;
    String resultPath;

    public TreeMap<String, ArrayList<String>> getResults() {
        return results;
    }

    public Searcher(String query, Parse parser, boolean isSemantic, String saveResoltQuery) {

        ranker = new Ranker(isSemantic);
        this.parser = parser;
        this.query = query;
        termTF = new HashMap<>();
        allRelevantDocs = new ArrayList<>();
        size = 50;
        results = new TreeMap<>();
        resultPath = saveResoltQuery;
    }

    public Searcher(String path, boolean isSemantic, Parse parse, boolean isFile,String saveResoltQuery) {
        parser = parse;
        ranker = new Ranker(isSemantic);
        termTF = new HashMap<>();
        reader = new ReadQueries(path);
        allRelevantDocs = new ArrayList<>();
        size = 50;
        results = new TreeMap<>();
        resultPath = saveResoltQuery;

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
                getAllRelevantDocs();
                ArrayList<String> relevantDocs = getRelevantDocs();
                results.put(parsedQuery.getId(), relevantDocs);
                //System.out.println("All relevant docs for query " + doc.getId() + ":");
                //for (String d : relevantDocs) {
                //    System.out.println(d);
                //}
            }
            writeResults(resultPath);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("something went wrong, happy debugging! :)");
        }
    }

    private void writeResults(String resultPath) throws IOException {
        File resultsFile = new File(resultPath + "\\results.txt");
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

    private ArrayList<String> getRelevantDocs() {
        return ranker.rank(parsedQuery, allRelevantDocs, size, termTF);
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
                    doc.addTermPositions(term, positions);
                }
            }
            else {
                Document newDoc = getDocDetails(docID);
                assert newDoc != null;
                newDoc.addTermPositions(term, positions);
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
}
