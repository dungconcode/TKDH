package com.mycompany.gametkdh;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.ArrayList;
public class Player {
    double x = 0;
    double y = 0;
    double size = 0.15; // Giữ nguyên để làm hitbox va chạm vật lý

    double angle = 0;
    double aimAngle = 0;

    double targetX = 0;
    double targetY = 0;

    double speed = 0.01;
    boolean isMoving = false;

    int hp = 5;
    int invincibleTimer = 0;
    
    BufferedImage imageRight;
    BufferedImage imageLeft;
    
    public Player() {
        BufferedImage originalImage = Image.loadImage("/Player.png");
        
        // Ảnh nhìn sang phải: Đối xứng Ox (y' = -y) để chống ngược đầu
        imageRight = apDungDoiXungThuCong(originalImage, false, true);
        
        // Ảnh nhìn sang trái: Đối xứng Oy (x' = -x) để lật mặt, Ox (y' = -y) để chống ngược đầu
        imageLeft = apDungDoiXungThuCong(originalImage, true, true);
    }

    private BufferedImage apDungDoiXungThuCong(BufferedImage img, boolean doiXungOy, boolean doiXungOx) {
        if (img == null) return null;
        
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y_coord = 0; y_coord < h; y_coord++) {
            for (int x_coord = 0; x_coord < w; x_coord++) {
                
                int x_phay = x_coord;
                int y_phay = y_coord;
                
                if (doiXungOy) {
                    x_phay = (w - 1) - x_coord;
                }
                
                if (doiXungOx) {
                    y_phay = (h - 1) - y_coord;
                }
                
                int pixelColor = img.getRGB(x_coord, y_coord);
                result.setRGB(x_phay, y_phay, pixelColor);
            }
        }
        return result;
    }

    public void updateAngle(double mouseX, double mouseY) {
        aimAngle = MathUtils.angleTo(x, y, mouseX, mouseY);
    }

    public void setTarget(double tx, double ty) {
        targetX = tx;
        targetY = ty;
        isMoving = true;
        angle = MathUtils.angleTo(x, y, targetX, targetY);
    }

    // Trong Player.java, sửa lại hàm update
    public void update(ArrayList<Obstacle> obstacles) {
        if (invincibleTimer > 0) invincibleTimer--;
        if (!isMoving || hp <= 0) return;

        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < speed) {
            // Kiểm tra đích đến cuối cùng
            if (!checkCollision(targetX, targetY, obstacles)) {
                x = targetX;
                y = targetY;
            }
            isMoving = false;
            return;
        }

        // Tính toán vị trí dự kiến tiếp theo
        double nextX = x + (dx / dist) * speed;
        double nextY = y + (dy / dist) * speed;

        // Chỉ cập nhật nếu vị trí tiếp theo không chạm tường
        if (!checkCollision(nextX, nextY, obstacles)) {
            x = nextX;
            y = nextY;
        } else {
            isMoving = false; // Dừng lại nếu đâm vào tường
        }
    }

// Hàm kiểm tra va chạm đơn giản
private boolean checkCollision(double nx, double ny, ArrayList<Obstacle> obstacles) {
    for (Obstacle ob : obstacles) {
        // Kiểm tra xem vị trí (nx, ny) có nằm trong Obstacle nào không
        // Cộng/trừ thêm 'size' để nhân vật không bị lún vào tường
        if (nx + size > ob.x && nx - size < ob.x + ob.width &&
            ny + size > ob.y && ny - size < ob.y + ob.height) {
            return true;
        }
    }
    return false;
}

    public void takeDamage() {
        if (invincibleTimer > 0) return; 
        if (hp > 0) hp--;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void draw(Graphics2D g) {
        if (!isAlive()) return;

        // Lựa chọn ảnh dựa trên hướng chuột
        BufferedImage currentImg;
        if (Math.abs(aimAngle) > Math.PI / 2) {
            currentImg = imageLeft; 
        } else {
            currentImg = imageRight;
        }

        // Lấy kích thước pixel thật của bức ảnh
        int realWidth = 0;
        int realHeight = 0;
        
        if (currentImg != null) {
            realWidth = currentImg.getWidth();
            realHeight = currentImg.getHeight();
        }

        // Vẽ vòng bất tử căn theo kích thước thật của ảnh
        if (invincibleTimer > 0) {
            Graphics2D g2d = (Graphics2D) g;

            // Lưu lại nét vẽ cũ để không làm ảnh hưởng đến các đối tượng khác được vẽ sau này
            Stroke oldStroke = g2d.getStroke();

            g2d.setColor(new Color(255, 255, 0, 150));

            // Thiết lập độ dày viền (ví dụ: 5.0f). Bạn có thể thay đổi số này để chỉnh độ dày.
            g2d.setStroke(new BasicStroke(5.0f)); 

            g2d.drawOval(
                (int)(x * 1000) - realWidth / 2 - 10,
                (int)(y * 1000) - realHeight / 2 - 10,
                realWidth + 20,
                realHeight + 20
            );

            // Khôi phục lại nét vẽ mặc định
            g2d.setStroke(oldStroke);
        }
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x * 1000, y * 1000);

        // Vẽ ảnh với kích thước thật, căn giữa trục tọa độ
        if (currentImg != null) {
            g2.drawImage(
                currentImg, 
                -realWidth / 2, 
                -realHeight / 2, 
                realWidth, 
                realHeight, 
                null
            );
        }
        g2.dispose();

        drawAimTriangle(g);
    }

    private void drawAimTriangle(Graphics2D g) {
        double anpha = aimAngle; 

        double offset = size * 2;
        double scale = 0.5;

        double x1_orig = x + offset;
        double y1_orig = y;

        double x2_orig = x + offset - size * scale;
        double y2_orig = y + size * scale * 0.6;

        double x3_orig = x + offset - size * scale;
        double y3_orig = y - size * scale * 0.6;

        double cosA = Math.cos(anpha);
        double sinA = Math.sin(anpha);

        double x11 = x1_orig * cosA - y1_orig * sinA + (1 - cosA) * x + sinA * y;
        double y11 = x1_orig * sinA + y1_orig * cosA - sinA * x + (1 - cosA) * y;

        double x22 = x2_orig * cosA - y2_orig * sinA + (1 - cosA) * x + sinA * y;
        double y22 = x2_orig * sinA + y2_orig * cosA - sinA * x + (1 - cosA) * y;

        double x33 = x3_orig * cosA - y3_orig * sinA + (1 - cosA) * x + sinA * y;
        double y33 = x3_orig * sinA + y3_orig * cosA - sinA * x + (1 - cosA) * y;

        Polygon p = new Polygon();
        p.addPoint((int)(x11 * 1000), (int)(y11 * 1000));
        p.addPoint((int)(x22 * 1000), (int)(y22 * 1000));
        p.addPoint((int)(x33 * 1000), (int)(y33 * 1000));

        g.setColor(Color.RED);
        g.fillPolygon(p);
    }
}