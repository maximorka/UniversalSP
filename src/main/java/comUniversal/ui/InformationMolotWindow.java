

package comUniversal.ui;

import comUniversal.Core;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//import javafx.css.Style;

public class InformationMolotWindow {

    public static Button sendB;
    public static ProgressBar progressBar;
    public static TextArea message;

    int countSymbol=0;


    @FXML
    private TextField time;
    @FXML
    private TextField corNumberTextField;
    @FXML
    private TextField corKoefTextField;

    @FXML
    private TextField freq1TextField;
    @FXML
    private TextField freq2TextField;
    @FXML
    private TextField freq3TextField;
    @FXML
    private TextField freq4TextField;
    @FXML
    private TextField command1TextField;
    @FXML
    private TextField command2TextField;
    @FXML
    private TextField command3TextField;
    @FXML
    private TextField command4TextField;
    @FXML
    private TextField dataSeansTextField;
    @FXML
    private TextField timeSeansTextField;
    @FXML
    private TextField f1SeansTextField;
    @FXML
    private TextField f2SeansTextField;

    @FXML
    private TextField urlRgTextField;


    @FXML
    private ComboBox modeRxComboBox;
    @FXML
    private ComboBox speedTxComboBox;
    @FXML
    private ComboBox timeRepeatTxComboBox;
    @FXML
    private ComboBox countRepeatTxComboBox;

    @FXML
    private Button testButton;
    @FXML
    private Button majorButton;
    @FXML
    private Button saveRgButton;
        @FXML
    private Button sendButton;
    @FXML
    private Button stopTxButton;
    @FXML
    private Button choiceTxRgButton;

    @FXML
    private CheckBox cycleTxCheckBox;

    @FXML
    private TextArea messageReceived;
    @FXML
    private TextArea txRgTextArea;

    @FXML
    private ListView serviceMessageTxList;

    @FXML
    public TabPane tab;
    @FXML
    public AnchorPane addSeans;

    @FXML
    public CheckBox check1Freq;
    @FXML
    public CheckBox check2Freq;
    @FXML
    public CheckBox check3Freq;
    @FXML
    public CheckBox check4Freq;

    @FXML
    private Label timeRepeatLabel;
    @FXML
    private Label countRepeatLabel;
    @FXML
    private ProgressBar txProgressBar;

    @FXML
    public void initialize() {
        TimeTh timeT = new TimeTh();
        timeT.start();
        System.out.println("initialize() information window");

        majorButton.setDisable(true);
        saveRgButton.setDisable(true);
        modeRxComboBox.setDisable(true);
        testButton.setDisable(true);
        speedTxComboBox.setDisable(true);
        //timeTxComboBox.setDisable(true);
        choiceTxRgButton.setDisable(true);
        serviceMessageTxList.setDisable(true);
        //stopTxButton.setDisable(true);
        urlRgTextField.setDisable(true);

        ObservableList <String> mode = FXCollections.observableArrayList("Іркут","5ЧТ20");
        modeRxComboBox.setItems(mode);
        modeRxComboBox.setValue("Виберіть");
        ObservableList <String> speedTx = FXCollections.observableArrayList("100","250");
        speedTxComboBox.setItems(speedTx);
        speedTxComboBox.setValue("Виберіть");

        ObservableList <String> timeRepeatTx = FXCollections.observableArrayList("1","2","3");
        timeRepeatTxComboBox.setItems(timeRepeatTx);
        ObservableList <String> countTx = FXCollections.observableArrayList("1","2","3");
        countRepeatTxComboBox.setItems(countTx);

        ObservableList <String> serviceMessageTx = FXCollections.observableArrayList("ПОВТОРІТЬ","ЗАЧЕКАЙТЕ","ДОДАТКОВИЙ СЕАНС");
        serviceMessageTxList.setItems(serviceMessageTx);

        message = new TextArea();
        message = messageReceived;

        progressBar = new ProgressBar();
        progressBar = txProgressBar;
        sendB = new Button();
        sendB = sendButton;

        addSeans.setVisible(false);
        //timeRepeatTxComboBox.setDisable(false);
        //timeRepeatLabel.setDisable(false);
        //countRepeatTxComboBox.setDisable(true);
        countRepeatTxComboBox.setVisible(false);
        //countRepeatLabel.setDisable(true);
        countRepeatLabel.setVisible(false);
        timeRepeatTxComboBox.setValue("Виберіть");
//        messageReceived.setEditable(false);
        corNumberTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (checkMather(corNumberTextField, "\\d{3}")) {
                    corNumberTextField.setStyle("-fx-control-inner-background: green");
                }else  {
                    corNumberTextField.setStyle("-fx-control-inner-background: white");
                    if(!checkMather(corNumberTextField, "\\d+") || (!checkMather(corNumberTextField, "\\d{0,3}"))){
                        if(corNumberTextField.getText ().length()!=0) {
                            corNumberTextField.setText("" + corNumberTextField.getText().substring(0, corNumberTextField.getText().length() - 1));
                            corNumberTextField.positionCaret(3);
                            if (checkMather(corNumberTextField, "\\d{3}")) {
                                corNumberTextField.setStyle("-fx-control-inner-background: green");
                            }
                        }
                    }
                }
            }
        });
        tab.getSelectionModel().selectedItemProperty().addListener((javafx.beans.value.ChangeListener<? super Tab>) (observable, oldTab, newTab) -> {
            if(newTab.getText().equals ("РАДІОГРАМИ")) {
                countRepeatTxComboBox.setVisible(true);
                timeRepeatTxComboBox.setVisible(false);
                timeRepeatLabel.setVisible(false);
                countRepeatLabel.setVisible(true);
                countRepeatTxComboBox.setValue("Виберіть");
            }else {
                countRepeatTxComboBox.setVisible(false);
                countRepeatLabel.setVisible(false);
                timeRepeatTxComboBox.setVisible(true);
                timeRepeatLabel.setVisible(true);
                timeRepeatTxComboBox.setValue("Виберіть");
            }
        });

stopTxButton.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        Core.getCore().dev.infAdd.clearTxQueue();
    }
});
        serviceMessageTxList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addSeans.setVisible(false);
                String serviceMessage = (String) serviceMessageTxList.getSelectionModel().getSelectedItem();
                System.out.println(serviceMessage );
                if(serviceMessage.equals("ДОДАТКОВИЙ СЕАНС")){
                    addSeans.setVisible(true);

                }
            }
        });
        speedTxComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int speed =  Integer.parseInt((String) speedTxComboBox.getValue());
                Core.getCore().dev.modulatorPsk.setRelativeBaudeRate(((float) speed) / 3000.f);
            }
        });
        command1TextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (command1TextField.getText().length()==0){
                    command1TextField.setStyle("-fx-control-inner-background: white");
                }else {
                    if (checkMather(command1TextField, "\\d{2}") && Integer.parseInt(command1TextField.getText())<51) {

                        command1TextField.setStyle("-fx-control-inner-background: green");
                    } else {
                        command1TextField.setStyle("-fx-control-inner-background: red");
                    }
                }
            }
        });

        command2TextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (command2TextField.getText().length()==0){
                    command2TextField.setStyle("-fx-control-inner-background: white");
                }else {
                    if (checkMather(command2TextField, "\\d{2}") && Integer.parseInt(command2TextField.getText())<51) {

                        command2TextField.setStyle("-fx-control-inner-background: green");
                    } else {
                        command2TextField.setStyle("-fx-control-inner-background: red");
                    }
                }
            }
        });

        command3TextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (command3TextField.getText().length()==0){
                    command3TextField.setStyle("-fx-control-inner-background: white");
                }else {
                    if (checkMather(command3TextField, "\\d{2}") && Integer.parseInt(command3TextField.getText())<51) {

                        command3TextField.setStyle("-fx-control-inner-background: green");
                    } else {
                        command3TextField.setStyle("-fx-control-inner-background: red");
                    }
                }
            }
        });

        command4TextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (command4TextField.getText().length()==0){
                    command4TextField.setStyle("-fx-control-inner-background: white");
                }else {
                    if (checkMather(command4TextField, "\\d{2}") && Integer.parseInt(command4TextField.getText())<51) {

                        command4TextField.setStyle("-fx-control-inner-background: green");
                    } else {
                        command4TextField.setStyle("-fx-control-inner-background: red");
                    }
                }
            }
        });
        check1Freq.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (check1Freq.isSelected()){

                    freq1TextField.setStyle("-fx-control-inner-background: green");
                }else {

                    freq1TextField.setStyle("-fx-control-inner-background: white");
                }
                check2Freq.setSelected(false);
                freq2TextField.setStyle("-fx-control-inner-background: white");
                check3Freq.setSelected(false);
                freq3TextField.setStyle("-fx-control-inner-background: white");
                check4Freq.setSelected(false);
                freq4TextField.setStyle("-fx-control-inner-background: white");
            }
        });
        check2Freq.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (check2Freq.isSelected()){

                    freq2TextField.setStyle("-fx-control-inner-background: green");
                }else {

                    freq2TextField.setStyle("-fx-control-inner-background: white");
                }
                check1Freq.setSelected(false);
                freq1TextField.setStyle("-fx-control-inner-background: white");
                check3Freq.setSelected(false);
                freq3TextField.setStyle("-fx-control-inner-background: white");
                check4Freq.setSelected(false);
                freq4TextField.setStyle("-fx-control-inner-background: white");
            }
        });
        check3Freq.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (check3Freq.isSelected()){

                    freq3TextField.setStyle("-fx-control-inner-background: green");
                }else {

                    freq3TextField.setStyle("-fx-control-inner-background: white");
                }
                check1Freq.setSelected(false);
                freq1TextField.setStyle("-fx-control-inner-background: white");
                check2Freq.setSelected(false);
                freq2TextField.setStyle("-fx-control-inner-background: white");
                check4Freq.setSelected(false);
                freq4TextField.setStyle("-fx-control-inner-background: white");
            }
        });
        check4Freq.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (check4Freq.isSelected()){

                    freq4TextField.setStyle("-fx-control-inner-background: green");
                }else {

                    freq4TextField.setStyle("-fx-control-inner-background: white");
                }

                check1Freq.setSelected(false);
                freq1TextField.setStyle("-fx-control-inner-background: white");
                check2Freq.setSelected(false);
                freq2TextField.setStyle("-fx-control-inner-background: white");
                check3Freq.setSelected(false);
                freq3TextField.setStyle("-fx-control-inner-background: white");
            }
        });
//        sendButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//               String txt ="";// serviceMessage.getText();
//
//                System.out.println("Fine");
//
//                Core.getCore().dev.infAdd.add(txt);
//                //Core.getCore().dev.infAdd.add("011101011101011101011101011101011101");
//               // Core.getCore().device[0].groupAdd.add(txt);
////                sendButton.setDisable(true);
////                speed100RadioButton.setDisable(true);
////                speed200RadioButton.setDisable(true);
////                speedLabel.setDisable(true);
//
//            }
//        });
//        speed100RadioButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                speed200RadioButton.setSelected(false);
////                if(Core.getCore().countConectedDevice == 1) {
////                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);
////                    Core.getCore().device[0].optimalNonCoherentD?modulatorPsk100.setRelativeBaudRate(100.f / 3000.f);
////                }else if(Core.getCore().countConectedDevice == 2) {
////                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(100.f / 3000.f);
////                    Core.getCore().device[1].optimalNonCoherentD?modulatorPsk100.setRelativeBaudRate(100.f / 3000.f);
////                }
//            }
//        });
//        speed200RadioButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                speed100RadioButton.setSelected(false);
////                if(Core.getCore().countConectedDevice == 1) {
////                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(250.f / 3000.f);
////                    Core.getCore().device[0].optimalNonCoherentD?modulatorPsk100.setRelativeBaudRate(250.f / 3000.f);
////                }else if(Core.getCore().countConectedDevice == 2) {
////                    Core.getCore().device[0].modulatorPsk.setRelativeBaudeRate(250.f / 3000.f);
////                    Core.getCore().device[1].optimalNonCoherentD?modulatorPsk100.setRelativeBaudRate(250.f / 3000.f);
////                }
//            }
//        });
    }

    public void updatePercentRadiogram(int percent) {
        //System.out.println("Percent"+percent);
        float percentNow = (float) 1-((float)percent/(float)100);
        Platform.runLater(() -> {
            progressBar.setProgress(percentNow);
        });
        if(percent==0){
            Platform.runLater(() -> {
                sendB.setDisable(false);
                progressBar.setProgress(0);
            });
        }
    }

    public void setTextMessage(int data, int speed) {
        String newLine = System.getProperty("line.separator");
        String tmp = Integer.toString(data);
        if (speed == 100) {
            if (tmp.equals("10")) {
                Platform.runLater(() -> {
                    message.appendText("*");
                });
            } else {
                Platform.runLater(() -> {
                    message.appendText(tmp);
                });
            }

            countSymbol++;
            if (countSymbol % 5 == 0) {
                Platform.runLater(() -> {
                    message.appendText(" ");
                });

            }
            if (countSymbol % 50 == 0) {
                //int countgroup = countSymbol/5;
                Platform.runLater(() -> {
                    message.appendText("\n");
                });
            }
        }
    }

    public void setTextMessageStrum(String data) {

        Platform.runLater(() -> {
            message.appendText(data);
            message.appendText("\n");
        });
    }

    public void openFile(ActionEvent actionEvent) throws IOException {
        //Core.setGeneratorIQServerModeTx();
    }

    public void sendingMessage(ActionEvent actionEvent) {


        int activeTab = tab.getSelectionModel().getSelectedIndex();
        if (corNumberTextField.getText().matches("\\d{3}")) {

            Core.getCore().dev.infAdd.setNumberCorespondent(corNumberTextField.getText());
            if (activeTab == 1) {
                String tmp = (String) countRepeatTxComboBox.getValue();
                if (tmp.matches("\\d{1}") != true) {
                    inf("Кількість");
                    return;
                }
                checkMather(corKoefTextField, "\\d{5}");
                String string = txRgTextArea.getText();
                sendButton.setDisable(true);
                Core.getCore().dev.infAdd.addDataRg(string,tmp);
            }else {
                checkFrequency();
                checkCommand();
                String tmp = (String) timeRepeatTxComboBox.getValue();
                if (tmp.matches("\\d{1}") != true) {
                    inf("Час");
                    return;
                }
                sendButton.setDisable(true);
                Core.getCore().dev.infAdd.addDataService(tmp);
            }

//            boolean freqBoolean = validation(freqTextField);
//            boolean serviceMessageBoolean = validation(serviceMessage);
//            String freqString = freqTextField.getText();
//            String serviceMessageString = serviceMessage.getText();
//            if (freqBoolean && serviceMessageBoolean && command.getValue() == null) {
//                inf2();
//            } else if (freqBoolean) {
//                if (serviceMessageBoolean) {
//                    if (command.getValue() != null) {
//                        clear();
//                        sendData.parsData();
//                    }
//                } else if (checkMather(serviceMessage, "\\d{3,78}")) {
//                    sendData.setServiceMessage(serviceMessageString);
//                    clear();
//                    sendData.parsData();
//                } else {
//                    inf("???????? ????????????");
//                }
//            } else if (checkMather(freqTextField, "\\d{5}")) {
//                sendData.setFrequencyTx(freqString);
//                if (validation(serviceMessage)) {
//                    clear();
//                    sendData.parsData();
//                } else if (checkMather(serviceMessage, "\\d{3,78}")) {
//                    sendData.setServiceMessage(serviceMessageString);
//                    clear();
//                    sendData.parsData();
//                } else {
//                    inf("???????? ????????????");
//                }
//            } else {
//                inf("????????? ?????");
//            }
        } else {
            inf("Позивний");
        }

    }

    private boolean checkMather(TextField textField, String matches) {

        return textField.getText().replaceAll(" ", "").matches(matches);
    }

    private boolean validation(TextField textField) {
        return textField.getText().isEmpty();
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
        alert.setContentText("??????? ????????");
        alert.showAndWait();
    }

    private void clear() {
        //freq1.clear();
        //serviceMessage.clear();
        //command.setValue(null);
    }
    private void checkFrequency(){
        if(check1Freq.isSelected() || check2Freq.isSelected() || check3Freq.isSelected()|| check4Freq.isSelected()){
            if(checkMather(corKoefTextField, "\\d{5}")){
                Core.getCore().dev.infAdd.setKoef(corKoefTextField.getText());
                if(check1Freq.isSelected()){
                    if(checkMather(freq1TextField, "\\d{5}")){
                        Core.getCore().dev.infAdd.setSendFrequency(freq1TextField.getText());
                    }else
                        inf("Перевідна частота №1");
                }
                if(check2Freq.isSelected()){
                    if(checkMather(freq2TextField, "\\d{5}")){
                        Core.getCore().dev.infAdd.setSendFrequency(freq2TextField.getText());
                    }else
                        inf("Перевідна частота №2");
                }
                if(check3Freq.isSelected()){
                    if(checkMather(freq3TextField, "\\d{5}")){
                        Core.getCore().dev.infAdd.setSendFrequency(freq3TextField.getText());
                    }else
                        inf("Перевідна частота №3");
                }
                if(check4Freq.isSelected()){
                    if(checkMather(freq4TextField, "\\d{5}")){
                        Core.getCore().dev.infAdd.setSendFrequency(freq4TextField.getText());
                    }else
                        inf("Перевідна частота №4");
                }

            }else {
                inf("Коефіцієнт");
            }
        }
    }
    private void checkCommand(){
        String command ="";
        if (command1TextField.getText() != null && !(command1TextField.getText().equals(""))) {


            if(checkMather(command1TextField, "\\d{2}")){
                 command = command1TextField.getText();

            }else {

                inf("Команда 1");
                return;
            }
        }
        if (command2TextField.getText() != null && !(command2TextField.getText().equals(""))) {
            if(checkMather(command2TextField, "\\d{2}")){
                command = command.concat(command2TextField.getText());

            }else {

                inf("Команда 2");
                return;
            }
        }
        if (command3TextField.getText() != null && !(command3TextField.getText().equals(""))) {
            if(checkMather(command3TextField, "\\d{2}")){
                command = command.concat(command3TextField.getText());

            }else {

                inf("Команда 3");
                return;
            }
        }
        if (command4TextField.getText() != null && !(command4TextField.getText().equals(""))) {
            if(checkMather(command4TextField, "\\d{2}")){
                command = command.concat(command4TextField.getText());

            }else {
                
                inf("Команда 4");
                return;
            }
        }
        Core.getCore().dev.infAdd.setCommand1(command);
    }
    public class TimeTh extends Thread{
        @Override
        public void run() {
            while (true){
                LocalTime myObj = LocalTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
                String formattedDate = myObj.format(myFormatObj);
                Platform.runLater(() -> {
                    time.setText(String.valueOf(formattedDate));
            });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

