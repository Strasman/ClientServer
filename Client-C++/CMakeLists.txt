cmake_minimum_required(VERSION 3.21)
project(assignment3SPLcpp)

set(CMAKE_CXX_STANDARD 11)

set(Boost_INCLUDE_DIR /cygdrive/C/cygwin64/usr/include)

find_package(Boost REQUIRED COMPONENTS system)
include_directories(${Boost_INCLUDE_DIRS})

add_executable(assignment3SPLcpp BGSclient.cpp, include/connectionHandler.h, src/connectionHandler.cpp, src/echoClient.cpp include/KeyboardReader.h src/KeyboardReader.cpp include/ServerReader.h src/ServerReader.cpp)
target_link_libraries(${assignment3SPLcpp} ${Boost_LIBRARIES})
