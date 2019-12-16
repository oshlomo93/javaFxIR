package Parse;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class DictionaryOfDocument{

    Hashtable<String , Integer> termAndTF;
    Hashtable<String , ArrayList<Integer>> termAndAllPos;


    public DictionaryOfDocument() {
        termAndTF = new Hashtable<>();
        termAndAllPos =new Hashtable<>();
    }

    public int size() {
        return termAndTF.size();
    }

    public boolean isEmpty() {
        return termAndTF.isEmpty();
    }

    //All Positions of the term in the document
    public ArrayList<Integer> getAllPosition(String term) {
        return termAndAllPos.get(term);
    }

    //Does the term exist in this position?
    public boolean isTermExistInPosition(String term , int position) {
        if(termAndAllPos.containsKey(term)){
            ArrayList<Integer> allPortionOfTerm = getAllPosition(term);
            if(allPortionOfTerm.contains(position)){
                return true;
            }
        }
        return false;
    }

    //Add term to Dictionary
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

    public int getTFofTerm(String term){
        return termAndTF.get(term);
    }


    public void clear(){
        termAndTF.clear();
        termAndAllPos.clear();
    }

    public Set<String> getTerms(){
        return termAndAllPos.keySet();
    }


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

    public Hashtable<String, Integer> getTermAndTF() {
        return termAndTF;
    }

    public Hashtable<String, ArrayList<Integer>> getTermAndAllPos() {
        return termAndAllPos;
    }

    public boolean containsTerm(String term){
        if(termAndTF.containsKey(term)){
            return true;
        }
        return false;
    }

    // get string that looks like that: "tf;position(1),position(2),position(3)....position(n);"
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

}
