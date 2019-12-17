package GUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TermAndTf {

    private SimpleStringProperty termName ;
    private SimpleStringProperty tf;


    public TermAndTf(String term, String tf) {
        this.termName = new SimpleStringProperty(term);
        this.tf = new SimpleStringProperty(tf);
    }


    public String getTermName() {
        return termName.get();
    }

    public SimpleStringProperty termNameProperty() {
        return termName;
    }

    public final void setTermName(String termName) {
        this.termName.set(termName);
    }

    public String getTf() {
        return tf.get();
    }

    public SimpleStringProperty tfProperty() {
        return tf;
    }

    public final void setTf(String tf) {
        this.tf.set(tf);
    }
}


