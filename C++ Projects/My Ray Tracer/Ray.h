#ifndef MY_RAY_TRACER_RAY_H
#define MY_RAY_TRACER_RAY_H


#include "Vec3.h"
class Ray {
 public:

  Ray(Vec3 dir, Vec3 origin) : dir(dir.norm()), origin(origin) { }

  Vec3 dir, origin;

};


#endif //MY_RAY_TRACER_RAY_H
