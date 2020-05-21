package comUniversal;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SPApp extends Application {
    public static Main INSTANCE;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/StartWindow.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Універсальне програмне забезпечення");
        stage.setScene(scene);
        stage.setX(212);
        stage.setY(20);
        stage.show();
        stage.setOnCloseRequest(event -> {
            System.out.println("platform exit");
            Platform.exit();
            System.exit(0);
        });
    }
    public static void main(String[] args) {

        launch(args);
    }


}