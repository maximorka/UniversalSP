package comUniversal.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.fxmisc.richtext.InlineCssTextArea;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InformationWindow {

    @FXML
    private Label speedReceiveLabel;
    @FXML
    private Label freqReceiveLabel;
    @FXML
    private Label speedL;
    @FXML
    private Label freqL;
    @FXML
    private Circle indicator;
    @FXML
    private Label statusLabel;
    @FXML
    private Pane oldPane;
    @FXML
    private Pane newPane;

    @FXML
    private Button clearText;
    @FXML
    private Label statusL;
    int countGroup=0;
    @FXML
    private TextArea text;
   // public InlineCssTextArea special;
    //public InlineCssTextArea number;

    private int frequency = 0;


    private static final String ERROR_STYLE = "-fx-fill: #9c9a94;";
    private static final String WAIT_STYLE = "-fx-fill: #9c9a94;";
    private static final String RECEIVE_STYLE = "-fx-fill: #26d137;";
    private static final String ANY_STYLE = "-fx-fill: #d1c826;";
    private static final String WARN_STYLE = "-fx-fill: black; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String NUMB_STYLE = "-fx-fill: blue; -fx-font-family: Consolas; -fx-font-size: 14;";

    @FXML
    private void initialize() {

        System.out.println("initialize() information window");

        speedL.setVisible(false);
        freqL.setVisible(false);
        //statusL.setVisible(false);
        //speedReceiveLabel.setTextFill(Color.web("#00cc00"));
        text.setWrapText(true);
        text.autosize();
        indicator.setStyle(WAIT_STYLE);
        text.setEditable(false);

//        special.setOnScroll(new EventHandler<ScrollEvent>() {
//            @Override
//            public void handle(ScrollEvent event) {
//                System.out.println("what");
//            }
//        });

//        send.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                String txt = mes.getText();
//                Core.getCore().dev.groupAdd.add(txt);
//                System.out.println("hel");
//            }
//        });

        clearText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                text.clear();
                countGroup = 0;
            }
        });
    }

//    private String longText() {
//        StringBuilder stringBuilder = new StringBuilder();
//    }

    public void setTextMessage(String data) {
        text.appendText(data);

        countGroup++;
        if (countGroup % 10 == 0) {
            Platform.runLater(() -> {
                text.appendText("\n\r");
            });
        }
    }

    public void enterTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("�������� dd.MM.yyyy � HH:mm:ss");
        Date date = new Date();
        String time = formatter.format(date);

        text.appendText("\n\r");
        text.appendText("\n\r");
        text.appendText(time);
        text.appendText("\n\r");
        countGroup = 0;
    }


    public void setAlgoritm(int algoritm, int speed){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                speedReceiveLabel.setText("");
                freqReceiveLabel.setText("");

                speedL.setVisible(false);
                freqL.setVisible(false);
                indicator.setStyle(WAIT_STYLE);
                if(algoritm == 1){
                    speedL.setVisible(true);
                    freqL.setVisible(true);
                    indicator.setStyle(RECEIVE_STYLE);
                    speedReceiveLabel.setText(Integer.toString(speed));
                    freqReceiveLabel.setText(Integer.toString(frequency)+" ��");
                } else if(algoritm == 2){
                    speedL.setVisible(false);
                    freqL.setVisible(false);

                    indicator.setStyle(ANY_STYLE);
                    speedReceiveLabel.setText("");
                    freqReceiveLabel.setText("");
                }

            }
        });
    }
    public void setFreq(float freq){
        this.frequency = (int)freq;
    }


}

