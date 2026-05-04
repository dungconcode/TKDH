#include "Compass.h"
#include "MathUtils.h"
#include <GL/glut.h>
#include <cmath>

Compass::Compass() {
    x = -0.78f;
    y = -0.72f;
    radius = 0.18f;
}
void quay(float &x, float &y, float goc) {
    float rad = goc * PI / 180.0f;

    float t = x;

    x = x * cos(rad) - y * sin(rad);
    y = t * sin(rad) + y * cos(rad);
}
void Compass::draw(float angle, float playerX, float playerY, Enemy enemies[], int enemyCount) {
    // vòng tròn la bàn
    glColor3f(0, 0, 0);
    glBegin(GL_LINE_LOOP);
    for (int i = 0; i < 360; i++) {
        float rad = i * PI / 180.0f;
        glVertex2f(x + cos(rad) * radius, y + sin(rad) * radius);
    }
    glEnd();

    // kim la bàn
    glPushMatrix();
    glTranslatef(x, y, 0);
    glRotatef(angle - 90, 0, 0, 1);

    glColor3f(1, 0, 0);
    glBegin(GL_TRIANGLES);
        glVertex2f(0.0f, radius * 0.8f);
        glVertex2f(-radius * 0.25f, -radius * 0.4f);
        glVertex2f(radius * 0.25f, -radius * 0.4f);
    glEnd();

    glPopMatrix();
    // tam giác xanh ch? hu?ng enemy
    for (int i = 0; i < enemyCount; i++) {
        float dx = enemies[i].getX() - playerX;
        float dy = enemies[i].getY() - playerY;

        float enemyAngle = atan2(dy, dx) * 180.0f / PI;

        // tam giác enemy ban d?u n?m ? phía trên la bàn
	    float x1 = 0.0f;
	    float y1 = radius * 0.9f;
	
	    float x2 = -radius * 0.08f;
	    float y2 = radius * 0.72f;
	
	    float x3 = radius * 0.08f;
	    float y3 = radius * 0.72f;
	
	    // quay quanh g?c t?a d? t?m th?i
	    quay(x1, y1, enemyAngle - 90);
	    quay(x2, y2, enemyAngle - 90);
	    quay(x3, y3, enemyAngle - 90);
	
	    // d?ch v? tâm la bàn x, y
	    glColor3f(0, 0.7f, 1);
	    glBegin(GL_TRIANGLES);
	        glVertex2f(x + x1, y + y1);
	        glVertex2f(x + x2, y + y2);
	        glVertex2f(x + x3, y + y3);
	    glEnd();

        glPopMatrix();
    }
    // ch? N
    glColor3f(0, 0, 0);
    glRasterPos2f(x - 0.025f, y - radius - 0.07f);
    glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'N');
    
    // ch? S (South)
	glRasterPos2f(x - 0.025f, y - radius - 0.07f);
	glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'S');
	
	// ch? E (East)
	glRasterPos2f(x + radius + 0.02f, y - 0.02f);
	glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'E');
	
	// ch? W (West)
	glRasterPos2f(x - radius - 0.05f, y - 0.02f);
	glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'W');
}
