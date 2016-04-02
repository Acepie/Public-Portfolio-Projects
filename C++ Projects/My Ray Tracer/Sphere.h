#ifndef MY_RAY_TRACER_SPHERE_H
#define MY_RAY_TRACER_SPHERE_H

#include "Vec3.h"
#include "Ray.h"

#include <cmath>

class Sphere {
 public:
  Vec3 center, color, emission;
  double radius, reflectivity, transparency;


  Sphere(const Vec3 &center,
         const Vec3 &color,
         const Vec3 &emission,
         double radius,
         double reflectivity,
         double transparency)
      : center(center), color(color), emission(emission), radius(radius), reflectivity(reflectivity),
        transparency(transparency) { }

  bool insideSphere(const Vec3& point) {
    return center.dist(point) < radius;
  }

  //if ray collides with sphere returns the distance along the ray else returns 0
  double collisionPoint(const Ray& ray) const {
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
};


#endif //MY_RAY_TRACER_SPHERE_H
