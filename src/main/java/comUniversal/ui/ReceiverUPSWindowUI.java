package comUniversal.ui;

import comUniversal.Core;
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
import javafx.scene.image.Image;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ReceiverUPSWindowUI implements ParamsSettings {

    private List<ParamsSettings> settings = new ArrayList<>();

    Image Ok = new Image("/images/check.png");
    Image notOk = new Image("/images/close.png");

    public static TextField ipTextRx;
    public static TextField freqRxText;
    public static Label freqRxLabel;
    public static Label freqHzRxLabel;
    public static Label widthRxText;
    public static Label modeRxText;
    public static Button changeIPRxButton;

    @FXML
    private TextField rxFrequencyTextField;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button changeSettingsIPbutton;
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
        ipTextRx = new TextField();
        ipTextRx = ipTextField;

        modeRxText = new Label();
        modeRxText = modeDeviceLabel;

        widthRxText = new Label();
        widthRxText = widthDeviceLabel;

        freqRxText = new TextField();
        freqRxText = rxFrequencyTextField;

        changeIPRxButton = new Button();
        changeIPRxButton = changeSettingsIPbutton;

        freqRxLabel = new Label();
        freqRxLabel = rxFrequencyLabel;

        freqHzRxLabel = new Label();
        freqHzRxLabel = rxFrequencyHzLabel;
        restoreAll(Params.SETTINGS);

        rxFrequencyHzLabel.setDisable(true);
        rxFrequencyLabel.setDisable(true);
        changeSettingsIPbutton.setDisable(true);
        rxFrequencyTextField.setDisable(true);
        rxFrequencyTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int frequency = Integer.parseInt(rxFrequencyTextField.getText());
                if(Core.getCore().countConectedDevice == 1){
                    Core.getCore().device[0].driverHorizon.ddcSetFrequency(frequency);
                }else{
                    Core.getCore().device[1].driverHorizon.ddcSetFrequency(frequency);
                }

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
                if(Core.getCore().countConectedDevice == 1){
                    Core.getCore().device[0].driverHorizon.ducSetMode(Mode.DISABLE);
                    Core.getCore().device[0].driverHorizon.ddcSetMode(Mode.DISABLE);
                    Core.getCore().device[0].kylymDecoder100.setRunning(false);
                    Core.getCore().countConectedDevice = 0;
                    Core.getCore().device[0].driverHorizon.ethernetSet(ip,maskInt,port,get);
                    changeSettingsIPbutton.setDisable(true);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    changeSettingsIPbutton.setDisable(false);
                    Core.getCore().device[0].bufferController.setWorkingThread(false);
                    Core.getCore().device[0].ethernetDriver.closeSocket();
                    Core.getCore().mainUI.setConnectButton();
                }else{
                    Core.getCore().device[1].driverHorizon.ducSetMode(Mode.DISABLE);
                    Core.getCore().device[1].driverHorizon.ddcSetMode(Mode.DISABLE);
                    Core.getCore().device[1].kylymDecoder100.setRunning(false);

                    Core.getCore().device[0].driverHorizon.ducSetMode(Mode.DISABLE);
                    Core.getCore().device[0].driverHorizon.ddcSetMode(Mode.DISABLE);

                    Core.getCore().countConectedDevice = 0;
                    Core.getCore().device[1].driverHorizon.ethernetSet(ip,maskInt,port,get);
                    changeSettingsIPbutton.setDisable(true);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    changeSettingsIPbutton.setDisable(false);
                    Core.getCore().device[0].bufferController.setWorkingThread(false);

                    Core.getCore().device[0].ethernetDriver.closeSocket();
                    Core.getCore().device[1].ethernetDriver.closeSocket();
                    Core.getCore().mainUI.setConnectButton();
                }


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

    public String getIP(){
        return ipTextRx.getText();
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
    public void getFrequencyRx(int data) {
        String tmp = Integer.toString(data);
        Platform.runLater(() -> {
            if(!freqRxText.isFocused()){
                freqRxText.setText(tmp);
            }
        });
    }
    public void getModeRx(Mode data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            modeRxText.setText(tmp);
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
            widthRxText.setText(qwe);
        });
    }

    public void updateVisibility(boolean state) {
        Platform.runLater(() -> {
            freqHzRxLabel.setDisable(state);
            freqRxLabel.setDisable(state);
            freqRxText.setDisable(state);
            changeIPRxButton.setDisable(state);

        });
    }
    @Override
    public void saveAll(Params params) {
    }

    @Override
    public void restoreAll(Params params) {
        //Restore Ethernet params 1
        ipTextField.setText(Params.SETTINGS.getString("ethernet-ip-address", "192.168.0.1"));
        //rxFrequencyTextField.setText(Params.SETTINGS.getString("rx_UPS_frequency", "128000"));
    }
}
