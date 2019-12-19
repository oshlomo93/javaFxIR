package GUI;

import java.io.IOException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * The switch between the controller and the main functionality of the system
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

    /**
     * The main function that starts the retrieval process
     * @param corpusAndStopWordsStringPath
     * @param postingFilesStringPath
     * @param isStemmer
     * @throws IOException
     */
    public void start(String  corpusAndStopWordsStringPath, String  postingFilesStringPath, boolean isStemmer) throws IOException {

        myModel.start(corpusAndStopWordsStringPath, postingFilesStringPath, isStemmer);
    }

    /**
     * This method deletes all information from the disk and from the ram
     * @param selected
     */
    public void resetIr(boolean selected) {
        myModel.resetIr(selected);
    }


    /**
     *Returns the dictionary sorted
     * @return Map<String, String>
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
        myModel.uploadDict(postingFilesStringPath, selected);
    }

    /**
     * Returns the number of undocumented documents
     * @return int
     */
    public int getNumOfDoc() {
        return myModel.getNumOfDoc();
    }
}
