package com.mycompany.gametkdh;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Obstacle {
    double x, y, width, height;

    public Obstacle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(100, 100, 100)); // Màu xám cho tường
        g.fillRect((int)(x * 1000), (int)(y * 1000), (int)(width * 1000), (int)(height * 1000));
        
        // Vẽ viền cho rõ nét
        g.setColor(Color.BLACK);
        g.drawRect((int)(x * 1000), (int)(y * 1000), (int)(width * 1000), (int)(height * 1000));
    }

    // Kiểm tra xem một điểm (px, py) có nằm trong tường không
    public boolean contains(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
}