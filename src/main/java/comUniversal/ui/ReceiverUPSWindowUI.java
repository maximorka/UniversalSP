package comUniversal.ui;

import comUniversal.Core;
import comUniversal.lowLevel.DriverHorizon.Mode;
import comUniversal.lowLevel.DriverHorizon.Width;
import comUniversal.ui.setting.ParamsSettings;
import comUniversal.util.Params;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ReceiverUPSWindowUI implements ParamsSettings {
    public Map<String, String> item;
    @FXML
    private TextField rxFrequencyTextField;
    @FXML
    private Label rxType;
    @FXML
    private TextField ipTextField;
    @FXML
    private Label modeDeviceLabel;
    @FXML
    private Label widthDeviceLabel;
    @FXML
    private Label rxFrequencyLabel;
    @FXML
    private Label rxFrequencyHzLabel;
    @FXML
    public void initialize() {
        System.out.println("initialize() setting receiver");

        String rm = Core.getCore().mainUI.getTypeRx();
        item = new HashMap<>();
        item = (Params.RXRM.getMap(rm));
        rxType.setText(item.get("typeRx"));
        ipTextField.setText(item.get("ip"));
        rxFrequencyTextField.setText(item.get("freq"));
        widthDeviceLabel.setText(item.get("widht"));
        modeDeviceLabel.setText(item.get("mode"));
        restoreAll(Params.SETTINGS);

        rxFrequencyLabel.setDisable(true);
        rxFrequencyTextField.setDisable(true);

        ipTextField.setDisable(true);
        rxFrequencyTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int frequency = Integer.parseInt(rxFrequencyTextField.getText());

                if(Core.getCore().dev.driverHorizon != null){
                    Core.getCore().dev.driverHorizon.ddcSetFrequency(frequency);

                }else{
                    Core.getCore().dev.driverHorizon.ddcSetFrequency(frequency);
                }

//                if(Core.getCore().device[0].driverHorizon != null){
//                    Core.getCore().device[0].driverHorizon.ddcSetFrequency(frequency);
//                }else{
//                    Core.getCore().device[1].driverHorizon.ddcSetFrequency(frequency);
//                }

            }
        });

//rx.setOnAction(new EventHandler<ActionEvent>() {
//    @Override
//    public void handle(ActionEvent event) {
//        ipTextField.setDisable(false);
//
//        String rm = Core.getCore().receiverUPSWindowUI.getRM();
//        String ip = Params.RXRM.getString(rm, "192.168.0.1");
//
//        ipTextField.setText(ip);
//    }
//});
        //testIP();
        ipTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                testIP();
            }
        });
    }

    public String getIP(){
        return ipTextField.getText();
    }
//    public String getRM(){
//        return (String) rx.getValue();
//    }
    public void testIP() {
        try {
            String ipAddress = ipTextField.getText();
            InetAddress inet = InetAddress.getByName(ipAddress);
            if (inet.isReachable(500)) {
                ipTextField.setStyle("-fx-text-fill: green;");
            } else {
                ipTextField.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }
    public void getFrequencyRx(int data) {
        String tmp = Integer.toString(data);
        Platform.runLater(() -> {
            if(!rxFrequencyTextField.isFocused()){
                rxFrequencyTextField.setText(tmp);
            }
        });
    }
    public void getModeRx(Mode data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            modeDeviceLabel.setText(tmp);
        });
    }

    public void getWidthRx(Width data) {
        String tmp = "h";
        switch (data){
            case kHz_48:
                tmp="48 êÃö";
                break;
            case kHz_24:
                tmp="48 êÃö";
                break;
            case kHz_12:
                tmp="12 êÃö";
                break;
            case kHz_6:
                tmp="6 êÃö";
                break;
            case kHz_3:
                tmp="3 êÃö";
                break;

        }
        String qwe = tmp.toString();
        Platform.runLater(() -> {
            widthDeviceLabel.setText(qwe);
        });
    }

    public void updateVisibility(boolean state) {
        Platform.runLater(() -> {
            rxFrequencyHzLabel.setDisable(state);
            rxFrequencyLabel.setDisable(state);
            rxFrequencyTextField.setDisable(state);
//            changeSettingsIPbutton.setDisable(state);

        });
    }
    public void updateRm() {
//        Platform.runLater(() -> {
//            int size =  rx.getItems().size();
//            rx.getItems().remove(0,size);
//            ObservableList<String> langs = FXCollections.observableArrayList();
//            langs = Params.RXRM.getKey();
//            rx.setItems(langs);
//        });
    }
    public void deleteItemRm(String key) {
        Platform.runLater(() -> {
//            int size =  rx.getItems().size();
//            for (int i = 0; i <size ; i++) {
//                rx.getItems().remove(0,size);
//            }

        });
    }
    @Override
    public void saveAll(Params params) {
    }

    @Override
    public void restoreAll(Params params) {
        //Restore Ethernet params 1
        //ipTextField.setText(Params.SETTINGS.getString("ethernet-ip-address", "192.168.0.1"));
        //rxFrequencyTextField.setText(Params.SETTINGS.getString("rx_UPS_frequency", "128000"));
    }
    public int getFr(){
        return Integer.parseInt(rxFrequencyTextField.getText());
    }
}
