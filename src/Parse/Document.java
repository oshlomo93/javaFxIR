package Parse;

import java.util.*;

public class Document {

    String Id;
    int maxTf;
    int numOfTerms;
    int numOfWords;
    public DictionaryOfDocument listOfWord;

    public Document(String id){
        if(id != null && id.length() >0){
            Id= id;
            int maxTf = 0;
            int numOfTerms = 0;
            int numOfWords = 0;
            listOfWord = new DictionaryOfDocument();
        }
    }

    public Document(String word, String word1, String word2, String word3) {
        this.Id = word;
        this.maxTf = Integer.valueOf(word1);
        this.numOfTerms = Integer.valueOf(word2);
        this.numOfWords = Integer.valueOf(word3);
    }

    public String getId() {
        return Id;
    }

    public boolean addTermByName(String word , int position){
        if(word != null && word.length()>0){
            listOfWord.addTerm(word , position);
            numOfWords++;
            return true;
        }
        return false;
    }

    public  void clean() {

        listOfWord.clear();
    }

    @Override
    public String toString() {
        String str = "" + Id + ";" + maxTf + ";" + numOfTerms + ";" + numOfWords + ";";
        return str;
    }

    //@Override
    //public String toString() {
    //    String toReturn= "Document:" + "\n"+
    //            "Id: " + Id + "\n"+
    //            "The words in this document:";
    //    Object []allWord =  listOfWord.getTerms().toArray();
    //    for(int i=0 ; i< listOfWord.size() ; i++){
    //        toReturn = toReturn +"\n"+ "Word: "+ allWord[i].toString() + ", count: " + listOfWord.getTFofTerm((String) allWord[i])+", Position:";
    //        ArrayList<Integer> allPositions =listOfWord.getAllPosition((String) allWord[i]);
    //        for (int j =0; j<allPositions.size() ; j++){
    //            toReturn+= " "+allPositions.get(j);
    //        }
    //    }
    //    return toReturn;
    //}

    public int sizeOfDocument(){
        return numOfWords;
    }

    public int numOfTerms(){
        return listOfWord.size();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Document){
            Document documentComper = (Document) obj;
            if(documentComper.Id.equals(this.Id)){
                if(documentComper.isSameTerms(this.listOfWord)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSameTerms(DictionaryOfDocument sameTerm ){
        if(sameTerm != null) {
            if (sameTerm.equals(this.listOfWord)) {
                return true;
            }
        }
        return false;
    }


    public void addTerm(Term term , int i){
        if(term != null) {
            addTermByName(term.getTerm() , i);
            int count = listOfWord.getTFofTerm(term.getTerm());
            if (count > maxTf)
                maxTf = count;
        }
    }

    // get string that looks like that: "DocId;tf;position(1),position(2),position(3)....position(n);
    public String getDetails(String term){
        String ans= "";
        if(listOfWord.containsTerm(term)){
            ans =getId()+";"+listOfWord.getDetails(term);
        }
        return ans;
    }

}
