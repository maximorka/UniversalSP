
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

public class TransmitterUPSWindowUI implements ParamsSettings {

    private List<ParamsSettings> settings = new ArrayList<>();

    Image Ok = new Image("/images/check.png");
    Image notOk = new Image("/images/close.png");

    public static TextField ipTextTx;

    public static Label modeTxText;
    public static Label widthTxText;
    public static Label procentTxText;
    public static TextField freqTxText;
    public static Button changeIPTxButton;

    @FXML
    private TextField txFrequencyTextField;

    @FXML
    private TextField ipTextField;

    @FXML
    private ImageView testIpImageView;

    @FXML
    private Button changeSettingsbutton;
    @FXML
    private Label procentDeviceLabel;
    @FXML
    private Label modeDeviceLabel;
    @FXML
    private Label widthDeviceLabel;

    @FXML
    public void initialize() {
        System.out.println("initialize() setting transmitter");
        ipTextTx = new TextField();
        ipTextTx = ipTextField;

        modeTxText = new Label();
        modeTxText = modeDeviceLabel;

        widthTxText = new Label();
        widthTxText = widthDeviceLabel;

        procentTxText = new Label();
        procentTxText = procentDeviceLabel;

        freqTxText = new TextField();
        freqTxText = txFrequencyTextField;
        changeIPTxButton = new Button();
        changeIPTxButton = changeSettingsbutton;
        restoreAll(Params.SETTINGS);
        changeSettingsbutton.setVisible(false);
        txFrequencyTextField.setDisable(true);
        txFrequencyTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int frequency = Integer.parseInt(txFrequencyTextField.getText());

                Core.getCore().driverHorizon.ducSetFrequency(frequency);
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
    public String getIP(){
        return ipTextTx.getText();
    }

    @Override
    public void saveAll(Params params) {
        Params.SETTINGS.putString("ethernet-ip-address", ipTextField.getText());
    }

    public void updatePercent(int percent) {
        String procent = Integer.toString(percent);
        Platform.runLater(() -> {
            procentTxText.setText(procent);
        });
    }

    public void getFrequencyTx(int data) {
        String tmp = Integer.toString(data);
        Platform.runLater(() -> {
            freqTxText.setText(tmp);
        });
    }
    public void getModeTx(Mode data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            modeTxText.setText(tmp);
        });
    }
    public void getWidthTx(Width data) {
        String tmp = String.valueOf(data);
        Platform.runLater(() -> {
            widthTxText.setText(tmp);
        });
    }
    public void updateVisibility(){
        changeIPTxButton.setVisible(true);
    }
    @Override
    public void restoreAll(Params params) {
        //Restore Ethernet params 1
        ipTextField.setText(Params.SETTINGS.getString("ethernet-ip-address", "192.168.0.1"));
        //txFrequencyTextField.setText(Params.SETTINGS.getString("rx_UPS_frequency", "128000"));

    }
}
