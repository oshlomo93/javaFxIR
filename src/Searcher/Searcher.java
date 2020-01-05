package Searcher;

import Parse.Document;
import Parse.Parse;
import Ranker.Ranker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Searcher {

    String query;
    HashMap<String, String> queries;
    Ranker  ranker;
    Document parsedQuery;
    Parse parser;
    List<Document> allRelevantDocs;
    HashMap<String, Integer> termTF;
    ReadQueries reader;
    int size;
    IdentifyEntityInDocument allEntities;
    HashMap<String, ArrayList<String>> results;

    public Searcher(String query, Parse parser, boolean isSemantic) {
        ranker = new Ranker(isSemantic);
        this.parser = parser;
        this.query = query;
        //this.allEntities = new IdentifyEntityInDocument(parser.getPostingPath());
        termTF = new HashMap<>();
        allRelevantDocs = new ArrayList<>();
        size = 10;
    }

    public Searcher(String path, boolean isSemantic) {
        ranker = new Ranker(isSemantic);
        termTF = new HashMap<>();
        reader = new ReadQueries(path);
        size = 50;
        results = new HashMap<>();
    }

    public void start() throws IOException {
        try {
            if (reader != null) {
                getAllQueries();
            }
            parseQueries();
            for (Document doc : parser.allDocs) {
                parsedQuery = doc;
                getAllRelevantDocs();
                ArrayList<String> topRelevantDocs = getRelevantDocs();
                results.put(parsedQuery.getId(), topRelevantDocs);
            }
        }
        catch (Exception e) {
        }
    }

    private void getAllQueries() throws IOException {
        try {
            reader.readQueries();
            queries = reader.getQueries();
        }
        catch (Exception e) {
        }
    }

    private void parseQueries() {
        if (query != null){
            parser.startParseDocument("query", query);
        }
        else if (queries != null) {
            for (Map.Entry<String,String> query : queries.entrySet()) {
                parser.startParseDocument(query.getKey(), query.getValue());
            }
        }
    }

    public ArrayList<String> getRelevantDocs() {
        ArrayList<String> topRankDocs = ranker.rank(parsedQuery, allRelevantDocs, size, termTF);
        return topRankDocs;
    }

    private void getAllRelevantDocs() throws IOException {
        ArrayList<String> allQueryTerms = parsedQuery.getAllTerms();
        for (String term : allQueryTerms) {
            if (!termTF.containsKey(term))
                getDocsWithTerm(term);
        }
        getDocsTerms();
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
                Document newDoc = new Document(docID);
                newDoc.addTermPositions(term, positions);
                allRelevantDocs.add(newDoc);
            }
            counter += tf;
        }
        return counter;
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
        for (Document doc : allRelevantDocs) {
            if (doc.getId().equals(docID))
                return true;
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

    private void getDocsTerms() throws IOException {
        for (Document doc : allRelevantDocs) {
            ArrayList<String> allDocTerms = readTerms(doc.getId());
            doc.listOfWord.setTerms(allDocTerms);
        }
    }

    private ArrayList<String> readTerms(String docName) throws IOException {
        ArrayList<String> allTerms = new ArrayList<>();
        File terms = new File(parser.getPostingPath() + "\\documentsTerms.txt");
        try {
            FileReader fileReader = new FileReader(terms);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while((line = reader.readLine()) != null) {
                String[] words = line.split(";");
                if (words[0].equals(docName)) {
                    for (int i=1; i<words.length; i++) {
                        allTerms.add(words[0]);
                    }
                    return  allTerms;
                }
            }
        }
        catch (Exception e) {
            System.out.println("problem with reading the terms");
        }
        return allTerms;
    }
}
