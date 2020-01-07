package GUI;

import Searcher.IdentifyEntityInDocument;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Item {
    private SimpleStringProperty queryTitle ;
    private Button button;
    private ArrayList<String> allDocForEachQ;

    public Item(String title,ArrayList<String> allDocForEachQ, String path ){
        queryTitle = new SimpleStringProperty(title);
        button = new Button("Show relevant documents");
        button.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                buttonAct();
            }
        });
        if(allDocForEachQ!=null && !allDocForEachQ.isEmpty()){
            this.allDocForEachQ = allDocForEachQ;
        }
        String entityPath = path+"\\documentsEntities.txt";
        IdentifyEntityInDocument identifyEntityInDocument = new IdentifyEntityInDocument(entityPath);
       // identifyEntityInDocument.getAllEntities();
    }

    public String getQueryTitle() {
        return queryTitle.get();
    }

    public SimpleStringProperty queryTitleProperty() {
        return queryTitle;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public void buttonAct(){
        Stage stageAllQueries = new Stage();
        stageAllQueries.setTitle("All Document:");
        TableView tableView = new TableView();
        TableColumn<String, Item> column1 = new TableColumn<>("Document ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("docTitle"));
        TableColumn<String, Item> column2 = new TableColumn<>("5 common entities");
        column2.setCellValueFactory(new PropertyValueFactory<>("button"));
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        for (String docId: allDocForEachQ) {
            ItemDoc itemDoc= new ItemDoc(docId);
            tableView.getItems().add(itemDoc);
        }
        VBox vbox = new VBox(tableView);

        Scene scene = new Scene(vbox);

        stageAllQueries.setScene(scene);

        stageAllQueries.show();

    }



}
