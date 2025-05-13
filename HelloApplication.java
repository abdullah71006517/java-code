package com.example.dresscollection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
   public static Stage primaryStage;

    public static void changeScene(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFileName));
            Scene scene = new Scene(loader.load(), 855, 603);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Scene - " + fxmlFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 855, 603);
        stage.setTitle("JavaFX App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
