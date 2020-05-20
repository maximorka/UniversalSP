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

public class ReceiverUPSWindowUI implements ParamsSettings {

    private List<ParamsSettings> settings = new ArrayList<>();

    Image Ok = new Image("/images/check.png");
    Image notOk = new Image("/images/close.png");


    public static TextField ipTextRx;
    public static TextField freqRxText;
    public static Label widthRxText;
    public static Label modeRxText;
    public static Button changeIPRxButton;

    @FXML
    private TextField rxFrequencyTextField;

    @FXML
    private TextField ipTextField;

    @FXML
    private ImageView testIpImageView;

    @FXML
    private Button changeSettingsbutton;
    @FXML
    private Label modeDeviceLabel;
    @FXML
    private Label widthDeviceLabel;

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
        changeIPRxButton = changeSettingsbutton;
        restoreAll(Params.SETTINGS);

        changeSettingsbutton.setVisible(false);
        rxFrequencyTextField.setDisable(true);
        rxFrequencyTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int frequency = Integer.parseInt(rxFrequencyTextField.getText());
                Core.getCore().driverHorizon.ddcSetFrequency(frequency);
            }
        });

        changeSettingsbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Core.getCore().driverHorizon.ddcGetFrequency();
            }
        });

        testIP();
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
                testIpImageView.setImage(Ok);

            } else {

                testIpImageView.setImage(notOk);
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }
    public void getFrequencyRx(int data) {
        String tmp = Integer.toString(data);
        Platform.runLater(() -> {
            freqRxText.setText(tmp);
        });
    }
    public void getModeRx(Mode data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            modeRxText.setText(tmp);
        });
    }

    public void getWidthRx(Width data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            widthRxText.setText(tmp);
        });
    }
    public void updateVisibility(){
        changeIPRxButton.setVisible(true);
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
