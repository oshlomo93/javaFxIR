package Ranker;

import Parse.Document;

import java.util.HashMap;

public class BM25 implements IRanker {
    double b = 0.75;
    double k = 1.2;
    int size;

    @Override
    public double rankDoc(Document query ,Document document, HashMap<String, Integer> tf) {

        return 0;
    }

    private int getNumberOfWordInDocument(Document document){
        if (document!= null) {
            return document.getDocumetSize();
        }
        return 0;
    }
    // tf(i)
    private int termFrequencyInDoc(){
        return 0;
    }

    // df(i)
    private int amountOfDocumentsTermAppears(String term){
        return 0;
    }

    private int numberOfDocument(){
        return size;
    }




}
