package com.mycompany.gametkdh;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameOverScreen {
    BufferedImage gameOverImage;
    BufferedImage restartButton;
    BufferedImage homeButton;

    Rectangle restartRect;
    Rectangle homeRect;

    public GameOverScreen() {
        gameOverImage = Image.loadImage("/GameOver.png");
        restartButton = Image.loadImage("/X.png");
        homeButton = Image.loadImage("/Home.png");
    }

    public void draw(Graphics2D g, int screenWidth, int screenHeight, int score) {
        Graphics2D g2 = (Graphics2D) g.create();

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // nền tối
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // ảnh Game Over
        int imgW = 700;
        int imgH = 400;

        g2.drawImage(
                gameOverImage,
                centerX - imgW / 2,
                centerY - 220,
                imgW,
                imgH,
                null
        );

        // điểm coin
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.drawString("Coins: " + score, centerX - 90, centerY - 30);

        // nút Restart
        restartRect = new Rectangle(centerX - 550, centerY -200, 700, 500);

        g2.drawImage(
                restartButton,
                restartRect.x,
                restartRect.y,
                restartRect.width,
                restartRect.height,
                null
        );

        // nút Home
        homeRect = new Rectangle(centerX - 100, centerY - 150, 700, 500);

        g2.drawImage(
                homeButton,
                homeRect.x,
                homeRect.y,
                homeRect.width,
                homeRect.height,
                null
        );

        g2.dispose();
    }

    public boolean clickRestart(Point p) {
        return restartRect != null && restartRect.contains(p);
    }

    public boolean clickHome(Point p) {
        return homeRect != null && homeRect.contains(p);
    }
}