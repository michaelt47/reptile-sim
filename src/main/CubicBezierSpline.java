package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CubicBezierSpline {

    public static List<Point2D.Double> calculateSpline(Point2D.Double[] points, int segments) {
        List<Point2D.Double> spline = new ArrayList<>();

        for (int i = 0; i < points.length - 1; i++) {
            Point2D.Double p0 = points[i];
            Point2D.Double p3 = points[i + 1];

            // Calculate control points (simple Catmull-Rom style)
            Point2D.Double p1, p2;
            if (i == 0) {
                p1 = new Point2D.Double(p0.x + (p3.x - p0.x) / 3, p0.y + (p3.y - p0.y) / 3);
            } else {
                Point2D.Double prev = points[i - 1];
                p1 = new Point2D.Double(p0.x + (p3.x - prev.x) / 6, p0.y + (p3.y - prev.y) / 6);
            }

            if (i == points.length - 2) {
                p2 = new Point2D.Double(p3.x - (p3.x - p0.x) / 3, p3.y - (p3.y - p0.y) / 3);
            } else {
                Point2D.Double next = points[i + 2];
                p2 = new Point2D.Double(p3.x - (next.x - p0.x) / 6, p3.y - (next.y - p0.y) / 6);
            }

            // Generate curve points for this segment
            for (int j = 0; j <= segments; j++) {
                double t = j / (double) segments;
                spline.add(calculateBezierPoint(t, p0, p1, p2, p3));
            }
        }

        return spline;
    }

    private static Point2D.Double calculateBezierPoint(double t, Point2D.Double p0, Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        double x = Math.pow(1 - t, 3) * p0.x +
                3 * Math.pow(1 - t, 2) * t * p1.x +
                3 * (1 - t) * Math.pow(t, 2) * p2.x +
                Math.pow(t, 3) * p3.x;

        double y = Math.pow(1 - t, 3) * p0.y +
                3 * Math.pow(1 - t, 2) * t * p1.y +
                3 * (1 - t) * Math.pow(t, 2) * p2.y +
                Math.pow(t, 3) * p3.y;

        return new Point2D.Double(x, y);
    }
}


