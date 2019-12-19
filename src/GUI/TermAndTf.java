package GUI;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class will help us update the table that will appear on the screen
 */
public class TermAndTf {

    private SimpleStringProperty termName ;
    private SimpleStringProperty tf;


    public TermAndTf(String term, String tf) {
        this.termName = new SimpleStringProperty(term);
        this.tf = new SimpleStringProperty(tf);
    }

}


