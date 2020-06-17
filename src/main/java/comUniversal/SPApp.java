package comUniversal;

//import com.sun.tools.javac.Main;
import comUniversal.ui.MainUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SPApp extends Application {
    //public static Main INSTANCE;
    private Stage primaryStage;
    MainUI mainUIw;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/StartWindow.fxml"));
        //Parent root = fxmlLoader.load(getClass().getResource("/StartWindow.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("”н≥версальне програмне забезпеченн€");
        stage.setScene(scene);
        stage.setX(212);
        stage.setY(15);
        stage.show();
        stage.setOnCloseRequest(event -> {
            System.out.println("platform exit");
            Platform.exit();
            System.exit(0);
        });

        mainUIw = fxmlLoader.getController();
        Core.getCore().mainUI = mainUIw;
    }
    public void test(){

    }
    public static void main(String[] args) {

        launch(args);
    }


}