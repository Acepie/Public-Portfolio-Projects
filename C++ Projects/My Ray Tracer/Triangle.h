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
        Vec3 dist = v0 - ray.origin;
        Vec3 norm = r1.cross(r2);

        if (fabs(norm.dot(ray.dir)) < EPS) {
            return 0;
        }

        double ratio = norm.dot(dist) / norm.dot(ray.dir);

        if (ratio < 0) {
            return 0;
        }

        Vec3 intersectPoint = ray.origin + (ray.dir * ratio);

        double r12, r11, r22, ri1, ri2;
        r12 = r1.dot(r2);
        r11 = r1.dot(r1);
        r22 = r2.dot(r2);
        ri1 = r1.dot(intersectPoint - v0);
        ri2 = r2.dot(intersectPoint - v0);

        double s, t;

        s = (r12 * ri2 - r22 * ri1) / (r12 * r12 - r11 * r22);

        if (0 > s || 1 < s) {
            return 0;
        }

        t = (r12 * ri1 - r11 * ri2) / (r12 * r12 - r11 * r22);

        if (0 > t || 1 < t) {
            return 0;
        }

        if (0 > s + t || 1 < s + t) {
            return 0;
        }

        return ratio;
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
