//
// Created by Ameen on 4/6/2016.
//

#ifndef MY_RAY_TRACER_SHAPE_H
#define MY_RAY_TRACER_SHAPE_H


#include "Ray.h"
class Shape {
 public:
  virtual ~Shape() = default;
  virtual double collisionPoint(const Ray &ray) const = 0;

  //must be proceeded by collisionPoint
  virtual Vec3 normal(const Ray &ray, double dist) const = 0;

  Vec3 center, color, emission;
  double reflectivity, transparency;
 protected:
  Shape() { }

  Shape(const Vec3 &center, const Vec3 &color, const Vec3 &emission, double reflectivity, double transparency) : center(
      center), color(color), emission(emission), reflectivity(reflectivity), transparency(transparency) { }
};


#endif //MY_RAY_TRACER_SHAPE_H
