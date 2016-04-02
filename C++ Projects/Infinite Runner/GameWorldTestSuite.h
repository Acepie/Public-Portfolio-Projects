#include <cxxtest/TestSuite.h>
#include "GameWorld.h"

class GameWorldTestSuite : public CxxTest::TestSuite
{
  // a testable game
  GameWorld * game;
  
public:

  // initialize the testable game
  void setUp()
  {
    game = new GameWorld();
  }
  
  // memory management
  void tearDown()
  {
    delete game;
  }

  // check glut string for full screen
  void testGlutGameString()
  {
    TS_ASSERT_EQUALS(game->glutGameString(), "1980x1020:32");
  }
  
  // test on tick method
  void testUpdateWorld()
  {
    TS_ASSERT_EQUALS(game->playerXCoord(), 0);
    TS_ASSERT_EQUALS(game->playerYCoord(), 0);
    
    game->updateWorld();
    
    TS_ASSERT_EQUALS(game->playerXCoord(), 1);
    TS_ASSERT_EQUALS(game->playerYCoord(), 0);
  }
  
  // test on tick method
  void testUpdateWorldWithUp()
  {
    TS_ASSERT_EQUALS(game->playerXCoord(), 0);
    TS_ASSERT_EQUALS(game->playerYCoord(), 0);
    
    game->up();
    game->updateWorld();
    
    TS_ASSERT_EQUALS(game->playerXCoord(), 1);
    TS_ASSERT_EQUALS(game->playerYCoord(), 5.98);
  }
  
  // test reset method
  void testReset()
  {
    TS_ASSERT_EQUALS(game->playerXCoord(), 0);
    TS_ASSERT_EQUALS(game->playerYCoord(), 0);
    
    game->updateWorld();
    game->up();
    
    TS_ASSERT_EQUALS(game->playerXCoord(), 1);
    TS_ASSERT_EQUALS(game->playerYCoord(), 3);
    
    game->reset();
    
    TS_ASSERT_EQUALS(game->playerXCoord(), 0);
    TS_ASSERT_EQUALS(game->playerYCoord(), 0);
  }
  
  // test up method
  void testUp()
  {
    TS_ASSERT_EQUALS(game->playerXCoord(), 0);
    TS_ASSERT_EQUALS(game->playerYCoord(), 0);
    
    game->up();
    
    TS_ASSERT_EQUALS(game->playerXCoord(), 0);
    TS_ASSERT_EQUALS(game->playerYCoord(), 3);
  }
  
  //test gameOver method
  void testGameOver()
  {
    TS_ASSERT_EQUALS(game->gameOver(), false);
    for (int i = 0; i <= 1080; i++){
      game->updateWorld();
    }
    TS_ASSERT(game->gameOver());
  }
  
  //test swapLevels method
  void testSwapLevels()
  {
    GameWorld * game2;
    game2 = new GameWorld(game);
    game2->swapLevels();
    TS_ASSERT(game->getObstacles() != game2->getObstacles());
    delete game2;
  }
};
