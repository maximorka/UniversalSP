package comUniversal.ui;

import javafx.beans.property.SimpleStringProperty;

public  class RM {
    private final SimpleStringProperty name;
    private final SimpleStringProperty ip;

    RM(String Name, String Ip ) {
        this.name = new SimpleStringProperty(Name);
        this.ip = new SimpleStringProperty(Ip);

    }

    public String getName() {
        return name.get();
    }
    public void setName(String fName) {
        name.set(fName);
    }

    public String getIp() {
        return ip.get();
    }
    public void setIP(String fName) {
        ip.set(fName);
    }

}