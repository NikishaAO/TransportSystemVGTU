import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Manager;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Start extends Application {

    @Override
    public void start (Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("view/LoginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

        Manager currentManager;



    }
    public static void main(String[] args) {
        launch();
    }
}