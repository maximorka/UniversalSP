
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
    public static Label widthLabel;
    public static Label modeLabel;
    public static Label initLabel;
    public static TextField freqText;
    public static Label procentText;
    public static TextField ipText;
    public static TextField portText;
    @FXML
    private Label initDeviceLabel;
    @FXML
    private Label modeDeviceLabel;
    @FXML
    private Label widthDeviceLabel;
    @FXML
    private Label procentDeviceLabel;
    @FXML
    public TextField rxFrequencyTextField;
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
        ipText = new TextField();
        ipText = ipTextField;

        portText = new TextField();
        portText = portTextField;


        widthLabel = new Label();
        widthLabel = this.widthDeviceLabel;
        modeLabel = new Label();
        modeLabel = this.modeDeviceLabel;
        initLabel = new Label();
        initLabel = this.initDeviceLabel;
        freqText = new TextField();
        freqText = this.rxFrequencyTextField;
        procentText = new Label();
        procentText = this.procentDeviceLabel;
        this.initDeviceLabel = new Label("");
        this.modeDeviceLabel = new Label("");
        this.widthDeviceLabel = new Label("");
        this.restoreAll(Params.SETTINGS);
        this.changeSettingsbutton.setVisible(true);
        testIP();

        getFreqRxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int frequency = Integer.parseInt(rxFrequencyTextField.getText());
                Core.getCore().driverHorizon.ddcSetFrequency(frequency);
            }
        });

        ipTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                testIP();
            }
        });
    }


    public void updatePercent(int percent) {
        String procent = Integer.toString(percent);
        Platform.runLater(() -> {
            procentText.setText(procent);
        });
    }

    public void getFrequency(int data) {
        String tmp = Integer.toString(data);
        Platform.runLater(() -> {
            freqText.setText(tmp);
        });
    }

    public void getInit(String data) {
        Platform.runLater(() -> {
            initLabel.setText(data);
        });
    }

    public void getMode(Mode data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            modeLabel.setText(tmp);
        });
    }

    public void getWidth(Width data) {
        String tmp = String.valueOf(data);
        System.out.println(tmp);
        Platform.runLater(() -> {
            widthLabel.setText(tmp);
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
