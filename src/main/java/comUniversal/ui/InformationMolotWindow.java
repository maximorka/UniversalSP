

package comUniversal.ui;

import comUniversal.SendData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

//import javafx.css.Style;

public class InformationMolotWindow {

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
    private TextField corNumberTextField;

    @FXML
    private TextField freqTextField;

    @FXML
    private ChoiceBox<String> command;

    @FXML
    private TextField serviceMessage;
    @FXML
    private Label speedLabel;

    @FXML
    private RadioButton speed100RadioButton;
    @FXML
    private RadioButton speed200RadioButton;
    @FXML
    public Button sendButton;

    @FXML
    private ProgressBar txProgressBar;

    @FXML
    private TextArea messageReceived;


    @FXML
    public void initialize() {

        System.out.println("initialize() information window");
        //txProgressBar = new ProgressBar();
        message = new TextArea();
        message = messageReceived;

        progressBar = new ProgressBar();
        progressBar = txProgressBar;

        RadioButton100 =new RadioButton();
        RadioButton100 = speed100RadioButton;

        RadioButton200 =new RadioButton();
        RadioButton200 = speed200RadioButton;

        speedL = new Label();
        speedL = speedLabel;

        messageReceived.setEditable(false);

        for (int i = 1; i <= 9; i++) {
            command.getItems().add(String.valueOf(i));
        }

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                String txt = messageTransmitter.getText();
//                System.out.println("Fine");
//                Core.getCore().device[0].groupAdd.add(txt);
//                sendButton.setDisable(true);
//                speed100RadioButton.setDisable(true);
//                speed200RadioButton.setDisable(true);
//                speedLabel.setDisable(true);

            }
        });
        speed100RadioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                speed200RadioButton.setSelected(false);
//                if(Core.getCore().countConectedDevice == 1) {
//                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);
//                    Core.getCore().device[0].optimalNonCoherentDåmodulatorPsk100.setRelativeBaudRate(100.f / 3000.f);
//                }else if(Core.getCore().countConectedDevice == 2) {
//                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);
//                    Core.getCore().device[1].optimalNonCoherentDåmodulatorPsk100.setRelativeBaudRate(100.f / 3000.f);
//                }
            }
        });
        speed200RadioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                speed100RadioButton.setSelected(false);
//                if(Core.getCore().countConectedDevice == 1) {
//                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(250.f / 3000.f);
//                    Core.getCore().device[0].optimalNonCoherentDåmodulatorPsk100.setRelativeBaudRate(250.f / 3000.f);
//                }else if(Core.getCore().countConectedDevice == 2) {
//                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(250.f / 3000.f);
//                    Core.getCore().device[1].optimalNonCoherentDåmodulatorPsk100.setRelativeBaudRate(250.f / 3000.f);
//                }
            }
        });
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

    public void openFile(ActionEvent actionEvent) throws IOException {
        //Core.setGeneratorIQServerModeTx();
    }

    public void sendingMessage(ActionEvent actionEvent) {
        if (corNumberTextField.getText().matches("\\d{3}")) {
            SendData sendData = new SendData();
            sendData.setNumberCorespondent(corNumberTextField.getText());
            if (command.getValue() != null) {
                sendData.setCommand(Integer.parseInt(command.getValue()));
            }
            boolean freqBoolean = validation(freqTextField);
            boolean serviceMessageBoolean = validation(serviceMessage);
            String freqString = freqTextField.getText();
            String serviceMessageString = serviceMessage.getText();
            if (freqBoolean && serviceMessageBoolean && command.getValue() == null) {
                inf2();
            } else if (freqBoolean) {
                if (serviceMessageBoolean) {
                    if (command.getValue() != null) {
                        clear();
                        sendData.parsData();
                    }
                } else if (checkMather(serviceMessage, "\\d{3,78}")) {
                    sendData.setServiceMessage(serviceMessageString);
                    clear();
                    sendData.parsData();
                } else {
                    inf("Ñëóæáîâå ïîâ³äîìëåííÿ");
                }
            } else if (checkMather(freqTextField, "\\d{5}")) {
                sendData.setFrequencyTx(freqString);
                if (validation(serviceMessage)) {
                    clear();
                    sendData.parsData();
                } else if (checkMather(serviceMessage, "\\d{3,78}")) {
                    sendData.setServiceMessage(serviceMessageString);
                    clear();
                    sendData.parsData();
                } else {
                    inf("Ñëóæáîâå ïîâ³äîìëåííÿ");
                }
            } else {
                inf("Ïåðåâ³äíà ãðóïà");
            }
        } else {
            inf("Íîìåð êîðåñïîíäåíòà");
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
        alert.setContentText("Íå êîðåêòíå çíà÷åííÿ");
        alert.showAndWait();
    }

    private void inf2() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(" ");
        alert.setHeaderText(null);
        alert.setContentText("Ââåä³òü çíà÷åííÿ");
        alert.showAndWait();
    }

    private void clear() {
        //freq1.clear();
        //serviceMessage.clear();
        //command.setValue(null);
    }
}



