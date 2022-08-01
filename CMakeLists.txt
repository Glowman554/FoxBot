cmake_minimum_required (VERSION 3.6)
project(foxboot)

add_subdirectory(sleepy-discord)

add_executable(foxboot
	src/main.cpp
)

target_link_libraries(foxboot sleepy-discord)