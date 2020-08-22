

package comUniversal.ui;

import comUniversal.BitLevel.InfAdd;
import comUniversal.Core;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

//import javafx.css.Style;

public class InformationMolotWindow {
    InfAdd inf = new InfAdd();
    Date date1 = new Date();
    public static Button sendB;
    public static RadioButton RadioButton100;
    public static RadioButton RadioButton200;
    public static ProgressBar progressBar;
    public static TextArea message;
    public static Label speedL;

    int numberCorespon;
    private int currentSpeed;

    private double[] tmpValues;
    byte[] buf;


    public byte index = 0;
    public int flagPoint = 0;

    public boolean frequencyFlag = false;
    public boolean filterFlag = false;
    public boolean serverFlag = false;
    public int freq = 0;
    public int filter = 0;

    private int step_count = 0;

    private int step = 0;
    String point = "1024";
    javafx.scene.image.Image image1;

    double scaleGra = 1;
    int flagDraw = 0;
    public static int frequencyWinradio = 0;
    public static int filterWinradio = 0;

    //private HorizonDevice[] horizonDevice;
    // private HorizonDevice horizonDevice1;
    private boolean running = true;
    private String typeRx;
    private String typeTx;
    private String mode;

    int countSymbol=0;

    @FXML
    private TextField time;
    @FXML
    private TextField corNumberTextField;
    @FXML
    private TextField corKoefTextField;

    @FXML
    private TextField freq1TextField;
    @FXML
    private TextField freq2TextField;
    @FXML
    private TextField freq3TextField;
    @FXML
    private TextField freq4TextField;
    @FXML
    private TextField command1TextField;
    @FXML
    private TextField command2TextField;
    @FXML
    private TextField command3TextField;
    @FXML
    private TextField command4TextField;
    @FXML
    private TextField dataSeansTextField;
    @FXML
    private TextField timeSeansTextField;
    @FXML
    private TextField f1SeansTextField;
    @FXML
    private TextField f2SeansTextField;

    @FXML
    private TextField urlRgTextField;


    @FXML
    private ComboBox modeRxComboBox;
    @FXML
    private ComboBox speedTxComboBox;
    @FXML
    private ComboBox timeTxComboBox;

    @FXML
    private Button testButton;
    @FXML
    private Button majorButton;
    @FXML
    private Button saveRgButton;
    //    @FXML
//    private Button sendButton;
    @FXML
    private Button stopTxButton;
    @FXML
    private Button choiceTxRgButton;

    @FXML
    private CheckBox cycleTxCheckBox;

    @FXML
    private TextArea messageReceived;
    @FXML
    private TextArea txRgTextArea;

    @FXML
    private ListView serviceMessageTxList;

    @FXML
    public TabPane tab;
    @FXML
    public AnchorPane addSeans;

    @FXML
    private Label speedLabel;
    @FXML
    private ProgressBar txProgressBar;

    @FXML
    public void initialize() {
        TimeTh timeT = new TimeTh();
        timeT.start();
        System.out.println("initialize() information window");


        //txProgressBar = new ProgressBar();
        ObservableList <String> mode = FXCollections.observableArrayList("≤ÍÛÚ","5◊“20");
        modeRxComboBox.setItems(mode);
        modeRxComboBox.setValue("¬Ë·Â≥Ú¸");
        ObservableList <String> speedTx = FXCollections.observableArrayList("100","250");
        speedTxComboBox.setItems(speedTx);
        speedTxComboBox.setValue("¬Ë·Â≥Ú¸");

        ObservableList <String> timeTx = FXCollections.observableArrayList("3 ı‚","6 ı‚","12 ı‚");
        timeTxComboBox.setItems(timeTx);

        ObservableList <String> serviceMessageTx = FXCollections.observableArrayList("œŒ¬“Œ–≤“‹","«¿◊≈ ¿…“≈","ƒŒƒ¿“ Œ¬»… —≈¿Õ—");
        serviceMessageTxList.setItems(serviceMessageTx);

        message = new TextArea();
        message = messageReceived;

        progressBar = new ProgressBar();
        progressBar = txProgressBar;

        speedL = new Label();
        speedL = speedLabel;
        addSeans.setVisible(false);
//        messageReceived.setEditable(false);

        for (int i = 1; i <= 9; i++) {
            // command.getItems().add(String.valueOf(i));
        }
        serviceMessageTxList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addSeans.setVisible(false);
                String serviceMessage = (String) serviceMessageTxList.getSelectionModel().getSelectedItem();
                System.out.println(serviceMessage );
                if(serviceMessage.equals("ƒŒƒ¿“ Œ¬»… —≈¿Õ—")){
                    addSeans.setVisible(true);

                }
            }
        });
//        sendButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//               String txt ="";// serviceMessage.getText();
//
//                System.out.println("Fine");
//
//                Core.getCore().dev.infAdd.add(txt);
//                //Core.getCore().dev.infAdd.add("011101011101011101011101011101011101");
//               // Core.getCore().device[0].groupAdd.add(txt);
////                sendButton.setDisable(true);
////                speed100RadioButton.setDisable(true);
////                speed200RadioButton.setDisable(true);
////                speedLabel.setDisable(true);
//
//            }
//        });
//        speed100RadioButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                speed200RadioButton.setSelected(false);
////                if(Core.getCore().countConectedDevice == 1) {
////                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);
////                    Core.getCore().device[0].optimalNonCoherentD?modulatorPsk100.setRelativeBaudRate(100.f / 3000.f);
////                }else if(Core.getCore().countConectedDevice == 2) {
////                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);
////                    Core.getCore().device[1].optimalNonCoherentD?modulatorPsk100.setRelativeBaudRate(100.f / 3000.f);
////                }
//            }
//        });
//        speed200RadioButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                speed100RadioButton.setSelected(false);
////                if(Core.getCore().countConectedDevice == 1) {
////                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(250.f / 3000.f);
////                    Core.getCore().device[0].optimalNonCoherentD?modulatorPsk100.setRelativeBaudRate(250.f / 3000.f);
////                }else if(Core.getCore().countConectedDevice == 2) {
////                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(250.f / 3000.f);
////                    Core.getCore().device[1].optimalNonCoherentD?modulatorPsk100.setRelativeBaudRate(250.f / 3000.f);
////                }
//            }
//        });
    }

    public void updatePercentRadiogram(int percent) {

        float percentNow = (float) 1-((float)percent/(float)100);
        Platform.runLater(() -> {
            progressBar.setProgress(percentNow);
        });
        if(percent==0){
            Platform.runLater(() -> {
                sendB.setDisable(false);
                RadioButton100.setDisable(false);
                RadioButton200.setDisable(false);
                speedL.setDisable(false);
                progressBar.setProgress(0);
            });
        }
    }

    public void setTextMessage(int data, int speed) {
        String newLine = System.getProperty("line.separator");
        String tmp = Integer.toString(data);
        if (speed == 100) {
            if (tmp.equals("10")) {
                Platform.runLater(() -> {
                    message.appendText("*");
                });
            } else {
                Platform.runLater(() -> {
                    message.appendText(tmp);
                });
            }

            countSymbol++;
            if (countSymbol % 5 == 0) {
                Platform.runLater(() -> {
                    message.appendText(" ");
                });

            }
            if (countSymbol % 50 == 0) {
                //int countgroup = countSymbol/5;
                Platform.runLater(() -> {
                    message.appendText("\n");
                });
            }
        }
    }

    public void setTextMessageStrum(String data) {

        Platform.runLater(() -> {
            message.appendText(data);
            message.appendText("\n");
        });
    }

    public void openFile(ActionEvent actionEvent) throws IOException {
        //Core.setGeneratorIQServerModeTx();
    }

    public void sendingMessage(ActionEvent actionEvent) {

        int activeTab = tab.getSelectionModel().getSelectedIndex();
        if (corNumberTextField.getText().matches("\\d{3}")) {

            Core.getCore().dev.infAdd.setNumberCorespondent(corNumberTextField.getText());
            if (activeTab == 1) {
                String string = txRgTextArea.getText();
                Core.getCore().dev.infAdd.addDataRg(string);
            }else {
                if (command1TextField.getText() != null && !(command1TextField.getText().equals(""))) {
                    Core.getCore().dev.infAdd.setCommand1(Integer.parseInt(command1TextField.getText()));
                }
                if (command2TextField.getText() != null && !(command2TextField.getText().equals(""))) {
                    Core.getCore().dev.infAdd.setCommand2(Integer.parseInt(command2TextField.getText()));
                }
                if (command3TextField.getText() != null && !(command3TextField.getText().equals(""))) {
                    Core.getCore().dev.infAdd.setCommand3(Integer.parseInt(command3TextField.getText()));
                }
                if (command4TextField.getText() != null && !(command4TextField.getText().equals(""))) {
                    Core.getCore().dev.infAdd.setCommand4(Integer.parseInt(command4TextField.getText()));
                }
            }





//            boolean freqBoolean = validation(freqTextField);
//            boolean serviceMessageBoolean = validation(serviceMessage);
//            String freqString = freqTextField.getText();
//            String serviceMessageString = serviceMessage.getText();
//            if (freqBoolean && serviceMessageBoolean && command.getValue() == null) {
//                inf2();
//            } else if (freqBoolean) {
//                if (serviceMessageBoolean) {
//                    if (command.getValue() != null) {
//                        clear();
//                        sendData.parsData();
//                    }
//                } else if (checkMather(serviceMessage, "\\d{3,78}")) {
//                    sendData.setServiceMessage(serviceMessageString);
//                    clear();
//                    sendData.parsData();
//                } else {
//                    inf("???????? ????????????");
//                }
//            } else if (checkMather(freqTextField, "\\d{5}")) {
//                sendData.setFrequencyTx(freqString);
//                if (validation(serviceMessage)) {
//                    clear();
//                    sendData.parsData();
//                } else if (checkMather(serviceMessage, "\\d{3,78}")) {
//                    sendData.setServiceMessage(serviceMessageString);
//                    clear();
//                    sendData.parsData();
//                } else {
//                    inf("???????? ????????????");
//                }
//            } else {
//                inf("????????? ?????");
//            }
        } else {
            inf("œÓÁË‚ÌËÈ");
        }

    }

    private boolean checkMather(TextField textField, String matches) {

        return textField.getText().replaceAll(" ", "").matches(matches);
    }

    private boolean validation(TextField textField) {
        return textField.getText().isEmpty();
    }
    private void inf(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(text);
        alert.setHeaderText(null);
        alert.setContentText("ÕÂ ÍÓÂÍÚÌÂ ÁÌ‡˜ÂÌÌˇ");
        alert.showAndWait();
    }

    private void inf2() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(" ");
        alert.setHeaderText(null);
        alert.setContentText("??????? ????????");
        alert.showAndWait();
    }

    private void clear() {
        //freq1.clear();
        //serviceMessage.clear();
        //command.setValue(null);
    }
    public class TimeTh extends Thread{
        @Override
        public void run() {
            while (true){
                LocalTime myObj = LocalTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formattedDate = myObj.format(myFormatObj);

                time.setText(String.valueOf(formattedDate));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

