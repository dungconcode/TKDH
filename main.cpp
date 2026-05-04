#include <GL/glut.h>
#include "Player.h"
#include "Compass.h"
#include "Enemy.h"
#include <ctime>
#include <cmath>
#define PI 3.14159265
int windowWidth = 900;
int windowHeight = 600;

Player player;
Compass compass;

Enemy enemies[5];

float cameraX = 0.0f;
float cameraY = 0.0f;
void drawMap() {
    glColor3f(0.8f, 0.8f, 0.8f);

    glBegin(GL_LINES);

    for (float i = -5.0f; i <= 5.0f; i += 0.2f) {
        // du?ng d?c
        glVertex2f(i, -5.0f);
        glVertex2f(i,  5.0f);

        // du?ng ngang
        glVertex2f(-5.0f, i);
        glVertex2f( 5.0f, i);
    }

    glEnd();
}
void drawHealth(Player &player) {
    int hp = player.getHP();

    float startX = 0.48f;  // kéo sang trái
    float startY = 0.85f;
    float r = 0.035f;

    for (int i = 0; i < hp; i++) {
        float cx = startX + i * 0.1f;
        float cy = startY;

        glColor3f(1, 0, 0);

        glBegin(GL_POLYGON);
        for (int j = 0; j < 360; j++) {
            float rad = j * PI / 180.0f;
            glVertex2f(cx + cos(rad) * r, cy + sin(rad) * r);
        }
        glEnd();
    }
}
void display() {
    glClear(GL_COLOR_BUFFER_BIT);

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    glTranslatef(-cameraX, -cameraY, 0);

    drawMap();
    for (int i = 0; i < 5; i++) {
    	enemies[i].draw();
	}
    player.draw();

    glLoadIdentity();
    drawHealth(player);
    compass.draw(player.getAngle(), player.getX(), player.getY(), enemies, 5);

    glFlush();
}



void mouseMove(int mouseX, int mouseY) {
    float worldX = cameraX + ((float)mouseX / windowWidth * 2.0f - 1.0f);
    float worldY = cameraY + (1.0f - (float)mouseY / windowHeight * 2.0f);

    player.updateAngle(worldX, worldY);

    glutPostRedisplay();
}

void reshape(int w, int h) {
    windowWidth = w;
    windowHeight = h;

    glViewport(0, 0, w, h);

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(-1, 1, -1, 1);

    glMatrixMode(GL_MODELVIEW);
}

void mouseClick(int button, int state, int mouseX, int mouseY) {
    if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN) {
        float worldX = cameraX + ((float)mouseX / windowWidth * 2.0f - 1.0f);
        float worldY = cameraY + (1.0f - (float)mouseY / windowHeight * 2.0f);

        player.setTarget(worldX, worldY);
    }
}
void init() {
    glClearColor(1, 1, 1, 1);
    
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(-1, 1, -1, 1);

    glMatrixMode(GL_MODELVIEW);
}
void update(int value) {
    player.update();
    
    for (int i = 0; i < 5; i++) {
        enemies[i].update(player.getX(), player.getY(), player);
    }
    
    cameraX = player.getX();
    cameraY = player.getY();

    glutPostRedisplay();
    glutTimerFunc(16, update, 0);
}
int main(int argc, char** argv) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowSize(windowWidth, windowHeight);
    glutCreateWindow("ktdh - Nhan vat 2D va la ban");

    init();

    glutDisplayFunc(display);
    glutPassiveMotionFunc(mouseMove);
    glutMotionFunc(mouseMove);
    glutReshapeFunc(reshape);
    
	glutMouseFunc(mouseClick);
	glutTimerFunc(16, update, 0);
    glutMainLoop();
    return 0;
}
