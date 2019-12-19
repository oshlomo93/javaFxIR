package GUI;

import Parse.Parse;

import java.io.IOException;
import java.util.Map;
import java.util.Observable;

/**
 *  * This class controls all engine activity
 */
public class Model extends Observable {

    Parse parse;

    public Model() {

    }


    /**
     * The function that runs the entire engine
     * @param corpusAndStopWordsStringPath
     * @param postingFilesStringPath
     * @param isStemer
     * @throws IOException
     */
    public void start(String corpusAndStopWordsStringPath , String  postingFilesStringPath, boolean isStemer) throws IOException {
            parse = new Parse(corpusAndStopWordsStringPath, postingFilesStringPath, isStemer);
            if(parse!= null) {
                parse.parseAllDocs();
            }
    }

    public void resetIr() {
        parse.exit();
        parse =null;
    }


    public Map<String, String> getSortedDict() {
        return parse.getSortedDict();
    }

    public void uplodeDict(String postingFilesStringPath, boolean selected) throws IOException {
        parse = new Parse(postingFilesStringPath, selected);
    }

    /**
     * @return Receive the number of documents in the repository
     */
    public int getNumofDoc() {
        return parse.getNumofDoc();
    }
}
