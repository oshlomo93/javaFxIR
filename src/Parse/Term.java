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

    @Override
    public String toString() {
        String toReturn= "Term: " + term + " appears " + getCount() + " times in " + listOfDocument.size() + " documents";
        return toReturn;
    }

    public int numOfthisTermInDic(){
        int count = 0;
        for (Integer val: listOfDocument.values()
        ) {
            count = count+ val;
        }
        return count;
    }

    public int getValOfDocument(String document){
        if(document.length() >0 && document !=null){
            if(listOfDocument.containsKey(document)){
                int val = listOfDocument.get(document);
                return val;
            }
        }
        return -1;
    }

    public int maxVal(){
        int max =0;
        for (Integer val : listOfDocument.values()) {
            if(max < val){
                max= val;
            }
        }
        return max;
    }

    int numOfUniqueDoc(){
        return listOfDocument.size();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Term){
            Term termComper = (Term) obj;
            if(termComper.getTerm().equals(this.term)){
                if(termComper.isSameDocs(this.listOfDocument)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSameDocs(HashMap<String, Integer> sameDocs){
        if(sameDocs != null) {
            if (sameDocs.equals(this.listOfDocument)) {
                return true;
            }
        }
        return false;
    }

    public String getType() {
        return type;
    }
}
