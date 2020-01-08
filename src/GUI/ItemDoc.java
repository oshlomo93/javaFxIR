package GUI;

import Searcher.IdentifyEntityInDocument;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class ItemDoc {
    private SimpleStringProperty docTitle ;
    private Button button;
    IdentifyEntityInDocument identifyEntityInDocument;



    public ItemDoc(String title, String path){
        docTitle = new SimpleStringProperty(title);
        button = new Button("5 common entities");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                buttonAct();
            }
        });

        String entityPath = path;
        identifyEntityInDocument = new IdentifyEntityInDocument(entityPath);
    }

    public String getDocTitle() {
        return docTitle.get();
    }

    public SimpleStringProperty docTitleProperty() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle.set(docTitle);
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public void buttonAct(){
        ArrayList<String[]> entityAndVal = identifyEntityInDocument.getAllEntities(this.docTitle.getValue());
        Stage dictionay = new Stage();
        dictionay.setTitle("The 5 common entities:");
        TableView tableView = new TableView();
        TableColumn<String, EntityItem> column1 = new TableColumn<>("Entity");
        column1.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        TableColumn<String, EntityItem> column2 = new TableColumn<>("Frequency");
        column2.setCellValueFactory(new PropertyValueFactory<>("tf"));
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);

        for (String[] entityAndValRank :entityAndVal) {
            String nameT = entityAndValRank[0];
            String tf = entityAndValRank[1];
            EntityItem entityItem = new EntityItem(nameT, tf);
            tableView.getItems().add(entityItem);
        }

        VBox vbox = new VBox(tableView);

        Scene scene = new Scene(vbox);

        dictionay.setScene(scene);

        dictionay.show();
    }



}
