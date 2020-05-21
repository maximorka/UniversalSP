package comUniversal.ui;

import comUniversal.Core;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String txt = messageTransmitter.getText();
                System.out.println("Fine");
                Core.getCore().groupAdd.add(txt);
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
                Core.getCore().modulatorPsk.setRelativeBaudeRate(100.f/3000.f);
                Core.getCore().demodulatorPsk.setParametrs(100.f,3000.f);
            }
        });
        speed200RadioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                speed100RadioButton.setSelected(false);
                Core.getCore().modulatorPsk.setRelativeBaudeRate(250.f/3000.f);
                Core.getCore().demodulatorPsk.setParametrs(250.f,3000.f);
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
        String tmp = Integer.toString(data);
        if(tmp.equals("12")){
            Platform.runLater(() -> {
            message.appendText("*");
            });
        }else {
            Platform.runLater(() -> {
            message.appendText(tmp);
        });
        }

    }

}

