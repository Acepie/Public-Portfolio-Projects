#include <cxxtest/TestSuite.h>
#include "spike.h"

class SpikeTestSuite : public CxxTest::TestSuite
{
  Spike * spike;
  
public:

void setUp()
{
  spike = new Spike(1000, 1000);
}

void tearDown()
{
  delete spike;
}

void testCopyConstructor()
{
  Spike * spike_copy;
  spike_copy = new Spike(Spike);
  TS_ASSERT_EQUALS(spike, spike_copy);
  delete spike_copy;
}

void testIsColliding()
{
  TS_ASSERT_EQUALS(spike->isColliding(spike->getXCoord(), spike->getYCoord()), true);
  TS_ASSERT_EQUALS(spike->isColliding(2000, 2000), false);
}

};
