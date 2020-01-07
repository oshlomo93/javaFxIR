package GUI;

import javafx.beans.property.SimpleStringProperty;

public class EntityItem {
    private SimpleStringProperty entityName ;
    private SimpleStringProperty tf;


    public EntityItem(String entityName, String tf) {
        this.entityName = new SimpleStringProperty(entityName);
        this.tf = new SimpleStringProperty(tf);
    }

    public String getEntityName() {
        return entityName.get();
    }

    public SimpleStringProperty entityNameProperty() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName.set(entityName);
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



}

