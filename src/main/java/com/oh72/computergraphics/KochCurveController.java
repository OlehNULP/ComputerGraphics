package com.oh72.computergraphics;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author Oleh Hembarovskyi
 * @link oleh.hembarovskyi@embrox.com
 * @since 08/11/2022
 **/
public class KochCurveController {
    @FXML
    private Button btnHome;
    @FXML
    private Button btnHelp;
    @FXML
    private Spinner<Integer> numberOfIterations;
    @FXML
    private TextField centerX;
    @FXML
    private TextField centerY;
    @FXML
    private ChoiceBox<Mode> mode;
    @FXML
    private Spinner<Integer> iterationCounter;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnDownload;
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ColorPicker background;


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
            Image image = new Image("koch-fractal.png");
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(500);
            alert.setGraphic(imageView);
            alert.setTitle("Інформація");
            alert.setContentText("Побудова кривої починається з відрізка одиничної довжини – це 0-е покоління кривої Коха. Для отримання кожного подальшого покоління, всі ланки попереднього покоління необхідно замінити зменшеним утворюючим елементом. Крива n-го покоління при будь-якому кінцевому n називається передфракталом. При n, прямуючому до нескінченності, крива Коха стає фрактальним об’єктом.");
            alert.setHeaderText("");
            alert.showAndWait();
        });

        mode.getItems().addAll(Mode.values());
        mode.setValue(Mode.DYNAMIC);
        btnStart.setOnMouseReleased(mouseEvent -> dynamic());
        numberOfIterations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
        mode.setOnAction(mouseEvent -> {
            if (Objects.equals(mode.getValue(), Mode.BY_STEP)) {
                iterationCounter.setDisable(Boolean.FALSE);
                btnStart.setDisable(Boolean.TRUE);
                numberOfIterations.setDisable(Boolean.TRUE);
            } else {
                iterationCounter.setDisable(Boolean.TRUE);
                btnStart.setDisable(Boolean.FALSE);
                numberOfIterations.setDisable(Boolean.FALSE);
            }
        });

        iterationCounter.setDisable(Boolean.TRUE);
        iterationCounter.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
        iterationCounter.setOnMouseReleased(mouseEvent -> byStep());

        colorPicker.setValue(Color.BLACK);
        background.setValue(Color.WHITE);

        centerX.setText("333");
        centerY.setText("333");

        btnDownload.setOnAction((e) -> {
            FileChooser saveFile = new FileChooser();
            saveFile.setTitle("Save File");

            File file = saveFile.showSaveDialog(new Stage());
            System.out.println("is file null ? " + file);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Error!");
                }
            }
        });
    }

    public void dynamic() {
        setup(numberOfIterations.getValue());
    }

    public void byStep() {
        setup(iterationCounter.getValue());
    }

    public void setup(int maxIterations) {

        double x;
        double y;

        try {
            x = Double.parseDouble(centerX.getText());

            if (x <= 0 || x >= canvas.getWidth()) {
                throw new Exception();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(String.format("X should be in [100, %d]", (int) canvas.getWidth()));
            alert.show();

            throw new RuntimeException();
        }

        try {
            y = Double.parseDouble(centerY.getText());

            if (y <= 0 || y >= canvas.getHeight()) {
                throw new Exception();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(String.format("Y should be in [100, %d]", (int) canvas.getHeight()));
            alert.show();

            throw new RuntimeException();
        }

        double maxSide = Math.min(x, canvas.getWidth() - x) * 2;

        Coordinate coordinateA = new Coordinate(x - maxSide / 2D, y);
        Coordinate coordinateE = new Coordinate(x + maxSide / 2D, y);
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coordinateA);
        calculateKochCurve(coordinateA, coordinateE, 0, coordinates, maxIterations);
        coordinates.add(coordinateE);

        Iterator<Coordinate> coordinateIterator = coordinates.listIterator();
        canvas.getGraphicsContext2D().setFill(background.getValue());
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().setStroke(colorPicker.getValue());


        if (coordinateIterator.hasNext()) {
            Coordinate previous = coordinateIterator.next();
            while (coordinateIterator.hasNext()) {
                Coordinate coordinate = coordinateIterator.next();
                canvas.getGraphicsContext2D().strokeLine(previous.getX(), previous.getY(), coordinate.getX(), coordinate.getY());
                previous = coordinate;
            }
        }
    }

    public void calculateKochCurve(Coordinate coordinateA, Coordinate coordinateE, int iteration,
                                   List<Coordinate> coordinates, int maxIterations) {
        if (iteration > maxIterations) {
            return;
        }

        double deltaX = coordinateE.getX() - coordinateA.getX();
        double deltaY = coordinateE.getY() - coordinateA.getY();

        double xB = coordinateA.getX() + deltaX / 3D;
        double yB = coordinateA.getY() + deltaY / 3D;
        Coordinate coordinateB = new Coordinate(xB, yB);

        double xC = ((coordinateA.getX() + coordinateE.getX()) / 2D - Math.sqrt(3D) * (coordinateA.getY() - coordinateE.getY()) / 6);
        double yC = ((coordinateA.getY() + coordinateE.getY()) / 2D - Math.sqrt(3D) * (coordinateE.getX() - coordinateA.getX()) / 6);
        Coordinate coordinateC = new Coordinate(xC, yC);

        double xD = coordinateE.getX() - deltaX / 3D;
        double yD = coordinateE.getY() - deltaY / 3D;
        Coordinate coordinateD = new Coordinate(xD, yD);

        iteration++;

        calculateKochCurve(coordinateA, coordinateB, iteration, coordinates, maxIterations);
        coordinates.add(coordinateB);
        calculateKochCurve(coordinateB, coordinateC, iteration, coordinates, maxIterations);
        coordinates.add(coordinateC);
        calculateKochCurve(coordinateC, coordinateD, iteration, coordinates, maxIterations);
        coordinates.add(coordinateD);
        calculateKochCurve(coordinateD, coordinateE, iteration, coordinates, maxIterations);
    }
}
