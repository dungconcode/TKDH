#include <GL/glut.h>
#include <cmath>

const float PI = 3.14159265f;

int windowWidth = 900;
int windowHeight = 600;

float playerX = 0.0f;
float playerY = 0.0f;
float playerAngle = 90.0f;

float mouseWorldX = 0.0f;
float mouseWorldY = 0.0f;

void drawCircle(float x, float y, float r) {
    glBegin(GL_LINE_LOOP);
    for (int i = 0; i < 360; i++) {
        float rad = i * PI / 180.0f;
        glVertex2f(x + cos(rad) * r, y + sin(rad) * r);
    }
    glEnd();
}

void drawTriangle(float x, float y, float size, float angle) {
    glPushMatrix();

    glTranslatef(x, y, 0);
    glRotatef(angle - 90, 0, 0, 1);

    glBegin(GL_TRIANGLES);
        glVertex2f(0.0f, size);
        glVertex2f(-size * 0.5f, -size * 0.5f);
        glVertex2f(size * 0.5f, -size * 0.5f);
    glEnd();

    glPopMatrix();
}

void drawPlayer() {
    glColor3f(0.1f, 0.5f, 1.0f);
    drawTriangle(playerX, playerY, 0.15f, playerAngle);
}

void drawCompass() {
    float cx = -0.78f;
    float cy = -0.72f;
    float r = 0.18f;

    glColor3f(0, 0, 0);
    drawCircle(cx, cy, r);

    glColor3f(1, 0, 0);
    drawTriangle(cx, cy, 0.13f, playerAngle);

    glColor3f(0, 0, 0);
    glRasterPos2f(cx - 0.025f, cy - r - 0.07f);
    glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, 'N');
}

void display() {
    glClear(GL_COLOR_BUFFER_BIT);

    drawPlayer();
    drawCompass();

    glFlush();
}

void mouseMove(int x, int y) {
    mouseWorldX = (float)x / windowWidth * 2.0f - 1.0f;
    mouseWorldY = 1.0f - (float)y / windowHeight * 2.0f;

    float dx = mouseWorldX - playerX;
    float dy = mouseWorldY - playerY;

    playerAngle = atan2(dy, dx) * 180.0f / PI;

    glutPostRedisplay();
}

void init() {
    glClearColor(1, 1, 1, 1);

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(-1, 1, -1, 1);

    glMatrixMode(GL_MODELVIEW);
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

int main(int argc, char** argv) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowSize(windowWidth, windowHeight);
    glutCreateWindow("Nhan vat xoay theo chuot + la ban");

    init();

    glutDisplayFunc(display);
    glutPassiveMotionFunc(mouseMove);
    glutMotionFunc(mouseMove);
    glutReshapeFunc(reshape);

    glutMainLoop();
    return 0;
}
