#pragma once
#include <core/client_handler.h>

#include <tgbot/tgbot.h>

class telegram_client_handler : public client_handler {
	public:
		telegram_client_handler();
		~telegram_client_handler();

		char* get_name() override;
		bool is_bot_owner(char* id) override;

		void run();

		TgBot::Bot bot;
};