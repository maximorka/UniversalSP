package comUniversal.ui;

import comUniversal.Core;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.InlineCssTextArea;
//import javafx.css.Style;

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
    private Label statusLabel;
    @FXML
    private Pane oldPane;
    @FXML
    private Pane newPane;
    @FXML
    private TextArea mes;
    @FXML
    private Button send;

    int countSymbol=0;

    public InlineCssTextArea special;
    public InlineCssTextArea number;

    private int frequency = 0;

    private static final String ERROR_STYLE = "-fx-fill: red; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String RECOVERY_STYLE = "-fx-fill: red; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String WARN_STYLE = "-fx-fill: black; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String NUMB_STYLE = "-fx-fill: blue; -fx-font-family: Consolas; -fx-font-size: 14;";

    @FXML
    private void initialize() {

        System.out.println("initialize() information window");
        speedL.setVisible(false);
        freqL.setVisible(false);
        speedReceiveLabel.setTextFill(Color.web("#00cc00"));
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String txt = mes.getText();
                Core.getCore().dev.groupAdd.add(txt);
            }
        });
    }

    public void setTextMessage(String message) {
//        String tmp;
//
//        String style;
//
//        if ((data == 10) || (data == 110)) {
//            style = ERROR_STYLE;
//            tmp = "*";
//        } else if(data >= 100){
//            data -= 100;
//            style = ERROR_STYLE;
//            tmp = String.valueOf(data);
//        } else{
//            style = WARN_STYLE;
//            tmp = String.valueOf(data);
//        }


            Platform.runLater(() -> {
                // получаем границы нового сообещения
                int from = special.getLength();
                int to = from + 1;
                // добавили сообещние
//                special.appendText("\u0332");
                special.appendText(message + " ");

                // указали для него стиль
                special.setStyle(from, to, WARN_STYLE);
            });


//        countSymbol++;
//        if (countSymbol % 5 == 0) {
//            Platform.runLater(() -> {
//                // получаем границы нового сообещения
//                int from = special.getLength();
//                int to = from + 1;
//                // добавили сообещние
//                special.appendText(" ");
//                // указали для него стиль
//                special.setStyle(from, to, WARN_STYLE);
//                //numberM.appendText(" ");
//
//            });
//
//        }
        Platform.runLater(() -> {
            if (special.getCurrentLineEndInParargraph() % 60 == 0) {
                String num = String.valueOf(special.getCurrentLineEndInParargraph() / 6);
                if (num.equals(10)) {
                    number.deleteText(0, special.getCurrentLineEndInParargraph());
                }
                int from = number.getLength();
                int to = from + 2;
                number.appendText(num);
                number.setStyle(from, to, NUMB_STYLE);
            }
        });
    }

    public void setAlgoritm(int algoritm, int speed){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText("");
                speedReceiveLabel.setText("");
                freqReceiveLabel.setText("");

                speedL.setVisible(false);
                freqL.setVisible(false);
                if(algoritm == 1){
                    speedL.setVisible(true);
                    freqL.setVisible(true);
                    statusLabel.setTextFill(Color.web("#00cc00"));
                    statusLabel.setText("Прийом");
                    speedReceiveLabel.setText(Integer.toString(speed));
                    freqReceiveLabel.setText(Integer.toString(frequency));
                } else if(algoritm == 2){
                    speedL.setVisible(true);
                    freqL.setVisible(true);
                    statusLabel.setTextFill(Color.web("#e6e600"));
                    statusLabel.setText("Очікування");
                    speedReceiveLabel.setText(Integer.toString(speed));
                    freqReceiveLabel.setText(Integer.toString(frequency));
                }

            }
        });
    }
    public void setFreq(float freq){
        this.frequency = (int)freq;
    }
}

