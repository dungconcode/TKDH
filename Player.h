#ifndef PLAYER_H
#define PLAYER_H

class Player {
private:
    float x, y;
    float size;
    float angle;
    float aimAngle;
    float targetX, targetY;
    float speed;
    bool isMoving;
    int hp;
public:
    Player();

    void draw();
    void update();
    void updateAngle(float mouseX, float mouseY);
    void setTarget(float tx, float ty); // ?? THI?U DÒNG NÀY
    void takeDamage();
	int getHP();
    float getX();
    float getY();
    float getAngle();
};

#endif
