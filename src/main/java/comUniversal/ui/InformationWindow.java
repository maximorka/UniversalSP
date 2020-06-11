package comUniversal.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

//import javafx.css.Style;

public class InformationWindow {
    public static TextArea message100;

    @FXML
    private TextArea messageReceived100;
    int countSymbol=0;

    @FXML
    public void initialize() {

        System.out.println("initialize() information window");
        //txProgressBar = new ProgressBar();
        message100 = new TextArea();
        message100 = messageReceived100;
        messageReceived100.setEditable(true);
    }
    public void setTextMessage(int data) {

        String tmp = Integer.toString(data);
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
                Platform.runLater(() -> {
                    message100.appendText("\n");
                });
            }
        }
}

