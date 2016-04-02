#include "World.h"

void World::updateWorld() {
    x += dx;
    y += dy;
}

void World::reset() {
    x = 0;
    y = 0;
    dx = 0;
    dy = 0;
}

void World::up() {
    dy += acceleration;
}

void World::down() {
    dy -= acceleration;
}

void World::left() {
    dx -= acceleration;
}

void World::right() {
    dx += acceleration;
}

const char *World::glutGameString() {
    std::string string(std::to_string(worldWidth) + "x" + std::to_string(worldHeight) + ":32");
    return string.c_str();
}

float World::getY() {
    return y;
}

float World::getX() {
    return x;
}

int World::getWidth() {
    return worldWidth;
}

int World::getHeight() {
    return worldHeight;
}