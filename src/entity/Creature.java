package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.geom.Point2D;

public class Creature {
    GamePanel gp;
    CreatureNode[] body;
    int distanceBetweenNodes;
    int totalNodes;
    int maxSpeed;
    double maxAngle;
    double velocity = 0.0;

    public Creature() {
    }

    /**
     * Create a default creature with one node of size 25 and a max speed of 4
     */
    public Creature(GamePanel gp) {
        this.gp = gp;

        body = new CreatureNode[1];
        body[0] = new CreatureNode(100, 100, 25, Math.PI);
        totalNodes = 1;
        maxSpeed = 4;
        maxAngle = 0.06;
    }

    /**
     * Default draw for creature, draws node circles
     */
    void draw(Graphics2D g) {
        g.setColor(Color.white);
        for (CreatureNode node : body) {


      // optionally draw arrows for each circle representing the direction the circle is facing.
//            node.DrawDirArrow(g2);

            g.drawArc((int)(Math.ceil(node.x - node.size / 2.0)), (int)(Math.ceil(node.y - node.size / 2.0)), node.size, node.size, 0, 360);
        }
    }

    /**
     * Helper method for draw() which creates an array containing 2 points
     * on each node, 1 for the left side of the node and 1 for the right.
     * <br>
     * The order of the array starts on the left side of the head node,
     * follows down the left side of the body, and comes back up through the
     * right side ending on the right side of the head node.
     */
    Point2D.Double[] getLeftRightPoints() {
        Point2D.Double[] points = new Point2D.Double[totalNodes * 2];

        for (int i = 0; i < totalNodes; i++) {
            // radius of the circle
            double rad = body[i].size / 2.0;

            double centerX = body[i].x;
            double centerY = body[i].y;

            // calculate left point coordinates
            double leftPointX = centerX - rad * Math.cos(body[i].direction + Math.PI / 2);
            double leftPointY = centerY - rad * Math.sin(body[i].direction + Math.PI / 2);

            // calculate right point coordinates
            double rightPointX = centerX - rad * Math.cos(body[i].direction - Math.PI / 2);
            double rightPointY = centerY - rad * Math.sin(body[i].direction - Math.PI / 2);

            points[i] = new Point2D.Double(leftPointX, leftPointY);
            // right points are in reverse order
            points[points.length - i - 1] = new Point2D.Double(rightPointX, rightPointY);
        }

        return points;
    }

    /**
     * Draws white eyes on the head of a creature.
     * @param circleScalar
     * The scalar of the head node's circle. The eyes are drawn on the circumference of this circle.
     * The higher the scalar the smaller the circle.
     * @param distanceBetweenEyes
     * How far apart the two eyes are on the circumference, given in radians.
     */
    void drawCreatureEyes(Graphics2D g, double circleScalar, double distanceBetweenEyes) {
        g.setColor(Color.white);
        int diameter = body[0].size / 4;
        int radius = diameter / 2;
        // the circle's circumference on which the eyes lay
        int nodeRad = (int)(body[0].size / circleScalar);
        //

        // calculate left point coordinates
        double leftPointX = body[0].x - nodeRad * Math.cos(body[0].direction - distanceBetweenEyes + Math.PI / 2);
        double leftPointY = body[0].y - nodeRad * Math.sin(body[0].direction - distanceBetweenEyes + Math.PI / 2);

        // calculate right point coordinates
        double rightPointX = body[0].x - nodeRad * Math.cos(body[0].direction + distanceBetweenEyes - Math.PI / 2);
        double rightPointY = body[0].y - nodeRad * Math.sin(body[0].direction + distanceBetweenEyes - Math.PI / 2);

        g.fillArc(
                (int)(leftPointX) - radius,
                (int)(leftPointY) - radius,
                diameter,
                diameter,
                0,
                360
        );
        g.fillArc(
                (int)(rightPointX) - radius,
                (int)(rightPointY) - radius,
                diameter,
                diameter,
                0,
                360
        );
    }

}
