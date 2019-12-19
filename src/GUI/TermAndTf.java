package GUI;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class will help us update the table that will appear on the screen
 */
public class TermAndTf {

    public String getTermName() {
        return termName.get();
    }

    public SimpleStringProperty termNameProperty() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName.set(termName);
    }

    public String getTf() {
        return tf.get();
    }

    public SimpleStringProperty tfProperty() {
        return tf;
    }

    public void setTf(String tf) {
        this.tf.set(tf);
    }

    private SimpleStringProperty termName ;
    private SimpleStringProperty tf;


    public TermAndTf(String term, String tf) {
        this.termName = new SimpleStringProperty(term);
        this.tf = new SimpleStringProperty(tf);
    }

}


