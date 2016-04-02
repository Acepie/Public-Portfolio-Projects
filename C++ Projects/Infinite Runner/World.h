#ifndef OPENGL_WORLD_H
#define OPENGL_WORLD_H


#include <string>

/*
Simple example world that moves a triangle around.
*/
class World {
public:
	//initializes world
    World():x(0),y(0),dx(0),dy(0),acceleration(.001), worldWidth(800), worldHeight(600){}

    //get the X coordinate of the triangle
    float getX();

    //get the Y coordinate of the triangle
    float getY();

    //get the width of the world
    int getWidth();

    //get the height of the world
    int getHeight();

    //function that makes a string saying how glut should set up the screen
    //necessary for full screen but otherwise not necessary
    const char* glutGameString();

    //update this world on tick
    void updateWorld();

    //resets triangle to center of screen
    void reset();

    //react to up arrow
    void up();

    //react to down arrow
    void down();

    //react to left arrow
    void left();

    //react to right arrow
    void right();

private:
    //triangle position
    float x, y;
    //triangle speed
    float dx, dy;
    //triangle acceleration
    const float acceleration;
	//screen dimensions
    const int worldWidth, worldHeight;
};


#endif //OPENGL_WORLD_H
