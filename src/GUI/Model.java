package GUI;

import Parse.Parse;

import java.io.IOException;
import java.util.Map;
import java.util.Observable;

public class Model extends Observable {

    Parse parse;

    public Model() {

    }


    public void start(String corpusAndStopWordsStringPath , String  postingFilesStringPath, boolean isStemer) throws IOException {
        parse = new Parse(corpusAndStopWordsStringPath, postingFilesStringPath, isStemer);
        parse.parseAllDocs();
        notifyObservers("EndIr");
    }

    public void resetIr() {
        parse.exit();
        parse =null;
    }


    public Map<String, String> getSortedDict() {
        return parse.getSortedDict();
    }

    public void uplodeDict(String postingFilesStringPath) throws IOException {
        parse = new Parse(postingFilesStringPath);
    }
}
