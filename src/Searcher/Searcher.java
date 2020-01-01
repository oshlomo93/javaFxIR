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

public class Searcher {

    List<String> queries;
    Ranker  ranker;
    Document parsedQuery;
    Parse parser;
    List<Document> allRelevantDocs;
    HashMap<String, Integer> termTF;
    int size;

    public Searcher(String query, Parse parser) {
        ranker = new Ranker();
        this.parser = parser;
        queries = new ArrayList<>();
        termTF = new HashMap<>();
        queries.add(query);
        allRelevantDocs = new ArrayList<>();
        size = 10;

    }

    public Searcher(String[] allQueries) {
        ranker = new Ranker();
        termTF = new HashMap<>();
        queries = getAllQueries(allQueries);
        size = 50;
    }

    public void start() {
        parseQueries();
        parsedQuery = parser.allDocs.get(0);
        getAllRelevantDocs();
        ArrayList<String> relevantDocs = getRelevantDocs();
        for (String doc : relevantDocs) {
            System.out.println(doc);
        }
    }

    private void parseQueries() {
        for (String query : queries) {
            parser.startParseDocument("query", query);
        }

    }
    private List<String> getAllQueries(String[] allQueries) {
        List<String> ans = new ArrayList<>();
        for (String query : allQueries) {
            ans.add(query);
        }
        return ans;
    }

    public ArrayList<String> getRelevantDocs() {
        ArrayList<String> topRankDocs = ranker.rank(parsedQuery, allRelevantDocs, size, termTF);
        return topRankDocs;
    }

    private void getAllRelevantDocs() {
        ArrayList<String> allQueryTerms = parsedQuery.getAllTerms();
        for (String term : allQueryTerms) {
            if (!termTF.containsKey(term))
                getDocsWithTerm(term);
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
}
