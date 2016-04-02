#ifndef SPIKE_H
#define SPIKE_H

#include "obstacle.h"

// A triangular obstacle
class Spike : public Obstacle
{
public:
	Spike(int screenX, int screenY);
	Spike(const Spike & other);
	Spike & operator=(const Spike & rhs);
	virtual int getXCoord();
	virtual int getYCoord();
	virtual bool isColliding(int playerX, int playerY);
	virtual void draw(int screenX, int screenY);
	
private:
	int width;
	int height;
	int location;
};

#endif