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

public class Sword {
    double angle = 0;
    double swingAngle = Math.toRadians(45);

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

    public int update(Player player, Enemy[] enemies) {
        angle = player.angle;
        int killed = 0;

        if (!attacking) {
            swingAngle = Math.toRadians(45);
            return 0;
        }

        attackFrame++;

        double t = attackFrame / 15.0;
        swingAngle = Math.toRadians(67.5 - t * 135.0);

        double swordWorldAngle = angle + swingAngle;

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

        if (attackFrame >= 15) {
            attacking = false;
            swingAngle = Math.toRadians(45);
        }

        return killed;
    }

    public void draw(Graphics2D g, Player player) {
        Graphics2D g2 = (Graphics2D) g.create();

        // vị trí kiếm bám theo player
        g2.translate(player.x * 1000, player.y * 1000);

        // kiếm quay theo hướng player + góc chém
        g2.rotate(player.angle + swingAngle);

        // đẩy kiếm ra cạnh player
        g2.translate(0.18 * 1000, 0.12 * 1000);

        g2.setColor(new Color(70, 35, 10));
        g2.fillRect(
                (int)(0.00 * 1000),
                (int)(-width * 1000),
                (int)(0.10 * 1000),
                (int)(width * 2 * 1000)
        );

        g2.setColor(new Color(70, 70, 230));
        g2.fillRect(
                (int)(0.10 * 1000),
                (int)(-width * 0.6 * 1000),
                (int)((length - 0.10) * 1000),
                (int)(width * 1.2 * 1000)
        );

        Polygon tip = new Polygon();
        tip.addPoint((int)((length + 0.08) * 1000), 0);
        tip.addPoint((int)(length * 1000), (int)(width * 1000));
        tip.addPoint((int)(length * 1000), (int)(-width * 1000));

        g2.fillPolygon(tip);

        g2.dispose();
    }
}
