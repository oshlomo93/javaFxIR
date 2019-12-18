package GUI;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
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
    private Stage dictionay;

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

    public void startIr(ActionEvent actionEvent) {

        try {
            long startTime = System.currentTimeMillis();
            viewModel.start(corpusAndStopWordsStringPath, postingFilesStringPath, stemmer.isSelected());
            long endTime= System.currentTimeMillis();
            resetButton.setDisable(false);
            showDict.setDisable(false);
            long time = (endTime-startTime)/1000;
            int numOfDoc= viewModel.getNumOfDoc();
            int numOfTerms=viewModel.getSortedDict().size();
            String toShow = "The number of unique terms in the database: " +numOfTerms+
                    "\nNumber of documents: " +numOfDoc+
                    "\nTotal time: "+ time +" s";
            showAlert(toShow, "Process information:");

        } catch (IOException e) {
            showAlert("Please put a proper path to the posting files and a proper path to the document repository", "Error Alert");

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


    public void showDictionaryOnScreen(ActionEvent actionEvent){
        dictionay = new Stage();
        dictionay.setTitle("The Dictionary:");
        TableView tableView = new TableView();
        TableColumn<String, TermAndTf> column1 = new TableColumn<>("Term");
        column1.setCellValueFactory(new PropertyValueFactory<>("termName"));
        TableColumn<String, TermAndTf> column2 = new TableColumn<>("Frequency");
        column2.setCellValueFactory(new PropertyValueFactory<>("tf"));
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        Map<String, String> getSortedDict = viewModel.getSortedDict();
        Object[] allTerms = getSortedDict.keySet().toArray();
        Object[] allTf = getSortedDict.values().toArray();
        for (int i = 0; i < getSortedDict.size(); i++) {
            String nameT = (String) allTerms[i];
            String tf = (String) allTf[i];
            TermAndTf termAndTf = new TermAndTf(nameT, tf);
            tableView.getItems().add(termAndTf);
        }

        VBox vbox = new VBox(tableView);

        Scene scene = new Scene(vbox);

        dictionay.setScene(scene);

        dictionay.show();
    }

    private void showAlert(String  strAlert , String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle(title);
        alert.setContentText(strAlert);
        alert.show();
    }

    public void uploadDict(ActionEvent actionEvent)  {
        try {
            if(postingFilesStringPath!=null) {
                viewModel.uplodeDict(postingFilesStringPath,stemmer.isSelected() );
                resetButton.setDisable(false);
                showDict.setDisable(false);
            }
            else {
                showAlert("Please put a proper path to the posting files" , "Error Alert");
            }
        } catch (IOException e) {
            showAlert("Please put a proper path to the posting files", "Error Alert");
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
