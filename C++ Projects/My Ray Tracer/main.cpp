#include <fstream>
#include <vector>
#include <algorithm>
#include "Vec3.h"
#include "Sphere.h"

#define MAX_DEPTH 5
#define M_PI 3.1415926535

Vec3 traceRay(const Ray &ray, std::vector<Sphere> spheres, int depth) {

  double closest = INFINITY;
  const Sphere *closest_sphere = nullptr;

  std::for_each(spheres.begin(), spheres.end(), [&](const Sphere &s) {
    auto dist = s.collisionPoint(ray);

    if (dist != 0 && dist < closest) {
      closest = dist;
      closest_sphere = &s;
    }
  });

  if (closest == INFINITY) {
    return Vec3{126.0 / 255.0, 192.0 / 255.0, 238.0 / 255.0};
  }

  Vec3 hit_point = ray.origin + (ray.dir * closest);
  Vec3 norm_vec = hit_point - closest_sphere->center;
  norm_vec = norm_vec.norm();
  bool inside = false;

  if (norm_vec.dot(ray.dir) > 0) {
    norm_vec = -norm_vec;
    inside = true;
  }

  Vec3 color{0, 0, 0};

  if ((closest_sphere->reflectivity > 0 || closest_sphere->transparency > 0) && depth < MAX_DEPTH) {
    double facing_amount = -(norm_vec.dot(ray.dir));

    double fresnel = .1 + .9 * pow(1 - facing_amount, 3);

    Vec3 reflection_dir = ray.dir - norm_vec * 2 * norm_vec.dot(ray.dir);
    reflection_dir = reflection_dir.norm();

    Vec3 reflection = traceRay(Ray{reflection_dir, hit_point + norm_vec * 0.0001}, spheres, depth + 1);

    Vec3 refraction{0.0, 0.0, 0.0};

    if (closest_sphere->transparency > 0) {
      double refract_index = 1.1, eta = inside ? refract_index : 1 / refract_index;
      double cosi = -norm_vec.dot(ray.dir);
      double k = 1 - eta * eta * (1 - pow(cosi, 2));

      Vec3 refract_dir = ray.dir * eta + norm_vec * (eta * cosi - sqrt(k));
      refract_dir = refract_dir.norm();
      refraction = traceRay(Ray{refract_dir, hit_point - norm_vec * 0.0001}, spheres, depth + 1);
    }

    color = mul(refraction * (1 - fresnel) * closest_sphere->transparency
                    + (reflection * fresnel), closest_sphere->color);
  } else {
    for (auto i = 0; i < spheres.size(); i++) {
      if (spheres[i].emission != Vec3(0, 0, 0)) {
        Vec3 transmission_rate{1.0, 1.0, 1.0};
        Vec3 light_dir = spheres[i].center - hit_point;
        light_dir = light_dir.norm();
        for (auto j = 0; j < spheres.size(); j++) {
          if (i != j) {
            if (spheres[j].collisionPoint(Ray{light_dir, hit_point + norm_vec * 0.0001}) != 0) {
              transmission_rate = Vec3{0, 0, 0};
              break;
            }
          }
        }

        color += mul(mul(closest_sphere->color, transmission_rate)
                         * std::max(0.0, norm_vec.dot(light_dir)),
                     spheres[i].emission);
      }
    }
  }

  return color + closest_sphere->emission;
}

int main() {

  std::vector<Sphere> spheres{};

  spheres.push_back(Sphere{Vec3{0.0, -100004.0, 0.0}, Vec3{0.8, 0.8, 0.8}, Vec3{0.0, 0.0, 0.0}, 100000.0, 0.0, 0.0});

  spheres.push_back(Sphere{Vec3{0.0, 0.0, -20}, Vec3{1.0, 0.32, 0.36}, Vec3{0.0, 0.0, 0.0}, 4, 0.8, 0.5});
  spheres.push_back(Sphere{Vec3{5.0, -1.0, -15.0}, Vec3{0.9, 0.76, 0.46}, Vec3{0.0, 0.0, 0.0}, 2, 0.0, 0.0});
  spheres.push_back(Sphere{Vec3{5.0, 0.0, -25.0}, Vec3{0.65, 0.77, 0.97}, Vec3{0.0, 0.0, 0.0}, 3, 0.3, 0.0});
  spheres.push_back(Sphere{Vec3{-5.5, 0.0, -15.0}, Vec3{0.9, 0.9, 0.9}, Vec3{0.0, 0.0, 0.0}, 3, 0.5, 0.0});

  spheres.push_back(Sphere{Vec3{0.0, 200.0, -30.0}, Vec3{0.0, 0.0, 0.0}, Vec3{0.6, 0.6, 0.5}, 3, 0.0, 0.0});


  float width = 1920, height = 1080;

  Vec3 *image = new Vec3[(int) width * (int) height];
  Vec3 *pix = image;
  double fov = 60.0, asp = width / (double) height;
  double ang = tan(M_PI * 0.5 * fov / 180.);

  for (auto j = 0; j < height; j++) {
    for (auto i = 0; i < width; i++, pix++) {
      double x = (2 * ((i + 0.5) / width) - 1) * ang * asp;
      double y = (1 - 2 * ((j + 0.5) / height)) * ang;
      Vec3 dir{x, y, -1};
      dir = dir.norm();
      *pix = traceRay(Ray{dir, Vec3{0.0, 0.0, 0.0}}, spheres, 0);
    }
  }

  std::ofstream ofs("./untitled.ppm", std::ios::out | std::ios::binary);
  ofs << "P6\n" << width << " " << height << "\n255\n";
  for (unsigned i = 0; i < width * height; ++i) {
    ofs << (unsigned char) (std::min(float(1), (const float &) image[i].x) * 255) <<
        (unsigned char) (std::min(float(1), (const float &) image[i].y) * 255) <<
        (unsigned char) (std::min(float(1), (const float &) image[i].z) * 255);
  }
  ofs.close();
  delete[] image;

  return 0;
}