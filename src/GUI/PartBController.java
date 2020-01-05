package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PartBController implements Initializable {
    public TextField selectQueryFilePath;
    public TextField queryVal;
    ViewModel viewModel;
    String queryFilePath;
    String query;
    boolean isSemantic;
    allQueriesController allQueriesController;
    private  Stage stageAllQueries;

    @FXML
    AnchorPane queryStage;
    public CheckBox semantic;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void enableButtons() {
    }

    public void startFindDoc(ActionEvent actionEvent) throws IOException {
        setQuery();
        if(queryFilePath != null && queryFilePath.length()>0 ) {
            isSemantic = semantic.isSelected();
            viewModel.setSercherByPath(queryFilePath, isSemantic);
            showAllQuery();


            //todo
        }
        else if(query!= null && query.length()>0){
            isSemantic = semantic.isSelected();
            viewModel.setSercher(query, isSemantic);
            showAllQuery();

            //todo
        }
        else {
            showAlert("Please select a path to a query file or write a query" ,"Query is invalid");
        }

    }

    private void showAllQuery() throws IOException {
        HashMap<String, ArrayList<String>> allDocForEachQ= viewModel.startFindDoc();
        if(allDocForEachQ!=null && allDocForEachQ.size()>0){
            try {
                if (stageAllQueries == null) {
                    stageAllQueries = new Stage();
                    stageAllQueries.setTitle("All Queries");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    Parent root = fxmlLoader.load(getClass().getResource("/queriesTitle.fxml").openStream());
                    Scene scene = new Scene(root, 650, 500);
                    stageAllQueries.setScene(scene);
                    stageAllQueries.setResizable(false);
                    allQueriesController = fxmlLoader.getController();
                    allQueriesController.setViewModel(this.viewModel);
                    stageAllQueries.initModality(Modality.APPLICATION_MODAL);
                    allQueriesController.addAllTitlesToTable(allDocForEachQ);
                }

                stageAllQueries.show();
            } catch(Exception ignored) {
                showAlert("Something went wrong please try again", "Error");
            }

        }
        else{
            showAlert("Something went wrong please try again", "Error:");
        }

    }

    private void showAlert(String  strAlert , String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle(title);
        alert.setContentText(strAlert);
        alert.show();
    }


    public void selectQueryFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        Stage stage =  (Stage) queryStage.getScene().getWindow() ;
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            queryFilePath = file.getAbsolutePath();
            selectQueryFilePath.setText(queryFilePath);
        }
    }

    public void setQuery(){
        query = queryVal.getText();
    }
}
