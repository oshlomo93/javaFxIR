package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.net.URL;
import java.util.*;

public class dictionaryShowControler implements Initializable {

    @FXML
    private TableView<TermAndTf> tbData;
    @FXML
    public TableColumn<TermAndTf, String> termNameStringTableColumn;

    @FXML
    public TableColumn<TermAndTf, String> tfStringTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        termNameStringTableColumn.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        tfStringTableColumn.setCellValueFactory(new PropertyValueFactory<>("FirstName"));

        //add your data to the table here.
        tbData.setItems(termAndFtModel);
    }

    // add your data here from any source
    private ObservableList<TermAndTf> termAndFtModel;

    public void showDict(Map<String, String> getSortedDict) {
        termAndFtModel = FXCollections.observableArrayList();
        Object [] allTerms = getSortedDict.keySet().toArray();
        Object [] allTf = getSortedDict.values().toArray();
        for(int i = 0  ; i<getSortedDict.size() ; i++){
            String nameT = (String) allTerms[i];
            String tf = (String) allTf[i];
            TermAndTf termAndTf = new TermAndTf(nameT ,tf );
            termAndFtModel.add(termAndTf);
            break;
        }

        tbData.setItems(termAndFtModel);

    }


}




