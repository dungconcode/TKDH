#ifndef ENEMY_H
#define ENEMY_H
#include "Player.h"
class Enemy {
private:
    float x, y;
    float size;
    float speed;
    

public:
    Enemy();

    void spawnRandom();
    void update(float playerX, float playerY, Player &player);
    void draw();
    
    float getX();
	float getY();
};

#endif
