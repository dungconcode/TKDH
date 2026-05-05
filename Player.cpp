#include "Player.h"
#include "MathUtils.h"
#include <GL/glut.h>
#include <cmath>



#define RADS 0.017453293


GLuint texture;
int width, height, channels;


Player::Player() {
    x = 0.0f;
    y = 0.0f;
    size = 0.15f;
    angle = 0.0f;
    hp = 5;
    aimAngle = 0.0f;
    targetX = x;
    targetY = y;
    speed = 0.01f;
    isMoving = false;

}
bool Player::isAlive() {
    return hp > 0;
}
void Player::takeDamage() {
    if (hp > 0) hp--;
}

int Player::getHP() {
    return hp;
}
void Player::updateAngle(float mouseX, float mouseY) {
    float dx = mouseX - x;
    float dy = mouseY - y;

    aimAngle = atan2(dy, dx) * 180.0f / PI;
}
void Player::setTarget(float tx, float ty) {
    targetX = tx;
    targetY = ty;
    isMoving = true;
    
    float dx = targetX - x;
    float dy = targetY - y;

    angle = atan2(dy, dx) * 180.0f / PI;
    
}

void QuayTamGiac(
    float x1, float y1,
    float x2, float y2,
    float x3, float y3,
    float xq, float yq,
    float goc
) {
    float anpha = RADS * goc;

    float x11 = x1 * cos(anpha) - y1 * sin(anpha)
              + (1 - cos(anpha)) * xq
              + sin(anpha) * yq;

    float y11 = x1 * sin(anpha) + y1 * cos(anpha)
              - sin(anpha) * xq
              + (1 - cos(anpha)) * yq;

    float x22 = x2 * cos(anpha) - y2 * sin(anpha)
              + (1 - cos(anpha)) * xq
              + sin(anpha) * yq;

    float y22 = x2 * sin(anpha) + y2 * cos(anpha)
              - sin(anpha) * xq
              + (1 - cos(anpha)) * yq;

    float x33 = x3 * cos(anpha) - y3 * sin(anpha)
              + (1 - cos(anpha)) * xq
              + sin(anpha) * yq;

    float y33 = x3 * sin(anpha) + y3 * cos(anpha)
              - sin(anpha) * xq
              + (1 - cos(anpha)) * yq;

    glBegin(GL_TRIANGLES);
        glVertex2f(x11, y11);
        glVertex2f(x22, y22);
        glVertex2f(x33, y33);
    glEnd();
}

void Player::draw() {
    glPushMatrix();

    // ===== V? PLAYER HÌNH TRÒN + 2 M?T =====
    if (hp <= 0) return;
	glPushMatrix();

	glTranslatef(x, y, 0);
	glRotatef(angle, 0, 0, 1); // quay theo hu?ng di chuy?n / kim la bàn
	
	// thân hình tròn
	glColor3f(0, 1, 0);
	glBegin(GL_POLYGON);
	for (int i = 0; i < 360; i++) {
	    float rad = i * PI / 180.0f;
	    glVertex2f(cos(rad) * size, sin(rad) * size);
	}
	glEnd();
	
	// m?t trái
	glColor3f(0, 0, 0);
	glBegin(GL_POLYGON);
	for (int i = 0; i < 360; i++) {
	    float rad = i * PI / 180.0f;
	    glVertex2f(size * 0.35f + cos(rad) * size * 0.13f,
	               size * 0.35f + sin(rad) * size * 0.13f);
	}
	glEnd();
	
	// m?t ph?i
	glBegin(GL_POLYGON);
	for (int i = 0; i < 360; i++) {
	    float rad = i * PI / 180.0f;
	    glVertex2f(size * 0.35f + cos(rad) * size * 0.13f,
	              -size * 0.35f + sin(rad) * size * 0.13f);
	}
	glEnd();
	
	glPopMatrix();



    // ===== V? TAM GIÁC (XOAY THEO CHU?T) =====
	float offset = size * 2;   // ?? d?y ra xa hon
	float scale = 0.5f;           // ?? nh? l?i
	
	float x1 = x + offset;
	float y1 = y;
	
	float x2 = x + offset - size * scale;
	float y2 = y + size * scale * 0.6f;
	
	float x3 = x + offset - size * scale;
	float y3 = y - size * scale * 0.6f;
	
	float rx1, ry1, rx2, ry2, rx3, ry3;
	
	
	glColor3f(1, 0, 0);
	QuayTamGiac(x1, y1, x2, y2, x3, y3, x, y, aimAngle);
	glEnd();
	glPopMatrix();
}

float Player::getAngle() {
    return angle;
}
void Player::update() {
    if (!isMoving) return;

    float dx = targetX - x;
    float dy = targetY - y;

    float distance = sqrt(dx * dx + dy * dy);

    if (distance < speed) {
        x = targetX;
        y = targetY;
        isMoving = false;
        return;
    }

    x += dx / distance * speed;
    y += dy / distance * speed;
}

float Player::getX() {
    return x;
}

float Player::getY() {
    return y;
}
