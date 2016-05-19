//
// Created by Ameen on 4/6/2016.
//

#ifndef MY_RAY_TRACER_SHAPE_H
#define MY_RAY_TRACER_SHAPE_H


#include "Ray.h"

/**
 * Represents any shape that can be represented in a scene
 */
class Shape {
 public:
  virtual ~Shape() = default;

  /**
   * Return the distance a ray must travel to collide to this shape.
   * Returns 0 if there is no collision
   *
   * @param ray Incoming ray
   * @return distance from ray to collision point
   */
  virtual double collisionPointDist(const Ray &ray) const = 0;

  /**
   * Calculate the normal vector at the point that a given ray hits.
   * Should be called after collisionPointDist() to ensure that the ray collides
   * @param ray the ray that is colliding
   * @param dist the length of the ray
   * @return the direction of the normal
   */
  virtual Vec3 normal(const Ray &ray, double dist) const = 0;

  Vec3 center, color, emission;
  double reflectivity, transparency;
 protected:
  Shape() { }

  Shape(const Vec3 &center, const Vec3 &color, const Vec3 &emission, double reflectivity, double transparency) : center(
      center), color(color), emission(emission), reflectivity(reflectivity), transparency(transparency) { }
};


#endif //MY_RAY_TRACER_SHAPE_H
