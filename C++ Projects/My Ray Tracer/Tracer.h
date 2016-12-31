//
// Created by Ameen on 5/18/2016.
//

#define MAX_DEPTH 5

#ifndef MY_RAY_TRACER_TRACER_H
#define MY_RAY_TRACER_TRACER_H


#include <c++/memory>
#include <c++/vector>
#include <fstream>

#include "Vec3.h"
#include "Ray.h"
#include "Shape.h"

/**
 * Helper class used to trace rays in a scene
 */
class Tracer {

const std::vector<std::unique_ptr<Shape>> shapes;

 public:
  
  /**
   * Creates a Tracer that can display a scene
   * @param shapes a list of all the shapes in the scene
   */
  Tracer(std::vector<std::unique_ptr<Shape>> &shapes) :shapes(std::move(shapes)) {}


  /**
   * Traces a ray to determine the proper color to display at a given point
   * @param ray the ray to follow
   * @param the current ray trace depth
   */
  Vec3 traceRay(const Ray &ray, int depth);

  /**
   * Traces entire scene from origin and renders it to a ppm file called testscene.ppm
   * pixels truncated to ints
   * @param width width of image in pixels 
   * @param height height of image in pixels
   */
  void traceScene(const float width, const float height);

};


#endif //MY_RAY_TRACER_TRACER_H
