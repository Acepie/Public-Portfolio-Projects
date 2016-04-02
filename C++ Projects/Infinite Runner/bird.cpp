#include "bird.h"
#include <random>
#include <GL/glu.h>

Bird::Bird(int screenX, int screenY) {
  locationX = rand() % 4 * (screenX * .2f) + .1f * screenX;
  locationY = rand() % 2 * (screenY * .1f) + .1f * screenY;
  size = (rand() % 50) + 50;
}

Bird::Bird(const Bird &other) {
  locationX = other.locationX;
  locationY = other.locationY;
  size = other.size;
}

Bird &Bird::operator=(const Bird &rhs) {
  locationX = rhs.locationX;
  locationY = rhs.locationY;
  size = rhs.size;
  return *this;
}

int Bird::getXCoord() {
  return locationX;
}

int Bird::getYCoord() {
  return locationY;
}

bool Bird::isColliding(int playerX, int playerY) {
  return (size / 2.0f + 25) > sqrt(pow(playerX - locationX, 2) + pow(playerY - locationY, 2));
}

void Bird::draw(int screenX, int screenY) {
  float twoPi = 2.0 * 3.14159;

  float glX = ((locationX - screenX / 2.0f) * 3.6f) / screenX;
  float glY = ((locationY - screenY / 2.0f) * 2.0f) / screenY;

  glBegin(GL_TRIANGLE_FAN);
  glVertex3f(glX, glY, -2.4f);
  for (int i = 0; i <= 20; i++) {
    glVertex3f((glX + ((size * 1.8f / screenX) * cos(twoPi * (i / 20.0f)))),
               (glY + ((size / (float) screenY) * sin(twoPi * (i / 20.0f)))),
               -2.4f);
  }
  glEnd();
}

