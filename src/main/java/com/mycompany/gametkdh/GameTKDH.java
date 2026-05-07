/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gametkdh;

/**
 *
 * @author lequo
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GameTKDH extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener {
    int windowWidth = 900;
    int windowHeight = 600;
    BufferedImage healthImage = Image.loadImage("/Health.png");
    CoinAnimation coinAnimation = new CoinAnimation();
    Player player = new Player();
    Compass compass = new Compass();
    Sword sword = new Sword();

    Enemy[] enemies = new Enemy[10];

    PauseGame pauseGame = new PauseGame();
    int score = 0;

    double cameraX = 0;
    double cameraY = 0;

    Timer timer = new Timer(16, this);

    GameOverScreen gameOverScreen = new GameOverScreen();
    JFrame frame;
    public GameTKDH(JFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setBackground(new Color(220,220,220));
        setFocusable(true);

        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy();
        }

        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);

        //timer.start();
    }
    public void startGame() {
        timer.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // =========================
        // VẼ WORLD: map, enemy, player, sword
        // =========================
        Graphics2D world = (Graphics2D) g.create();
        world.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        world.translate(getWidth() / 2, getHeight() / 2);
        world.scale(getWidth() / 2.0 / 1000.0, -getHeight() / 2.0 / 1000.0);

        // giống glTranslatef(-cameraX, -cameraY, 0)
        world.translate(-cameraX * 1000, -cameraY * 1000);

        drawMap(world);

        for (Enemy e : enemies) {
            e.draw(world);
        }

        if (player.isAlive()) {
            player.draw(world);
            sword.draw(world, player);
        }

        
        world.dispose();


        // =========================
        // VẼ UI: máu, score, la bàn
        // =========================
        Graphics2D ui = (Graphics2D) g.create();
        ui.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ui.translate(getWidth() / 2, getHeight() / 2);
        ui.scale(getWidth() / 2.0 / 1000.0, -getHeight() / 2.0 / 1000.0);

        // KHÔNG có camera ở đây
        drawHealth(ui);
        drawScore(ui);
        pauseGame.draw((Graphics2D) g, getWidth(), getHeight());
        compass.draw(ui, player, enemies);

        if (!player.isAlive()) {
            gameOverScreen.draw((Graphics2D) g, getWidth(), getHeight(), score);
        }

        ui.dispose();
    }

    void drawMap(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);

        for (double i = -5.0; i <= 5.0; i += 0.2) {
            g.drawLine((int)(i * 1000), (int)(-5 * 1000), (int)(i * 1000), (int)(5 * 1000));
            g.drawLine((int)(-5 * 1000), (int)(i * 1000), (int)(5 * 1000), (int)(i * 1000));
        }
    }

    void drawHealth(Graphics2D g) {

        int heartW = 80;
        int heartH = 80;

        int startX = 480;
        int startY = 820;

        for (int i = 0; i < player.hp; i++) {

            int x = startX + i * 85;

            g.drawImage(
                    healthImage,
                    x,
                    startY,
                    heartW,
                    heartH,
                    null
            );
        }
    }

    void drawScore(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.scale(1, -1);

        int coinX = (int)(0.48 * 1000);
        int coinY = (int)(-0.72 * 1000);

        coinAnimation.draw(g2, coinX, coinY - 45, 60, 60);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.drawString("Coins: " + score, coinX + 80, coinY);

        g2.dispose();
    }

    void drawGameOver(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.scale(1, -1);
        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.drawString("GAME OVER", -130, 0);

        g2.dispose();
    }

    void restartGame() {
        player = new Player();
        sword = new Sword();
        compass = new Compass();
        pauseGame = new PauseGame();
        gameOverScreen = new GameOverScreen();

        score = 0;
        cameraX = 0;
        cameraY = 0;

        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy();
        }

        timer.start();
        requestFocusInWindow();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!pauseGame.isPaused() && player.isAlive()) {
            player.update();

            for (Enemy enemy : enemies) {
                enemy.update(player);
            }

            coinAnimation.update();
            score += sword.update(player, enemies);

            cameraX = player.x;
            cameraY = player.y;
        }

        repaint();
    }

    double screenToWorldX(int mouseX) {
        return cameraX + ((double) mouseX / getWidth() * 2.0 - 1.0);
    }

    double screenToWorldY(int mouseY) {
        return cameraY + (1.0 - (double) mouseY / getHeight() * 2.0);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        pauseGame.handleMouseMoved(e.getPoint());
        repaint();

        if (!pauseGame.isPaused()) {
            player.updateAngle(screenToWorldX(e.getX()), screenToWorldY(e.getY()));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!player.isAlive()) {
            if (gameOverScreen.clickRestart(e.getPoint())) {
                restartGame();
                repaint();
                return;
            }

            if (gameOverScreen.clickHome(e.getPoint())) {
                timer.stop();

                GameMenu menu = new GameMenu(frame);

                frame.setContentPane(menu);
                frame.revalidate();
                frame.repaint();

                return;
            }

            return;
        }
        if (pauseGame.handleMousePressed(e.getPoint())) {
            repaint();
            return;
        }
        if (player.isAlive() && e.getButton() == MouseEvent.BUTTON1) {
            player.setTarget(screenToWorldX(e.getX()), screenToWorldY(e.getY()));
        }
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (player.isAlive() && e.getKeyCode() == KeyEvent.VK_SPACE) {
            sword.startAttack();
        }
    }

    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game TKDH");

        GameMenu menu = new GameMenu(frame);

        frame.setContentPane(menu);
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
