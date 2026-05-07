package com.mycompany.gametkdh;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author lequo
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
public class GameMenu extends JPanel implements MouseListener {

    BufferedImage background;
    BufferedImage playButton;

    JFrame frame;

    Rectangle playRect;
    Polygon helpTriangle;
    boolean showTutorial = false;
    Rectangle helpRect;
    double helpAngle = 0;
    BufferedImage tutorialImage;
    BufferedImage closeButton;
    BufferedImage darkBackground;

    Rectangle closeRect;
    public GameMenu(JFrame frame) {
        this.frame = frame;

        setPreferredSize(new Dimension(900, 600));

        background = Image.loadImage("/StartMenu.png");
        playButton = Image.loadImage("/PLAY.png");
        darkBackground = Image.loadImage("/Cancle_BG.png");
        tutorialImage = Image.loadImage("/Turto.png");
        closeButton = Image.loadImage("/X.png");
        playRect = new Rectangle(325, 500, 250, 100);

        addMouseListener(this);
    }
    private void updateHelpTriangle() {
        helpRect = new Rectangle(30, 30, 120, 80);
    }

    private void quay(double[] p, double goc) {
        double rad = goc * Math.PI / 180.0;

        double t = p[0];

        p[0] = p[0] * Math.cos(rad) - p[1] * Math.sin(rad);
        p[1] = t * Math.sin(rad) + p[1] * Math.cos(rad);
    }
    private void drawHelpTriangle(Graphics2D g2) {
        updateHelpTriangle();

        int cx = helpRect.x + helpRect.width / 2;
        int cy = helpRect.y + helpRect.height / 2;

        double[] p1 = {50, 0};
        double[] p2 = {-50, -35};
        double[] p3 = {-50, 35};

        quay(p1, Math.toDegrees(helpAngle));
        quay(p2, Math.toDegrees(helpAngle));
        quay(p3, Math.toDegrees(helpAngle));

        Polygon triangle = new Polygon();
        triangle.addPoint((int)(cx + p1[0]), (int)(cy + p1[1]));
        triangle.addPoint((int)(cx + p2[0]), (int)(cy + p2[1]));
        triangle.addPoint((int)(cx + p3[0]), (int)(cy + p3[1]));

        g2.setColor(Color.RED);
        g2.fillPolygon(triangle);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Help", helpRect.x + 120, helpRect.y + 50);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        updateHelpTriangle();

        // background full màn hình
        g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        // nút play
        g2.drawImage(
                playButton,
                playRect.x,
                playRect.y,
                playRect.width,
                playRect.height,
                null
        );

        // tam giác Help góc trái trên
        drawHelpTriangle(g2);

        // nếu đang bật tutorial thì vẽ ảnh Turto
        if (showTutorial) {

            // background đen
        g2.drawImage(
                darkBackground,
                0,
                0,
                getWidth(),
                getHeight(),
                null
        );
        int tutoX = getWidth() / 2 - 300;
        int tutoY = getHeight() / 2 - 200;

        int tutoW = 600;
        int tutoH = 400;

        // ảnh tutorial
        g2.drawImage(
                tutorialImage,
                tutoX,
                tutoY,
                tutoW,
                tutoH,
                null
        );

        // nút X
        closeRect = new Rectangle(
                tutoX - 200,   // bên trái bảng
                tutoY - 200,   // phía trên bảng
                600,           // rộng hơn
                500            // cao hơn
        );

        g2.drawImage(
                closeButton,
                closeRect.x,
                closeRect.y,
                closeRect.width,
                closeRect.height,
                null
        );
    }
    }

   @Override
    public void mousePressed(MouseEvent e) {

        // bấm nút X
        if (showTutorial &&
            closeRect != null &&
            closeRect.contains(e.getPoint())) {

            showTutorial = false;
            helpAngle = 0; // quay lại hướng sang phải
            repaint();
            return;
        }

        // bấm Help
        if (helpRect != null && helpRect.contains(e.getPoint())) {
            showTutorial = true;
            helpAngle = Math.toRadians(90); // quay xuống
            repaint();
            return;
        }

        // bấm Play
        if (playRect.contains(e.getPoint())) {

            GameTKDH game = new GameTKDH(frame);

            frame.setContentPane(game);
            frame.revalidate();
            frame.repaint();

            game.requestFocusInWindow();
            game.startGame();
        }
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
