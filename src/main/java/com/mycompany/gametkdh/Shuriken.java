/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gametkdh;

import java.awt.*;

public class Shuriken {
    double x, y;
    double speed = 0.015;
    double size = 0.04; // Kích thước của phi tiêu
    double angle;       // Hướng bay của phi tiêu tới mục tiêu
    double spinAngle = 0; // Góc quay tròn quanh trục của phi tiêu

    boolean active = true;

    public Shuriken(double startX, double startY, double targetX, double targetY) {
        this.x = startX;
        this.y = startY;
        this.angle = MathUtils.angleTo(startX, startY, targetX, targetY);
    }

    // Hàm quay điểm p quanh tâm (xq, yq) một góc goc - Áp dụng đúng công thức trong tài liệu
    void quay(double[] p, double xq, double yq, double goc) {
        double cos = Math.cos(goc);
        double sin = Math.sin(goc);

        double px = p[0];
        double py = p[1];

        p[0] = px * cos - py * sin + (1 - cos) * xq + sin * yq;
        p[1] = px * sin + py * cos - sin * xq + (1 - cos) * yq;
    }

    public void update(Player player) {
        if (!active) return;
        
        // Cập nhật vị trí bay thẳng
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;

        // Tăng góc xoay để tạo hiệu ứng quay tròn (tăng khoảng 0.4 radian mỗi frame)
        spinAngle += 0.4;
        if (spinAngle > Math.PI * 2) {
            spinAngle -= Math.PI * 2;
        }

        // Xử lý va chạm
        double dist = MathUtils.distance(x, y, player.x, player.y);
        if (dist < size + player.size) {
            player.takeDamage();
            active = false;
        }

        // Xóa phi tiêu khi ra khỏi map
        if (x < -6.0 || x > 6.0 || y < -6.0 || y > 6.0) {
            active = false;
        }
    }

    public void draw(Graphics2D g) {
        if (!active) return;

        // Tâm quay chính là vị trí hiện tại của phi tiêu
        double xq = x;
        double yq = y;

        // Khởi tạo 8 đỉnh của phi tiêu (tạo hình ngôi sao 4 cánh kiểu Ninja)
        double[] p1 = {x, y + size};           // Mũi nhọn trên
        double[] p2 = {x + size * 0.25, y + size * 0.25}; // Góc lõm trên phải
        double[] p3 = {x + size, y};           // Mũi nhọn phải
        double[] p4 = {x + size * 0.25, y - size * 0.25}; // Góc lõm dưới phải
        double[] p5 = {x, y - size};           // Mũi nhọn dưới
        double[] p6 = {x - size * 0.25, y - size * 0.25}; // Góc lõm dưới trái
        double[] p7 = {x - size, y};           // Mũi nhọn trái
        double[] p8 = {x - size * 0.25, y + size * 0.25}; // Góc lõm trên trái

        // Áp dụng công thức quay cho từng điểm quanh tâm (xq, yq)
        quay(p1, xq, yq, spinAngle);
        quay(p2, xq, yq, spinAngle);
        quay(p3, xq, yq, spinAngle);
        quay(p4, xq, yq, spinAngle);
        quay(p5, xq, yq, spinAngle);
        quay(p6, xq, yq, spinAngle);
        quay(p7, xq, yq, spinAngle);
        quay(p8, xq, yq, spinAngle);

        // Đưa các điểm đã quay vào đa giác để vẽ
        Polygon p = new Polygon();
        p.addPoint((int)(p1[0] * 1000), (int)(p1[1] * 1000));
        p.addPoint((int)(p2[0] * 1000), (int)(p2[1] * 1000));
        p.addPoint((int)(p3[0] * 1000), (int)(p3[1] * 1000));
        p.addPoint((int)(p4[0] * 1000), (int)(p4[1] * 1000));
        p.addPoint((int)(p5[0] * 1000), (int)(p5[1] * 1000));
        p.addPoint((int)(p6[0] * 1000), (int)(p6[1] * 1000));
        p.addPoint((int)(p7[0] * 1000), (int)(p7[1] * 1000));
        p.addPoint((int)(p8[0] * 1000), (int)(p8[1] * 1000));

        // Vẽ màu nền cho phi tiêu
        g.setColor(Color.DARK_GRAY);
        g.fillPolygon(p);
        
        // Vẽ viền cho phi tiêu sắc nét hơn
        g.setColor(Color.LIGHT_GRAY);
        g.drawPolygon(p);
        
        // Vẽ thêm một cái vòng tròn nhỏ (cái lỗ) ở giữa phi tiêu cho giống thật
        g.setColor(Color.WHITE);
        g.fillOval(
            (int)((x - size * 0.15) * 1000), 
            (int)((y - size * 0.15) * 1000), 
            (int)(size * 0.3 * 1000), 
            (int)(size * 0.3 * 1000)
        );
    }
}