package comUniversal.ui;



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
