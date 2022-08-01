include_directories(../../include)
include_directories(include)

add_library(
    util_commands
    SHARED
    main.cpp
	say_command.cpp
	help_command.cpp
)

add_custom_command(TARGET util_commands
	POST_BUILD
	COMMAND /bin/bash -c "cp -v ${CMAKE_CURRENT_BINARY_DIR}/*.so ${CMAKE_CURRENT_BINARY_DIR}/.."
)