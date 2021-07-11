cmake_minimum_required(VERSION 3.16)
project(bez4)

set(CMAKE_CXX_STANDARD 17)

add_executable(bez4_encrypt encrypt.cpp common.cpp)
add_executable(bez4_decrypt decrypt.cpp common.cpp)

find_package(OpenSSL REQUIRED)
target_link_libraries(bez4_encrypt OpenSSL::SSL)
target_link_libraries(bez4_decrypt OpenSSL::SSL)