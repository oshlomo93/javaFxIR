package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PartBController implements Initializable {
    public TextField selectQueryFilePath;
    public TextField queryVal;
    ViewModel viewModel;
    String queryFilePath;
    String query;
    boolean isSemantic;
    private Stage stageAllQueries;

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
        if (queryFilePath != null && queryFilePath.length() > 0) {
            isSemantic = semantic.isSelected();
            viewModel.setSercherByPath(queryFilePath, isSemantic);
            showAllQuery();


            //todo
        } else if (query != null && query.length() > 0) {
            isSemantic = semantic.isSelected();
            viewModel.setSercher(query, isSemantic);
            showAllQuery();

            //todo
        } else {
            showAlert("Please select a path to a query file or write a query", "Query is invalid");
        }

    }

    private void showAllQuery() throws IOException {
        HashMap<String, ArrayList<String>> allDocForEachQ = viewModel.startFindDoc();
        if(allDocForEachQ != null && !allDocForEachQ.isEmpty()) {
            stageAllQueries = new Stage();
            stageAllQueries.setTitle("All queries::");
            TableView tableView = new TableView();
            TableColumn<String, Item> column1 = new TableColumn<>("Query Title");
            column1.setCellValueFactory(new PropertyValueFactory<>("queryTitle"));
            TableColumn<String, Item> column2 = new TableColumn<>("Show relevant documents");
            column2.setCellValueFactory(new PropertyValueFactory<>("button"));
            tableView.getColumns().add(column1);
            tableView.getColumns().add(column2);
            Object[] allQueries =  allDocForEachQ.keySet().toArray();
            for (int i = 0; i < allQueries.length; i++) {
                String nameT = (String) allQueries[i];
                Item item= new Item(nameT, allDocForEachQ.get(nameT));
                tableView.getItems().add(item);
            }

            VBox vbox = new VBox(tableView);

            Scene scene = new Scene(vbox);

            stageAllQueries.setScene(scene);

            stageAllQueries.show();
        }
        else {
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
