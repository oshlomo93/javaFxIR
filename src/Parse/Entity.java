package Parse;

import java.util.HashSet;
import java.util.Set;

/**
 * This class creates terms that are entities
 */
public class Entity implements IRules {

    Set<String> listOfEntity;

    public Entity() {
        listOfEntity = new HashSet<>();
    }

    /**
     * word test whether it represents entity
     * @param word
     * @return boolean
     */
    @Override
    public boolean amIThis(String word) {
        return false;
    }

    /**
     * 2-word test whether it represents entity
     * @param wordone
     * @param wordtwo
     * @return boolean
     */
    @Override
    public boolean amIThis(String wordone, String wordtwo) {
        if(amIThis(wordone) && amIThis(wordtwo)){
            return true;
        }
        return false;
    }

    /**
     * 3-word test whether it represents entity
     * @param word1
     * @param word2
     * @param word3
     * @return boolean
     */
    @Override
    public boolean amIThis(String word1, String word2, String word3) {
        if(amIThis(word1) && amIThis(word2) && amIThis(word3)){
            return true;
        }
        return false;
    }

    /**
     * 4-word test whether it represents entity
     * @param word1
     * @param word2
     * @param word3
     * @param word4
     * @return boolean
     */
    @Override
    public boolean amIThis(String word1, String word2, String word3, String word4) {
        if(amIThis(word1) && amIThis(word2) && amIThis(word3) && amIThis(word4)){
            return true;
        }
        return false;
    }


    /**
     * Creates a entity type term
     * @param word
     * @return Object Term
     */
    @Override
    public Term makeTerm(String word) {
        Term term = null;
        String[] allWords =word.split(" ");
        if(allWords.length ==1){
            return null;
        }

        else if(allWords.length == 2){
            if(amIThis(allWords[0] ,allWords[1] )){
                term = new Term(word ,"Entity" );
                listOfEntity.add(word);
                return term;
            }
            return null;
        }

        else if(allWords.length == 3){
            if(amIThis(allWords[0] ,allWords[1]  , allWords[2])){
                term = new Term(word ,"Entity" );
                listOfEntity.add(word);
                return term;
            }
            return null;
        }

        else if(allWords.length == 4){
            if(amIThis(allWords[0] ,allWords[1]  , allWords[2] , allWords[2])){
                term = new Term(word ,"Entity" );
                listOfEntity.add(word);
                return term;
            }
            return null;
        }

        return term;
    }

}

