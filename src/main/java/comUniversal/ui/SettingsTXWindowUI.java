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

public class SettingsTXWindowUI {
private boolean edit = false;
    @FXML
    private TextField txRmTextField;
    @FXML
    private ComboBox txType;
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField txFrequencyTextField;
    @FXML
    private ComboBox widthTx;
    @FXML
    private ComboBox modeTx;
    @FXML
    private Button saveButton;
    @FXML
    private Label modeLabel;
    @FXML
    private Label widthLabel;

    @FXML
    public void initialize() {

        System.out.println("initialize() setting TX");

        widthTx.setVisible(false);
        modeTx.setVisible(false);
        modeLabel.setVisible(false);
        widthLabel.setVisible(false);

        ObservableList <String> typeRx = FXCollections.observableArrayList("√оризонт","√оризонт+");
        txType.setItems(typeRx);
        ObservableList <String> widht = FXCollections.observableArrayList("3","6","12","24","48");
        widthTx.setItems(widht);
        ObservableList <String> mode = FXCollections.observableArrayList("1");
        modeTx.setItems(mode);


saveButton.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        System.out.println();

        if (Params.TXRM.getBooleanName(txRmTextField.getText()) != -1) {
            System.out.println("FIND");
            Params.TXRM.deleteKeyName(txRmTextField.getText());
        }
        Params.TXRM.putStringRMName(txRmTextField.getText());

        Params.TXRM.putStringTXSettings(txRmTextField.getText(), txType.getValue().toString(), ipTextField.getText(), txFrequencyTextField.getText(), "3", "1");
        Params.TXRM.save();
        Core.getCore().settingController.updateSettingsTx();
        Core.getCore().mainUI.updateTxType();
        //its coomit
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
        txRmTextField.setText(rm);

        item = (Params.TXRM.getMap(rm));
        txType.setValue(item.get("typeTx"));
        widthTx.setValue(item.get("widht"));
        modeTx.setValue(item.get("mode"));
        //rxType.setText(item.get("typeRx"));
        ipTextField.setText(item.get("ip"));
        txFrequencyTextField.setText(item.get("freq"));
        //widthDeviceLabel.setText(item.get("widht"));
        //modeDeviceLabel.setText(item.get("mode"));
    }
}
