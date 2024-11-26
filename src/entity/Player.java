package entity;

import java.awt.*;
import java.awt.geom.Point2D;

import main.BezierSplineDrawer;
import main.GamePanel;
import main.KeyHandler;
import main.MouseHandler;

import static java.lang.Math.atan2;

public class Player extends Creature {

    KeyHandler keyH;
    MouseHandler mouseH;
    Creature creature;

    boolean cursorMovement = false;


    /// Default player constructor which creates a player with
    /// one node of size 25 and with a maxSpeed of 4
    public Player(Creature creature, KeyHandler keyH, MouseHandler mouseH) {

        this.creature = creature;
        this.gp = creature.gp;
        this.keyH = keyH;
        this.mouseH = mouseH;


        this.body = creature.body;
        totalNodes = creature.totalNodes;
        maxSpeed = creature.maxSpeed;
        maxAngle = creature.maxAngle;
        distanceBetweenNodes = creature.distanceBetweenNodes;
    }

    /// Method which updates the position of a player.
    public void update() {

        Point2D.Double move;

        if (mouseH.mouseClick || keyH.spacePressed) {
            cursorMovement = !cursorMovement;
            mouseH.mouseClick = false;
            keyH.spacePressed = false;
        }

        if (cursorMovement) {
            move = toPoint(mouseH.mouseX, mouseH.mouseY);
        } else {
            move = keyMove();
        }

        // Update the player's position
        if (move.x != 0 || move.y != 0) {
            movePlayer(move);
        }

    }

    /// Helper method for update() which calculates
    /// the change in position the player's head node
    /// needs to take in order to move towards the point of interest
    /// and returns the values in the form of a Point2D.Double object
    private Point2D.Double toPoint(double x, double y) {
        Point2D.Double move;

        // calculate change in x and change in y
        double deltaX = x - body[0].x;
        double deltaY = y - body[0].y;

        // Pythagorean theorem to find distance from center of head node to cursor
        double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);



        if (length > body[0].size / 2.0) {
            // update velocity
            double accelerationStep = getAccelerationStep(length);

            velocity += accelerationStep;

            if (velocity > maxSpeed) {
                velocity = maxSpeed;
            }
            if (velocity < 0) {
                velocity = 0;
            }


            double dirToMouse = Math.atan2(deltaY, deltaX);
            double angleDiff = normalizeAngle(dirToMouse - body[0].direction);

            // Ensure the angular change does not exceed maxAngle
            if (Math.abs(angleDiff) > maxAngle) {
                body[0].direction += Math.signum(angleDiff) * maxAngle * velocity / maxSpeed;
            } else {
                body[0].direction += angleDiff * velocity / maxSpeed;
            }

            // Normalize direction to keep it within [-PI, PI]
            body[0].direction = normalizeAngle(body[0].direction);
        } else {
            velocity = 0;
        }



        // Only move if cursor is certain distance away from head
        if (length > body[0].size / 3.0) {
            move = new Point2D.Double(Math.cos(body[0].direction), Math.sin(body[0].direction));
        } else {
            move = new Point2D.Double(0, 0);
        }

        return move;
    }

    private double getAccelerationStep(double length) {
        double stoppingDistance = (Math.pow(velocity, 2.0)) / (maxSpeed / 15.0);

        double speed;

        if (length < stoppingDistance) {
            speed = 0;
        } else {
            speed = length * 0.0285 * maxSpeed;
        }


        if (speed > maxSpeed) {
            speed = maxSpeed;
        }
        double speedDiff = speed - velocity;
        double accelerationStep;

        if (speedDiff < 0) { // decelerate
            accelerationStep = Math.max(speedDiff, -maxSpeed / 30.0);
        } else { // accelerate
            accelerationStep = Math.min(speedDiff, maxSpeed / 60.0);
        }
        return accelerationStep;
    }


    double normalizeAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }


    /// Helper method for update() which updates the direction
    /// of the player's head node and if an up key is pressed
    /// the player moves in the updated direction.
    /// Returns a Point2D.Double object representing the change in x and y
    /// of the head node.
    private Point2D.Double keyMove() {
        // Check horizontal movement
        double dir = 0;

        Point2D.Double move = new Point2D.Double(0, 0);

        if (keyH.upPressed) {
            if (keyH.leftPressed) {
                dir -= maxAngle * velocity / maxSpeed;
            }
            if (keyH.rightPressed) {
                dir += maxAngle * velocity / maxSpeed;
            }
            if (velocity < maxSpeed) {
                velocity += maxSpeed / 60.0;
            }
            if (velocity > maxSpeed) {
                velocity = maxSpeed;
            }

        } else {
            if (velocity > 0) {
                velocity -= maxSpeed / 60.0;
            }
            if (velocity < 0) {
                velocity = 0;
            }
        }

        if (velocity > 0) {
            // Update direction of head node
            body[0].direction += dir;

            move = new Point2D.Double(Math.cos(body[0].direction), Math.sin(body[0].direction));
        }

        return move;
    }

    /// Moves the player's head node by the amounts specified in
    /// the Point2D.Double object that is passed.
    /// Also updates the position and direction of any subsequent nodes in the players body.
    public void movePlayer(Point2D.Double move) {
        body[0].x += move.getX() * velocity;
        body[0].y += move.getY() * velocity;


        for (int i = 1; i < body.length; i++) {
            double xDistance = body[i-1].x - body[i].x;
            double yDistance = body[i-1].y - body[i].y;
            double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

            double targetDistance = distanceBetweenNodes;
            if (distance > targetDistance) {
                double moveFactor = (distance - targetDistance) / distance;
                body[i].x += xDistance * moveFactor;
                body[i].y += yDistance * moveFactor;
                body[i].direction = atan2( yDistance, xDistance );
            }
        }
    }

    /**
     Main method for drawing the player.
    */
    public void draw(Graphics2D g) {

        creature.draw(g);


    }


}
