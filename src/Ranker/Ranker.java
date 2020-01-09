package Ranker;

import Parse.Document;

import java.util.*;

public class Ranker {

    private double bm25Per;
    private double semanticPer;

    private HashMap<String, Integer> documentIdAndSize;
    private Map<String, Integer> docIdAndNuberOfUniqeTermInDoc;

    private BM25 bm25;
    private Semantic semantic;


    public Ranker(boolean isSemantic ){
        bm25 = new BM25();
        semantic = new Semantic();
        documentIdAndSize = new HashMap<>();
        docIdAndNuberOfUniqeTermInDoc= new HashMap<>();
        if(isSemantic){
            this.bm25Per = 0.3;
            this.semanticPer = 0.7;
        }
        else {
            this.bm25Per = 0.7;
            this.semanticPer = 0.3;
        }
    }

    public ArrayList<String> rank(Document query , List<Document> documents, int size, HashMap<String, Integer> tf){
        ArrayList<String> toReturn = new ArrayList<>();
        if(query!=null && documents!=null && documents.size()>0){
            HashMap<String, Double> docAndValRank =new HashMap<>();

            for (Document document: documents) {
                if(isTheDocumentValid(document)){
                    documentIdAndSize.put(document.getId(), document.getDocumetSize());
                    docIdAndNuberOfUniqeTermInDoc.put(document.getId(), document.getAllTerms().size());
                }
            }
            for (Document document: documents) {
                if(isTheDocumentValid(document)) {
                    double myRank = rankDoc(query, document, tf,docIdAndNuberOfUniqeTermInDoc);
                    docAndValRank.put(document.getId(), myRank);
                }
            }
            toReturn = merge(docAndValRank, size);

        }
        return toReturn;
    }

    private double rankDoc(Document query ,Document document, HashMap<String, Integer> tf,Map<String, Integer> docIdAndNuberOfUniqeTermInDoc) {
        double toRet=0;
        if(isTheDocumentValid(document)&& tf!=null && !tf.isEmpty()) {
            double valBm25 = bm25.rankDoc(query,document,tf, documentIdAndSize,docIdAndNuberOfUniqeTermInDoc);
            double valSem =0;
            if(semanticPer != 0) {
                valSem= semantic.rankDoc(query, document, tf, documentIdAndSize,docIdAndNuberOfUniqeTermInDoc);
            }
            toRet = valBm25 * bm25Per + valSem * semanticPer;
        }
        return toRet;
    }

    private boolean isTheDocumentValid(Document document){
        if(document != null ){
            return document.listOfWord != null && !document.listOfWord.isEmpty();
        }
        return false;
    }

    private ArrayList<String> merge(HashMap<String, Double> docAndValRank , int size ){
        ArrayList<String> toReturn = new ArrayList<>();
        if(docAndValRank!= null){
            int addStr= 0;
            while (!docAndValRank.isEmpty()){
                ArrayList <String> getDocId = getStringsWithMaxVal(docAndValRank);
                if(getDocId != null && !getDocId.isEmpty()) {
                    if(getDocId.size()+addStr < size){
                        toReturn.addAll(getDocId);
                        addStr = addStr +getDocId.size();
                    }
                    else {
                        while (!getDocId.isEmpty() && addStr<size){
                            toReturn.add(getDocId.remove(0));
                            addStr++;
                        }
                    }
                }

            }
        }
        return toReturn;
    }

    private ArrayList<String> getStringsWithMaxVal(HashMap<String, Double> docAndValRank){
        ArrayList<String> toReturn = new ArrayList<>();
        if(docAndValRank!=null && !docAndValRank.isEmpty()){

            toReturn = new ArrayList<>();
            ArrayList<String>  toRemove = new ArrayList<>();
            double maxVal =findMaxVal(docAndValRank);
            for (String docId :docAndValRank.keySet()) {
                if(maxVal == docAndValRank.get(docId)){
                    toReturn.add(docId);
                    toRemove.add(docId);
                }
            }
            for(int i = 0; i<toRemove.size() ; i++){
                docAndValRank.remove(toReturn.get(i));
            }

        }
        return toReturn;
    }

    private double findMaxVal(HashMap<String, Double> docAndValRank){
        double toReturn = 0;
        if(docAndValRank!=null && !docAndValRank.isEmpty()){
            Collection<Double> allVal = docAndValRank.values();
            toReturn = Collections.max(allVal);
        }
        return  toReturn;
    }


}
