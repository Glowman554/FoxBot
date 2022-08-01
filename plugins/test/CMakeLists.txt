include_directories(../../include)

add_library(
    test
    SHARED
    main.cpp
)

add_custom_command(TARGET test
	POST_BUILD
	COMMAND /bin/bash -c "cp -v ${CMAKE_CURRENT_BINARY_DIR}/*.so ${CMAKE_CURRENT_BINARY_DIR}/.."
)