package com.oh72.computergraphics;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;

/**
 * @author Oleh Hembarovskyi
 * @link oleh.hembarovskyi@embrox.com
 * @since 08/11/2022
 **/
public class TriangleMovementController {
    @FXML
    private Button btnHome;
    @FXML
    private Button btnHelp;
    @FXML
    private Canvas canvas;
    @FXML
    private Spinner<Integer> spnX1;
    @FXML
    private Spinner<Integer> spnY1;
    @FXML
    private Spinner<Integer> spnX2;
    @FXML
    private Spinner<Integer> spnY2;
    @FXML
    private Spinner<Integer> spnX3;
    @FXML
    private Spinner<Integer> spnY3;
    @FXML
    private Spinner<Integer> spnX;
    @FXML
    private Spinner<Integer> spnY;
    @FXML
    private Spinner<Double> spnZoom;

    private Triangle triangle;
    private int prevX1 = -50;
    private int prevY1 = 10;
    private int prevX2 = -100;
    private int prevY2 = 50;
    private int prevX3 = -30;
    private int prevY3 = 100;
    private int prevX = 0;
    private int prevY = 0;
    private double prevZoom = 1;
    private boolean ignore = true;
    private boolean outOfCanvas = false;

    @FXML
    public void initialize() {
        Image img = new Image("home.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        btnHome.setOnMouseReleased(MainMenuController.HOME);
        btnHome.setGraphic(view);
        btnHome.setText("");
        btnHelp.setOnMouseReleased(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Image image = new Image("triangle-movement.png");
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(500);
            alert.setGraphic(imageView);
            alert.setTitle("Інформація");
            alert.setContentText("Афінним називається перетворення, що має такі властивостями:\n" +
                    "● будь-яке афінне перетворення може бути представлене як послідовність операцій з числа найпростіших: зсув, розтягнення/стиснення, поворот;\n" +
                    "● зберігаються прямі лінії, паралельність прямих, відношення довжин відрізків, що лежать на одній прямій, і відношення площ фігур.\n" +
                    "\n" +
                    "Афінні перетворення координат на площині:\n" +
                    "(X, Y) – двовимірна система координат,\n" +
                    "(x, y) – координати старої системи в новій системі координат.");
            alert.setHeaderText("");
            alert.showAndWait();
        });

        spnX.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-(int) canvas.getWidth(), (int) canvas.getWidth(), prevX, 5));
        spnY.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-(int) canvas.getHeight(), (int) canvas.getHeight(), prevY, 5));
        spnZoom.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1000, prevZoom, 0.03));
        spnX1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-(int) canvas.getWidth(), (int) canvas.getWidth(), prevX1, 5));
        spnY1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-(int) canvas.getHeight(), (int) canvas.getHeight(), prevY1, 5));
        spnX2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-(int) canvas.getWidth(), (int) canvas.getWidth(), prevX2, 5));
        spnY2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-(int) canvas.getHeight(), (int) canvas.getHeight(), prevY2, 5));
        spnX3.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-(int) canvas.getWidth(), (int) canvas.getWidth(), prevX3, 5));
        spnY3.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-(int) canvas.getHeight(), (int) canvas.getHeight(), prevY3, 5));

        InvalidationListener invalidationListener = event -> {
            if (!ignore) {
                checkChanges();
            }
        };

        spnX.valueProperty().addListener(invalidationListener);
        spnY.valueProperty().addListener(invalidationListener);
        spnZoom.valueProperty().addListener(invalidationListener);
        spnX1.valueProperty().addListener(invalidationListener);
        spnY1.valueProperty().addListener(invalidationListener);
        spnX2.valueProperty().addListener(invalidationListener);
        spnY2.valueProperty().addListener(invalidationListener);
        spnX3.valueProperty().addListener(invalidationListener);
        spnY3.valueProperty().addListener(invalidationListener);

        ignore = false;
        outOfCanvas = false;

        updateTriangle();
        calculate();
        draw();
    }

    public void updateTriangle() {
        if (triangle == null) {
            triangle = new Triangle(
                    new Coordinate((double) spnX1.getValue(), (double) spnY1.getValue()),
                    new Coordinate((double) spnX2.getValue(), (double) spnY2.getValue()),
                    new Coordinate((double) spnX3.getValue(), (double) spnY3.getValue()));
        }

        triangle.setInitPoints(
                new Coordinate((double) spnX1.getValue(), (double) spnY1.getValue()),
                new Coordinate((double) spnX2.getValue(), (double) spnY2.getValue()),
                new Coordinate((double) spnX3.getValue(), (double) spnY3.getValue()));
    }

    public void calculate() {
        triangle.calculate(spnX.getValue(), spnY.getValue(), spnZoom.getValue());
    }


    public void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setLineWidth(3);
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Coordinate[] points = triangle.getPoints();
        drawLine(g, points[0], points[1], Color.DARKRED);
        drawLine(g, points[1], points[2], Color.DARKRED);
        drawLine(g, points[2], points[0], Color.DARKRED);

        points = triangle.getMirrorPoints();
        drawLine(g, points[0], points[1], Color.DARKGREEN);
        drawLine(g, points[1], points[2], Color.DARKGREEN);
        drawLine(g, points[2], points[0], Color.DARKGREEN);

        drawLine(g, new Coordinate(0D, -1000D), new Coordinate(0D, 1000D), Color.BLACK);
        drawLine(g, new Coordinate(-1000D, 0D), new Coordinate(1000D, 0D), Color.BLACK);

        if (outOfCanvas) {
            g.setStroke(Color.DEEPPINK);
            g.setFont(new Font("System Regular", 35));
            g.strokeText("Трикутник вийшов за межі, дія була повернута, все добре)", 55, canvas.getHeight() / 2 - 50);
        }
    }

    private void drawLine(GraphicsContext g, Coordinate a, Coordinate b, Color color) {
        a = normilizeCoordinate(a);
        b = normilizeCoordinate(b);

        g.setStroke(color);
        g.strokeLine(a.x, a.y, b.x, b.y);
    }

    private void checkChanges() {
        updateTriangle();
        calculate();

        if (Arrays.stream(triangle.getPoints()).anyMatch(this::isCoordinateOutOfCanvas)
                || Arrays.stream(triangle.getMirrorPoints()).anyMatch(this::isCoordinateOutOfCanvas)) {
            restorePreviousState();
            updateTriangle();
            calculate();
            outOfCanvas = true;
        } else {
            saveState();
            outOfCanvas = false;
        }

        draw();
    }

    private void restorePreviousState() {
        ignore = true;
        spnX.getValueFactory().setValue(prevX);
        spnY.getValueFactory().setValue(prevY);
        spnZoom.getValueFactory().setValue(prevZoom);
        spnX1.getValueFactory().setValue(prevX1);
        spnY1.getValueFactory().setValue(prevY1);
        spnX2.getValueFactory().setValue(prevX2);
        spnY2.getValueFactory().setValue(prevY2);
        spnX3.getValueFactory().setValue(prevX3);
        spnY3.getValueFactory().setValue(prevY3);
        ignore = false;
    }

    private void saveState() {
        prevX1 = spnX1.getValue();
        prevY1 = spnY1.getValue();
        prevX2 = spnX2.getValue();
        prevY2 = spnY2.getValue();
        prevX3 = spnX3.getValue();
        prevY3 = spnY3.getValue();
        prevX = spnX.getValue();
        prevY = spnY.getValue();
        prevZoom = spnZoom.getValue();
    }

    private boolean isCoordinateOutOfCanvas(Coordinate coordinate) {
        double midWidth = canvas.getWidth() / 2;
        double midHeight = canvas.getHeight() / 2;

        return coordinate.x < -midWidth
                || coordinate.x > midWidth
                || coordinate.y < -midHeight
                || coordinate.y > midHeight;
    }

    private Coordinate normilizeCoordinate(Coordinate coordinate) {
        double midWidth = canvas.getWidth() / 2;
        double midHeight = canvas.getHeight() / 2;

        double x1 = coordinate.x + midWidth;
        double y1 = midHeight - coordinate.y;

        return new Coordinate(x1, y1);
    }
}
