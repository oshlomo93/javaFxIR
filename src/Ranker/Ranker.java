package Ranker;

import Parse.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ranker {

    double bm25Per = (1/3);
    double clickDataPer = (1/3);
    double semanticPer = (1/3);


    ClickstreamData clickstreamData;
    BM25 bm25;
    Semantic semantic;


    public Ranker(){

    }

    public ArrayList<String> rank(Document query , List<Document> documents){
        if(query!=null && documents!=null && documents.size()>0){
            ArrayList<String> strings = new ArrayList<>();
            HashMap<String, Integer> docAndVal;
            for (Document document: documents) {
                double myRank= rankDoc(document);
            }
        }





        return null;
    }

    private double rankDoc(Document document) {
        double toRet=0;
        if(isTheDocumentValid(document) ) {
            double valBm25 = bm25.rankDoc();
            double valSem = semantic.rankDoc();
            double valClickData = clickstreamData.rankDoc();

            toRet = valBm25 * bm25Per + valSem * semanticPer + valClickData * clickDataPer;
        }
        return toRet;
    }

    private boolean isTheDocumentValid(Document document){
        if(document != null ){
            if(document.listOfWord!= null && document.listOfWord.isEmpty() ){
                return true;
            }
        }
        return false;
    }

}
