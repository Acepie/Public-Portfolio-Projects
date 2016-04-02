#ifdef WIN32
#define WIN32_LEAN_AND_MEAN
#define WIN32_EXTRA_LEAN

#include <windows.h>
#endif

#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>
#include <math.h>
#include <string>
#include "GameWorld.h"

//global declaration of current world
GameWorld world;
//global declaration for some initialization variables
//probably best to ignore
int windID;
bool gameMode;

//clears background
void setup() {
  glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
}

//draws objects
void display() {
  //clears buffers
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  //gets data from world and makes some constants for use in drawing
  const float relSize = .1; //unused?
  const int worldWidth = world.getWorldWidth();
  const int worldHeight = world.getWorldHeight();
  const int playerXCoord = world.playerXCoord();
  const int playerYCoord = world.playerYCoord();
  const float scalar = 1.8;
  const float playerX = scalar * (float) (playerXCoord - worldWidth / 2) / (worldWidth / 2);
  const float playerY = (float) (playerYCoord - worldHeight / 2) / (worldHeight / 2);

  //sets color
  glColor3f(1.0f, 0.0f, 0.0f);

  float twoPi = 3.14159 * 2;

  //draws vertices and connects them into triangles
  glBegin(GL_TRIANGLE_FAN);
  glVertex3f(playerX, playerY, -2.4f);
  for (int i = 0; i <= 20; i++) {
    glVertex3f((playerX + ((180 / (2.0f * worldWidth)) * cos(twoPi * (i / 20.0f)))),
               (playerY + ((100 / (2.0f * worldHeight)) * sin(twoPi * (i / 20.0f)))),
               -2.4f);
  }
  glEnd();

  glColor3f(0.0f, 0.0f, 0.0f);

  std::vector<Obstacle*> v1 = world.getObstacles();
  for (std::vector<Obstacle*>::iterator it = v1.begin(); it != v1.end(); ++it) {
    (*it)->draw(worldWidth, worldHeight);
  }

  glRasterPos3f(-1.7f, .9f, -2.4f);
  const char* score = ("Score: " + std::to_string(world.score)).c_str();
  for (int i = 0; i < strlen(score); i++) {
    glutBitmapCharacter(GLUT_BITMAP_HELVETICA_18, score[i]);
  }

  //swap pixelbuffer
  glutSwapBuffers();
}

//adjusts objects display size if screen size is manually changed
void changeSize(int w, int h) {
  if (h == 0) {
    h = 1;
  }
  float ratio = ((float) w) / h;

  glMatrixMode(GL_PROJECTION);

  glLoadIdentity();

  glViewport(0, 0, w, h);

  gluPerspective(45, ratio, 1, 1000);

  glMatrixMode(GL_MODELVIEW);
}

//update world and redraw view
void update() {
  world.updateWorld();
  glutPostRedisplay();
}

//interact with keys. currently only looks at escape and space
//keys are based on ascii char
void onKey(unsigned char key, int x, int y) {
  switch (key) {
    case 27:
      if (gameMode) {
        glutLeaveGameMode();
      }
      else {
        exit(0);
      }
      break;
    case 32:
      world.reset();
      break;
    case 13:
      world.hardreset();
      break;
    default:
      break;
  }
}

//interact with special keys that don't have ascii representations
void onSpecialKey(int key, int x, int y) {
  switch (key) {
    case GLUT_KEY_UP:
      world.up();
      break;
    default:
      break;
  }
}

//initialize world and setup glut callbacks
void init() {
  setup();

  glutDisplayFunc(display);
  glutIdleFunc(update);
  glutReshapeFunc(changeSize);
  glutKeyboardFunc(onKey);
  glutSpecialFunc(onSpecialKey);
}

//makes window to be displayed
void createContext() {
  glutGameModeString(world.glutGameString());
  if (glutGameModeGet(GLUT_GAME_MODE_POSSIBLE)) {
    gameMode = true;
    glutEnterGameMode();
  }
  else {
    gameMode = false;
    glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE);
    glutInitWindowSize(world.getWorldWidth(), world.getWorldHeight());
    windID = glutCreateWindow("Hello World");
  }
}

int main(int argc, char *argv[]) {
  //initialize glut
  glutInit(&argc, argv);

  //make window
  createContext();

  //more initialization stuff
  init();

  //runs world
  glutMainLoop();

  return 0;
}
