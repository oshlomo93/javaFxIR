package GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * This dish controls the entire view of our app - the retrieval engine
 */
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
    public Button query;
    String corpusAndStopWordsStringPath;
    String postingFilesStringPath;
    private Stage dictionay;
    private  Stage stagePartBController;

    private PartBController partBController;

    ViewModel viewModel;


    @Override
    public void update(Observable o, Object arg) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * This function controls the opening of the folder to get the path to the document store and stop words
     * @param actionEvent
     */
    public void loadCorpusPath(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage =  (Stage) winStage.getScene().getWindow() ;
        File file = directoryChooser.showDialog(stage);
        if(file != null){
            corpusAndStopWordsStringPath = file.getAbsolutePath();
            dirOforCorTextField.setText(corpusAndStopWordsStringPath);
        }
    }

    /**
     * This function controls the opening of the folder to get the path where the files are saved pasting
     * @param actionEvent
     */
    public void loadPastingPath(ActionEvent actionEvent) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage =  (Stage) winStage.getScene().getWindow() ;
        File file = directoryChooser.showDialog(stage);
        if(file != null){
            postingFilesStringPath = file.getAbsolutePath();
            dirOfPostingFiles.setText(postingFilesStringPath);
        }
    }

    /**
     * This function will work when you click Start
     * @param actionEvent
     */
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
            query.setDisable(false);
            String toShow = "The number of unique terms in the database: " +numOfTerms+
                    "\nNumber of documents: " +numOfDoc+
                    "\nTotal time: "+ time +" s";
            showAlert(toShow, "Process information:");

        } catch (IOException e) {
            showAlert("Please put a proper path to the posting files and a proper path to the document repository", "Error Alert");

        }
    }

    /**
     * This function will work when you click reset
     * @param actionEvent
     */
    public void resetIr(ActionEvent actionEvent) {
        viewModel.resetIr(stemmer.isSelected());
        dirOforCorTextField.setText("");
        dirOfPostingFiles.setText("");
        corpusAndStopWordsStringPath= "";
        postingFilesStringPath= "";
        showDict.setDisable(true);
        resetButton.setDisable(true);
    }


    /**
     * This function will work when you click show dictionary
     * @param actionEvent
     */
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

    /**
     * This function notifies the screen according to the strings they gave it
     * @param strAlert -The message
     * @param title- The title of the message
     */
    private void showAlert(String  strAlert , String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle(title);
        alert.setContentText(strAlert);
        alert.show();
    }

    /**
     * Upload all the necessary information to ram
     * @param actionEvent
     */
    public void uploadDict(ActionEvent actionEvent)  {
        try {
            if(postingFilesStringPath!=null) {
                viewModel.uplodeDict(postingFilesStringPath,stemmer.isSelected() );
                resetButton.setDisable(false);
                showDict.setDisable(false);
                query.setDisable(false);
            }
            else {
                showAlert("Please put a proper path to the posting files" , "Error Alert");
            }
        } catch (IOException e) {
            showAlert("Please put a proper path to the posting files", "Error Alert");
        }
    }


    /** Updating the viewModel
     * @param viewModel
     */
    public void setViewModel(ViewModel viewModel) {
        if(viewModel != null){
            this.viewModel = viewModel;
        }
    }

    /**
     * Exit the system properly when the X button is pressed
     * @param stage
     */
    public void exitCorrectly(Stage stage) {
        stage.close();
    }

    /**
     * When you click on a query, you get an option to choose several ways to select the query, a new window opens
     * @param
     */
    public void openQueryOnions() { //ActionEvent actionEvent
        try {
            if (stagePartBController == null) {
                stagePartBController = new Stage();
                stagePartBController.setTitle("Query");
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = fxmlLoader.load(getClass().getResource("/partB.fxml").openStream());
                Scene scene = new Scene(root, 650, 500);
                stagePartBController.setScene(scene);
                stagePartBController.setResizable(false);
                partBController = fxmlLoader.getController();
                partBController.setViewModel(this.viewModel);

                if(stemmer.isSelected()) {
                    String parhToEntity=postingFilesStringPath+ "\\WithStemming";
                    partBController.setPath(parhToEntity);
                }
                else {
                    String parhToEntity=postingFilesStringPath+ "\\WithoutStemming";
                    partBController.setPath(parhToEntity);

                }
                stagePartBController.initModality(Modality.APPLICATION_MODAL);
            }
            stagePartBController.show();
        } catch(Exception ignored) {
            showAlert("Something went wrong please try again", "Error");
        }

    }
}
