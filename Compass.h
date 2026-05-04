#ifndef COMPASS_H
#define COMPASS_H
#include "Enemy.h"
class Compass {
private:
    float x, y;
    float radius;

public:
    Compass();

   void draw(float angle, float playerX, float playerY, Enemy enemies[], int enemyCount);
};

#endif
