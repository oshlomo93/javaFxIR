package GUI;

import Parse.Parse;

import java.io.IOException;
import java.util.Observable;

public class Model extends Observable {

    Parse parse;

    public Model(){
        parse  =new Parse();
        

    }


    public void start(String corpusAndStopWordsStringPath , String  postingFilesStringPath, boolean isStemer) throws IOException {
        parse = new Parse(corpusAndStopWordsStringPath, postingFilesStringPath, isStemer);
        parse.parseAllDocs();
        notifyObservers("EndIr");
    }

}
