#ifndef LEVEL_H
#define LEVEL_H

#include <vector>
#include "obstacle.h"

class Level {
 public:
  Level(int screenX, int screenY);
  ~Level();
  Level(const Level &other);
  Level &operator=(const Level &rhs);
  const std::vector<Obstacle *> &getObstacles() const;

 private:
  std::vector<Obstacle *> obstacles;
};

#endif