package com.example.parser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ParserApplication extends Application {
    static Stage stStage;
    @Override
    public void start(Stage stage) throws IOException {
        stStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(ParserApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 725);
        stage.setTitle("Golang Parser");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}