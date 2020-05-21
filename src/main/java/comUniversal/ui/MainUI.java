package comUniversal.ui;


import comUniversal.Core;
import comUniversal.lowLevel.DriverHorizon.Mode;
import comUniversal.lowLevel.DriverHorizon.Width;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainUI {
    Stage stageRx = new Stage();
    Stage stageTx = new Stage();
    public static Button connect;


    @FXML
    private Label rxLabel;
    @FXML
    private ChoiceBox typeRxChoicebox;
    @FXML
    private Label txLabel;
    @FXML
    private Label typeProgramLabel;
    @FXML
    private ChoiceBox typeTxChoicebox;
    @FXML
    private ChoiceBox modeWorkChoicebox;

    @FXML
    private Button connectButton;


    @FXML
    public void initialize() {

        connect = new Button();
        connect =  connectButton;
        connectButton.setDisable(true);
        modeWorkChoicebox.setDisable(true);
        typeProgramLabel.setDisable(true);

        ObservableList <String> typeRx = FXCollections.observableArrayList("Відсутній","Горизонт");
        typeRxChoicebox.setItems(typeRx);

        ObservableList <String> typeTx = FXCollections.observableArrayList("Відсутній","Горизонт");
        typeTxChoicebox.setItems(typeTx);

        ObservableList <String> typeMode = FXCollections.observableArrayList("Килим");
        modeWorkChoicebox.setItems(typeMode);

        typeRxChoicebox.getSelectionModel().selectFirst();
        typeTxChoicebox.getSelectionModel().selectFirst();

        modeWorkChoicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(modeWorkChoicebox.getValue() == "Килим" ){
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/Information.fxml"));
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
                    stage.setX(447);
                    stage.setY(112);
                    stage.setTitle("Килим");
                    stage.setScene(scene);
                    stage.show();
                    connectButton.setDisable(false);
                }
            }
        });
        typeRxChoicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(typeRxChoicebox.getValue() == "Горизонт") {

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/ReceiverUPSWindow.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stageRx.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            windowEvent.consume();
                        }
                    });
                    stageRx.setX(212);
                    stageRx.setY(112);
                    stageRx.setTitle("Приймач");
                    stageRx.setScene(scene);
                    stageRx.show();
                    typeProgramLabel.setDisable(false);
                    modeWorkChoicebox.setDisable(false);
                }else if(typeRxChoicebox.getValue() == "Відсутній"){
                    stageRx.close();
                    if(typeRxChoicebox.getValue() == "Відсутній" && typeTxChoicebox.getValue() == "Відсутній"){
                        typeProgramLabel.setDisable(true);
                    }
                }
            }
        });

        typeTxChoicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(typeTxChoicebox.getValue() == "Горизонт") {

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/TransmiterUPSWindow.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stageTx.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            windowEvent.consume();
                        }
                    });
                    stageTx.setX(212);
                    stageTx.setY(367);
                    stageTx.setTitle("Передавач");
                    stageTx.setScene(scene);
                    stageTx.show();
                   typeProgramLabel.setDisable(false);
                    modeWorkChoicebox.setDisable(false);

                }else if(typeTxChoicebox.getValue() == "Відсутній"){
                    stageTx.close();
                    if(typeRxChoicebox.getValue() == "Відсутній" && typeTxChoicebox.getValue() == "Відсутній"){
                        typeProgramLabel.setDisable(true);
                    }
                }

            }
        });

        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String ip = "";
                String ipRx = Core.getCore().receiverUPSWindowUI.getIP();
                String ipTx = Core.getCore().transmitterUPSWindowUI.getIP();

                if (ipRx.equals(ipTx)){
                    ip = ipRx;
                }

                int port = Integer.parseInt(Params.SETTINGS.getString("ethernet_port", "80"));


                String con = "-fx-background-color: #00cd00";
                if (connectButton.getStyle() != con) {
                    if(Core.getCore().ethernetDriver.doInit(ip, port)){
                        Platform.runLater(()->{
                            connectButton.setText("Відключитись");
                            connectButton.setStyle("-fx-background-color: #00cd00");
                            rxLabel.setDisable(true);
                            txLabel.setDisable(true);
                            typeProgramLabel.setDisable(true);
                            modeWorkChoicebox.setDisable(true);
                            typeRxChoicebox.setDisable(true);
                            typeTxChoicebox.setDisable(true);
                            Core.getCore().transmitterUPSWindowUI.updateVisibility(false);
                            Core.getCore().receiverUPSWindowUI.updateVisibility(false);
                        });
                        Core.getCore().driverHorizon.ducSetWidth(Width.kHz_3);
                        Core.getCore().driverHorizon.ddcSetWidth(Width.kHz_3);

                        Core.getCore().driverHorizon.ducSetMode(Mode.ENABLE);
                        Core.getCore().driverHorizon.ddcSetMode(Mode.ENABLE);

                    }

                } else {
                    Core.getCore().ethernetDriver.closeSocket();
                    Platform.runLater(()-> {
                        connectButton.setText("Підключитись");
                        connectButton.setStyle("-fx-background-color: #c0ae9d");
                        rxLabel.setDisable(false);
                        txLabel.setDisable(false);
                        typeProgramLabel.setDisable(false);
                        modeWorkChoicebox.setDisable(false);
                        typeRxChoicebox.setDisable(false);
                        typeTxChoicebox.setDisable(false);
                        Core.getCore().transmitterUPSWindowUI.updateVisibility(true);
                        Core.getCore().receiverUPSWindowUI.updateVisibility(true);
                    });

                }


            }
        });

        Core.getCore().setRunning(true);

    }

public void setConnectButton(){
    Platform.runLater(()-> {
        connect.setText("Підключитись");
        connect.setStyle("-fx-background-color: #c0ae9d");
    });
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
