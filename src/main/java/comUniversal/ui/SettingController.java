package comUniversal.ui;
//
//import com.molot.lowlevel.rw.ReaderWriter;
//import com.molot.lowlevel.rw.ReaderWriterFactory;
//import com.molot.lowlevel.rw.data.input.byteprocessor.ByteDataProcessor;
//import com.molot.ui.setting.ParamsSettings;
//import com.molot.util.Params;

import comUniversal.Core;
import comUniversal.deviceLevel.Dev;
import comUniversal.ui.setting.RM;
import comUniversal.util.EthernetUtils;
import comUniversal.util.Params;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class SettingController{
private SettingsRMWindowUI settingsRMWindowUI;
    private SettingsTXWindowUI settingsTXWindowUI;
   private Dev dev;
    private TableColumn rxNameCol;
    private TableColumn rxIpCol;
    private TableColumn rxFreq;
    private TableColumn txNameCol;
    private TableColumn txIpCol;

    @FXML
    private Button addRxRm;
    @FXML
    private Button deleteItemRx;
    @FXML
    private Button addTxRm;
    @FXML
    private Button deleteItemTx;
    @FXML
    private Button editRxRm;
    @FXML
    private Button editTxRm;
    @FXML
    private TextField ipTextField;
    @FXML
    private Button connectButton;

    @FXML
    private Button changeSettingsIPbutton;

    @FXML
    private TableView<RM> tableRx;
    @FXML
    private TableView<RM> tableTx;
    ObservableList dataRx;
    ObservableList<RM> dataTx;

    @FXML
    public void initialize() {
        System.out.println("initialize() setting");

        rxNameCol = new TableColumn("Назва");
        rxNameCol.setMinWidth(130);
        rxNameCol.setCellValueFactory(new PropertyValueFactory<RM, String>("Name"));
        //rxNameCol.setCellFactory(TextFieldTableCell.<String> forTableColumn());

        //tableRx.setEditable(true);

        txNameCol = new TableColumn("Назва");
        txNameCol.setMinWidth(130);
        txNameCol.setCellValueFactory(new PropertyValueFactory<RM, String>("Name"));
        txNameCol.setCellFactory(TextFieldTableCell.<RM> forTableColumn());


        //tableTx.setEditable(true);

        addRxRm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/SettingsRM.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = new Stage();
                stage.setX(212);
                stage.setY(367);
                stage.setTitle("РМ");
                stage.setScene(scene);
                stage.show();
            }


        });
        deleteItemRx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int t = tableRx.getSelectionModel().getFocusedIndex();
                RM rm = tableRx.getSelectionModel().getSelectedItem();
                Params.RXRM.deleteKeyName(rm.getName());
                Params.RXRM.deleteKeyRMSetings(rm.getName());
                Params.RXRM.save();
                Core.getCore().mainUI.updateRmType();
                dataRx.remove(t);
            }
        });
        editRxRm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int t = tableRx.getSelectionModel().getFocusedIndex();
                RM rm = tableRx.getSelectionModel().getSelectedItem();

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/SettingsRM.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                settingsRMWindowUI = fxmlLoader.getController();
                Core.getCore().settingsRMWindowUI = settingsRMWindowUI;//Params.RXRM.deleteKeyName(rm.getName());

                Core.getCore().settingsRMWindowUI.setEdit(rm.getName());
                Stage stage = new Stage();
                stage.setX(212);
                stage.setY(90);
                stage.setTitle("РМ");
                stage.setScene(scene);

                stage.show();


            }
        });
        editTxRm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("EDIT");
                int t = tableTx.getSelectionModel().getFocusedIndex();
                RM rm = tableTx.getSelectionModel().getSelectedItem();

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/SettingsTX.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                settingsTXWindowUI = fxmlLoader.getController();
                Core.getCore().settingsTXWindowUI = settingsTXWindowUI;//Params.RXRM.deleteKeyName(rm.getName());

                Core.getCore().settingsTXWindowUI.setEditTx(rm.getName());
                Stage stage = new Stage();
                stage.setX(212);
                stage.setY(90);
                stage.setTitle("TX");
                stage.setScene(scene);

                stage.show();


            }
        });

        addTxRm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/SettingsTX.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = new Stage();
                stage.setX(212);
                stage.setY(367);
                stage.setTitle("РМ");
                stage.setScene(scene);
                stage.show();
            }
        });
        deleteItemTx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int t = tableTx.getSelectionModel().getFocusedIndex();
                dataTx.remove(t);
            }
        });
        ipTextField.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        testIP();
    }
});
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ConnectUnConnect();
            }
        });
        changeSettingsIPbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ipAddress = ipTextField.getText();
                String mask = "255.255.255.0";
                String Getaway = "192.168.0.0";
                int port = 81;

                byte[] ipBytes = EthernetUtils.ipToByteArray(ipAddress);
                byte[] maskBytes = EthernetUtils.ipToByteArray(mask);
                byte[] gatewayBytes = EthernetUtils.ipToByteArray(Getaway);
                int ip = ByteBuffer.wrap(ipBytes).getInt();
                int maskInt = ByteBuffer.wrap(maskBytes).getInt();
                int get = ByteBuffer.wrap(gatewayBytes).getInt();

                dev.driverHorizon.ethernetSet(ip,maskInt,port,get);
                changeSettingsIPbutton.setDisable(true);

                try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    changeSettingsIPbutton.setDisable(false);
                dev.disConect("type");
                Platform.runLater(()-> {
                    connectButton.setText("Підключитись");
                    connectButton.setStyle("-fx-background-color: #c0ae9d");
                    changeSettingsIPbutton.setVisible(false);


                });
//                if(Core.getCore().device[0].driverHorizon != null){
//                    Core.getCore().device[0].driverHorizon.ducSetMode(Mode.DISABLE);
//                    Core.getCore().device[0].driverHorizon.ddcSetMode(Mode.DISABLE);
//                    Core.getCore().device[0].kylymDecoder100.setRunning(false);
//                    Core.getCore().countConectedDevice = 0;
//                    Core.getCore().device[0].driverHorizon.ethernetSet(ip,maskInt,port,get);
//                    changeSettingsIPbutton.setDisable(true);
//
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }
//                    changeSettingsIPbutton.setDisable(false);
//                    Core.getCore().device[0].bufferController.setWorkingThread(false);
//                    Core.getCore().device[0].ethernetDriver.closeSocket();
//                    Core.getCore().mainUI.setConnectButton();
//                }else{
//                    Core.getCore().device[1].driverHorizon.ducSetMode(Mode.DISABLE);
//                    Core.getCore().device[1].driverHorizon.ddcSetMode(Mode.DISABLE);
//                    Core.getCore().device[1].kylymDecoder100.setRunning(false);
//
//                    //Core.getCore().device[0].driverHorizon.ducSetMode(Mode.DISABLE);
//
//                    Core.getCore().countConectedDevice = 0;
//                    Core.getCore().device[1].driverHorizon.ethernetSet(ip,maskInt,port,get);
//                    changeSettingsIPbutton.setDisable(true);
//
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }
//                    changeSettingsIPbutton.setDisable(false);
//                    //Core.getCore().device[0].bufferController.setWorkingThread(false);
//
//                    //Core.getCore().device[0].ethernetDriver.closeSocket();
//                    Core.getCore().device[1].ethernetDriver.closeSocket();
//                    Core.getCore().mainUI.setConnectButton();
//                }



            }
        });

        updateSettingsRxRmTable();
        updateSettingsTxRmTable();
    }

    private void updateSettingsRxRmTable(){

        ObservableList<String> data = Params.RXRM.getKeyName();
        dataRx = FXCollections.observableArrayList();
        for (int i = 0; i <data.size() ; i++) {
            String p = new String(Params.RXRM.getStringName(i));
            dataRx.add(new RM(p));
        }

        tableRx.setItems(dataRx);
        tableRx.getColumns().addAll(rxNameCol);
    }
    public void updateSettingsRx(){

        ObservableList<String> data = Params.RXRM.getKeyName();
        dataRx = FXCollections.observableArrayList();
        for (int i = 0; i <data.size() ; i++) {
            String p = new String(Params.RXRM.getStringName(i));
            dataRx.add(new RM(p));
        }
        tableRx.setItems(dataRx);

    }
    private void updateSettingsTxRmTable(){
        ObservableList<String> data = Params.TXRM.getKeyName();
        dataTx = FXCollections.observableArrayList();
        for (int i = 0; i <data.size() ; i++) {
            String p = new String(Params.TXRM.getStringName(i));
            dataTx.add(new RM(p));
        }

        tableTx.setItems(dataTx);
        tableTx.getColumns().addAll(txNameCol);
    }
    public void updateSettingsTx(){

        ObservableList<String> data = Params.TXRM.getKeyName();
        dataTx = FXCollections.observableArrayList();
        for (int i = 0; i <data.size() ; i++) {
            String p = new String(Params.TXRM.getStringName(i));
            dataTx.add(new RM(p));
        }
        tableTx.setItems(dataTx);

    }
    private void updateJsonRx(){
        int size = tableRx.getItems().size();
//        ObservableList<RM> data = tableRx.getItems();
//        Params.RXRM.deleteAll();
//        for (int i = 0; i <size ; i++) {
//            Params.RXRM.putStringRMName(data.get(i).getName());
//            Params.RXRM.putStringRMSettings(data.get(i).getName(), data.get(i).getIp(),"128003");
//        }

    }
    private void updateJsonTx(){
        int size = tableTx.getItems().size();
        ObservableList<RM> data = tableTx.getItems();
        Params.TXRM.deleteAll();
        for (int i = 0; i <size ; i++) {
            //Params.TXRM.putString(data.get(i).getName());
        }

    }
    public void testIP() {
        try {
            String ipAddress = ipTextField.getText();
            InetAddress inet = InetAddress.getByName(ipAddress);
            if (inet.isReachable(500)) {
                ipTextField.setStyle("-fx-text-fill: green;");
            } else {
                ipTextField.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }
    private void ConnectUnConnect() {
        String con = "-fx-background-color: #00cd00";
        if (connectButton.getStyle() != con) {
            connectToEthernet();
            Platform.runLater(()->{
                connectButton.setText("Відключитись");
                connectButton.setStyle("-fx-background-color: #00cd00");
                changeSettingsIPbutton.setVisible(true);


            });

        } else {
            dev.disConect("typeDev");
            Platform.runLater(()-> {
                connectButton.setText("Підключитись");
                connectButton.setStyle("-fx-background-color: #c0ae9d");
                changeSettingsIPbutton.setVisible(false);


            });
        }
    }
    private void connectToEthernet() {
        try {
            String ip = ipTextField.getText();
            int port = 81;
            System.out.println(ip);
            dev = new Dev("settings");
            boolean con = dev.conect(ip, port);

            if (!con) {
                 JOptionPane.showMessageDialog(null, "Не вдається під'єднатись по вказаній IP адресі");
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            //JOptionPane.showMessageDialog(null, "Не вдається під'єднатись!");
        }
    }
    public void shutdown() {
//        if (horizonDevice != null && horizonDevice.isConnected()) {
//            horizonDevice.close();
//        }
//        saveAll(Params.SETTINGS);
        //updateJsonRx();
       // updateJsonTx();
///commit commit
        Params.RXRM.save();
        Params.TXRM.save();
       // Core.getCore().receiverUPSWindowUI.updateRm();
        System.out.println("Stop setting");
    }
    private void inf(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(text);
        alert.setHeaderText(null);
        alert.setContentText("Значення");
        alert.showAndWait();
    }

//    @Override
//    public void saveAll(Params params) {
//        String typeRx = typeRxChoiceBox.getValue().toString();
//        Params.SETTINGS.putString("type_rx", typeRx);
//
//        String typeTx = typeTxChoiceBox.getValue().toString();
//        Params.SETTINGS.putString("type_tx", typeTx);
//
//        //Save ethernet params 1
//        Params.SETTINGS.putString("ethernet_ip", textFieldIP.getText());
//        Params.SETTINGS.putString("ethernet_port", textFieldPort.getText());
//    }
//
//    @Override
//    public void restoreAll(Params params) {
//
//        //Restore Ethernet params 1
//        textFieldIP.setText(Params.SETTINGS.getString("ethernet-ip-address", "192.168.0.1"));
//        textFieldPort.setText(Params.SETTINGS.getString("ethernet-port", "80"));
//
//        //Restore RX TX type device
//        typeRxChoiceBox.setValue(Params.SETTINGS.getString("type_rx", "P160"));
//        typeTxChoiceBox.setValue(Params.SETTINGS.getString("type_tx", "P160"));
//
//        //Restore RX TX freq
////        freqRxTextField.setText(Params.SETTINGS.getString("rx_freq", "128000"));
//       // freqTxTextField.setText(Params.SETTINGS.getString("tx_freq", "128000"));
//
//
//
//        //Restore bit inversion
////        rxInverse.setSelected(Params.SETTINGS.getBoolean("rx_inverse", true));
////        txInverse.setSelected(Params.SETTINGS.getBoolean("tx_inverse", false));
////
////        //Save check virial device
////        checkVirial.setSelected(Params.SETTINGS.getBoolean("checkVirial", false));
//
//        //Restore work speed
//        //WorkSpeed workSpeed = WorkSpeed.valueOf (Params.SETTINGS.getString("modem_speed", WorkSpeed.speed250.name()));
//        //	speedCombobox.setSelectedItem(workSpeed);
//    }
}
