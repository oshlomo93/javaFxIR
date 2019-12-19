package Parse;

/**
 * This class returns on each document its existing terms,
 * in addition to the number of terms, and the frequency of each term in the document
 */
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

    /**
     * Return the ID Of the document
     * @return  String
     */
    public String getId() {
        return Id;
    }

    /**
     * The storm of a document by the string
     * @param word
     * @param position
     * @return
     */
    public boolean addTermByName(String word , int position){
        if(word != null && word.length()>0){
            listOfWord.addTerm(word , position);
            numOfWords++;
            return true;
        }
        return false;
    }

    /**
     * Returns a string representing the document
     * @return String
     */
    @Override
    public String toString() {
        String str = "" + Id + ";" + maxTf + ";" + numOfTerms + ";" + numOfWords + ";";
        return str;
    }


    /**
     * Is this document the same as the one received
     * @param obj
     * @return boolean
     */
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

    /**
     * Does the same term exist in the document
     * @param sameTerm
     * @return boolean
     */
    public boolean isSameTerms(DictionaryOfDocument sameTerm ){
        if(sameTerm != null) {
            if (sameTerm.equals(this.listOfWord)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Add a term to a document
     * @param term
     * @param i- Position the term in the document
     */
    public void addTerm(Term term , int i){
        if(term != null) {
            addTermByName(term.getTerm() , i);
            int count = listOfWord.getTFofTerm(term.getTerm());
            if (count > maxTf)
                maxTf = count;
        }
    }

    /**
     * get string that looks like that: "DocId;tf;position(1),position(2),position(3)....position(n);
     * @param term
     * @return String
     */

    public String getDetails(String term){
        String ans= "";
        if(listOfWord.containsTerm(term)){
            ans =getId()+";"+listOfWord.getDetails(term);
        }
        return ans;
    }

}
