//
// Created by Ameen on 4/6/2016.
//

#ifndef MY_RAY_TRACER_TRIANGLE_H
#define MY_RAY_TRACER_TRIANGLE_H

#define EPS 1e-8

#include "Vec3.h"
#include "Shape.h"


class Triangle : public Shape {
public:
    Vec3 v0{}, v1{}, v2{};

    Triangle(const Vec3 &color,
             const Vec3 &emission,
             double reflectivity,
             double transparency,
             const Vec3 &v0, const Vec3 &v1, const Vec3 &v2)
            : Shape(Vec3{}, color, emission, reflectivity, transparency), v0(v0), v1(v1), v2(v2) {
        center = (v0 + v1 + v2) * (1.0 / 3.0);
    }


    double collisionPointDist(const Ray &ray) const override {

        Vec3 r1 = v1 - v0;
        Vec3 r2 = v2 - v0;

        Vec3 p = ray.dir.cross(r2);
        double det = r1.dot(p);

        if (fabs(det) < EPS) {
            return 0;
        }

        double invDet = 1/det;
        Vec3 t = ray.origin - v0;
        double u = t.dot(p) * invDet;
        if (u < 0 || u > 1) {
            return 0;
        }

        Vec3 q = t.cross(r1);
        double v = ray.dir.dot(q) * invDet;
        if (v < 0 || u + v > 1) {
            return 0;
        }

        return r2.dot(q) * invDet;
    }

    Vec3 normal(const Ray &ray, double dist) const override {
        Vec3 r1 = v1 - v0;
        Vec3 r2 = v2 - v0;
        Vec3 normal = r1.cross(r2);
        normal = normal.norm();

        return normal;
    }
};


#endif //MY_RAY_TRACER_TRIANGLE_H
