#include "Enemy.h"
#include "MathUtils.h"
#include <GL/glut.h>
#include <cmath>
#include <cstdlib>

Enemy::Enemy() {
    size = 0.08f;
    speed = 0.004f;
    spawnRandom();
}

void Enemy::spawnRandom() {
    do {
        x = -2.0f + (rand() % 400) / 100.0f;
        y = -2.0f + (rand() % 400) / 100.0f;
    } while (sqrt(x*x + y*y) < 0.5f); // tránh spawn g?n player
}

void Enemy::update(float playerX, float playerY, Player &player) {
    float dx = playerX - x;
    float dy = playerY - y;

    float distance = sqrt(dx * dx + dy * dy);

    if (distance < size + 0.15f) {
        player.takeDamage();   // ?? TR? MÁU
        spawnRandom();
        return;
    }

    if (distance == 0) return;

    x += dx / distance * speed;
    y += dy / distance * speed;
}

void Enemy::draw() {
    glColor3f(0, 0.5f, 1); // xanh

    glBegin(GL_POLYGON);
    for (int i = 0; i < 360; i++) {
        float rad = i * PI / 180.0f;
        glVertex2f(x + cos(rad) * size, y + sin(rad) * size);
    }
    glEnd();
}
float Enemy::getX() {
    return x;
}

float Enemy::getY() {
    return y;
}
