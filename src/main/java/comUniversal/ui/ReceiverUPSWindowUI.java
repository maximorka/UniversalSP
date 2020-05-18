package comUniversal.ui;

import comUniversal.Core;
import comUniversal.ui.setting.ParamsSettings;
import comUniversal.util.Params;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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


    @FXML
    private TextField rxFrequencyTextField;
    @FXML
    private TextField txFrequencyTextField;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private ImageView testIpImageView;

    @FXML
    private Button changeSettingsbutton;

    @FXML
    public void initialize() {
        System.out.println("initialize() setting receiver");

        restoreAll(Params.SETTINGS);
        //changeSettingsbutton.setVisible(false);
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
    public void test(int data){
        String tmp = Integer.toString(data);
        rxFrequencyTextField.setText(tmp);
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

    @Override
    public void saveAll(Params params) {

    }

    @Override
    public void restoreAll(Params params) {
        //Restore Ethernet params 1
        ipTextField.setText(Params.SETTINGS.getString("ethernet-ip-address", "192.168.0.1"));
        portTextField.setText(Params.SETTINGS.getString("ethernet-port", "80"));
        rxFrequencyTextField.setText(Params.SETTINGS.getString("rx_UPS_frequency", "128000"));

    }
}
