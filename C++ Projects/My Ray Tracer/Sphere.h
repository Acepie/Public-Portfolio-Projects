#ifndef MY_RAY_TRACER_SPHERE_H
#define MY_RAY_TRACER_SPHERE_H

#include "Vec3.h"
#include "Ray.h"
#include "Shape.h"

#include <cmath>

class Sphere : public Shape {
 public:
  double radius;

  Sphere(const Vec3 &center,
         const Vec3 &color,
         const Vec3 &emission,
         double radius,
         double reflectivity,
         double transparency)
      : Shape(center, color, emission, reflectivity, transparency), radius(radius) {
  }

  bool insideSphere(const Vec3& point) {
    return center.dist(point) < radius;
  }

  //if ray collides with sphere returns the distance along the ray else returns 0
  virtual double collisionPointDist(const Ray &ray) const {
    Vec3 diff = center - ray.origin;

    double diff_dot_ray = diff.dot(ray.dir);

    double det = pow(diff_dot_ray, 2) - diff.dot(diff) + pow(radius, 2);

    if (det < 0) {
      return 0;
    } else {
      det = sqrt(det);
    }

    if (diff_dot_ray - det > 0.00001) {
      return (diff_dot_ray - det);
    } else if (diff_dot_ray + det > 0.00001) {
      return (diff_dot_ray + det);
    } else {
      return 0;
    }
  }

  virtual Vec3 normal(const Ray &ray, double dist) const override {
    Vec3 norm_vec = ray.origin + (ray.dir * dist) - center;
    norm_vec = norm_vec.norm();

    return norm_vec;
  }
};


#endif //MY_RAY_TRACER_SPHERE_H
