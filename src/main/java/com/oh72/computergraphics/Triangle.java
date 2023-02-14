package com.oh72.computergraphics;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Triangle {
    private final Coordinate[] initPoints;
    private final double[][] translateMatrix = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    private final double[][] mirrorMatrix = {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}};
    private final double[][] zoomMatrix = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

    private Coordinate[] points;
    private Coordinate[] mirrorPoints;

    public Triangle(Coordinate p1, Coordinate p2, Coordinate p3) {
        initPoints = new Coordinate[]{p1, p2, p3};
        points = copyPoints(initPoints);
        mirrorPoints = copyPoints(initPoints);
    }

    public void calculate(double x, double y, double zoom) {
        Coordinate[] points = copyPoints(initPoints);
        translate(points, x, y);
        zoom(points, zoom);
        this.points = copyPoints(points);

        mirror(points);
        this.mirrorPoints = points;
    }

    private Coordinate[] copyPoints(Coordinate[] points) {
        return Arrays.stream(points)
                .sequential()
                .map(coordinate -> new Coordinate(coordinate.x, coordinate.y))
                .collect(Collectors.toList())
                .toArray(Coordinate[]::new);
    }

    private void translate(Coordinate[] points, double x, double y) {
        translateMatrix[0][2] = x;
        translateMatrix[1][2] = y;

        for (Coordinate point : points) {
            double x1 = point.getX();
            double y1 = point.getY();

            point.setLocation(
                    x1 * translateMatrix[0][0] + y1 * translateMatrix[0][1] + translateMatrix[0][2],
                    x1 * translateMatrix[1][0] + y1 * translateMatrix[1][1] + translateMatrix[1][2]);
        }
    }

    private void zoom(Coordinate[] points, double coefficient) {
        Coordinate center = getCenter(points);
        zoomTranslate(points, -center.getX(), -center.getY());

        zoomMatrix[0][0] = coefficient;
        zoomMatrix[1][1] = coefficient;

        for (Coordinate point : points) {
            double x1 = point.getX();
            double y1 = point.getY();

            point.setLocation(
                    x1 * zoomMatrix[0][0] + y1 * zoomMatrix[0][1] + zoomMatrix[0][2],
                    x1 * zoomMatrix[1][0] + y1 * zoomMatrix[1][1] + zoomMatrix[1][2]);
        }

        zoomTranslate(points, center.getX(), center.getY());
    }

    private Coordinate getCenter(Coordinate[] points) {
        double x = (points[0].getX() + points[1].getX() + points[2].getX()) / 3.0;
        double y = (points[0].getY() + points[1].getY() + points[2].getY()) / 3.0;

        return new Coordinate(x, y);
    }

    private void zoomTranslate(Coordinate[] points, double x, double y) {
        for (Coordinate point : points) {
            point.setLocation(point.getX() + x, point.getY() + y);
        }
    }

    private void mirror(Coordinate[] points) {
        for (Coordinate point : points) {
            double x1 = point.getX();
            double y1 = point.getY();

            point.setLocation(
                    x1 * mirrorMatrix[0][0] + y1 * mirrorMatrix[0][1] + mirrorMatrix[0][2],
                    x1 * mirrorMatrix[1][0] + y1 * mirrorMatrix[1][1] + mirrorMatrix[1][2]);
        }
    }

    public Coordinate[] getPoints() {
        return points;
    }

    public Coordinate[] getMirrorPoints() {
        return mirrorPoints;
    }

    public void setInitPoints(Coordinate p1, Coordinate p2, Coordinate p3) {
        initPoints[0] = p1;
        initPoints[1] = p2;
        initPoints[2] = p3;
    }
}
