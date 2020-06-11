package comUniversal.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import org.fxmisc.richtext.InlineCssTextArea;
//import javafx.css.Style;

public class InformationWindow {

    public static InlineCssTextArea message;
    public static InlineCssTextArea numberGroup;
    public static RadioButton receiveFlag;
    public static Label speedReceive;
    public static Label freqReceive;
    public static Label spLabel;
    public static Label frLabel;
    @FXML
    private RadioButton receiveRadioButton;
    @FXML
    private Label speedReceiveLabel;
    @FXML
    private Label freqReceiveLabel;
    @FXML
    private Label speedL;
    @FXML
    private Label freqL;
    @FXML
    private Pane oldPane;
    @FXML
    private Pane newPane;

    int countSymbol=0;

    public InlineCssTextArea special;
    public InlineCssTextArea number;

    private static final String ERROR_STYLE = "-fx-fill: red; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String WARN_STYLE = "-fx-fill: black; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String NUMB_STYLE = "-fx-fill: blue; -fx-font-family: Consolas; -fx-font-size: 14;";

    @FXML
    public void initialize() {

        System.out.println("initialize() information window");

        message = new InlineCssTextArea();
        message = special;
        numberGroup = new InlineCssTextArea();
        numberGroup = number;
        receiveFlag = new RadioButton();
        receiveFlag = receiveRadioButton;
        speedReceive = new Label();
        speedReceive = speedReceiveLabel;
        freqReceive = new Label();
        freqReceive = freqReceiveLabel;
        spLabel = new Label();
        spLabel = speedL;
        frLabel = new Label();
        frLabel = freqL;

    }


    public void setTextMessage(int data,String speed) {
        String tmp = String.valueOf(data);

        if (tmp.equals("10")) {
            Platform.runLater(() -> {

                speedReceive.setText(speed);
                //numberM.appendText("*");
                // получаем границы нового сообещения
                int from = message.getLength();
                //System.out.println("from:"+from);
                int to = from + 1;
                //System.out.println("to:"+to);
                // добавили сообещние
                message.appendText("*");
                // указали для него стиль
                message.setStyle(from, to, ERROR_STYLE);
            });
        } else {
            Platform.runLater(() -> {

                speedReceive.setText(speed);
                // получаем границы нового сообещения
                int from = message.getLength();
                int to = from + 1;
                // добавили сообещние
                message.appendText(tmp);
                // указали для него стиль
                message.setStyle(from, to, WARN_STYLE);
                //numberM.appendText(tmp);
            });
        }

        countSymbol++;
        if (countSymbol % 5 == 0) {
            Platform.runLater(() -> {
                // получаем границы нового сообещения
                int from = message.getLength();
                int to = from + 1;
                // добавили сообещние
                message.appendText(" ");
                // указали для него стиль
                message.setStyle(from, to, WARN_STYLE);
                //numberM.appendText(" ");

            });


        }
        Platform.runLater(() -> {

            if (message.getCurrentLineEndInParargraph() % 60 == 0) {
                String num = String.valueOf(message.getCurrentLineEndInParargraph() / 6);
                if (num.equals(10)) {
                    numberGroup.deleteText(0, message.getCurrentLineEndInParargraph());
                }
                int from = numberGroup.getLength();
                int to = from + 2;
                numberGroup.appendText(num);
                numberGroup.setStyle(from, to, NUMB_STYLE);
            }
        });

    }

    public void setFl(boolean flag){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                receiveFlag.setSelected(flag);
            }
        });
    }
    public void setFreq(float freq){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(receiveFlag.isSelected()){
                    int fr =  (int) freq;
                    freqReceive.setText(Integer.toString(fr));
                }
            }
        });
    }

}

