include_directories(../../include)
include_directories(include)

add_subdirectory(sleepy-discord)

set_property(TARGET sleepy-discord PROPERTY POSITION_INDEPENDENT_CODE ON)
set_property(TARGET cpr PROPERTY POSITION_INDEPENDENT_CODE ON)

add_library(
    discord
    SHARED
    main.cpp
	discord_client_handler.cpp
	discord_client_command_handler.cpp
)

target_link_libraries(
	discord
	sleepy-discord
)