package com.mycompany.gametkdh;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Boss {
    double x, y;
    double size = 0.15;
    double attackRange = 2.5; 
    
    double moveTargetX, moveTargetY;
    double speed = 0.003;
    double angle = 0; // Hướng mặt của Boss
    
    int cooldown = 0;
    int maxCooldown = 60; 
    
    static Random rand = new Random();

    BufferedImage image = Image.loadImage("/Boss.png");

    public Boss() {
        x = -4.0 + rand.nextDouble() * 8.0;
        y = -4.0 + rand.nextDouble() * 8.0;
        setRandomTarget();
    }

    private void setRandomTarget() {
        moveTargetX = -4.0 + rand.nextDouble() * 8.0;
        moveTargetY = -4.0 + rand.nextDouble() * 8.0;
        angle = MathUtils.angleTo(x, y, moveTargetX, moveTargetY);
    }

    public void update(Player player, ArrayList<Shuriken> shurikens) {
        double dx = moveTargetX - x;
        double dy = moveTargetY - y;
        double distToTarget = Math.sqrt(dx * dx + dy * dy);

        if (distToTarget < speed) {
            setRandomTarget();
        } else {
            x += Math.cos(angle) * speed;
            y += Math.sin(angle) * speed;
        }

        if (cooldown > 0) cooldown--;
        
        double distToPlayer = MathUtils.distance(x, y, player.x, player.y);
        double angleToPlayer = MathUtils.angleTo(x, y, player.x, player.y);
        double angleDiff = Math.abs(MathUtils.normalizeAngle(angleToPlayer - angle));

        if (distToPlayer <= attackRange && angleDiff <= Math.toRadians(45) && cooldown <= 0) {
            shurikens.add(new Shuriken(x, y, player.x, player.y));
            cooldown = maxCooldown; 
        }
    }

    public void draw(Graphics2D g) {
        if (image == null) return;

        // Kích thước vẽ
        int drawW = (int)(size * 3 * 1000);
        int drawH = (int)(size * 3 * 1000);

        AffineTransform oldAt = g.getTransform();

        // Di chuyển đến tâm Boss
        g.translate(x * 1000, y * 1000);
        
        // Xoay ảnh theo hướng di chuyển của Boss
        g.rotate(angle + Math.PI/2); 

        // Vẽ ảnh (tâm ảnh tại gốc tọa độ mới)
        g.drawImage(image, -drawW / 2, -drawH / 2, drawW, drawH, null);

        g.setTransform(oldAt);

    }
}