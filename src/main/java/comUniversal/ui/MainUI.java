package comUniversal.ui;


import comUniversal.Core;
import comUniversal.util.Params;
import javafx.application.Platform;
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
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainUI {
    String oldTypeRx = "";
    @FXML
    private ChoiceBox typeRxChoicebox;

    @FXML
    private ChoiceBox typeTxChoicebox;
    @FXML
    private ChoiceBox modeWorkChoicebox;

    @FXML
    private Button connectButton;
    @FXML
    private ComboBox test;

    @FXML
    public void initialize() {

        ObservableList <String> typeRx = FXCollections.observableArrayList("УПС1","rty");
        typeRxChoicebox.setItems(typeRx);
//test.setItems(typeRx);
        ObservableList <String> typeTx = FXCollections.observableArrayList("УПС1");
        typeTxChoicebox.setItems(typeTx);

        ObservableList <String> typeMode = FXCollections.observableArrayList("Горизонт");
        modeWorkChoicebox.setItems(typeMode);

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

//        test.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                System.out.println("show");
//                stage.show();
//            }
//        });


//        typeRxChoicebox.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                System.out.println("show");
//                stage.show();
//            }
//        });

        typeRxChoicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("show11");
                stage.show();
                oldTypeRx = typeRxChoicebox.getValue().toString();

            }
        });

       

//        typeRxChoicebox.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//
//                if(typeRxChoicebox.getValue() == null ){
//
//                }else {
//                    if(!stage.isShowing()){
//                        System.out.println("show");
//                        stage.show();
//                    }
//
//                    oldTypeRx = typeRxChoicebox.getValue().toString();
//
//                }
//            }
//        });


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
                String ip = Params.SETTINGS.getString("ethernet-ip-address", "192.168.0.1");
                int port = 80;



                String con = "-fx-background-color: #00cd00";
                if (connectButton.getStyle() != con) {
                    if(Core.getCore().ethernetDriver.doInit(ip, port)){
                        Platform.runLater(()->{
                            connectButton.setText("Відключитись");
                            connectButton.setStyle("-fx-background-color: #00cd00");
                        });
                    }

                } else {
                    Core.getCore().ethernetDriver.closeSocket();
                    Platform.runLater(()-> {
                        connectButton.setText("Підключитись");
                        connectButton.setStyle("-fx-background-color: #c0ae9d");
                    });

                }


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
