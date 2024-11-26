package main;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Handles the drawing of a cubic Bézier curve.
 * <br>
 * Can draw an outline of the shape, or fill with color.
 */
public class BezierSplineDrawer extends JPanel {

    /** Draws a cubic bezier spline given
     * a Graphics2D object, array of points, line width,
     * and an outline color.
     */
    public static void drawOutline(Graphics2D g, Point2D.Double[] points, int lineWidth, Color outlineColor) {

        g.setStroke(new BasicStroke(lineWidth));
        g.setColor(outlineColor);

        // Enable antialiasing for smoother curves
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate and draw the Bézier spline
        // 'segments' can be increased to create more realistic curves
        List<Point2D.Double> spline = CubicBezierSpline.calculateSpline(points, 20);

        for (int i = 0; i < spline.size() - 1; i++) {
            Point2D.Double p1 = spline.get(i);
            Point2D.Double p2 = spline.get(i + 1);
            g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        }
    }

    /** Draws a cubic bezier spline shape given
     * a Graphics2D object, array of points, line width,
     * an outline color, and a fill color.
     * <br>
     * The Point2D array that is passed in must have an equal starting and end index
     * to ensure that the shape is complete.
     */
    public static void drawWithFill(Graphics2D g, Point2D.Double[] points, int lineWidth, Color outlineColor, Color fillColor) {

        // Enable antialiasing for smoother curves
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate and draw the Bézier spline
        // 'segments' can be increased to create more realistic curves
        List<Point2D.Double> spline = CubicBezierSpline.calculateSpline(points, 30);


        // Create Path2D object to represent the shape
        Path2D path = new Path2D.Double();
        if (!spline.isEmpty()) {
            Point2D.Double start = spline.get(0);
            path.moveTo(start.x, start.y);
            for (int i = 1; i < spline.size(); i++) {
                Point2D.Double point = spline.get(i);
                path.lineTo(point.x, point.y);
            }
            path.closePath(); // Close the path to form a complete shape
        }

        g.setColor(fillColor);
        g.fill(path);

        // draw the outline
        g.setStroke(new BasicStroke(lineWidth));
        g.setColor(outlineColor);
        g.draw(path);
    }
}


