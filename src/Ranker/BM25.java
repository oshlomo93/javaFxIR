package Ranker;

import Parse.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BM25 implements IRanker {
    private double b = 0.75;
    private double k = 1.2;

    @Override
    public double rankDoc(Document query , Document document, HashMap<String, Integer> tf, HashMap<String, Integer> documentIdAndSize, Map<String, Integer> docIdAndNuberOfUniqeTermInDoc) {
        double sum =0;
        if(query!=null && document!= null && tf!= null &&tf.size()>0 && documentIdAndSize!=null && documentIdAndSize.size()>0) {
            ArrayList<String> allTerms =query.getAllTerms();
            for (String term: allTerms )  {
                sum = sum + rankBM25perTerm(term,documentIdAndSize,document,tf,docIdAndNuberOfUniqeTermInDoc);
            }
        }
        return sum;
    }

    // tf(i)
    private int termFrequencyInDoc(String term , Document document){
        int val= 0;
        if(term!= null && term.length()>0 && document!= null) {
            if (document.listOfWord.getTermAndTF().containsKey(term))
                val = document.listOfWord.getTermAndTF().get(term);
        }
        return val;
    }

    // df(i)
    private int amountOfDocumentsTermAppears(String term, HashMap<String, Integer> tf){
        if(term!= null && term.length()>0 && tf!= null && tf.size()>0) {
            return tf.get(term);
        }
        return 0;
    }

    private double avgDocumentsSize(HashMap<String, Integer> documentIdAndSize){
        return avgDocbySomthing(documentIdAndSize);
    }

    private double rankBM25perTerm(String term , HashMap<String, Integer> documentIdAndSize, Document document,HashMap<String, Integer> tf, Map<String, Integer> docIdAndNuberOfUniqeTermInDoc){
        double toReturn =0;

        if(term != null && term.length()>0 && documentIdAndSize!=null &&documentIdAndSize.size()>0 && document!= null && tf!= null && tf.size()>0) {

            int N = documentIdAndSize.size();
            int dfi = amountOfDocumentsTermAppears(term, tf);
            int tfi = termFrequencyInDoc(term, document);


            //int dSize = document.getDocumetSize();
            //double avgD = avgDocumentsSize(documentIdAndSize);

            int dSize = document.getAllTerms().size();
            double avgD= avgDocumentsUniqSize(docIdAndNuberOfUniqeTermInDoc);

            double partA= log_N_Fractional_Dfi(N, dfi);
            double partB = k_plus_1_Dual_tfi(tfi);
            double partC = calculationWithBAndWithKAndWithTfi_d_fra_avgD(dSize , avgD , tfi);

            double partD= newHelpFormula(tfi, dSize);
            if(partC!=0){
                //toReturn = partA*(partB/partC); //todo
                toReturn = partD*(partB/partC);
            }
        }

        return toReturn;
    }

    //calculation -> log(N/dfi)
    private double log_N_Fractional_Dfi(int N , int dfi){
        double toReturn=0;
        if(dfi>0){
            double nFDfi= N/dfi;
            toReturn = Math.log(nFDfi);
        }
        return toReturn;
    }

    private double newHelpFormula(double Fij, double Aij){
        double toReturn =0;
        if(Fij>0 && Aij>0) {
            double part_1 = 1 + Math.log(Fij);
            double part_2= 1+Math.log(Aij);
            toReturn = part_1/part_2;
            toReturn = toReturn*Fij;
        }
        return toReturn;
    }

    //calculation -> (k+1)*tfi
    private double k_plus_1_Dual_tfi(int tfi){
        return (k+1)*tfi;
    }

    //calculation -> |d|/avg(d)
    private double documentSize_Fractional_avgD(int dSize, double avgD){
        double toReturn=0;
        if(avgD!= 0){
            toReturn = dSize/avgD;
        }
        return toReturn;
    }

    //calculation -> (1-b)+b*(|d|/avg(d))
    private double calculationWithB_d_fra_avgD(int dSize, double avgD){
       // double d_fra_avgD = documentSize_Fractional_avgD(dSize,avgD); // todo
        double d_fra_avgD= documentSize_Fractional_avgUniqTerms(dSize,avgD);
        return (1-b)+b*d_fra_avgD;
    }

    //calculation -> k*((1-b)+b*(|d|/avg(d)))
    private double calculationWithBAndWithK_d_fra_avgD(int dSize, double avgD){
        double b_d_fra_avgD =calculationWithB_d_fra_avgD(dSize, avgD);
        return b_d_fra_avgD*k;
    }



    //calculation -> k*((1-b)+b*(|d|/avg(d)))+tfi
    private double calculationWithBAndWithKAndWithTfi_d_fra_avgD(int dSize, double avgD, double tfi){
        double b_d_fra_avgD_k = calculationWithBAndWithK_d_fra_avgD(dSize,avgD);
        return b_d_fra_avgD_k+tfi;
    }

    //calculation -> |dUn|/avg(dUn)
    private double documentSize_Fractional_avgUniqTerms(int dUniqueSize, double avgDUniq){
        double toReturn=0;
        if(avgDUniq!= 0){
            toReturn = dUniqueSize/avgDUniq;
        }
        return toReturn;
    }


    private double avgDocumentsUniqSize(Map<String, Integer> docIdAndNuderOfUniqTermInDoc){
        return avgDocbySomthing(docIdAndNuderOfUniqTermInDoc);
    }

    private double avgDocbySomthing(Map<String, Integer> docIdAndNuderOfUniqTermInDoc) {
        int avgD = 0;
        if( docIdAndNuderOfUniqTermInDoc!=null &&!docIdAndNuderOfUniqTermInDoc.isEmpty()){
            int sum=0;
            for (String docId: docIdAndNuderOfUniqTermInDoc.keySet()) {
                sum = sum +docIdAndNuderOfUniqTermInDoc.get(docId);
            }
            avgD = sum/(docIdAndNuderOfUniqTermInDoc.size());
        }
        return avgD;
    }
}
