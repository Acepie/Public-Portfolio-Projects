#include "spike.h"
#include <random>
#include <GL/glu.h>

Spike::Spike(int screenX, int screenY) {
  width = rand() % 70 + 30;
  height = rand() % 50 + 30;
  location = screenX/7 + (rand() % 5) * screenX/7;
}

Spike::Spike(const Spike &rhs) {
  width = rhs.width;
  height = rhs.height;
  location = rhs.location;
}

Spike &Spike::operator=(const Spike &rhs) {
  width = rhs.width;
  height = rhs.height;
  location = rhs.location;
  return *this;
}

int Spike::getXCoord() {
  return location;
}

int Spike::getYCoord() {
  return 0;
}

bool Spike::isColliding(int playerX, int playerY) {
  return (playerX > (location - .5f * width)) && (playerX < (location + .5f * width)) && (playerY < height);
}

void Spike::draw(int screenX, int screenY) {
  float glLoc = ((location - screenX / 2.0f) * 3.6f) / screenX;
  float glWidth = (width * 3.6f) / screenX;
  float glHeight = (height * 2.0f) / screenY;
  glBegin(GL_TRIANGLES);
  glVertex3f(glLoc - .5f * glWidth, -1, -2.4f);
  glVertex3f(glLoc + .5f * glWidth, -1, -2.4f);
  glVertex3f(glLoc, -1 + glHeight, -2.4f);
  glEnd();
}
