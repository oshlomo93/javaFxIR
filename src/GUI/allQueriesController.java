package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class allQueriesController implements Initializable {

    public TableView tableTitleView;
    ViewModel viewModel;
    
    ShowResultForQueryController showResultForQueryController;
    private  Stage stageAllDocRelevat;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void addAllTitlesToTable(HashMap<String, ArrayList<String>> allDocForEachQ){
        if(allDocForEachQ!=null && !allDocForEachQ.isEmpty()){
            for (String title:allDocForEachQ.keySet()) {
                Hyperlink hyperlink = new Hyperlink(title);
                hyperlink.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        getAllDocForQuery(title,allDocForEachQ.get(title));
                    }
                });

                tableTitleView.getColumns().add(hyperlink);

            }
        }
    }

    private void getAllDocForQuery(String title, ArrayList<String> allDocRelvant){
        try {
            if (stageAllDocRelevat == null) {
                stageAllDocRelevat = new Stage();
                stageAllDocRelevat.setTitle("All Queries");
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = fxmlLoader.load(getClass().getResource("/showResoltForQuery.fxml").openStream());
                Scene scene = new Scene(root, 650, 500);
                stageAllDocRelevat.setScene(scene);
                stageAllDocRelevat.setResizable(false);
                showResultForQueryController = fxmlLoader.getController();
                showResultForQueryController.setViewModel(this.viewModel);
                stageAllDocRelevat.initModality(Modality.APPLICATION_MODAL);
            }
            stageAllDocRelevat.show();
        } catch(Exception ignored) {
            showAlert("Something went wrong please try again", "Error");
        }


    }

    private void showAlert(String  strAlert , String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(null);
        alert.setTitle(title);
        alert.setContentText(strAlert);
        alert.show();
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
