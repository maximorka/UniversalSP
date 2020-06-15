package comUniversal.ui;
//
//import com.molot.lowlevel.rw.ReaderWriter;
//import com.molot.lowlevel.rw.ReaderWriterFactory;
//import com.molot.lowlevel.rw.data.input.byteprocessor.ByteDataProcessor;
//import com.molot.ui.setting.ParamsSettings;
//import com.molot.util.Params;

import comUniversal.Core;
import comUniversal.deviceLevel.Dev;
import comUniversal.util.EthernetUtils;
import comUniversal.util.Params;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class SettingController{

   private Dev dev;

    @FXML
    private TextField rxRmTextField;
    @FXML
    private TextField ipRxTextField;
    @FXML
    private TextField txRmTextField;
    @FXML
    private TextField ipTxTextField;
    @FXML
    private Button addRxRm;
    @FXML
    private Button deleteItemRx;
    @FXML
    private Button addTxRm;
    @FXML
    private Button deleteItemTx;


    @FXML
    private TextField ipTextField;
    @FXML
    private Button connectButton;

    @FXML
    private Button changeSettingsIPbutton;

    @FXML
    private TableView table;


    @FXML
    public void initialize() {
        System.out.println("initialize() setting");
        // столбец для вывода имени

//          ObservableList<Person> data =
//                FXCollections.observableArrayList(
//                        new Person("Jacob", "Smith", "jacob.smith@example.com"),
//                        new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
//                        new Person("Ethan", "Williams", "ethan.williams@example.com"),
//                        new Person("Emma", "Jones", "emma.jones@example.com"),
//                        new Person("Michael", "Brown", "michael.brown@example.com")
//                );

        TableColumn NameCol = new TableColumn("Name");
        NameCol.setMinWidth(100);
        NameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("Name"));

        TableColumn ipCol = new TableColumn("IP");
        ipCol.setMinWidth(100);
        ipCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("ip"));

        TableColumn ipCol1 = new TableColumn("IP1");
        ipCol1.setMinWidth(100);
        ipCol1.setCellValueFactory(
                new PropertyValueFactory<Person, String>("ip1"));

        ObservableList<String> data = Params.RXRM.getKey();
        ObservableList<String> dataIp = FXCollections.observableArrayList();

        for (int i = 0; i <data.size() ; i++) {
            String p = new String(Params.RXRM.getString(data.get(i)));
            dataIp.add(p);
        }

        ObservableList<Person> data1 = FXCollections.observableArrayList();
        for (int i = 0; i <data.size() ; i++) {
            Person p = new Person(data.get(i), dataIp.get(i));
            data1.add(p);
        }

        table.setItems(data1);

        table.getColumns().addAll(NameCol, ipCol);
//        TableColumn<RM, SimpleStringProperty> nameColumn = new TableColumn<RM, SimpleStringProperty >("RM");
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        // столбец для вывода возраста
//        TableColumn<RM, SimpleStringProperty > ipColumn = new TableColumn<RM, SimpleStringProperty >("IP");
//        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
//
//
//
//        ObservableList<RM> langs = getUserList();
//        //langs = Params.RXRM.getKey();
//
//        Platform.runLater(()->{
//            table.setItems(langs);
//
//            table.getColumns().addAll(nameColumn, ipColumn);
//
//        });

        addRxRm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String rxRm = rxRmTextField.getText();
                String rxRmIP = ipRxTextField.getText();
                Params.RXRM.putString(rxRm, rxRmIP);
                if(Core.getCore().receiverUPSWindowUI!=null){
                    Core.getCore().receiverUPSWindowUI.updateRm();
                }

            }
        });

        deleteItemRx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String rxRm = rxRmTextField.getText();
                Params.RXRM.deleteKey(rxRm);
                Core.getCore().receiverUPSWindowUI.deleteItemRm(rxRm);
            }
        });

        addTxRm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String txRm = txRmTextField.getText();
                String txRmIP = ipTxTextField.getText();
                Params.TXRM.putString(txRm, txRmIP);

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
    }

//    private ObservableList<RM> getUserList() {
//
//        RM user1 = new RM("aweq", "234234");
//        RM user2 = new RM( "auytuweq", "23423423");
//        RM user3 = new RM( "awtyuteq", "23423");
//        ObservableList<RM> list = FXCollections.observableArrayList(user1, user2,user3);
//        return list;
//    }
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
        Params.RXRM.save();
        Params.TXRM.save();
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
