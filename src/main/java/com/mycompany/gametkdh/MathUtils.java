/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gametkdh;

/**
 *
 * @author lequo
 */
public class MathUtils {
    public static final double PI = Math.PI;

    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double angleTo(double x1, double y1, double x2, double y2) {
        return Math.atan2(y2 - y1, x2 - x1);
    }

    public static double normalizeAngle(double a) {
        while (a > Math.PI) a -= Math.PI * 2;
        while (a < -Math.PI) a += Math.PI * 2;
        return a;
    }
}
