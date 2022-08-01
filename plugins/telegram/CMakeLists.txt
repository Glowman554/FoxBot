include_directories(../../include)
include_directories(include)

add_subdirectory(tgbot-cpp)


add_library(
    telegram
    SHARED
    main.cpp
	telegram_client_command_handler.cpp
	telegram_client_handler.cpp
)

target_link_libraries(
	telegram
	TgBot
)

add_custom_command(TARGET telegram
	POST_BUILD
	COMMAND /bin/bash -c "cp -v ${CMAKE_CURRENT_BINARY_DIR}/*.so ${CMAKE_CURRENT_BINARY_DIR}/.."
)