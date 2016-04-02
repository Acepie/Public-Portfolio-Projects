#include <cxxtest/TestSuite.h>
#include "obstacle.h"

class ObstacleTestSuite : public CxxTest::TestSuite
{
  Obstacle * obstacle;
  
public:

void setUp()
{
  obstacle = new Spike(1000, 1000);
}

void tearDown()
{
  delete obstacle;
}

void testIsColliding()
{
  TS_ASSERT(obstacle->isColliding(obstacle->getXCoord(), obstacle->getYCoord()));
  TS_ASSERT_EQUALS(obstacle->isColliding(2000, 2000), false);
}
};
