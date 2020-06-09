package comUniversal.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;
import org.fxmisc.richtext.InlineCssTextArea;
//import javafx.css.Style;

public class InformationWindow {
    public static InlineCssTextArea message;
    public static InlineCssTextArea numberGroup;

    @FXML
    private TextArea messageReceived100;
    @FXML
    private TextFlow m;
    @FXML
    private Button q;
    @FXML
    private Pane oldPane;
    @FXML
    private Pane newPane;
    @FXML
    private TextArea qwerty;
    int countSymbol=0;

    public InlineCssTextArea special;
    public InlineCssTextArea number;

    private static final String ERROR_STYLE = "-fx-fill: red; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String WARN_STYLE = "-fx-fill: black; -fx-font-family: Consolas; -fx-font-size: 14;";
    private static final String NUMB_STYLE = "-fx-fill: blue; -fx-font-family: Consolas; -fx-font-size: 14;";

    @FXML
    public void initialize() {


        System.out.println("initialize() information window");
        //txProgressBar = new ProgressBar();
        message = new InlineCssTextArea();
        message = special;
        numberGroup = new InlineCssTextArea();
        numberGroup = number;



//        messageReceived100.setEditable(true);

    }

    public void setTextMessage(int data) {

        String tmp = String.valueOf(data);

        if (tmp.equals("10")) {
            Platform.runLater(() -> {

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
                // получаем границы нового сообещения
                int from = message.getLength();
                int to = from + 1;
                // добавили сообещние
                message.appendText(tmp);
                // указали для него стиль
                message.setStyle(from, to, WARN_STYLE);
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
            });

        }
        Platform.runLater(() -> {

           if( message.getCurrentLineEndInParargraph()%60==0){
               String num = String.valueOf(message.getCurrentLineEndInParargraph()/6);
               if(num.equals(10)){
                   numberGroup.deleteText(0,message.getCurrentLineEndInParargraph());
               }
               int from = numberGroup.getLength();
               int to = from + 2;
               numberGroup.appendText(num);
               numberGroup.setStyle(from, to, NUMB_STYLE);
           }
        });

    }

}

