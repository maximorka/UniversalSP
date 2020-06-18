package comUniversal.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
    private TextArea num;
    @FXML
    private Button send;
    @FXML
    private Label statusL;
    int countSymbol=0;
    @FXML
    private TextArea text;
   // public InlineCssTextArea special;
    public InlineCssTextArea number;

    private int frequency = 0;

    private static final String ERROR_STYLE = "-fx-fill: red; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String WARN_STYLE = "-fx-fill: black; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String NUMB_STYLE = "-fx-fill: blue; -fx-font-family: Consolas; -fx-font-size: 14;";

    @FXML
    private void initialize() {

        System.out.println("initialize() information window");

        speedL.setVisible(false);
        freqL.setVisible(false);
        statusL.setVisible(false);
        speedReceiveLabel.setTextFill(Color.web("#00cc00"));
        text.setWrapText(true);
        text.autosize();

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
    }
    private String longText() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                stringBuilder.append("line-");
            }
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }
    public void setTextMessage(int data) {
        String tmp = String.valueOf(data);

        if (tmp.equals("10")) {
            Platform.runLater(() -> {
//                // получаем границы нового сообещения
//                int from = special.getLength();
//                int to = from + 1;
//                // добавили сообещние
//                special.appendText("*");
//                // указали для него стиль
//                special.setStyle(from, to, ERROR_STYLE);
                text.appendText("*");
            });
        } else {
            Platform.runLater(() -> {
//                // получаем границы нового сообещения
//                int from = special.getLength();
//                int to = from + 1;
//                // добавили сообещние
//                special.appendText(tmp);
//                // указали для него стиль
//                special.setStyle(from, to, WARN_STYLE);
                //numberM.appendText(tmp);
                text.appendText(tmp);
            });
        }

        countSymbol++;
        if (countSymbol % 5 == 0) {

            Platform.runLater(() -> {
                // получаем границы нового сообещения
//                int from = special.getLength();
//                int to = from + 1;
//                // добавили сообещние
//                special.appendText(" ");
//                // указали для него стиль
//                special.setStyle(from, to, WARN_STYLE);
                //numberM.appendText(" ");
                text.appendText(" ");
            });

        }
        if(countSymbol % 50 == 0){
            int n = countSymbol % 50;
            Platform.runLater(() -> {
                num.appendText(n+"\n");
            });
            }
        }
//        Platform.runLater(() -> {
//            if (text.getCurrentLineEndInParargraph() % 60 == 0) {
//                String num = String.valueOf(special.getCurrentLineEndInParargraph() / 6);
//                if (num.equals(10)) {
//                    number.deleteText(0, special.getCurrentLineEndInParargraph());
//                }
//                int from = number.getLength();
//                int to = from + 2;
//                number.appendText(num);
//                number.setStyle(from, to, NUMB_STYLE);
//            }
//        });
    //}

    public void setAlgoritm(int algoritm, int speed){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText("");
                speedReceiveLabel.setText("");
                freqReceiveLabel.setText("");

                speedL.setVisible(false);
                freqL.setVisible(false);
                statusL.setVisible(false);
                if(algoritm == 1){
                    speedL.setVisible(true);
                    freqL.setVisible(true);
                    statusL.setVisible(true);
                    statusLabel.setTextFill(Color.web("#00cc00"));
                    statusLabel.setText("Прийом");
                    speedReceiveLabel.setText(Integer.toString(speed));
                    freqReceiveLabel.setText(Integer.toString(frequency));
                } else if(algoritm == 2){
                    speedL.setVisible(true);
                    freqL.setVisible(true);
                    statusL.setVisible(true);
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

