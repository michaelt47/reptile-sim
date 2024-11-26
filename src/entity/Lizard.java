package entity;

import main.BezierSplineDrawer;
import main.GamePanel;

import java.awt.*;
import java.awt.geom.Point2D;

public class Lizard extends Creature {

    public Lizard(GamePanel gp) {
        int[] nodeSizes = {50, 58, 40, 60, 68, 71, 65, 50, 28, 15, 11, 9, 7, 7};
        this.gp = gp;

        totalNodes = nodeSizes.length;
        body = new CreatureNode[totalNodes];
        maxSpeed = 4;
        distanceBetweenNodes = 24;
        maxAngle = 0.03;

        for (int i = 0; i < totalNodes; i++) {
            body[i] = new CreatureNode(100 * i + distanceBetweenNodes, 100, nodeSizes[i], Math.PI);
        }
    }

    /**
     * Lizard draw method:
     * <br>
     * <br>
     * Draws a cubic bezier spline that surrounds the shape of the nodes that make up the lizard.
     * Also draws eyes on the lizard.
     *<br>
     * To add:
     * <br>
     * Legs and arms with inverse kinematics
     */
    @Override
    void draw(Graphics2D g) {
        Point2D.Double[] leftRightPoints = getLeftRightPoints();
        Point2D.Double[] points = lizardHead(leftRightPoints);

        BezierSplineDrawer.drawWithFill(g, points, 2, Color.WHITE, new Color(0x802414));

        drawCreatureEyes(g, 2.4, .7);
    }


    /**
     * Transforms a passed in leftRightPoints array
     * into a body with a lizard head.
     */
    private Point2D.Double[] lizardHead(Point2D.Double[] leftRightPoints) {
        // first index in leftRightPoints is the left point of the head node
        // last index is the right point of the head node

        double tipSize = 0.4;
        double tipRadChange = 0.5;

        Point2D.Double tipRight = new Point2D.Double(body[0].x + body[0].size * tipSize * Math.cos(body[0].direction + tipRadChange), body[0].y + body[0].size * tipSize * Math.sin(body[0].direction + tipRadChange));
        Point2D.Double tipLeft = new Point2D.Double(body[0].x + body[0].size * tipSize * Math.cos(body[0].direction - tipRadChange), body[0].y + body[0].size * tipSize * Math.sin(body[0].direction - tipRadChange));

        // equivalent to however many points we are adding to the head
        int newPoints = 2;

        Point2D.Double[] result = new Point2D.Double[totalNodes * 2 + newPoints + 1];

        System.arraycopy(leftRightPoints, 0, result, 0, totalNodes * 2);
        // go from right to left adding the new points
        result[result.length - 3] = tipRight;
        result[result.length - 2] = tipLeft;
        result[result.length - 1] = result[0];

        return result;
    }
}
