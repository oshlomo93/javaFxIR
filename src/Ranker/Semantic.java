package Ranker;

import Parse.Document;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class Semantic implements IRanker {
    private static ILexicalDatabase db = new NictWordNet();

    @Override
    public double rankDoc(Document query ,Document document, HashMap<String, Integer> tf, HashMap<String, Integer> documentIdAndSize) {
        double toReturn=0;
        if(query != null && document!= null){
            ArrayList<String> allQueryTerms = query.getAllTerms();
            ArrayList<String> allDocTerms = document.getAllTerms();
            if(allDocTerms != null && allDocTerms.size()>0 && allQueryTerms!= null && allQueryTerms.size()>0) {
                for (String termInQuery : allQueryTerms) {
                    toReturn = getWordSimToArrWords(allDocTerms,termInQuery);
                }
                toReturn =toReturn/allQueryTerms.size();
            }
        }

        return toReturn;
    }


    private  double getSimTooWords(String word1 ,String word2 ){
        double toReturn =0;
        if(word1 != null && word2 != null && word1.length()>0 && word2.length()>0) {
            WS4JConfiguration.getInstance().setMFS(true);
            toReturn= new WuPalmer(db).calcRelatednessOfWords(word1, word2);
        }
        return toReturn;
    }

    private double getWordSimToArrWords(ArrayList<String> words , String word){
        double sumSim = 0;
        if(words!= null && words.size()>0 && word != null && word.length()>0){
            for (String wordCompered: words) {
                sumSim = sumSim +getSimTooWords(word, wordCompered);
            }
            sumSim= sumSim/words.size();
        }
        return sumSim;
    }


}
