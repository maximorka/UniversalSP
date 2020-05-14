package comUniversal.ui;


import comUniversal.Core;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainUI {
    @FXML
    private ChoiceBox typeRxChoicebox;

    @FXML
    private ChoiceBox typeTxChoicebox;
    @FXML
    private ChoiceBox modeWorkChoicebox;

    @FXML
    private Button connectButton;

    @FXML
    public void initialize() {
        ObservableList <String> typeRx = FXCollections.observableArrayList("УПС1");
        typeRxChoicebox.setItems(typeRx);

        ObservableList <String> typeTx = FXCollections.observableArrayList("УПС1");
        typeTxChoicebox.setItems(typeTx);

        ObservableList <String> typeMode = FXCollections.observableArrayList("Горизонт");
        modeWorkChoicebox.setItems(typeMode);

        typeRxChoicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ReceiverUPSWindow.fxml"));
                /*
                 * if "fx:controller" is not set in fxml
                 * fxmlLoader.setController(NewWindowController);
                 */
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Receiver UPS");
                stage.setScene(scene);
                stage.show();
            }
        });

        typeTxChoicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/TransmiterUPSWindow.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Transmiter UPS");
                stage.setScene(scene);
                stage.show();
            }
        });
//        ipTextField.setText("192.168.0.1");
        //portTextField.setText("81");
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                int port = Integer.parseInt(portTextField.getText());
//
//                String con = "-fx-background-color: #00cd00";
//                if (connectButton.getStyle() != con) {
//                    if(Core.getCore().ethernetDriver.doInit(ipTextField.getText(), port)){
//                        Platform.runLater(()->{
//                            connectButton.setText("Відключитись");
//                            connectButton.setStyle("-fx-background-color: #00cd00");
//                            ipTextField.setDisable(true);
//                            portTextField.setDisable(true);
//
//                        });
//                    }
//
//                } else {
//                    Core.getCore().ethernetDriver.closeSocket();
//                    Platform.runLater(()-> {
//                        connectButton.setText("Підключитись");
//                        connectButton.setStyle("-fx-background-color: #c0ae9d");
//                        ipTextField.setDisable(false);
//                        portTextField.setDisable(false);
//                    });
//
//                }


            }
        });

        Core.getCore().setRunning(true);

    }




    class UpdateUIThread extends Thread {
        @Override
        public void run() {
            while (true) {
//                if (Core.isReady()) {
//
//                    updateMessages();
//                    updatePacketStats();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
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
