package comUniversal.ui;

import comUniversal.Core;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InformationWindow {
    public static TextArea message;
    @FXML
    private TextField serviceMessage;
    @FXML
    private Button sendButton;
    @FXML
    private TextArea messageHistory;

    @FXML
    public void initialize() {
        System.out.println("initialize() information window");

        message = new TextArea();
        message = messageHistory;

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String txt = serviceMessage.getText();
                System.out.println("Fine");
                Core.getCore().groupAdd.add(txt);
            }
        });

    }

    public void setTextMessage(int data){
        String tmp = Integer.toString(data);
        message.appendText(tmp);
    }
}
