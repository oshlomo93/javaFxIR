package GUI;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowResultForQueryController implements Initializable {
    ViewModel myViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void setViewModel(ViewModel myViewModel) {
        this.myViewModel = myViewModel;
    }

}
