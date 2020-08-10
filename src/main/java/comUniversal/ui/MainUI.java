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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainUI {
    Stage stageProg = new Stage();
    Stage stageRx = new Stage();
    Stage stageTx = new Stage();
    public static Button connect;
    public static ChoiceBox modeWork;
    InformationWindow informationWindow;
    ReceiverUPSWindowUI receiverUPSWindowUI;
    TransmitterUPSWindowUI transmitterUPSWindowUI;
    SettingController settingController;
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
    private Button set;

    @FXML
    public void initialize() {

        connect = new Button();
        connect =  connectButton;
        connectButton.setDisable(true);

        modeWork =new ChoiceBox();
        modeWork = modeWorkChoicebox;


        rxLabel.setDisable(true);
        typeRxChoicebox.setDisable(true);

        txLabel.setDisable(true);
        typeTxChoicebox.setDisable(true);

        ObservableList<String> langsRx = FXCollections.observableArrayList();
        langsRx = Params.RXRM.getKeyName();
        typeRxChoicebox.setItems(langsRx);


        ObservableList<String> langsTx = FXCollections.observableArrayList();
        langsTx = Params.TXRM.getKeyName();
        typeTxChoicebox.setItems(langsTx);

        ObservableList <String> typeMode = FXCollections.observableArrayList("�����","C����");
        modeWorkChoicebox.setItems(typeMode);

        typeRxChoicebox.getSelectionModel().selectFirst();
        typeTxChoicebox.getSelectionModel().selectFirst();

        modeWorkChoicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(modeWorkChoicebox.getValue() == "�����" ){

                    stageTx.close();
                    txLabel.setDisable(true);
                    typeTxChoicebox.setDisable(true);
                    typeTxChoicebox.getSelectionModel().selectFirst();
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
                    stageProg.setX(443);
                    stageProg.setY(90);
                    stageProg.setTitle("����� - ��� �����");
                    stageProg.setScene(scene);
                    stageProg.show();
                    rxLabel.setDisable(false);
                    typeRxChoicebox.setDisable(false);
                   informationWindow = fxmlLoader.getController();
                }else if(modeWorkChoicebox.getValue() == "�����"){
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/InformationMolot.fxml"));

                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Stage stage = new Stage();
                    stageProg.setX(443);
                    stageProg.setY(90);
                    stageProg.setTitle("�����");
                    stageProg.setScene(scene);
                    stageProg.show();
                    rxLabel.setDisable(false);
                    typeRxChoicebox.setDisable(false);

                    txLabel.setDisable(false);
                    typeTxChoicebox.setDisable(false);
                }
            }
        });
        typeRxChoicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(typeRxChoicebox.getValue() != null) {

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
                    stageRx.setY(90);
                    stageRx.setTitle("RX");
                    stageRx.setScene(scene);
                    stageRx.show();

                        Core.getCore().createClassKylym();



                    connectButton.setDisable(false);
                    receiverUPSWindowUI = fxmlLoader.getController();
                    Core.getCore().informationWindow = informationWindow;
                    Core.getCore().receiverUPSWindowUI = receiverUPSWindowUI;
                }else if(typeRxChoicebox.getValue() == "³������"){
                    stageRx.close();
                    if(typeRxChoicebox.getValue() == "³������" && typeTxChoicebox.getValue() == "³������"){
                        connectButton.setDisable(true);
                    }
                }
            }
        });

        typeTxChoicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(typeTxChoicebox.getValue() == "��������" || typeTxChoicebox.getValue() == "��������+" ) {

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
                    stageTx.setTitle("TX");
                    stageTx.setScene(scene);
                    stageTx.show();
                    connectButton.setDisable(false);


                }else if(typeTxChoicebox.getValue() == "³������"){
                    stageTx.close();
                    if(typeRxChoicebox.getValue() == "³������" && typeTxChoicebox.getValue() == "³������"){
                        connectButton.setDisable(true);
                    }
                }

            }
        });

        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String con = "-fx-background-color: #00cd00";
                String typeRx = Core.getCore().receiverUPSWindowUI.getrxType();
                if (connectButton.getStyle() != con) {

                   if( Core.getCore().dev.conect(typeRx)) {
                        Platform.runLater(() -> {
                            connectButton.setText("³����������");
                            connectButton.setStyle("-fx-background-color: #00cd00");
                            rxLabel.setDisable(true);
                            txLabel.setDisable(true);
                            typeProgramLabel.setDisable(true);
                            modeWorkChoicebox.setDisable(true);
                            set.setDisable(true);
                            typeRxChoicebox.setDisable(true);
                            typeTxChoicebox.setDisable(true);
//                            Core.getCore().transmitterUPSWindowUI.updateVisibility(false);
                            Core.getCore().receiverUPSWindowUI.updateVisibility(false);
                        });
                 }
               } else {
//
//                    Core.getCore().setDriverConnect(false, "","",typeProgram);

                    Core.getCore().dev.disConect("kylym");
                    Platform.runLater(()-> {
                        connectButton.setText("ϳ����������");
                        connectButton.setStyle("-fx-background-color: #c0ae9d");
                        rxLabel.setDisable(false);
                        txLabel.setDisable(false);
                        typeProgramLabel.setDisable(false);
                        modeWorkChoicebox.setDisable(false);
                        typeRxChoicebox.setDisable(false);
                        set.setDisable(false);
                        Core.getCore().transmitterUPSWindowUI.updateVisibility(true);
                        Core.getCore().receiverUPSWindowUI.updateVisibility(true);
                    });
               }
            }
        });

    }

public void setConnectButton(){
    Platform.runLater(()-> {
        connect.setText("ϳ����������");
        connect.setStyle("-fx-background-color: #c0ae9d");
    });
}

    public void settings(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Setting.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        settingController = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setX(212);
        stage.setY(90);
        stage.setTitle("������������");
        stage.setScene(scene);
        stage.setOnHidden(event -> settingController.shutdown());
        stage.show();


        Core.getCore().settingController = settingController;

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
    public void updateRmType() {
        ObservableList<String> langsRx = FXCollections.observableArrayList();
        langsRx = Params.RXRM.getKeyName();
        System.out.println(langsRx);
        typeRxChoicebox.setItems(langsRx);
    }
    public void updateTxType() {
        ObservableList<String> langsTx = FXCollections.observableArrayList();
        langsTx = Params.TXRM.getKeyName();
        System.out.println(langsTx);
        typeTxChoicebox.setItems(langsTx);
    }
    public String getTypeRx() {
        return typeRxChoicebox.getValue().toString();
    }

    private void inf(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(text);
        alert.setHeaderText(null);
        alert.setContentText("�� �������� ��������");
        alert.showAndWait();
    }

    private void inf2() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(" ");
        alert.setHeaderText(null);
        alert.setContentText("������ ��������");
        alert.showAndWait();
    }


}
