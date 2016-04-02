#ifndef BIRD_H
#define BIRD_H

#include "obstacle.h"

// A triangular obstacle
class Bird: public Obstacle {
 public:
  Bird(int screenX, int screenY);
  Bird(const Bird &other);
  Bird &operator=(const Bird &rhs);
  virtual int getXCoord();
  virtual int getYCoord();
  virtual bool isColliding(int playerX, int playerY);
  virtual void draw(int screenX, int screenY);

 private:
  int locationX;
  int locationY;
  int size;
};

#endif