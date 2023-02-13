package com.oh72.computergraphics;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Oleh Hembarovskyi
 * @link oleh.hembarovskyi@embrox.com
 * @since 08/11/2022
 **/
public class DragonCurveController {
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
        btnHome.setOnMouseReleased(MainMenuController.HOME);
        btnHelp.setOnMouseReleased(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Image image = new Image("dragon-fractal.png");
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(500);
            alert.setGraphic(imageView);
            alert.setTitle("Інформація");
            alert.setContentText("Кожна ламана–“дракон” є лише наближенням до фракталу-“дракона” та складається з " +
                    "відрізків. Ламана з номером n складатиметься з 2n відрізків. Довжина кожного дорівнює  , де d – довжина" +
                    " вихідного відрізка. Якщо відрізки пронумерувати числами 0, 1, 2, ... і йти по ламаній, то після кожного " +
                    "відрізка потрібно здійснювати поворот. Напрямок повороту визначається номером k поточного відрізка:\n\n" +
                    " повернути праворуч, якщо k дає залишок 1 від ділення на 4;\n" +
                    "\n" +
                    "повернути ліворуч, якщо k дає залишок 3 від ділення на 4;\n" +
                    "\n" +
                    "овертати так, як після відрізка з номером k/2, якщо k парне.");
            alert.setHeaderText("");
            alert.showAndWait();
        });

        mode.getItems().addAll(Mode.values());
        mode.setValue(Mode.DYNAMIC);
        btnStart.setOnMouseReleased(mouseEvent -> dynamic());
        numberOfIterations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20));
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
        iterationCounter.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20));
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
        turns = getSequence(maxIterations);
        startingAngle = -maxIterations * (Math.PI / 4);

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

        double maxSide = (Math.min(Math.min(x, canvas.getWidth() - x), Math.min(y, canvas.getHeight() - y)) * 1.5);
        side = maxSide / Math.pow(2, maxIterations / 2D);

        paint(canvas.getGraphicsContext2D(), x, y);
    }

    public List<Integer> getSequence(int iterations) {
        List<Integer> turnSequence = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            List<Integer> copy = new ArrayList<>(turnSequence);
            Collections.reverse(copy);
            turnSequence.add(1);

            for (Integer turn : copy) {
                turnSequence.add(-turn);
            }
        }
        return turnSequence;
    }

    private List<Integer> turns;
    private double startingAngle, side;

    public void paint(GraphicsContext g, double x1, double y1) {
        g.setFill(background.getValue());
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setStroke(colorPicker.getValue());

        double angle = startingAngle;
        double x2 = x1 + (Math.cos(angle) * side);
        double y2 = y1 + (Math.sin(angle) * side);

        g.strokeLine(x1, y1, x2, y2);

        x1 = x2;
        y1 = y2;

        for (Integer turn : turns) {
            angle += turn * (Math.PI / 2);
            x2 = x1 + (Math.cos(angle) * side);
            y2 = y1 + (Math.sin(angle) * side);
            g.strokeLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
    }
}
