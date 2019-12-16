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
    public Button openLastDict;
    public Button showDict;
    public Button resetButton;
    public Button startButton;
    public TextField dirOforCorTextField;
    public TextField dirOfPostingFiles;
    public AnchorPane winStage;
    String corpusAndStopWordsStringPath;
    String postingFilesStringPath;

    ViewModel viewModel;


    @Override
    public void update(Observable o, Object arg) {
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

    public void startIr(ActionEvent actionEvent) throws IOException {
        if(postingFilesStringPath!= null && corpusAndStopWordsStringPath!= null ){
            viewModel.start(corpusAndStopWordsStringPath, postingFilesStringPath, stemmer.isSelected());
            resetButton.setDisable(false);
            showDict.setDisable(false);
        }
    }

    public void resetIr(ActionEvent actionEvent) {
        viewModel.resetIr();
        dirOforCorTextField.setText("");
        dirOfPostingFiles.setText("");
        corpusAndStopWordsStringPath= "";
        postingFilesStringPath= "";
        showDict.setDisable(true);
        resetButton.setDisable(true);
    }

    public void showDictionaryOnScreen(ActionEvent actionEvent) {

    }

    public void uploadDict(ActionEvent actionEvent) throws IOException {
        if (postingFilesStringPath != null && postingFilesStringPath.length() > 0){
            viewModel.uplodeDict(postingFilesStringPath);
            resetButton.setDisable(false);
            showDict.setDisable(false);
        }
    }


    public void setViewModel(ViewModel viewModel) {
        if(viewModel != null){
            this.viewModel = viewModel;
        }
    }

    public void exitCorrectly(Stage stage) {
        stage.close();
    }
}
