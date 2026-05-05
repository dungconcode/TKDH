#ifndef SWORD_H
#define SWORD_H

#include "Enemy.h"

class Sword {
private:
    float angle;
    float swingAngle;
    bool attacking;
    int attackFrame;

    float length;
    float width;
    float range;

public:
    Sword();

    void startAttack();
    int update(float playerX, float playerY, float playerAngle, Enemy enemies[], int enemyCount);
    void draw(float playerX, float playerY, float playerAngle);
};

#endif
