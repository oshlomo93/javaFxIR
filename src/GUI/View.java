package GUI;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

//the controller
public class View implements Observer, Initializable {

    public Button corpusAndStopWordsPath;
    public Button postingFilesPath;
    public CheckBox stemmer;
    public MenuItem help;
    public Button openLastDict;
    public Button showDict;
    public Button resetButton;
    public Button startButton;
    public MenuItem stopAndDelet;
    public TextField dirOforCorTextField;
    public TextField dirOfPostingFiles;
    public AnchorPane winStage;
    String corpusAndStopWordsStringPath;
    String postingFilesStringPath;

    ViewModel viewModel;


    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals("EndIr")){
            showDictionaryOnScrean(); // todo
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loadCorpusPath(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage =  (Stage) winStage.getScene().getWindow() ;
        File file = directoryChooser.showDialog(stage);
        if(file != null){
            corpusAndStopWordsStringPath = file.getAbsolutePath();
            dirOforCorTextField.setText(corpusAndStopWordsStringPath);
        }
    }

    public void loadPastingPath(ActionEvent actionEvent) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage =  (Stage) winStage.getScene().getWindow() ;
        File file = directoryChooser.showDialog(stage);
        if(file != null){
            postingFilesStringPath = file.getAbsolutePath();
            dirOfPostingFiles.setText(postingFilesStringPath);
        }
    }

    public void isStemmer(ActionEvent actionEvent) {
    }

    public void showHelp(ActionEvent actionEvent) {
    }

    public void startIr(ActionEvent actionEvent) throws IOException {
        if(postingFilesStringPath!= null && corpusAndStopWordsStringPath!= null ){
            viewModel.start(corpusAndStopWordsStringPath, postingFilesStringPath, stemmer.isSelected());
        }
    }

    public void resetIr(ActionEvent actionEvent) {
    }

    public void showDictionaryOnScrean(ActionEvent actionEvent) {
    }

    public void uploadDict(ActionEvent actionEvent) {
    }

    public void stopAndDeletTheIr(ActionEvent actionEvent) {
    }


    private void showDictionaryOnScrean(){ //todo

    }

    public void setViewModel(ViewModel viewModel) {
        if(viewModel != null){
            this.viewModel = viewModel;
        }
    }


    public void exitCorrectly() {
    }
}
