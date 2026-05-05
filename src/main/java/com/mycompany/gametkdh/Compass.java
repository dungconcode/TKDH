/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gametkdh;

import java.awt.*;

public class Compass {
    double x = -0.78;
    double y = -0.72;
    double radius = 0.18;
    void quay(double[] p, double xq, double yq, double goc) {
        double cos = Math.cos(goc);
        double sin = Math.sin(goc);

        double x = p[0];
        double y = p[1];

        p[0] = x * cos - y * sin + (1 - cos) * xq + sin * yq;
        p[1] = x * sin + y * cos - sin * xq + (1 - cos) * yq;
    }
    public void draw(Graphics2D g, Player player, Enemy[] enemies) {
        // vòng tròn la bàn cố định góc trái dưới
        g.setColor(Color.BLACK);
        g.drawOval(
                (int)((x - radius) * 1000),
                (int)((y - radius) * 1000),
                (int)(radius * 2 * 1000),
                (int)(radius * 2 * 1000)
        );

        // kim la bàn nằm trong vòng tròn, quay quanh tâm la bàn x, y
        double angle = player.angle;

        double x1 = x + radius * 0.8;
        double y1 = y;

        double x2 = x - radius * 0.3;
        double y2 = y + radius * 0.25;

        double x3 = x - radius * 0.3;
        double y3 = y - radius * 0.25;

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double x11 = x1 * cos - y1 * sin + (1 - cos) * x + sin * y;
        double y11 = x1 * sin + y1 * cos - sin * x + (1 - cos) * y;

        double x22 = x2 * cos - y2 * sin + (1 - cos) * x + sin * y;
        double y22 = x2 * sin + y2 * cos - sin * x + (1 - cos) * y;

        double x33 = x3 * cos - y3 * sin + (1 - cos) * x + sin * y;
        double y33 = x3 * sin + y3 * cos - sin * x + (1 - cos) * y;

        Polygon needle = new Polygon();
        needle.addPoint((int)(x11 * 1000), (int)(y11 * 1000));
        needle.addPoint((int)(x22 * 1000), (int)(y22 * 1000));
        needle.addPoint((int)(x33 * 1000), (int)(y33 * 1000));

        g.setColor(Color.RED);
        g.fillPolygon(needle);

        // marker enemy màu xanh trên la bàn
        for (Enemy e : enemies) {
            double enemyAngle = Math.atan2(e.y - player.y, e.x - player.x);

            // tam giác gốc (ở phía trên la bàn)
            double[] p1 = {x, y + radius * 0.9};
            double[] p2 = {x - radius * 0.08, y + radius * 0.72};
            double[] p3 = {x + radius * 0.08, y + radius * 0.72};

            // quay quanh tâm la bàn (x, y)
            quay(p1, x, y, enemyAngle - Math.PI / 2);
            quay(p2, x, y, enemyAngle - Math.PI / 2);
            quay(p3, x, y, enemyAngle - Math.PI / 2);

            Polygon p = new Polygon();
            p.addPoint((int)(p1[0] * 1000), (int)(p1[1] * 1000));
            p.addPoint((int)(p2[0] * 1000), (int)(p2[1] * 1000));
            p.addPoint((int)(p3[0] * 1000), (int)(p3[1] * 1000));

            g.setColor(Color.CYAN);
            g.fillPolygon(p);
        }

        // chữ hướng
        Graphics2D text = (Graphics2D) g.create();
        text.scale(1, -1);
        text.setColor(Color.BLACK);
        text.setFont(new Font("Arial", Font.BOLD, 18));

        text.drawString("N", (int)((x - 0.02) * 1000), (int)(-(y + radius + 0.06) * 1000));
        text.drawString("S", (int)((x - 0.02) * 1000), (int)(-(y - radius - 0.08) * 1000));
        text.drawString("E", (int)((x + radius + 0.03) * 1000), (int)(-y * 1000));
        text.drawString("W", (int)((x - radius - 0.05) * 1000), (int)(-y * 1000));

        text.dispose();
    }
}
