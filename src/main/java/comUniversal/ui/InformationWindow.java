package comUniversal.ui;

import comUniversal.Core;
import javafx.application.Platform;
//import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;

public class InformationWindow {
    public static TextArea message;

    public static Button sendB;
    public static RadioButton RadioButton100;
    public static RadioButton RadioButton200;
    public static ProgressBar progressBar;
    public static Label speedL;

    @FXML
    private ProgressBar txProgressBar;
    @FXML
    private TextArea messageTransmitter;
    @FXML
    public Button sendButton;
    @FXML
    private TextArea messageReceived;

    @FXML
    private RadioButton speed100RadioButton;
    @FXML
    private RadioButton speed200RadioButton;
    @FXML
    private Label speedLabel;
    @FXML
    private TextFlow tx;

    int countSymbol=0;

//private Style styledDocument;
public int procent1 = 0;
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

        sendB = new Button();
        sendB = sendButton;

        speedL = new Label();
        speedL = speedLabel;



        //styledDocument =messageReceived.getStyle();
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String txt = messageTransmitter.getText();
                System.out.println("Fine");
                Core.getCore().device[0].groupAdd.add(txt);
                sendButton.setDisable(true);
                speed100RadioButton.setDisable(true);
                speed200RadioButton.setDisable(true);
                speedLabel.setDisable(true);

            }
        });
        speed100RadioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                speed200RadioButton.setSelected(false);
                if(Core.getCore().countConectedDevice == 1) {
                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);
                    Core.getCore().device[0].optimalNonCoherentDеmodulatorPsk.setParametrs(100.f,48000.f);
                }else if(Core.getCore().countConectedDevice == 2) {
                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);
                    Core.getCore().device[1].optimalNonCoherentDеmodulatorPsk.setParametrs(100.f,48000.f);
                }
            }
        });
        speed200RadioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                speed100RadioButton.setSelected(false);
                if(Core.getCore().countConectedDevice == 1) {
                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(250.f / 3000.f);
                    Core.getCore().device[0].optimalNonCoherentDеmodulatorPsk.setParametrs(250.f,48000.f);
                }else if(Core.getCore().countConectedDevice == 2) {
                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(250.f / 3000.f);
                    Core.getCore().device[1].optimalNonCoherentDеmodulatorPsk.setParametrs(250.f,48000.f);
                }
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
    public void setTextMessage(int data){
        String newLine = System.getProperty("line.separator");
        String tmp = Integer.toString(data);


        if(tmp.equals("10")){
            Platform.runLater(() -> {

                message.appendText("*");
            });
        }else {
            Platform.runLater(() -> {

            message.appendText(tmp);
        });
        }

        countSymbol++;
        if(countSymbol % 5== 0){
            Platform.runLater(() -> {
                message.appendText(" ");
            });

        }
        if(countSymbol % 50== 0){
            int countgroup = countSymbol/5;
            Platform.runLater(() -> {
                message.appendText(" "+countgroup);
                message.appendText(newLine);
            });
        }


    }

}

