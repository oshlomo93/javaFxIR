package GUI;

import Parse.Parse;
import Searcher.Searcher;

import java.io.IOException;
import java.util.Map;
import java.util.Observable;

/**
 *  * This class controls all engine activity
 */
public class Model extends Observable {

    Parse parse;
    Searcher searcher;

    public Model() {

    }


    /**
     * The function that runs the entire engine
     * @param corpusAndStopWordsStringPath
     * @param postingFilesStringPath
     * @param isStemmer
     * @throws IOException
     */
    public void start(String corpusAndStopWordsStringPath , String  postingFilesStringPath, boolean isStemmer) throws IOException {
            parse = new Parse(corpusAndStopWordsStringPath, postingFilesStringPath, isStemmer);
            if(parse!= null) {
                parse.parseAllDocs();
            }
    }

    public void resetIr(boolean selected) {
        parse.exit(selected);
        parse =null;
    }

    //public void setParse() {
    //    searcher.setParse(parse);
    //}


    public Map<String, String> getSortedDict() {
        return parse.getSortedDict();
    }

    public void uploadDict(String postingFilesStringPath, boolean selected) throws IOException {
        parse = new Parse(postingFilesStringPath, selected);
    }

    /**
     * @return Receive the number of documents in the repository
     */
    public int getNumOfDoc() {
        return parse.getNumofDoc();
    }
}
