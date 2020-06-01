package comUniversal.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

//import javafx.css.Style;

public class InformationWindow {
    public static TextArea message100;
    public static TextArea message250;

    @FXML
    private TextArea messageReceived100;
    @FXML
    private TextArea messageReceived250;
    @FXML
    private ScrollPane pane;
    @FXML
    private TextArea qwerty;
    int countSymbol=0;
    int countSymbol250=0;
TextArea textArea;
String text;
    @FXML
    public void initialize() {

        System.out.println("initialize() information window");
        //txProgressBar = new ProgressBar();
        message100 = new TextArea();
        message100 = messageReceived100;
        message250 = new TextArea();
        message250 = messageReceived250;


        messageReceived100.setEditable(false);
        messageReceived250.setEditable(false);

       
    }


    public void setTextMessage(int data, int speed) {
        String newLine = System.getProperty("line.separator");
        String tmp = Integer.toString(data);
        if (speed == 100) {
            if (tmp.equals("10")) {
                Platform.runLater(() -> {
                    message100.appendText("*");
                });
            } else {
                Platform.runLater(() -> {
                    message100.appendText(tmp);
                });
            }

            countSymbol++;
            if (countSymbol % 5 == 0) {
                Platform.runLater(() -> {
                    message100.appendText(" ");
                });

            }
            if (countSymbol % 50 == 0) {
                //int countgroup = countSymbol/5;
                Platform.runLater(() -> {
                    message100.appendText("\n");
                });
            }
        }
        if (speed == 250) {
            if (tmp.equals("10")) {
                Platform.runLater(() -> {
                    message250.appendText("*");
                });
            } else {
                Platform.runLater(() -> {
                    message250.appendText(tmp);
                });
            }

            countSymbol250++;
            if (countSymbol250 % 5 == 0) {
                Platform.runLater(() -> {
                    message250.appendText(" ");
                });

            }
            if (countSymbol250 % 50 == 0) {
                //int countgroup = countSymbol/5;
                Platform.runLater(() -> {
                    message250.appendText("\n");
                });
            }
        }
    }

}

