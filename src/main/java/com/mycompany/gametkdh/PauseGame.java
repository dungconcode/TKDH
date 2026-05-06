/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gametkdh;

/**
 *
 * @author lequo
 */
import java.awt.*;
import java.awt.image.BufferedImage;

public class PauseGame {
    BufferedImage pauseButton;

    boolean paused = false;

    Rectangle pauseRect;
    Polygon resumeTriangle;

    public PauseGame() {
        pauseButton = Image.loadImage("/pause_btn.png");
    }

    public boolean isPaused() {
        return paused;
    }

    public void draw(Graphics2D g, int screenWidth, int screenHeight) {
        drawPauseButton(g, screenWidth);

        if (paused) {
            drawPauseScreen(g, screenWidth, screenHeight);
        }
    }

    private void drawPauseButton(Graphics2D g, int screenWidth) {
        pauseRect = new Rectangle(screenWidth - 90, 20, 60, 60);

        g.drawImage(
                pauseButton,
                pauseRect.x,
                pauseRect.y,
                pauseRect.width,
                pauseRect.height,
                null
        );
    }

    private void drawPauseScreen(Graphics2D g, int screenWidth, int screenHeight) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("PAUSED", screenWidth / 2 - 100, screenHeight / 2 - 80);

        int cx = screenWidth / 2;
        int cy = screenHeight / 2 + 60;

        resumeTriangle = new Polygon();
        resumeTriangle.addPoint(cx + 70, cy);
        resumeTriangle.addPoint(cx - 50, cy - 70);
        resumeTriangle.addPoint(cx - 50, cy + 70);

        g.setColor(Color.RED);
        g.fillPolygon(resumeTriangle);
    }

    public boolean handleMousePressed(Point p) {
        if (paused) {
            if (resumeTriangle != null && resumeTriangle.contains(p)) {
                paused = false;
                return true;
            }
            return true;
        }

        if (pauseRect != null && pauseRect.contains(p)) {
            paused = true;
            return true;
        }

        return false;
    }
}
