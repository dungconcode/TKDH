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
    BufferedImage pauseBackground;

    boolean paused = false;
    boolean hoverResume = false;

    Rectangle pauseRect;
    Polygon resumeTriangle;

    public PauseGame() {
        pauseButton = Image.loadImage("/pause_btn.png");
        pauseBackground = Image.loadImage("/pause_sence.png");
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
        pauseRect = new Rectangle(20, 20, 60, 60);

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

        g.drawImage(
            pauseBackground,
            0,
            0,
            screenWidth,
            screenHeight,
            null
        );
        int cx = screenWidth / 2;
        int cy = screenHeight / 2;

        double scale = hoverResume ? 1.5 : 1.0;

        // 3 điểm gốc
        int x1 = cx + 35;
        int y1 = cy;

        int x2 = cx - 25;
        int y2 = cy - 35;

        int x3 = cx - 25;
        int y3 = cy + 35;

        // áp dụng phép tỷ lệ quanh tâm tam giác
        x1 = scaleX(x1, cx, scale);
        y1 = scaleY(y1, cy, scale);

        x2 = scaleX(x2, cx, scale);
        y2 = scaleY(y2, cy, scale);

        x3 = scaleX(x3, cx, scale);
        y3 = scaleY(y3, cy, scale);

        resumeTriangle = new Polygon();
        resumeTriangle.addPoint(x1, y1);
        resumeTriangle.addPoint(x2, y2);
        resumeTriangle.addPoint(x3, y3);

        g.setColor(Color.RED);
        g.fillPolygon(resumeTriangle);
    }
    
    public void handleMouseMoved(Point p) {
        if (paused && resumeTriangle != null) {
            hoverResume = resumeTriangle.contains(p);
        } else {
            hoverResume = false;
        }
    }
    
    private int scaleX(int x, int cx, double s) {
        return (int)(cx + (x - cx) * s);
    }

    private int scaleY(int y, int cy, double s) {
        return (int)(cy + (y - cy) * s);
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
