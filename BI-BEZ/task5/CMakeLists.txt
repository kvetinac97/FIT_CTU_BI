cmake_minimum_required(VERSION 3.16)
project(bez5)

add_executable(bez5 main.c)

find_package(OpenSSL REQUIRED)
target_link_libraries(bez5 OpenSSL::SSL)