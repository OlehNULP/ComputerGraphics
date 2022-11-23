package com.oh72.computergraphics;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
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
import java.util.List;

/**
 * @author Oleh Hembarovskyi
 * @link oleh.hembarovskyi@embrox.com
 * @since 08/11/2022
 **/
public class ColorSchemeController {
    @FXML
    private Button btnBack;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnHelp;
    @FXML
    private Button btnUpload;
    @FXML
    private Button btnDownload;
    @FXML
    private Button btnConvert;
    @FXML
    private Button btnSubmit;
    @FXML
    private Canvas canvas;
    @FXML
    private ImageView imageView;
    @FXML
    private Slider sliderComponent;
    private Image image;
    private List<List<PixelRGB>> pixels;

    @FXML
    public void initialize() {
        image = new Image("default_image.png");
        imageView.setImage(image);
        newImagePixels(imageView.getImage());

        sliderComponent.setMin(0);
        sliderComponent.setMax(100);

        btnConvert.setOnMouseReleased(mouseEvent -> {
            convert();
            draw();
        });
        btnDownload.setOnAction(actionEvent -> download());
    }

    public void newImagePixels(Image image) {
        pixels = new ArrayList<>();

        for (int i = 0; i < image.getHeight(); i++) {
            List<PixelRGB> row = new ArrayList<>();

            for (int j = 0; j < image.getWidth(); j++) {
                Color color = image.getPixelReader().getColor(i, j);
                row.add(new PixelRGB(color.getRed() * 255, color.getGreen() * 255, color.getBlue() * 255));
            }

            pixels.add(row);
        }
    }

    public void download() {
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
    }

    public void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                PixelRGB pixelRGB = pixels.get(i).get(j);
                g.setFill(new Color(pixelRGB.getRed() / 255, pixelRGB.getGreen() / 255, pixelRGB.getBlue() / 255, 1));
                g.fillRect(i + 1, j + 1, i + 1, j + 1);
            }
        }

        g.setFill(Color.WHITE);
        g.fillRect(image.getWidth(), 0, canvas.getWidth(), canvas.getHeight());
        g.fillRect(0, image.getHeight(), canvas.getWidth(), canvas.getHeight());

    }

    public void convert() {
        PixelHSL[][] hslPixels = new PixelHSL[(int) image.getWidth()][(int) image.getHeight()];

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                hslPixels[i][j] = rgbToHsl(pixels.get(i).get(j));
            }
        }

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                pixels.get(i).set(j, hslToRgb(hslPixels[i][j]));
            }
        }
    }

    public PixelHSL rgbToHsl(PixelRGB pixelRGB) {
        double r = pixelRGB.getRed() / 255;
        double g = pixelRGB.getGreen() / 255;
        double b = pixelRGB.getBlue() / 255;

        double max = Math.max(Math.max(r, g), b);
        double min = Math.min(Math.min(r, g), b);
        double delta = max - min;

        double h;
        double s;
        double l;

        l = (max + min) / 2D;

        if (delta == 0) {
            h = 0;
        } else if (max == r) {
            h = 60 * (((g - b) / delta) % 6);
        } else if (max == g) {
            h = 60 * ((b - r) / delta + 2);
        } else {
            h = 60 * ((r - g) / delta + 4);
        }

        if (delta == 0) {
            s = 0;
        } else {
            s = delta / (1 - Math.abs(2 * l - 1));
        }

        s *= 100;
        l *= 100;

        return new PixelHSL(h, s, l);
    }

    public PixelRGB hslToRgb(PixelHSL pixelHSL) {
        double h = pixelHSL.getHue();
        double s = pixelHSL.getSaturation() / 100;
        double l = pixelHSL.getLightness() / 100;

        double c = (1 - Math.abs(2 * l - 1)) * s;
        double x = c * (1 - Math.abs((h / 60) % 2 - 1));
        double m = l - c / 2;

        double r;
        double g;
        double b;

        if (h >= 0 && h < 60) {
            r = c;
            g = x;
            b = 0;
        } else if (h >= 60 && h < 120) {
            r = x;
            g = c;
            b = 0;
        } else if (h >= 120 && h < 180) {
            r = 0;
            g = c;
            b = x;
        } else if (h >= 180 && h < 240) {
            r = 0;
            g = x;
            b = c;
        } else if (h >= 240 && h < 300) {
            r = x;
            g = 0;
            b = c;
        } else {
            r = c;
            g = 0;
            b = x;
        }

        r = (r + m) * 255;
        g = (g + m) * 255;
        b = (b + m) * 255;

        return new PixelRGB(r, g, b);
    }
}