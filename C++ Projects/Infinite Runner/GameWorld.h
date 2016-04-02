#ifndef OPENGL_GAMEWORLD_H
#define OPENGL_GAMEWORLD_H

#include "level.h"
#include "obstacle.h"


class GameWorld {
 public:
  GameWorld() : score(0), worldWidth(1920), worldHeight(1080), playerX(0), playerY(0), playerVelocity(0) {
    levelLoaded = new Level(worldWidth, worldHeight);
    nextLevel = new Level(worldWidth, worldHeight);
  }

  ~GameWorld() {
    delete levelLoaded;
    delete nextLevel;
  }

  //get the width of the world
  const int getWorldWidth() const;

  //get the height of the world
  const int getWorldHeight() const;

  //function that makes a string saying how glut should set up the screen
  //necessary for full screen but otherwise not necessary
  const char *glutGameString() const;

  //update this world on tick
  void updateWorld();

  //resets triangle to beginning of level
  void reset();

  //completely restart game
  void hardreset();

  //react to up arrow
  void up();

  //get player's x position in world
  const int playerXCoord() const;

  //get player's y position in level
  const int playerYCoord() const;

  //has the player collided with any obstacles
  bool gameOver() const;

  //get the player's current score
  int score;

  //get a list of the obstacles in the world
  std::vector<Obstacle*> getObstacles() const;

 private:
  void swapLevels();

  int worldWidth, worldHeight;
  int playerX;
  float playerY;
  float playerVelocity;
  Level *levelLoaded;
  Level *nextLevel;
};


#endif //OPENGL_GAMEWORLD_H
