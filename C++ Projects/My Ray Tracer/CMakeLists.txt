# cmake_minimum_required(VERSION <specify CMake version here>)
project(My_Ray_Tracer)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")

set(SOURCE_FILES main.cpp Sphere.h Vec3.h Ray.h)
add_executable(My_Ray_Tracer ${SOURCE_FILES})