//
// Created by Ameen on 5/18/2016.
//

#include "Tracer.h"

Vec3 Tracer::traceRay(const Ray &ray, std::vector<std::unique_ptr<Shape>> &shapes, int depth) {

  double closest = INFINITY;

  int ind = 0;
  int bestInd = 0;

  for (auto &shape : shapes) {
    auto dist = shape->collisionPointDist(ray);

    if (dist != 0 && dist < closest) {
      closest = dist;
      bestInd = ind;
    }
    ind++;
  };

  Shape *closest_sphere = shapes[bestInd].get();

  if (closest == INFINITY) {
    return Vec3{126.0 / 255.0, 192.0 / 255.0, 238.0 / 255.0};
  }

  Vec3 hit_point = ray.origin + (ray.dir * closest);
  Vec3 norm_vec = closest_sphere->normal(ray, closest);
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

    Vec3 reflection = traceRay(Ray{reflection_dir, hit_point + norm_vec * 0.0001}, shapes, depth + 1);

    Vec3 refraction{0.0, 0.0, 0.0};

    if (closest_sphere->transparency > 0) {
      double refract_index = 1.1, eta = inside ? refract_index : 1 / refract_index;
      double cosi = -norm_vec.dot(ray.dir);
      double k = 1 - eta * eta * (1 - pow(cosi, 2));

      Vec3 refract_dir = ray.dir * eta + norm_vec * (eta * cosi - sqrt(k));
      refract_dir = refract_dir.norm();
      refraction = traceRay(Ray{refract_dir, hit_point - norm_vec * 0.0001}, shapes, depth + 1);
    }

    color = mul(refraction * (1 - fresnel) * closest_sphere->transparency
                    + (reflection * fresnel * closest_sphere->reflectivity), closest_sphere->color);
  } else {
    for (auto &shapeI : shapes) {
      if (shapeI->emission != Vec3(0, 0, 0)) {
        Vec3 transmission_rate{1.0, 1.0, 1.0};
        Vec3 light_dir = shapeI->center - hit_point;
        light_dir = light_dir.norm();
        for (auto &shapeJ : shapes) {
          if (shapeI != shapeJ) {
            if (shapeJ->collisionPointDist(Ray{light_dir, hit_point + norm_vec * 0.0001}) != 0) {
              transmission_rate = Vec3{0, 0, 0};
              break;
            }
          }
        }

        color += mul(mul(closest_sphere->color, transmission_rate)
                         * std::max(0.0, norm_vec.dot(light_dir)),
                     shapeI->emission);
      }
    }
  }

  return color + closest_sphere->emission;
}
