#include <vector>
#include <random>
#include "level.h"
#include "bird.h"
#include "spike.h"

Level::Level(int screenX, int screenY) {
  obstacles = std::vector<Obstacle *>();
  int birds = rand() % 3;
  int spike = 2 - birds;
  if (spike == 0) {
    spike = 2;
  }
  while (birds > 0) {
    obstacles.push_back(new Bird(screenX, screenY));
    birds--;
  }
  while (spike > 0) {
    obstacles.push_back(new Spike(screenX, screenY));
    spike--;
  }
}

Level::~Level() {
}

Level::Level(const Level &other) {
  obstacles = std::vector<Obstacle *>(other.obstacles);
}

Level &Level::operator=(const Level &rhs) {
  obstacles = rhs.obstacles;
  return *this;
}

const std::vector<Obstacle *> &Level::getObstacles() const {
  return obstacles;
}
