cmake_minimum_required(VERSION 3.3)
project(OpenGL)

find_package(OpenGl)
find_package(GLUT)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
        main.cpp GameWorld.cpp GameWorld.h Level.h Level.cpp Obstacle.h bird.h bird.cpp spike.h spike.cpp)

add_executable(OpenGL ${SOURCE_FILES})

target_link_libraries(OpenGL libopengl32.a libglu32.a libfreeglut.a)