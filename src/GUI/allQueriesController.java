package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class allQueriesController implements Initializable {

    ViewModel viewModel;
    public TableColumn tableTitleView;

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
            }
        }
    }

    private void getAllDocForQuery(String title, ArrayList<String> allDocRelvant){


    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
