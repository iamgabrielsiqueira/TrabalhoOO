package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/loginWindow.fxml"));
        primaryStage.setTitle("Expense Manager");
        primaryStage.setScene(new Scene(root, 500, 575));
        primaryStage.getScene().getStylesheets().add("skin.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
