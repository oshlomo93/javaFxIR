package GUI;

import java.io.IOException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * This class controls all engine activity
 */
public class ViewModel extends Observable implements Observer {

    Model myModel;


    public ViewModel(Model model){
        if(model != null){
            myModel = model;
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void start(String  corpusAndStopWordsStringPath, String  postingFilesStringPath, boolean isStemmer) throws IOException {

        myModel.start(corpusAndStopWordsStringPath, postingFilesStringPath, isStemmer);
    }

    public void resetIr() {
        myModel.resetIr();
    }


    /**
     *
     * @return
     */
    public Map<String, String> getSortedDict() {
        return myModel.getSortedDict();
    }

    /**
     * Upload all the information we saved to disk to ram
     * @param postingFilesStringPath
     * @param selected
     * @throws IOException
     */
    public void uplodeDict(String postingFilesStringPath, boolean selected) throws IOException {
        myModel.uplodeDict(postingFilesStringPath, selected);
    }

    /**
     * Returns the number of undocumented documents
     * @return int
     */
    public int getNumOfDoc() {
        return myModel.getNumofDoc();
    }
}
