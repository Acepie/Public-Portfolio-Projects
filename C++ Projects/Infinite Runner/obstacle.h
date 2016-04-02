#ifndef OBSTACLE_H
#define OBSTACLE_H

class Obstacle {
 public:
  Obstacle() {}
  virtual int getXCoord() = 0;
  virtual int getYCoord() = 0;
  virtual bool isColliding(int playerX, int playerY) = 0;
  virtual void draw(int screenX, int screenY) = 0;
  virtual ~Obstacle(){};
 private:
  Obstacle(const Obstacle &other);
  Obstacle &operator=(const Obstacle &rhs);
};

#endif