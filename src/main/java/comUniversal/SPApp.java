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
        Parent root = FXMLLoader.load(getClass().getResource("/FirstWindow.fxml"));

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Universal Special Program");
        stage.setScene(scene);
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