package comUniversal.ui;

import comUniversal.Core;
import comUniversal.util.Params;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

public class SettingsRMWindowUI {
private boolean edit = false;
    @FXML
    private TextField rxRmTextField;
    @FXML
    private ComboBox rxType;
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField rxFrequencyTextField;
    @FXML
    private ComboBox widthRx;
    @FXML
    private ComboBox modeRx;
    @FXML
    private Button saveButton;
    @FXML
    private Label modeLabel;
    @FXML
    private Label widthLabel;

    @FXML
    public void initialize() {

        System.out.println("initialize() setting RM");

        widthRx.setVisible(false);
        modeRx.setVisible(false);
        modeLabel.setVisible(false);
        widthLabel.setVisible(false);

        ObservableList <String> typeRx = FXCollections.observableArrayList("√оризонт","√оризонт+");
        rxType.setItems(typeRx);
        ObservableList <String> widht = FXCollections.observableArrayList("3","6","12","24","48");
        widthRx.setItems(widht);
        ObservableList <String> mode = FXCollections.observableArrayList("1");
        modeRx.setItems(mode);


saveButton.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        System.out.println();

        if (Params.RXRM.getBooleanName(rxRmTextField.getText()) != -1) {
            System.out.println("FIND");
            Params.RXRM.deleteKeyName(rxRmTextField.getText());
        }
        Params.RXRM.putStringRMName(rxRmTextField.getText());

        Params.RXRM.putStringRMSettings(rxRmTextField.getText(), rxType.getValue().toString(), ipTextField.getText(), rxFrequencyTextField.getText(), "3", "1");
        Params.RXRM.save();
        Core.getCore().settingController.updateSettingsRx();
        Core.getCore().mainUI.updateRmType();
    }
});
    }


//    public String getIP(){
//        return ipTextField.getText();
//    }
//    public String getRM(){
//        return (String) rx.getValue();
//    }
//    public void testIP() {
//        try {
//            String ipAddress = ipTextField.getText();
//            InetAddress inet = InetAddress.getByName(ipAddress);
//            if (inet.isReachable(500)) {
//                ipTextField.setStyle("-fx-text-fill: green;");
//            } else {
//                ipTextField.setStyle("-fx-text-fill: red;");
//            }
//        } catch (Exception e) {
//            System.out.println("Exception:" + e.getMessage());
//        }
//    }
//    public void getFrequencyRx(int data) {
//        String tmp = Integer.toString(data);
//        Platform.runLater(() -> {
//            if(!rxFrequencyTextField.isFocused()){
//                rxFrequencyTextField.setText(tmp);
//            }
//        });
//    }
//    public void getModeRx(Mode data) {
//        String tmp = String.valueOf(data);
//        Platform.runLater(() -> {
//            modeDeviceLabel.setText(tmp);
//        });
//    }
//
//    public void getWidthRx(Width data) {
//        String tmp = "h";
//        switch (data){
//            case kHz_48:
//                tmp="48 к√ц";
//                break;
//            case kHz_24:
//                tmp="48 к√ц";
//                break;
//            case kHz_12:
//                tmp="12 к√ц";
//                break;
//            case kHz_6:
//                tmp="6 к√ц";
//                break;
//            case kHz_3:
//                tmp="3 к√ц";
//                break;
//
//        }
//        String qwe = tmp.toString();
//        Platform.runLater(() -> {
//            widthDeviceLabel.setText(qwe);
//        });
//    }
//
//    public void updateVisibility(boolean state) {
//        Platform.runLater(() -> {
//            rxFrequencyHzLabel.setDisable(state);
//            rxFrequencyLabel.setDisable(state);
//            rxFrequencyTextField.setDisable(state);
////            changeSettingsIPbutton.setDisable(state);
//
//        });
//    }
//    public void updateRm() {
//        Platform.runLater(() -> {
//            int size =  rx.getItems().size();
//            rx.getItems().remove(0,size);
//            ObservableList<String> langs = FXCollections.observableArrayList();
//            langs = Params.RXRM.getKey();
//            rx.setItems(langs);
//        });
//    }
//    public void deleteItemRm(String key) {
//        Platform.runLater(() -> {
//            int size =  rx.getItems().size();
//            for (int i = 0; i <size ; i++) {
//                rx.getItems().remove(0,size);
//            }
//
//        });
//    }
//    @Override
//    public void saveAll(Params params) {
//    }
//
//    @Override
//    public void restoreAll(Params params) {
//        //Restore Ethernet params 1
//        ipTextField.setText(Params.SETTINGS.getString("ethernet-ip-address", "192.168.0.1"));
//        //rxFrequencyTextField.setText(Params.SETTINGS.getString("rx_UPS_frequency", "128000"));
//    }
//    public int getFr(){
//        return Integer.parseInt(rxFrequencyTextField.getText());
//    }

    public void setEdit(String rm){
        Map<String, String> item = new HashMap<>();
        rxRmTextField.setText(rm);

        item = (Params.RXRM.getMap(rm));
        rxType.setValue(item.get("typeRx"));
        widthRx.setValue(item.get("widht"));
        modeRx.setValue(item.get("mode"));
        //rxType.setText(item.get("typeRx"));
        ipTextField.setText(item.get("ip"));
        rxFrequencyTextField.setText(item.get("freq"));
        //widthDeviceLabel.setText(item.get("widht"));
        //modeDeviceLabel.setText(item.get("mode"));
    }
}
