cmake_minimum_required (VERSION 3.6)
project(foxbot)

add_subdirectory(sleepy-discord)


include_directories(include)

add_executable(foxbot
	src/main.cpp
	src/utils/config.cpp
	src/utils/utils.cpp
	src/core/core.cpp
	src/core/client_command_handler.cpp
	src/core/platform/discord_client_handler.cpp
	src/core/platform/discord_client_command_handler.cpp
	src/core/command.cpp
)

target_link_libraries(foxbot sleepy-discord)