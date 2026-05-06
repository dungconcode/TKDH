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

public class CoinAnimation {
    private BufferedImage coinSheet;
    private BufferedImage[] frames;

    private int frameIndex = 0;
    private int frameCounter = 0;
    private int frameDelay = 8;

    public CoinAnimation() {
        coinSheet = Image.loadImage("/coin_sheet.png");

        int frameCount = 6;
        int frameWidth = coinSheet.getWidth() / frameCount;
        int frameHeight = coinSheet.getHeight();

        frames = new BufferedImage[frameCount];

        for (int i = 0; i < frameCount; i++) {
            frames[i] = coinSheet.getSubimage(
                    i * frameWidth,
                    0,
                    frameWidth,
                    frameHeight
            );
        }
    }

    public void update() {
        frameCounter++;

        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            frameIndex = (frameIndex + 1) % frames.length;
        }
    }

    public void draw(Graphics2D g, int x, int y, int w, int h) {
        if (frames == null) return;

        g.drawImage(frames[frameIndex], x, y, w, h, null);
    }
}
