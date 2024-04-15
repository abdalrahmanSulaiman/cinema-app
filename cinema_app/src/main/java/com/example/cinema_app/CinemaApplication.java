package com.example.cinema_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CinemaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("signIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 250, 200);
        stage.setResizable(false);
        stage.setTitle("Real Cinema");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}