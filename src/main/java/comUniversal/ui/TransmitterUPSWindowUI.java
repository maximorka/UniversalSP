
package comUniversal.ui;

        import comUniversal.lowLevel.DriverHorizon.Mode;
import comUniversal.lowLevel.DriverHorizon.Width;
import comUniversal.ui.setting.ParamsSettings;
import comUniversal.util.EthernetUtils;
import comUniversal.util.Params;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TransmitterUPSWindowUI implements ParamsSettings {

    private List<ParamsSettings> settings = new ArrayList<>();

//    public static TextField ipTextTx;
//
//    public static Label modeTxText;
//    public static Label widthTxText;
//    public static Label procentTxText;
//    public static TextField freqTxText;
//    public static Button changeIPTxButton;
//    public static Label freqTxLabel;
//    public static Label freqHzTxLabel;

    @FXML
    private TextField txFrequencyTextField;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button changeSettingsIPbutton;
    @FXML
    private Label procentDeviceLabel;
    @FXML
    private Label modeDeviceLabel;
    @FXML
    private Label widthDeviceLabel;
    @FXML
    private Label txFrequencyHzLabel;
    @FXML
    private Label txFrequencyLabel;
    @FXML
    public void initialize() {
        System.out.println("initialize() setting transmitter");
//        ipTextTx = new TextField();
//        ipTextTx = ipTextField;
//
//        modeTxText = new Label();
//        modeTxText = modeDeviceLabel;
//
//        widthTxText = new Label();
//        widthTxText = widthDeviceLabel;
//
//        procentTxText = new Label();
//        procentTxText = procentDeviceLabel;
//
//        freqTxText = new TextField();
//        freqTxText = txFrequencyTextField;
//        changeIPTxButton = new Button();
//        changeIPTxButton = changeSettingsIPbutton;
//
//        freqTxLabel = new Label();
//        freqTxLabel = txFrequencyLabel;
//
//        freqHzTxLabel = new Label();
//        freqHzTxLabel = txFrequencyHzLabel;

        restoreAll(Params.SETTINGS);

        txFrequencyHzLabel.setDisable(true);
        txFrequencyLabel.setDisable(true);
        changeSettingsIPbutton.setDisable(true);
        txFrequencyTextField.setDisable(true);
        txFrequencyTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int frequency = Integer.parseInt(txFrequencyTextField.getText());

                    //Core.getCore().device[0].driverHorizon.ducSetFrequency(frequency);


            }
        });
        changeSettingsIPbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String ipAddress = ipTextField.getText();
                String mask = "255.255.255.0";
                String Getaway = "192.168.0.0";
                int port = 80;

                byte[] ipBytes = EthernetUtils.ipToByteArray(ipAddress);
                byte[] maskBytes = EthernetUtils.ipToByteArray(mask);
                byte[] gatewayBytes = EthernetUtils.ipToByteArray(Getaway);
                int ip = ByteBuffer.wrap(ipBytes).getInt();
                int maskInt = ByteBuffer.wrap(maskBytes).getInt();
                int get = ByteBuffer.wrap(gatewayBytes).getInt();
//                if(Core.getCore().countConectedDevice == 1){
//
//                    Core.getCore().device[0].driverHorizon.ducSetMode(Mode.DISABLE);
//                    Core.getCore().device[0].driverHorizon.ddcSetMode(Mode.DISABLE);
//                    Core.getCore().device[0].kylymDecoder100.setRunning(false);
//                    Core.getCore().countConectedDevice = 0;
//                    Core.getCore().device[0].driverHorizon.ethernetSet(ip,maskInt,port,get);
//                    changeSettingsIPbutton.setDisable(true);
//
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }
//                    changeSettingsIPbutton.setDisable(false);
//                    Core.getCore().device[0].bufferController.setWorkingThread(false);
//                    Core.getCore().device[0].ethernetDriver.closeSocket();
//                    Core.getCore().mainUI.setConnectButton();
//                }else{
//
//                    Core.getCore().device[1].driverHorizon.ducSetMode(Mode.DISABLE);
//                    Core.getCore().device[1].driverHorizon.ddcSetMode(Mode.DISABLE);
//                    Core.getCore().device[1].kylymDecoder100.setRunning(false);
//
//                    Core.getCore().device[0].driverHorizon.ducSetMode(Mode.DISABLE);
//                    Core.getCore().device[0].driverHorizon.ddcSetMode(Mode.DISABLE);
//
//                    Core.getCore().countConectedDevice = 0;
//                    Core.getCore().device[0].driverHorizon.ethernetSet(ip,maskInt,port,get);
//                    changeSettingsIPbutton.setDisable(true);
//
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }
//                    changeSettingsIPbutton.setDisable(false);
//                    Core.getCore().device[0].bufferController.setWorkingThread(false);
//
//                    Core.getCore().device[0].ethernetDriver.closeSocket();
//                    Core.getCore().device[1].ethernetDriver.closeSocket();
//                    Core.getCore().mainUI.setConnectButton();
//                }
            }
        });
        //testIP();
        ipTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                testIP();
            }
        });
    }

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
    public String getIP(){
        return ipTextField.getText();
    }

    @Override
    public void saveAll(Params params) {
        Params.SETTINGS.putString("ethernet-ip-address", ipTextField.getText());
    }

    public void updatePercent(int percent) {
        String procent = Integer.toString(percent);

        Platform.runLater(() -> {
            procentDeviceLabel.setText(procent);
        });
    }

    public void getFrequencyTx(int data) {
        String tmp = Integer.toString(data);
        Platform.runLater(() -> {
            if(!txFrequencyTextField.isFocused()){
                txFrequencyTextField.setText(tmp);
            }
        });
    }
    public void getModeTx(Mode data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            modeDeviceLabel.setText(tmp);
        });
    }
    public void getWidthTx(Width data) {
        String tmp = new String("");
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
            if(txFrequencyHzLabel != null ){
                txFrequencyHzLabel.setDisable(state);
                txFrequencyLabel.setDisable(state);
                txFrequencyTextField.setDisable(state);
                changeSettingsIPbutton.setDisable(state);
            }

        });
    }

    @Override
    public void restoreAll(Params params) {
        //Restore Ethernet params 1
        ipTextField.setText(Params.SETTINGS.getString("ethernet-ip-address", "192.168.0.1"));
        //txFrequencyTextField.setText(Params.SETTINGS.getString("rx_UPS_frequency", "128000"));

    }
}
