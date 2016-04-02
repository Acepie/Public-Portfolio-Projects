#include <cxxtest/TestSuite.h>
#include "level.h"

class LevelTestSuite: public CxxTest::TestSuite {
	Level * level;

public:

	void setUp() {
		level = new Level(1000, 1000);
	}

	void tearDown() {
		delete level;
	}

	void testCopyConstructor() {
		Level * level_copy;
		level_copy = new Level(level);
		TS_ASSERT_EQUALS(level, level_copy);
		delete level_copy;
	}
};
