/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gametkdh;

import java.awt.*;
import java.util.ArrayList;

public class Sword {
    double angle = 0;
    double swingAngle = Math.toRadians(45);
    double spinAngle = 0;
    
    boolean attacking = false;
    int attackFrame = 0;

    double length = 0.45;
    double width = 0.035;
    double range = 0.55;

    public void startAttack() {
        if (!attacking) {
            attacking = true;
            attackFrame = 0;
        }
    }

    public int update(Player player, Enemy[] enemies, ArrayList<Boss> bosses) {
        angle = player.angle;
        int killed = 0;
        
        if (player.invincibleTimer > 0) {
            spinAngle += 0.2; // Tốc độ quay 
            if (spinAngle > Math.PI * 2) spinAngle -= Math.PI * 2;
            
            // va chạm quái thường
            for (Enemy e : enemies) {
                double dist = MathUtils.distance(player.x, player.y, e.x, e.y);
                if (dist <= range) {
                    e.spawnRandom();
                    killed++;
                }
            }
            // Xử lý va chạm với Boss
            for (int i = bosses.size() - 1; i >= 0; i--) {
                Boss b = bosses.get(i);
                double dist = MathUtils.distance(player.x, player.y, b.x, b.y);
                if (dist <= range) {
                    if (b.takeDamage()) { // Kiểm tra nhận sát thương
                        bosses.remove(i);
                        killed++; 
                    }
                }
            }
            
            return killed; // Đang bất tử bỏ qua đoạn chém thường 
        }
        
        spinAngle = 0; // reset góc xoay
        
        if (!attacking) {
            swingAngle = Math.toRadians(45);
            return 0;
        }

        attackFrame++;

        double t = attackFrame / 15.0;
        swingAngle = Math.toRadians(67.5 - t * 135.0);

        double swordWorldAngle = angle + swingAngle;
        
        // quái thường
        for (Enemy e : enemies) {
            double dist = MathUtils.distance(player.x, player.y, e.x, e.y);
            if (dist > range) continue;

            double enemyAngle = MathUtils.angleTo(player.x, player.y, e.x, e.y);
            double diff = MathUtils.normalizeAngle(enemyAngle - swordWorldAngle);

            if (Math.abs(diff) <= Math.toRadians(67.5)) {
                e.spawnRandom();
                killed++;
            }
        }
        
        // Boss
        for (int i = bosses.size() - 1; i >= 0; i--) {
            Boss b = bosses.get(i);
            double dist = MathUtils.distance(player.x, player.y, b.x, b.y);
            if (dist > range) continue;

            double bossAngle = MathUtils.angleTo(player.x, player.y, b.x, b.y);
            double diff = MathUtils.normalizeAngle(bossAngle - swordWorldAngle);

            if (Math.abs(diff) <= Math.toRadians(67.5)) {
                if (b.takeDamage()) { // Kiểm tra nhận sát thương
                    bosses.remove(i);
                    player.invincibleTimer = 300; 
                    killed++;
                }
            }
        }
        
        if (attackFrame >= 15) {
            attacking = false;
            swingAngle = Math.toRadians(45);
        }

        return killed;
    }
    
    void bienDoi(double[] p, double playerX, double playerY, double goc) {
        double cos = Math.cos(goc);
        double sin = Math.sin(goc);

        double lx = p[0];
        double ly = p[1];

        p[0] = lx * cos - ly * sin + playerX;
        p[1] = lx * sin + ly * cos + playerY;
    }
    
    public void draw(Graphics2D g, Player player) {
        double totalAngle = player.angle;
        if (player.invincibleTimer > 0) {
            totalAngle += spinAngle;
        } else {
            totalAngle += swingAngle;
        }

        double offsetX = 0.18;
        double offsetY = 0.12;

        // chuôi kiếm
        double[] h1 = {offsetX + 0.00, offsetY - width};
        double[] h2 = {offsetX + 0.10, offsetY - width};
        double[] h3 = {offsetX + 0.10, offsetY + width};
        double[] h4 = {offsetX + 0.00, offsetY + width};

        bienDoi(h1, player.x, player.y, totalAngle);
        bienDoi(h2, player.x, player.y, totalAngle);
        bienDoi(h3, player.x, player.y, totalAngle);
        bienDoi(h4, player.x, player.y, totalAngle);

        Polygon handle = new Polygon();
        handle.addPoint((int)(h1[0] * 1000), (int)(h1[1] * 1000));
        handle.addPoint((int)(h2[0] * 1000), (int)(h2[1] * 1000));
        handle.addPoint((int)(h3[0] * 1000), (int)(h3[1] * 1000));
        handle.addPoint((int)(h4[0] * 1000), (int)(h4[1] * 1000));

        g.setColor(new Color(70, 35, 10));
        g.fillPolygon(handle);

        // lưỡi kiếm
        double[] b1 = {offsetX + 0.10, offsetY - width * 0.6};
        double[] b2 = {offsetX + length, offsetY - width * 0.6}; 
        double[] b3 = {offsetX + length, offsetY + width * 0.6};
        double[] b4 = {offsetX + 0.10, offsetY + width * 0.6};

        bienDoi(b1, player.x, player.y, totalAngle);
        bienDoi(b2, player.x, player.y, totalAngle);
        bienDoi(b3, player.x, player.y, totalAngle);
        bienDoi(b4, player.x, player.y, totalAngle);

        Polygon blade = new Polygon();
        blade.addPoint((int)(b1[0] * 1000), (int)(b1[1] * 1000));
        blade.addPoint((int)(b2[0] * 1000), (int)(b2[1] * 1000));
        blade.addPoint((int)(b3[0] * 1000), (int)(b3[1] * 1000));
        blade.addPoint((int)(b4[0] * 1000), (int)(b4[1] * 1000));

        g.setColor(new Color(70, 70, 230));
        g.fillPolygon(blade);

        // vẽ mũi kiếm
        double[] t1 = {offsetX + length + 0.08, offsetY};
        double[] t2 = {offsetX + length, offsetY + width};
        double[] t3 = {offsetX + length, offsetY - width};

        bienDoi(t1, player.x, player.y, totalAngle);
        bienDoi(t2, player.x, player.y, totalAngle);
        bienDoi(t3, player.x, player.y, totalAngle);

        Polygon tip = new Polygon();
        tip.addPoint((int)(t1[0] * 1000), (int)(t1[1] * 1000));
        tip.addPoint((int)(t2[0] * 1000), (int)(t2[1] * 1000));
        tip.addPoint((int)(t3[0] * 1000), (int)(t3[1] * 1000));

        g.fillPolygon(tip); 
    }
}