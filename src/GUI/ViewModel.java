package GUI;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    Model myModel;


    public ViewModel(Model model){
        if(model != null){
            myModel = model;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Model) {
            if (arg.equals("EndIr") ) {
                notifyObservers("EndIr");
            }
        }
    }

    public void start(String  corpusAndStopWordsStringPath, String  postingFilesStringPath, boolean isStemmer) throws IOException {
        myModel.start(corpusAndStopWordsStringPath, postingFilesStringPath, isStemmer);
    }
}
