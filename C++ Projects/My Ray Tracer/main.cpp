#include <vector>
#include <c++/memory>
#include "Vec3.h"
#include "Sphere.h"
#include "Triangle.h"
#include "Tracer.h"

int main(int argc, char **argv) {

  std::vector<std::unique_ptr<Shape>> shapes{};

  shapes.push_back(std::make_unique<Sphere>(Sphere{Vec3{0.0, -100004.0, 0.0}, 
    Vec3{0.8, 0.8, 0.8}, 
    Vec3{}, 
    100000.0, 0.0, 0.0}));
  shapes.push_back(std::make_unique<Sphere>(Sphere{Vec3{0.0, 0.0, -20}, 
    Vec3{1.0, 0.32, 0.36}, 
    Vec3{}, 
    4, 1.0, 1.0}));
  shapes.push_back(std::make_unique<Sphere>(Sphere{Vec3{5.0, -1.0, -15.0}, 
    Vec3{0.9, 0.76, 0.46}, 
    Vec3{}, 
    2, 0.0, 0.0}));
  shapes.push_back(std::make_unique<Sphere>(Sphere{Vec3{5.0, 0.0, -25.0}, 
    Vec3{0.65, 0.77, 0.97}, 
    Vec3{}, 
    3, 0.5, 1.0}));
  shapes.push_back(std::make_unique<Sphere>(Sphere{Vec3{-5.5, 0.0, -15.0}, 
    Vec3{0.9, 0.9, 0.9}, 
    Vec3{}, 
    3, 0.8, 1.0}));
  shapes.push_back(std::make_unique<Triangle>(Triangle{Vec3{0.3, 0.9, 0.3}, 
    Vec3{}, 0.0, 0.0, 
    Vec3{0.0, 10.0, -40.0},
    Vec3{10.0, 20.0, -50.0}, 
    Vec3{0.0, 20.0, -50.0}}));
  shapes.push_back(std::make_unique<Triangle>(Triangle{Vec3{0.3, 0.9, 0.3}, 
    Vec3{}, 0.5, 0.0, 
    Vec3{0.0, 10.0, -40.0},
    Vec3{-10.0, 20.0, -50.0}, 
    Vec3{0.0, 20.0, -50.0}}));

  shapes.push_back(std::make_unique<Sphere>(Sphere{Vec3{0.0, 30.0, 30.0}, 
    Vec3{}, 
    Vec3{0.9, 0.9, 0.9}, 
    3, 0.0, 0.0}));

  Tracer tracer{shapes};

  const float width = 800, height = 600;

  tracer.traceScene(width, height);

  return 0;
}
