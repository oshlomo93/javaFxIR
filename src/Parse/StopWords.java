package Parse;

import java.io.*;
import java.util.ArrayList;

/**
 * This class gets a path to the address of the file containing all the stopping words,
 * in this way it identifies all the words that we should not create as terms for our retrieval engine
 */
public class StopWords {

    ArrayList <String> allStopWords;

    public StopWords(String path) throws IOException {
        if(path.length()> 0 &&  path != null){
            File newFile = new File(path);
                FileReader fileReader = new FileReader(newFile);
                BufferedReader reader = new BufferedReader(fileReader);
                allStopWords = new ArrayList<>();
                String stopWord= reader.readLine();
                while( stopWord != null){
                    allStopWords.add(stopWord);
                    stopWord =reader.readLine();
                }
        }
    }

    /**
     * This method receives a word and it omits whether this word is a stop word type
     * @param word
     * @return boolean
     */
    public boolean AmIStopWord(String word){
        if(allStopWords.contains(word)){
            return true;
        }
        String lowWord= word.toLowerCase();
        if(allStopWords.contains(lowWord)){
            return true;
        }
        return false;
    }


}
