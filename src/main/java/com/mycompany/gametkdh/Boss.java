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
    
    // Thêm máu và thời gian hồi sát thương
    int hp = 2; 
    int hitCooldown = 0; 
    
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

    // Hàm nhận sát thương
    public boolean takeDamage() {
        if (hitCooldown > 0) return false; // Miễn nhiễm sát thương khi đang nhấp nháy
        
        hp--;
        hitCooldown = 30; // Boss sẽ nhấp nháy trong 30 khung hình (~0.5 giây)
        
        return hp <= 0; // Trả về true nếu Boss chết
    }

    public void update(Player player, ArrayList<Shuriken> shurikens) {
        if (hitCooldown > 0) hitCooldown--; // Giảm thời gian chờ
        
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

        // HIỆU ỨNG NHẤP NHÁY
        // Nếu đang trong thời gian hitCooldown, cứ mỗi 10 frame thì 5 frame sẽ bị ẩn đi (không vẽ)
        if (hitCooldown > 0 && (hitCooldown % 10 < 5)) {
            return; 
        }

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