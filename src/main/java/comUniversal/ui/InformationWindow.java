package comUniversal.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InformationWindow {
    @FXML
    private TextField serviceMessage;
    @FXML
    private Button sendButton;

    @FXML
    public void initialize() {
        System.out.println("initialize() information window");

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String txt = serviceMessage.getText();
                System.out.println("Fine");
                //Core.getCore().groupAdd.add(txt);
            }
        });

    }
}
