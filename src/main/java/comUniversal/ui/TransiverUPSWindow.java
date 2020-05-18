
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class TransiverUPSWindow implements ParamsSettings {
    private List<ParamsSettings> settings = new ArrayList();
    Image Ok = new Image("/images/check.png");
    Image notOk = new Image("/images/close.png");
    public static Label widthRxLabel;
    public static Label modeRxLabel;
    public static Label widthTxLabel;
    public static Label modeTxLabel;
    public static Label initLabel;
    public static TextField freqRxText;
    public static TextField freqTxText;
    public static Label procentText;
    public static TextField ipText;
    public static TextField portText;
    @FXML
    private Label initDeviceLabel;
    @FXML
    private Label modeTxDeviceLabel;
    @FXML
    private Label widthTxDeviceLabel;
    @FXML
    private Label modeRxDeviceLabel;
    @FXML
    private Label widthRxDeviceLabel;
    @FXML
    private Label procentDeviceLabel;
    @FXML
    public TextField rxFrequencyTextField;
    @FXML
    public TextField txFrequencyTextField;
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private ImageView testIpImageView;
    @FXML
    private Button changeSettingsbutton;
    @FXML
    private Button getFreqRxButton;
    @FXML
    private Button getFreqTxButton;
    @FXML
    private Button getIPButton;



    @FXML
    public void initialize() {

        System.out.println("initialize() setting Transceiver");
        ipText = new TextField("");
        ipText = ipTextField;

        portText = new TextField("");
        portText = portTextField;


        widthRxLabel = new Label("");
        widthRxLabel = this.widthRxDeviceLabel;
        modeRxLabel = new Label("");
        modeRxLabel = this.modeRxDeviceLabel;

        widthTxLabel = new Label("");
        widthTxLabel = this.widthTxDeviceLabel;
        modeTxLabel = new Label("");
        modeTxLabel = this.modeTxDeviceLabel;
        initLabel = new Label("");
        initLabel = this.initDeviceLabel;

        freqRxText = new TextField("");
        freqRxText = this.rxFrequencyTextField;

        freqTxText = new TextField("");
        freqTxText = this.txFrequencyTextField;

        procentText = new Label("");
        procentText = this.procentDeviceLabel;

        rxFrequencyTextField.setVisible(false);
        this.changeSettingsbutton.setVisible(false);
        
        this.initDeviceLabel = new Label("");

        this.restoreAll(Params.SETTINGS);


        testIP();

        getFreqRxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                Core.getCore().driverHorizon.ddcGetFrequency();
            }
        });
        getFreqTxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Core.getCore().driverHorizon.ducGetFrequency();
                Core.getCore().driverHorizon.ducGetMode();
                Core.getCore().driverHorizon.ducGetWidth();
                Core.getCore().driverHorizon.ddcGetMode();
                Core.getCore().driverHorizon.ddcGetWidth();
                Core.getCore().driverHorizon.init();
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
            modeRxLabel.setText(tmp);
        });
    }
    public void getModeTx(Mode data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            modeTxLabel.setText(tmp);
        });
    }

    public void getWidthRx(Width data) {
        String tmp = String.valueOf(data);
        System.out.println(tmp);
        Platform.runLater(() -> {
            widthRxLabel.setText(tmp);
        });
    }

    public void getWidthTx(Width data) {
        String tmp = String.valueOf(data);
        System.out.println(tmp);
        Platform.runLater(() -> {
            widthTxLabel.setText(tmp);
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
