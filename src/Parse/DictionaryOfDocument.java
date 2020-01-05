package Parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

/**
 * This class holds all the data in the software,
 * what words appear in it and the location of each word
 */
public class DictionaryOfDocument{

    HashMap<String , Integer> termAndTF;
    HashMap<String , ArrayList<Integer>> termAndAllPos;
    ArrayList<String> allTerms;


    public DictionaryOfDocument() {
        termAndTF = new HashMap();
        termAndAllPos =new HashMap();
        allTerms = new ArrayList<>();
    }


    /**
     * All Positions of the term in the document
     * @param term
     * @return ArrayList<Integer>
     */

    public ArrayList<Integer> getAllPosition(String term) {
        return termAndAllPos.get(term);
    }



    /** Add term to Dictionary
     * @param term
     * @param position
     */

    public void addTerm(String term , int position){
        if(termAndTF.containsKey(term)){
            ArrayList<Integer> allOldPosition = termAndAllPos.get(term);
            allOldPosition.add(position);
            termAndAllPos.replace(term , allOldPosition);
            termAndTF.replace(term , allOldPosition.size());
        }
        else{
            ArrayList<Integer> newArrOfPosition = new ArrayList<>();
            newArrOfPosition.add(position);
            termAndTF.put(term , 1);
            termAndAllPos.put(term , newArrOfPosition);
        }

    }



    /**
     * @param term
     * @return int
     */
    public int getTFofTerm(String term){
        return termAndTF.get(term);
    }


    /**
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DictionaryOfDocument){
            DictionaryOfDocument dictionaryOfDocumentObj = (DictionaryOfDocument) obj;
            if(dictionaryOfDocumentObj.getTermAndAllPos().equals(this.termAndAllPos) && dictionaryOfDocumentObj.getTermAndTF().equals(this.termAndTF)){
                return true;
            }
        }
        return false;
    }

    /**  get term and Frequency of all terms in the document
     * @return Hashtable
     */
    public HashMap<String, Integer> getTermAndTF() {
        return termAndTF;
    }

    /**
     * Accept all terms and all their locations in the document
     * @return Hashtable
     */
    public HashMap<String, ArrayList<Integer>> getTermAndAllPos() {
        return termAndAllPos;
    }

    /**
     * Checks whether the term is present
     * @param term
     * @return boolean
     */
    public boolean containsTerm(String term){
        if(termAndTF.containsKey(term)) {
            return true;
        }
        return false;
    }

    /**
     * get string that looks like that: "tf;position(1),position(2),position(3)....position(n);"
     * @param termName
     * @return String
     */
    public String getDetails(String termName) {
        String ans ="";
        if(termAndTF.containsKey(termName)){
            ans = getTFofTerm(termName) +";";
            ArrayList<Integer> allPositionOfTerm= getAllPosition(termName);
            for (int i=0; i<allPositionOfTerm.size(); i++ ) {
                if(i==0){
                    ans = ans + allPositionOfTerm.get(i);
                }
                else{
                    ans = ans+ "," + allPositionOfTerm.get(i);
                }
                if(i == allPositionOfTerm.size()-1){
                    ans = ans +";";
                }
            }
        }
        return ans;
    }

    public boolean isEmpty(){
        if(termAndTF.isEmpty() || termAndAllPos.isEmpty()){
            return true;
        }
        return false;
    }

    public ArrayList<String> getAllTerms(){
        ArrayList<String> toRet = new ArrayList<>();
        toRet.addAll(termAndTF.keySet());
        return toRet;
    }

    public void setTerms(ArrayList<String> terms) {
        for (String term : terms) {
            allTerms.add(term);
        }
    }
}
