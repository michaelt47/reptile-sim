package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Creature;
import entity.Lizard;
import entity.Player;
import entity.Snake;

public class GamePanel extends JPanel implements Runnable{
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 24; // x
    final int maxScreenRow = 18; // y; 4:3 ratio
    final int screenWidth = tileSize * maxScreenCol; // 768 px
    final int screenHeight = tileSize * maxScreenRow; // 576 px

    MouseHandler mouseH = new MouseHandler();
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    Creature creature = new Creature(this);
    Lizard lizard = new Lizard(this);
    Snake snake = new Snake(this);
    Player player = new Player(snake, keyH, mouseH);

    int FPS = 60;
    public boolean cursorMovement = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.lightGray);
        this.setDoubleBuffered(true); // change to false in order to test if it is faster or not
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.setFocusable(true); // might want to change this
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {

        long currentTime;
        double drawInterval = 1000000000/FPS;
        double delta = 0; // allows for sub-pixel shit if wanted
        long lastTime = System.nanoTime();
//		long timer = 0;
//		int drawCount = 0; // fps display



        while(gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
//			timer += (currentTime - lastTime);
            lastTime = currentTime;


            if (delta >= 1) {
                update();
                repaint(); // calls the paintComponent method; built-in java feature
                delta--;
//				drawCount++;
            }

//			if (timer >= 1000000000) {
//				System.out.println("FPS: " + drawCount);
//				drawCount = 0;
//				timer = 0;
//			}


        }

    }


    public void update() {
        player.update();

    }




    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        player.draw(g2);

        g2.dispose();
    }
}
