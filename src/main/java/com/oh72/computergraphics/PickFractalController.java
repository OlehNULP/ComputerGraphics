package com.oh72.computergraphics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.oh72.computergraphics.CGApplication.WINDOW_HEIGHT;
import static com.oh72.computergraphics.CGApplication.WINDOW_WIDTH;

public class PickFractalController {
    @FXML
    private Button dragonCurve;
    @FXML
    private Button kochCurve;
    @FXML
    private Button colorScheme;
    @FXML
    private Button triangle;

    @FXML
    public void initialize() {
        kochCurve.setOnMouseReleased(event -> {
            Parent root;

            try {
                root = FXMLLoader.load(Objects.requireNonNull(PickFractalController.class.getResource("koch-curve-view.fxml")));
                Stage stage = new Stage();
                stage.setTitle("Koch Curve");
                stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
                stage.show();
                // Hide this current window (if this is what you want)
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        dragonCurve.setOnMouseReleased(event -> {
            Parent root;

            try {
                root = FXMLLoader.load(Objects.requireNonNull(PickFractalController.class.getResource("dragon-curve-view.fxml")));
                Stage stage = new Stage();
                stage.setTitle("Koch Curve");
                stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
                stage.show();
                // Hide this current window (if this is what you want)
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        colorScheme.setOnMouseReleased(event -> {
            Parent root;

            try {
                root = FXMLLoader.load(Objects.requireNonNull(PickFractalController.class.getResource("color-scheme-view.fxml")));
                Stage stage = new Stage();
                stage.setTitle("Color Scheme");
                stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
                stage.show();
                // Hide this current window (if this is what you want)
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        triangle.setOnMouseReleased(event -> {
            Parent root;

            try {
                root = FXMLLoader.load(Objects.requireNonNull(PickFractalController.class.getResource("triangle-movement-view.fxml")));
                Stage stage = new Stage();
                stage.setTitle("Triangle Movement");
                stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
                stage.show();
                // Hide this current window (if this is what you want)
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}