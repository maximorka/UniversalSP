package comUniversal.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;

//import javafx.css.Style;

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

