cmake_minimum_required (VERSION 3.6)
project(foxboot)

add_subdirectory(sleepy-discord)


include_directories(include)

add_executable(foxboot
	src/main.cpp
	src/utils/config.cpp
	src/utils/utils.cpp
)

target_link_libraries(foxboot sleepy-discord)