package GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ItemDoc {
    private SimpleStringProperty docTitle ;
    private Button button;


    public ItemDoc(String title){
        docTitle = new SimpleStringProperty(title);
        button = new Button("5 common entities");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                buttonAct();
            }
        });

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


    }



}
