package entity;

import main.BezierSplineDrawer;
import main.GamePanel;

import java.awt.*;
import java.awt.geom.Point2D;

public class Snake extends Creature {


    /**
     * Default snake constructor which creates a snake with predefined shape.
     * Has distance between nodes of 15, max speed of 7, and max angle of 0.07.
     */
    public Snake(GamePanel gp) {

        int[] nodeSizes = {25, 27, 18, 23, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 25, 22, 20, 18, 15, 12, 9, 8, 7, 6};
        this.gp = gp;

        totalNodes = nodeSizes.length;
        this.body = new CreatureNode[totalNodes];
        this.distanceBetweenNodes = 15;
        this.maxSpeed = 7;
        this.maxAngle = 0.06;

        for (int i = 0; i < nodeSizes.length; i++) {
            body[i] = new CreatureNode(100 + i * distanceBetweenNodes, 100, nodeSizes[i], Math.PI);
        }
    }

    /// Creates a snake with a specified number of nodes,
    /// node size, maxSpeed, distance between nodes, and maximum turn angle.
    public Snake(GamePanel gp, int totalNodes, int nodeSize, int maxSpeed, int distanceBetweenNodes, double maxAngle) {
        this.gp = gp;
        this.body = new CreatureNode[totalNodes];
        this.maxSpeed = maxSpeed;
        this.distanceBetweenNodes = distanceBetweenNodes;
        this.totalNodes = totalNodes;
        this.maxAngle = maxAngle;

        for (int i = 0; i < totalNodes; i++) {
            body[i] = new CreatureNode(100 + i * distanceBetweenNodes, 100, nodeSize, Math.PI);
        }
    }

    /**
     * Snake draw method:
     * <br>
     * <br>
     * Draws a cubic bezier spline that surrounds the shape of the nodes that make up the snake.
     * Also draws eyes on the snake.
     */
    @Override
    void draw(Graphics2D g) {
        Point2D.Double[] leftRightPoints = getLeftRightPoints();
        Point2D.Double[] points = snakeHead(leftRightPoints);

        BezierSplineDrawer.drawWithFill(g, points, 2, Color.WHITE, new Color(0x11802e));

        drawCreatureEyes(g, 2.2, .8);
    }

    /**
     * Transforms a passed in leftRightPoints array to
     * a body with a snake head.
     */
    private Point2D.Double[] snakeHead(Point2D.Double[] leftRightPoints) {
        // first index in leftRightPoints is the left point of the head node
        // last index is the right point of the head node

        double tipSize = 0.7;
        double tipRadChange = 0.25;

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
