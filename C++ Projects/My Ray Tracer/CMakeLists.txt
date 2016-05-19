cmake_minimum_required(VERSION 3.2)
project(My_Ray_Tracer)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")

set(SOURCE_FILES main.cpp Sphere.h Vec3.h Ray.h Triangle.h Shape.h Tracer.cpp Tracer.h)
add_executable(My_Ray_Tracer ${SOURCE_FILES})

add_subdirectory(googletest)

include_directories(googletest/googletest/include)
include_directories(googletest/googlemock/include)

target_link_libraries(My_Ray_Tracer gtest gtest_main)