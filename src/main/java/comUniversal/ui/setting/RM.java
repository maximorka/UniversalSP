package comUniversal.ui.setting;

import javafx.beans.property.SimpleStringProperty;

public  class RM {
    private final SimpleStringProperty name;


    public RM(String Name ) {
        this.name = new SimpleStringProperty(Name);
    }

    public String getName() {
        return name.get();
    }
    public void setName(String fName) {
        name.set(fName);
    }


}