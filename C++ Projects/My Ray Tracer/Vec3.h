#ifndef MY_RAY_TRACER_VEC3_H
#define MY_RAY_TRACER_VEC3_H

#include <cmath>

class Vec3 {
 public:
  Vec3(){}

  Vec3(double x, double y, double z) : x(x), y(y), z(z) { }

  void operator+=(const Vec3& other) {
    x += other.x;
    y += other.y;
    z += other.z;
  }

  void operator-=(const Vec3& other) {
    x -= other.x;
    y -= other.y;
    z -= other.z;
  }

  const Vec3 operator-(){
    return Vec3(-x, -y, -z);
  }

  double x, y, z;

  const Vec3 cross(const Vec3& other) {
    return Vec3(y *other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
  }

  const double dot(const Vec3& other) {
    return x * other.x + y * other.y + z * other.z;
  }

  const double dist (const Vec3& other) {
    return sqrt(pow(x - other.x, 2) + pow(y - other.y, 2) + pow(z - other.z, 2));
  }

  friend const Vec3 operator+(const Vec3& lhs, const Vec3& rhs) {
    return Vec3(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z);
  }

  friend const Vec3 operator-(const Vec3& lhs, const Vec3& rhs) {
    return Vec3(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z);
  }

  friend const Vec3 mul(const Vec3& lhs, const Vec3& rhs) {
    return Vec3(lhs.x * rhs.x, lhs.y * rhs.y, lhs.z * rhs.z);
  }

  friend const Vec3 operator*(const Vec3& lhs, float f) {
    return Vec3(lhs.x * f, lhs.y * f, lhs.z * f);
  }

  friend bool operator==(const Vec3& lhs, const Vec3& rhs) {
    return lhs.x == rhs.x && lhs.y == rhs.y && lhs.z == rhs.z;
  }

  friend bool operator!=(const Vec3& lhs, const Vec3& rhs) {
    return !(lhs == rhs);
  }

  const double magnitude() const {
    return sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));
  }

  Vec3& norm(){
    double mag = magnitude();
    if (mag > 0.000001)  {
      x /= mag;
      y /= mag;
      z /= mag;
    }
    return *this;
  }
};


#endif //MY_RAY_TRACER_VEC3_H
