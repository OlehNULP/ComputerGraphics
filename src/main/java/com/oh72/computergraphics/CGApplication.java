package com.oh72.computergraphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CGApplication extends Application {
    public static final int WINDOW_WIDTH = 1080;
    public static final int WINDOW_HEIGHT = 758;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CGApplication.class.getResource("color-scheme-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Computer Graphics");
        stage.setScene(scene);
        stage.setResizable(Boolean.FALSE);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}