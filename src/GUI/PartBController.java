package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PartBController implements Initializable {
    public TextField selectQueryFilePath;
    public TextField queryVal;
    ViewModel viewModel;
    String queryFilePath;
    String query;
    boolean isSemantic;
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

    public void startFindDoc(ActionEvent actionEvent) {
        setQuery();
        if(queryFilePath != null && queryFilePath.length()>0 ) {
            viewModel.setSercherByPath(queryFilePath);
            isSemantic = semantic.isSelected();
            //todo
        }
        else if(query!= null && query.length()>0){
            viewModel.setSercher(query);
            isSemantic = semantic.isSelected();
            //todo
        }
        else {
            showAlert("Please select a path to a query file or write a query" ,"Query is invalid");
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
