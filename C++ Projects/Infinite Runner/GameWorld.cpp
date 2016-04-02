#include <string>
#include "GameWorld.h"

const float gravity = .02;

const int GameWorld::getWorldWidth() const {
  return worldWidth;
}

const int GameWorld::getWorldHeight() const {
  return worldHeight;
}

const char *GameWorld::glutGameString() const {
  std::string string(std::to_string(worldWidth) + "x" + std::to_string(worldHeight) + ":32");
  return string.c_str();
}

void GameWorld::updateWorld() {
  if (!gameOver()) {
    if (playerY != 0) {
      playerVelocity -= gravity;
      playerY += playerVelocity;
      if (playerY < 0) {
        playerY = 0;
      }
    }
    playerX = (playerX + 1) % worldWidth;
    if (playerX == 0) {
      swapLevels();
    }
    score += 2;
  }
}

void GameWorld::reset() {
  playerX = 0;
  playerY = 0;
  playerVelocity = 0;
  score = 0;
}


void GameWorld::hardreset() {
  swapLevels();
  reset();
}

void GameWorld::up() {
  if (playerY == 0) {
    playerVelocity = 3;
    playerY += playerVelocity;
  }
}

const int GameWorld::playerXCoord() const {
  return playerX;
}

const int GameWorld::playerYCoord() const {
  return playerY;
}

bool GameWorld::gameOver() const {
  std::vector<Obstacle*> v1 = getObstacles();
  for (std::vector<Obstacle*>::iterator it = v1.begin(); it != v1.end(); ++it) {
    if ((*it)->isColliding(playerX, playerY)){
      return true;
    }
  }
  return false;
}

void GameWorld::swapLevels() {
  delete levelLoaded;
  levelLoaded = nextLevel;
  nextLevel = new Level(worldWidth, worldHeight);
}

std::vector<Obstacle*> GameWorld::getObstacles() const {
  std::vector<Obstacle*> v1(levelLoaded->getObstacles());
  std::vector<Obstacle*> v2(nextLevel->getObstacles());
  v1.insert(v1.end(), v2.begin(), v2.end());
  return v1;
}
