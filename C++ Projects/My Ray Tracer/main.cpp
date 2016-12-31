#include <fstream>
#include <vector>
#include <algorithm>
#include <c++/memory>
#include "Vec3.h"
#include "Sphere.h"
#include "Triangle.h"
//#include "gtest/gtest.h"
#include "Tracer.h"

/*TEST(CanIEvenTest, TrivialTest) {
  EXPECT_EQ(2, 1 + 1);
}

TEST(CanIEvenTest, TrivialTest2) {
  EXPECT_NE(2, 1);
}*/

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

  float width = 800, height = 600;

  Vec3 *image = new Vec3[(int) width * (int) height];
  Vec3 *pix = image;
  double fov = 60.0, asp = width / (double) height;
  double ang = tan(M_PI * 0.5 * fov / 180.);

  Tracer tracer{shapes};

  for (auto j = 0; j < height; j++) {
    for (auto i = 0; i < width; i++, pix++) {
      double x = (2 * ((i + 0.5) / width) - 1) * ang * asp;
      double y = (1 - 2 * ((j + 0.5) / height)) * ang;
      Vec3 dir{x, y, -1};
      dir = dir.norm();
      *pix = tracer.traceRay(Ray{dir, Vec3{0.0, 0.0, 0.0}}, 0);
    }
  }

  std::ofstream ofs("./testscene.ppm", std::ios::out | std::ios::binary);
  ofs << "P6\n" << width << " " << height << "\n255\n";
  for (unsigned i = 0; i < width * height; ++i) {
    ofs << (unsigned char) (std::min(float(1), (const float &) image[i].x) * 255) <<
        (unsigned char) (std::min(float(1), (const float &) image[i].y) * 255) <<
        (unsigned char) (std::min(float(1), (const float &) image[i].z) * 255);
  }
  ofs.close();
  delete[] image;

/*  ::testing::InitGoogleTest(&argc, argv);
  RUN_ALL_TESTS();*/

  return 0;
}
