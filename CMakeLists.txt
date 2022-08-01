cmake_minimum_required (VERSION 3.6)
project(foxbot)


add_subdirectory(plugins)

include_directories(include)

add_executable(foxbot
	src/main.cpp
	src/utils/config.cpp
	src/utils/utils.cpp
	src/core/core.cpp
	src/core/client_command_handler.cpp
	src/core/command.cpp
	src/plugin/plugin.cpp
)

target_link_libraries(foxbot dl)
set_property(TARGET foxbot PROPERTY ENABLE_EXPORTS ON)
