cmake_minimum_required(VERSION 3.17)
project(sem01)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g -fsanitize=thread -lpthread")

add_executable(sem01 solution.cpp sample_tester.cpp)