#include "Sword.h"
#include "MathUtils.h"
#include <GL/glut.h>
#include <cmath>

Sword::Sword() {
    angle = 0.0f;
    swingAngle = 0.0f;
    attacking = false;
    attackFrame = 0;

    length = 0.45f;
    width = 0.035f;
    range = 0.55f;
}

void Sword::startAttack() {
    if (!attacking) {
        attacking = true;
        attackFrame = 0;
    }
}

int Sword::update(float playerX, float playerY, float playerAngle, Enemy enemies[], int enemyCount) {
    angle = playerAngle;

    int killed = 0;

    if (!attacking) {
        swingAngle = 45.0f;
        return 0;
    }

    attackFrame++;

    float t = attackFrame / 15.0f;
    swingAngle = 67.5f - t * 135.0f;

    float swordWorldAngle = angle + swingAngle;

    for (int i = 0; i < enemyCount; i++) {
        float dx = enemies[i].getX() - playerX;
        float dy = enemies[i].getY() - playerY;

        float dist = sqrt(dx * dx + dy * dy);
        if (dist > range) continue;

        float enemyAngle = atan2(dy, dx) * 180.0f / PI;

        float diff = enemyAngle - swordWorldAngle;
        while (diff > 180) diff -= 360;
        while (diff < -180) diff += 360;

        if (fabs(diff) <= 67.5f) {
            enemies[i].spawnRandom();
            killed++;
        }
    }

    if (attackFrame >= 15) {
        attacking = false;
        swingAngle = 45.0f;
    }

    return killed;
}

void Sword::draw(float playerX, float playerY, float playerAngle) {
    float drawAngle = playerAngle + swingAngle;

    glPushMatrix();

    glTranslatef(playerX, playerY, 0);
    glRotatef(drawAngle, 0, 0, 1);

    // d?y ki?m ra c?nh player
    glTranslatef(0.18f, 0.12f, 0);

    // cán ki?m
    glColor3f(0.2f, 0.1f, 0.05f);
    glBegin(GL_QUADS);
        glVertex2f(0.00f, -width);
        glVertex2f(0.10f, -width);
        glVertex2f(0.10f,  width);
        glVertex2f(0.00f,  width);
    glEnd();

    // lu?i ki?m
    glColor3f(0.3f, 0.3f, 0.9f);
    glBegin(GL_QUADS);
        glVertex2f(0.10f, -width * 0.6f);
        glVertex2f(length, -width * 0.6f);
        glVertex2f(length,  width * 0.6f);
        glVertex2f(0.10f,  width * 0.6f);
    glEnd();

    // mui ki?m
    glBegin(GL_TRIANGLES);
        glVertex2f(length + 0.08f, 0);
        glVertex2f(length,  width);
        glVertex2f(length, -width);
    glEnd();

    glPopMatrix();
}
