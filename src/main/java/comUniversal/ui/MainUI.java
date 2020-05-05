package comUniversal.ui;


import comUniversal.Core;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainUI {
    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private Button connectButton;

    @FXML
    public void initialize() {
        ipTextField.setText("192.168.0.1");
        portTextField.setText("80");
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int port = Integer.parseInt(portTextField.getText());




                String con = "-fx-background-color: #00cd00";
                if (connectButton.getStyle() != con) {
                    Core.getInstance().ethernetDriver.doInit(ipTextField.getText(), port);
                    Platform.runLater(()->{
                        connectButton.setText("Відключитись");
                        connectButton.setStyle("-fx-background-color: #00cd00");

                    });

                } else {
                    Core.getInstance().ethernetDriver.closeSocket();
                    Platform.runLater(()-> {
                        connectButton.setText("Підключитись");
                        connectButton.setStyle("-fx-background-color: #c0ae9d");
                    });

                }


            }
        });

    }




    class UpdateUIThread extends Thread {
        @Override
        public void run() {
         //   while (running) {
//                if (Core.isReady()) {
//
//                    updateMessages();
//                    updatePacketStats();
         //   }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }


    private void inf(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(text);
        alert.setHeaderText(null);
        alert.setContentText("Не коректне значення");
        alert.showAndWait();
    }

    private void inf2() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(" ");
        alert.setHeaderText(null);
        alert.setContentText("Введіть значення");
        alert.showAndWait();
    }


}
