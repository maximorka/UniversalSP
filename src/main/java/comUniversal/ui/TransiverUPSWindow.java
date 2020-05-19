
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
import javafx.scene.image.ImageView;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TransiverUPSWindow implements ParamsSettings {
    private List<ParamsSettings> settings = new ArrayList();
    Image Ok = new Image("/images/check.png");
    Image notOk = new Image("/images/close.png");
    public static TextField widthRxText;
    public static TextField modeRxText;
    public static TextField widthTxText;
    public static TextField modeTxText;
    public static Label initLabel;
    public static TextField freqRxText;
    public static TextField freqTxText;
    public static Label procentText;
    public static TextField ipText;
    public static TextField portText;
    public static Button clearBuf;


    @FXML
    private Label initDeviceLabel;
    @FXML
    private Button clearBufButton;
    @FXML
    private Label procentDeviceLabel;
    @FXML
    private TextField ipTextField;
    @FXML
    private ImageView testIpImageView;
    @FXML
    private TextField portTextField;
    @FXML
    private Button getIPButton;
    @FXML
    private Button changeSettingsIPbutton;
    @FXML
    public TextField rxFrequencyTextField;
    @FXML
    public TextField txFrequencyTextField;
    @FXML
    public TextField rxWidthTextField;
    @FXML
    public TextField txWidthTextField;
    @FXML
    public TextField rxModeTextField;
    @FXML
    public TextField txModeTextField;
    @FXML
    private Button setSettings;
    @FXML
    private Button getSettings;

    @FXML
    public TextField maskTextField;

    @FXML
    public TextField getawayTextField;

    @FXML
    public void initialize() {

        System.out.println("initialize() setting Transceiver");

        initLabel = new Label("");
        initLabel = this.initDeviceLabel;

        clearBuf = new Button();
        clearBuf = clearBufButton;

        procentText = new Label("");
        procentText = this.procentDeviceLabel;

        ipText = new TextField("");
        ipText = ipTextField;

        portText = new TextField("");
        portText = portTextField;


        freqRxText = new TextField("");
        freqRxText = this.rxFrequencyTextField;

        freqTxText = new TextField("");
        freqTxText = this.txFrequencyTextField;

        widthRxText = new TextField("");
        widthRxText = rxWidthTextField;

        widthTxText = new TextField("");
        widthTxText = txWidthTextField;

        modeRxText = new TextField("");
        modeRxText = rxModeTextField;

        modeTxText = new TextField("");
        modeTxText = txModeTextField;

        this.restoreAll(Params.SETTINGS);

        testIP();

        getSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("get settings");
                Core.getCore().driverHorizon.ddcGetFrequency();
                Core.getCore().driverHorizon.ducGetFrequency();

                Core.getCore().driverHorizon.ducGetMode();
                Core.getCore().driverHorizon.ddcGetMode();

                Core.getCore().driverHorizon.ducGetWidth();
                Core.getCore().driverHorizon.ddcGetWidth();

                Core.getCore().driverHorizon.init();
            }
        });
        setSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int data = Integer.parseInt(rxFrequencyTextField.getText());
                Core.getCore().driverHorizon.ddcSetFrequency(data);
                data = Integer.parseInt(txFrequencyTextField.getText());
                Core.getCore().driverHorizon.ducSetFrequency(data);

                Width[] tmp = Width.values();
                data = Integer.parseInt(rxWidthTextField.getText());
                Core.getCore().driverHorizon.ddcSetWidth(tmp[data]);

                data = Integer.parseInt(txWidthTextField.getText());
                Core.getCore().driverHorizon.ducSetWidth(tmp[data]);
                Mode[] tmpMode = Mode.values();
                data = Integer.parseInt(rxModeTextField.getText());
                Core.getCore().driverHorizon.ddcSetMode(tmpMode[data]);

                data = Integer.parseInt(txModeTextField.getText());
                Core.getCore().driverHorizon.ducSetMode(tmpMode[data]);
            }
        });
        rxFrequencyTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int frequency = Integer.parseInt(rxFrequencyTextField.getText());
                Core.getCore().driverHorizon.ddcSetFrequency(frequency);
            }
        });

        txFrequencyTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int frequency = Integer.parseInt(txFrequencyTextField.getText());
                Core.getCore().driverHorizon.ducSetFrequency(frequency);
            }
        });
        ipTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                testIP();
            }
        });

        clearBufButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Core.getCore().driverHorizon.ducClearBuffer();
            }
        });

        getIPButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Core.getCore().driverHorizon.ethernetGet();
            }
        });
        changeSettingsIPbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String ipAddress = ipTextField.getText();
                String mask = maskTextField.getText();
                String Getaway = getawayTextField.getText();
                int port = Integer.parseInt(portTextField.getText());


                byte[] ipBytes = EthernetUtils.ipToByteArray(ipAddress);
                byte[] maskBytes = EthernetUtils.ipToByteArray(mask);
                byte[] gatewayBytes = EthernetUtils.ipToByteArray(Getaway);
                int ip = ByteBuffer.wrap(ipBytes).getInt();
                int maskInt = ByteBuffer.wrap(maskBytes).getInt();
                int get = ByteBuffer.wrap(gatewayBytes).getInt();

                Core.getCore().driverHorizon.ethernetSet(ip,maskInt,port,get);
                Core.getCore().ethernetDriver.closeSocket();
                changeSettingsIPbutton.setDisable(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        changeSettingsIPbutton.setDisable(false);
                    });

                }).start();



                Core.getCore().mainUI.setConnectButton();
            }
        });

    }
    public void updateEthernet(int ip, int port, int mask, int gateway){
        String portW = Integer.toString(port);
        Platform.runLater(() -> {
            portText.setText(portW);
        });
    }
    public void updateVisibility(){
        freqRxText.setVisible(true);
        freqTxText.setVisible(true);
    }
    public void updatePercent(int percent) {
        String procent = Integer.toString(percent);
        Platform.runLater(() -> {
            procentText.setText(procent);
        });
    }

    public void getFrequencyRx(int data) {
        String tmp = Integer.toString(data);
        Platform.runLater(() -> {
            freqRxText.setText(tmp);
        });
    }

    public void getFrequencyTx(int data) {
        String tmp = Integer.toString(data);
        Platform.runLater(() -> {
            freqTxText.setText(tmp);
        });
    }

    public void getInit(int data) {
        Platform.runLater(() -> {
            initLabel.setText(Integer.toString(data));
        });
    }

    public void getModeRx(Mode data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            modeRxText.setText(tmp);
        });
    }
    public void getModeTx(Mode data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            modeTxText.setText(tmp);
        });
    }

    public void getWidthRx(Width data) {
        String tmp = String.valueOf(data);
        System.out.println(tmp);
        Platform.runLater(() -> {
            widthRxText.setText(tmp);
        });
    }

    public void getWidthTx(Width data) {
        String tmp = String.valueOf(data);
        System.out.println(tmp);
        Platform.runLater(() -> {
            widthTxText.setText(tmp);
        });
    }



    public void testIP() {
        try {
            String ipAddress = this.ipTextField.getText();
            InetAddress inet = InetAddress.getByName(ipAddress);
            if (inet.isReachable(500)) {
                this.testIpImageView.setImage(this.Ok);
            } else {
                this.testIpImageView.setImage(this.notOk);
            }
        } catch (Exception var3) {
            System.out.println("Exception:" + var3.getMessage());
        }

    }
    public String getIP(){
        return ipText.getText();
    }
    public String getPort(){
        return portText.getText();
    }
    public void saveAll(Params params) {
        Params.SETTINGS.putString("rx_UPS_frequency", this.rxFrequencyTextField.getText());
    }

    public void restoreAll(Params params) {
        this.ipTextField.setText(Params.SETTINGS.getString("ethernet-ip-address", "192.168.0.1"));
        this.portTextField.setText(Params.SETTINGS.getString("ethernet-port", "80"));
        this.rxFrequencyTextField.setText(Params.SETTINGS.getString("rx_UPS_frequency", "128000"));
    }
}
