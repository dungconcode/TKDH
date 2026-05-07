package com.mycompany.gametkdh;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Shuriken {
    double x, y;
    double speed = 0.015;
    double size = 0.03; // bán kính va chạm
    double angle;
    boolean active = true;

    BufferedImage image = Image.loadImage("/Shuriken.png");

    public Shuriken(double startX, double startY, double targetX, double targetY) {
        this.x = startX;
        this.y = startY;
        this.angle = MathUtils.angleTo(startX, startY, targetX, targetY);
    }

    public void update(Player player) {
        if (!active) return;
        
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;

        double dist = MathUtils.distance(x, y, player.x, player.y);
        if (dist < size + player.size) {
            player.takeDamage();
            active = false;
        }

        if (x < -6.0 || x > 6.0 || y < -6.0 || y > 6.0) {
            active = false;
        }
    }

    public void draw(Graphics2D g) {
        if (!active || image == null) return;

        // Kích thước vẽ (đường kính)
        int drawW = (int)(size * 3 * 1000);
        int drawH = (int)(size * 3 * 1000);

        // Lưu trạng thái Graphics cũ
        AffineTransform oldAt = g.getTransform();

        // Di chuyển hệ tọa độ đến tâm phi tiêu
        g.translate(x * 1000, y * 1000);
        
        // Xoay ảnh theo hướng bay (cộng thêm 90 độ nếu ảnh gốc quay lên trên)
        g.rotate(angle + Math.PI/2); 

        // Vẽ ảnh sao cho tâm ảnh nằm đúng tọa độ (x, y)
        g.drawImage(image, -drawW / 2, -drawH / 2, drawW, drawH, null);

        // Khôi phục trạng thái Graphics
        g.setTransform(oldAt);
    }
}