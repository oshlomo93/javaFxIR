package Searcher;

import Parse.Document;
import Parse.Parse;
import Ranker.Ranker;

import java.util.ArrayList;
import java.util.List;

public class Searcher {

    List<String> queries;
    Ranker  ranker;
    Document parsedQuery;
    Parse parser;
    List<Document> allRelevantDocs;

    public Searcher(String query) {
        ranker = new Ranker();
        queries = new ArrayList<>();
        queries.add(query);

    }

    public Searcher(String[] allQueries) {
        ranker = new Ranker();
        queries = getAllQueries(allQueries);
    }


    private List<String> getAllQueries(String[] allQueries) {
        List<String> ans = new ArrayList<>();
        for (String query : allQueries) {
            ans.add(query);
        }
        return ans;
    }

    public ArrayList<String> getRelevantDocs() {
        ArrayList<String> topRankDocs = ranker.rank(parsedQuery, allRelevantDocs);
        return topRankDocs;
    }

    private void getAllRelevantDocs() {
        ArrayList<String> allQueryTerms = parsedQuery.getAllTerms();
        for (String term : allQueryTerms) {

        }
    }



}
