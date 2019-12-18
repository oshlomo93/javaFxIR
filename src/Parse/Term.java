package Parse;
import java.util.HashMap;
import java.util.Map;

/**
 * This class builds a term, it returns in the program the type of term and the documents it appeared in and the number of times it appeared.
 * Its purpose is to save more information every term
 */
public class Term {

   private String term;
   HashMap<String, Integer> listOfDocument;
   private  String type;

    public Term(String word , String termType){
        if(word != null && word.length() >0 && termType != null && termType.length()>0 ){
            term= word;
            listOfDocument = new HashMap<>();
            type = termType;
        }
    }

    /**
     * @return String that representing the term
     */
    public String getTerm() {
        return term;
    }

    private int getCount() {
        int count = 0;
        for (Map.Entry<String, Integer> entry: listOfDocument.entrySet()) {
            count += entry.getValue();
        }
        return count;
    }

    /**
     * @param document You get the document number that the term appeared
     * @return Has income been made successfully
     */
    public boolean add(String document){
        if(document != null && document.length()>0){
            if(!listOfDocument.containsKey(document)){
                listOfDocument.put(document , 1);
            }
            else{
                listOfDocument.replace(document ,listOfDocument.get(document).intValue()+1 );
            }
            return true;

        }
        return false;
    }

    /**
     * @return toString of the Term
     */
    @Override
    public String toString() {
        String toReturn= "Term: " + term + " appears " + getCount() + " times in " + listOfDocument.size() + " documents";
        return toReturn;
    }


}
