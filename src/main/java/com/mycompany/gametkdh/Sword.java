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
import java.util.ArrayList; // THÊM THƯ VIỆN NÀY

public class Sword {
    double angle = 0;
    double swingAngle = Math.toRadians(45);
    double spinAngle = 0; // Góc xoay 360 độ khi có buff

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

    // CẬP NHẬT TRUYỀN THÊM DANH SÁCH BOSS VÀO ĐÂY
    public int update(Player player, Enemy[] enemies, ArrayList<Boss> bosses) {
        angle = player.angle;
        int killed = 0;
        double swordWorldAngle = 0;
        boolean activeHitbox = false;

        // XỬ LÝ BUFF: Xoay kiếm liên tục nếu đang bất tử
        if (player.invincibleTimer > 0) {
            spinAngle += Math.toRadians(15); // Tốc độ xoay
            if (spinAngle > Math.PI * 2) spinAngle -= Math.PI * 2;
            swordWorldAngle = angle + spinAngle;
            activeHitbox = true; // Kiếm luôn trong trạng thái gây sát thương
        } 
        // XỬ LÝ CHÉM BÌNH THƯỜNG
        else {
            spinAngle = 0;
            if (!attacking) {
                swingAngle = Math.toRadians(45);
                return 0;
            }
            attackFrame++;
            double t = attackFrame / 15.0;
            swingAngle = Math.toRadians(67.5 - t * 135.0);
            swordWorldAngle = angle + swingAngle;
            activeHitbox = true;

            if (attackFrame >= 15) {
                attacking = false;
                swingAngle = Math.toRadians(45);
                activeHitbox = false;
            }
        }

        // KIỂM TRA VA CHẠM (GIẾT ĐỊCH VÀ BOSS)
        if (activeHitbox) {
            double hitAngle = (player.invincibleTimer > 0) ? Math.toRadians(90) : Math.toRadians(67.5);

            for (Enemy e : enemies) {
                double dist = MathUtils.distance(player.x, player.y, e.x, e.y);
                if (dist > range) continue;

                double enemyAngle = MathUtils.angleTo(player.x, player.y, e.x, e.y);
                double diff = MathUtils.normalizeAngle(enemyAngle - swordWorldAngle);

                if (Math.abs(diff) <= hitAngle) {
                    e.spawnRandom();
                    killed++;
                }
            }

            // Kiểm tra chém Boss
            for (int i = bosses.size() - 1; i >= 0; i--) {
                Boss b = bosses.get(i);
                double dist = MathUtils.distance(player.x, player.y, b.x, b.y);
                if (dist > range) continue;

                double bossAngle = MathUtils.angleTo(player.x, player.y, b.x, b.y);
                double diff = MathUtils.normalizeAngle(bossAngle - swordWorldAngle);

                if (Math.abs(diff) <= hitAngle) {
                    bosses.remove(i); // Tiêu diệt Boss
                    player.invincibleTimer = 300; // CẤP BUFF: 300 frame = 5 giây (60fps)
                }
            }
        }

        return killed;
    }

    public void draw(Graphics2D g, Player player) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(player.x * 1000, player.y * 1000);

        // Đổi góc vẽ tùy thuộc vào việc có buff hay không
        if (player.invincibleTimer > 0) {
            g2.rotate(player.angle + spinAngle);
        } else {
            g2.rotate(player.angle + swingAngle);
        }

        g2.translate(0.18 * 1000, 0.12 * 1000);

        g2.setColor(new Color(70, 35, 10));
        g2.fillRect((int)(0.00 * 1000), (int)(-width * 1000), (int)(0.10 * 1000), (int)(width * 2 * 1000));
        g2.setColor(new Color(70, 70, 230));
        g2.fillRect((int)(0.10 * 1000), (int)(-width * 0.6 * 1000), (int)((length - 0.10) * 1000), (int)(width * 1.2 * 1000));
        Polygon tip = new Polygon();
        tip.addPoint((int)((length + 0.08) * 1000), 0);
        tip.addPoint((int)(length * 1000), (int)(width * 1000));
        tip.addPoint((int)(length * 1000), (int)(-width * 1000));
        g2.fillPolygon(tip);
        g2.dispose();
    }
}
