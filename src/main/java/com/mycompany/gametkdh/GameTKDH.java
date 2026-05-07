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
import java.util.ArrayList;

public class GameTKDH extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener {
    int windowWidth = 900;
    int windowHeight = 600;

    Player player = new Player();
    Compass compass = new Compass();
    Sword sword = new Sword();

    Enemy[] enemies = new Enemy[10];
    
    ArrayList<Boss> bosses = new ArrayList<>();
    ArrayList<Shuriken> shurikens = new ArrayList<>();
    
    int score = 0;

    double cameraX = 0;
    double cameraY = 0;

    Timer timer = new Timer(16, this);

    public GameTKDH() {
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setBackground(Color.WHITE);
        setFocusable(true);

        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy();
        }

        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);

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
        
        // VẼ BOSS VÀ PHI TIÊU
        for (Boss b : bosses) {
            b.draw(world);
        }
        for (Shuriken s : shurikens) {
            s.draw(world);
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
        compass.draw(ui, player, enemies);

        if (!player.isAlive()) {
            drawGameOver(ui);
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
        double startX = 0.48;
        double startY = 0.85;
        double r = 0.035;

        g.setColor(Color.RED);

        for (int i = 0; i < player.hp; i++) {
            double cx = startX + i * 0.1;
            double cy = startY;

            g.fillOval(
                    (int)((cx - r) * 1000),
                    (int)((cy - r) * 1000),
                    (int)(r * 2 * 1000),
                    (int)(r * 2 * 1000)
            );
        }
    }

    void drawScore(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.scale(1, -1);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Score: " + score, (int)(0.55 * 1000), (int)(-0.72 * 1000));

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (player.isAlive()) {
            player.update();

            for (Enemy enemy : enemies) {
                enemy.update(player);
            }

            // sửa tính score
            int previousScore = score;
            score += sword.update(player, enemies, bosses);

            // sinh boss
            if (score / 5 > previousScore / 5) {
                int numberOfNewBosses = (score / 5) - (previousScore / 5);
                for (int i = 0; i < numberOfNewBosses; i++) {
                    bosses.add(new Boss());
                }
            }
            // Cập nhật Boss
            for (Boss b : bosses) {
                b.update(player, shurikens);
            }

            // Cập nhật Phi tiêu 
            for (int i = shurikens.size() - 1; i >= 0; i--) {
                Shuriken s = shurikens.get(i);
                s.update(player);
                if (!s.active) {
                    shurikens.remove(i);
                }
            }

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
        player.updateAngle(screenToWorldX(e.getX()), screenToWorldY(e.getY()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
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
        JFrame frame = new JFrame("Game TKDH - Player, Enemy, Sword, Compass");
        GameTKDH game = new GameTKDH();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.requestFocusInWindow();
    }
}
