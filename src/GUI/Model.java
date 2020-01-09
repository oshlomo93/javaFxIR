package GUI;

import Parse.Parse;
import Searcher.Searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 *  * This class controls all engine activity
 */
class Model extends Observable {

    private Parse parse;
    private Searcher searcher;

    Model() {

    }

    /**
     * The function that runs the entire engine
     * @param corpusAndStopWordsStringPath the path to corpus and stop words
     * @param postingFilesStringPath
     * @param isStemmer
     * @throws IOException - if something got wrong
     */
    void start(String corpusAndStopWordsStringPath, String postingFilesStringPath, boolean isStemmer) throws IOException {
            parse = new Parse(corpusAndStopWordsStringPath, postingFilesStringPath, isStemmer);
            if(parse!= null) {
                parse.parseAllDocs();
            }
    }

    /**
     * @param selected
     */
    void resetIr(boolean selected) {
        parse.exit(selected);
        parse =null;
    }

    /**
     * @return map of term and frequency
     */
    Map<String, String> getSortedDict() {
        return parse.getSortedDict();
    }

    /**
     * @param postingFilesStringPath
     * @param selected
     * @throws IOException
     */
    void uploadDict(String postingFilesStringPath, boolean selected) throws IOException {
        parse = new Parse(postingFilesStringPath, selected);
    }

    /**
     * @return Receive the number of documents in the repository
     */
    int getNumOfDoc() {
        return parse.getNumOfDoc();
    }

    HashMap<String, ArrayList<String>> startFindDoc() throws IOException {
        searcher.start();
        return searcher.getResults();
    }


    /**
     * @param queryFilePath String
     * @param isSemantic boolean
     */
    void setSearcherByPath(String queryFilePath, boolean isSemantic) { //todo
        searcher = new Searcher(queryFilePath,isSemantic, parse, true);
    }

    /**
     * @param query String
     * @param isSemantic boolean
     */
    void setSearcher(String query, boolean isSemantic) {
        searcher = new Searcher(query,parse, isSemantic);
    }
}
