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
        clickstreamData = new ClickstreamData();
        bm25 = new BM25();
        semantic = new Semantic();
    }

    public ArrayList<String> rank(Document query , List<Document> documents, int size, HashMap<String, Integer> tf){
        if(query!=null && documents!=null && documents.size()>0){
            ArrayList<String> strings = new ArrayList<>();
            HashMap<String, Double> docAndVal =new HashMap<>();
            for (Document document: documents) {
                double myRank= rankDoc(query ,document, tf);
                docAndVal.put(document.getId(),myRank);
            }

        }





        return null;
    }

    private double rankDoc(Document query ,Document document, HashMap<String, Integer> tf) {
        double toRet=0;
        if(isTheDocumentValid(document)&& tf!=null && !tf.isEmpty() ) {
            double valBm25 = bm25.rankDoc(query,document,tf);
            double valSem = semantic.rankDoc(query,document,tf);
            double valClickData = clickstreamData.rankDoc(query, document,tf);

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
