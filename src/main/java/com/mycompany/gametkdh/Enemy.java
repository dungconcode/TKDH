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
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy {
    double x, y;
    double size = 0.08;
    double speed = 0.004;
    BufferedImage image = Image.loadImage("/Enemy.png");
    static Random rand = new Random();

    public Enemy() {
        spawnRandom();
    }

    public void spawnRandom() {
        do {
            x = -2.0 + rand.nextDouble() * 4.0;
            y = -2.0 + rand.nextDouble() * 4.0;
        } while (Math.sqrt(x * x + y * y) < 0.5);
    }

    public void update(Player player) {
        double dx = player.x - x;
        double dy = player.y - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < size + player.size) {
            player.takeDamage();
            spawnRandom();
            return;
        }

        if (dist == 0) return;

        x += dx / dist * speed;
        y += dy / dist * speed;
    }

    public void draw(Graphics2D g) {
        int w = (int)(size * 2 * 1000);
        int h = (int)(size * 2 * 1000);

        g.drawImage(
            image,
            (int)((x - size) * 1000),
            (int)((y - size) * 1000),
            w,
            h,
            null
        );
    }
}
