/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gametkdh;
import java.awt.*;
/**
 *
 * @author lequo
 */
public class Player {
    double x = 0;
    double y = 0;
    double size = 0.15;

    double angle = 0;
    double aimAngle = 0;

    double targetX = 0;
    double targetY = 0;

    double speed = 0.01;
    boolean isMoving = false;

    int hp = 5;

    public void updateAngle(double mouseX, double mouseY) {
        aimAngle = MathUtils.angleTo(x, y, mouseX, mouseY);
    }

    public void setTarget(double tx, double ty) {
        targetX = tx;
        targetY = ty;
        isMoving = true;
        angle = MathUtils.angleTo(x, y, targetX, targetY);
    }

    public void update() {
        if (!isMoving || hp <= 0) return;

        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < speed) {
            x = targetX;
            y = targetY;
            isMoving = false;
            return;
        }

        x += dx / dist * speed;
        y += dy / dist * speed;
    }

    public void takeDamage() {
        if (hp > 0) hp--;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void draw(Graphics2D g) {
        if (!isAlive()) return;

        Graphics2D g2 = (Graphics2D) g.create();

        // sửa chỗ này
        g2.translate(x * 1000, y * 1000);
        g2.rotate(angle);

        // thân
        g2.setColor(Color.GREEN);
        g2.fillOval(
                (int)(-size * 1000),
                (int)(-size * 1000),
                (int)(size * 2 * 1000),
                (int)(size * 2 * 1000)
        );

        // mắt
        g2.setColor(Color.BLACK);
        double eyeR = size * 0.13;

        g2.fillOval(
                (int)((size * 0.35 - eyeR) * 1000),
                (int)((size * 0.35 - eyeR) * 1000),
                (int)(eyeR * 2 * 1000),
                (int)(eyeR * 2 * 1000)
        );

        g2.fillOval(
                (int)((size * 0.35 - eyeR) * 1000),
                (int)((-size * 0.35 - eyeR) * 1000),
                (int)(eyeR * 2 * 1000),
                (int)(eyeR * 2 * 1000)
        );

        g2.dispose();

        drawAimTriangle(g);
    }

    private void drawAimTriangle(Graphics2D g) {
        double RADS = 0.017453293;
        double anpha = RADS * Math.toDegrees(aimAngle); // đổi về độ giống C++

        // ===== 3 đỉnh tam giác gốc =====
        double offset = size * 2;
        double scale = 0.5;

        double x1 = x + offset;
        double y1 = y;

        double x2 = x + offset - size * scale;
        double y2 = y + size * scale * 0.6;

        double x3 = x + offset - size * scale;
        double y3 = y - size * scale * 0.6;

        // ===== ÁP DỤNG CÔNG THỨC GIỐNG C++ =====

        double x11 = x1 * Math.cos(anpha) - y1 * Math.sin(anpha)
                   + (1 - Math.cos(anpha)) * x
                   + Math.sin(anpha) * y;

        double y11 = x1 * Math.sin(anpha) + y1 * Math.cos(anpha)
                   - Math.sin(anpha) * x
                   + (1 - Math.cos(anpha)) * y;

        double x22 = x2 * Math.cos(anpha) - y2 * Math.sin(anpha)
                   + (1 - Math.cos(anpha)) * x
                   + Math.sin(anpha) * y;

        double y22 = x2 * Math.sin(anpha) + y2 * Math.cos(anpha)
                   - Math.sin(anpha) * x
                   + (1 - Math.cos(anpha)) * y;

        double x33 = x3 * Math.cos(anpha) - y3 * Math.sin(anpha)
                   + (1 - Math.cos(anpha)) * x
                   + Math.sin(anpha) * y;

        double y33 = x3 * Math.sin(anpha) + y3 * Math.cos(anpha)
                   - Math.sin(anpha) * x
                   + (1 - Math.cos(anpha)) * y;

        // ===== VẼ =====
        Polygon p = new Polygon();
        p.addPoint((int)(x11 * 1000), (int)(y11 * 1000));
        p.addPoint((int)(x22 * 1000), (int)(y22 * 1000));
        p.addPoint((int)(x33 * 1000), (int)(y33 * 1000));

        g.setColor(Color.RED);
        g.fillPolygon(p);
    }
}
