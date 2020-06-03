package comUniversal.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

//import javafx.css.Style;

public class InformationMolotWindow {
    public static TextArea message;
    public static TextArea message250;

    @FXML
    private TextArea messageReceived;

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
        message = new TextArea();
        message = messageReceived;

        messageReceived.setEditable(false);
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

}

