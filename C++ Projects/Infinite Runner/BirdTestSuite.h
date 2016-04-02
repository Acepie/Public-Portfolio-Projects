#include <cxxtest/TestSuite.h>
#include "bird.h"

class BirdTestSuite : public CxxTest::TestSuite
{
  Bird * bird;
  
public:

void setUp()
{
  bird = new Bird(1000, 1000);
}

void tearDown()
{
  delete bird;
}

void testCopyConstructor()
{
  Bird * bird_copy;
  bird_copy = new Bird(*bird);
  TS_ASSERT_EQUALS(bird, bird_copy);
  delete bird_copy;
}

void testIsColliding()
{
  TS_ASSERT_EQUALS(bird->isColliding(bird->getXCoord(), bird->getYCoord()), true);
  TS_ASSERT_EQUALS(bird->isColliding(1500, 1500), false);
}
};
