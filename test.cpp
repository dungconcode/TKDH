#include <GL/glut.h>
#include <cmath>

#define PI 3.14159265

// Tam giác ban d?u
float ax = 0, ay = 2;
float bx = 2, by = 2;
float cx = 1, cy = 4;

// Tâm quay (di?m b?t k?)
float xq = -1, yq = -1;

float goc = 2;

// Hàm quay 1 di?m quanh (xq, yq)
void quay(float &x, float &y, float goc, float xq, float yq) {
    float rad = goc * PI / 180;

    // t?nh ti?n v? g?c
    float xt = x - xq;
    float yt = y - yq;

    // quay
    float xr = xt * cos(rad) - yt * sin(rad);
    float yr = xt * sin(rad) + yt * cos(rad);

    // t?nh ti?n l?i
    x = xr + xq;
    y = yr + yq;
}

void tamgiac(float ax, float ay, float bx, float by, float cx, float cy) {
    glBegin(GL_TRIANGLES);
        glVertex2f(ax, ay);
        glVertex2f(bx, by);
        glVertex2f(cx, cy);
    glEnd();
}

void display() {
    glClear(GL_COLOR_BUFFER_BIT);

    // v? tr?c
    glColor3f(1,1,1);
    glBegin(GL_LINES);
        glVertex2f(-10,0); glVertex2f(10,0);
        glVertex2f(0,-10); glVertex2f(0,10);
    glEnd();

    // v? tâm quay (-1,-1)
    glColor3f(1,0,0);
    glPointSize(5);
    glBegin(GL_POINTS);
        glVertex2f(xq, yq);
    glEnd();

    // v? tam giác
    glColor3f(0,1,0);
    tamgiac(ax, ay, bx, by, cx, cy);

    glFlush();
}

void timer(int value) {
    quay(ax, ay, goc, xq, yq);
    quay(bx, by, goc, xq, yq);
    quay(cx, cy, goc, xq, yq);

    glutPostRedisplay();
    glutTimerFunc(30, timer, 0);
}

void init() {
    glClearColor(0,0,0,1);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(-10, 10, -10, 10);
}

int main(int argc, char** argv) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowSize(600,600);
    glutCreateWindow("Quay tam giac quanh diem (-1,-1)");

    init();
    glutDisplayFunc(display);
    glutTimerFunc(30, timer, 0);

    glutMainLoop();
    return 0;
}
