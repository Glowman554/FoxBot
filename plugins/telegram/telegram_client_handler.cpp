#include <telegram_client_handler.h>
#include <telegram_client_command_handler.h>
#include <core/core.h>

#include <debug.h>

telegram_client_handler::telegram_client_handler() : bot(global_config->get_key((char*) "telegram_token")) {
	telegram_client_handler* _this = this;
	this->bot.getEvents().onAnyMessage([&_this](TgBot::Message::Ptr message) {
		telegram_client_command_handler* cmd = new telegram_client_command_handler(message, _this);
		global_command_manager->on_command(cmd);
	});
}

telegram_client_handler::~telegram_client_handler() {}

void telegram_client_handler::run() {
	debugf("Bot username: %s\n", this->bot.getApi().getMe()->username.c_str());
	bot.getApi().deleteWebhook();

	TgBot::TgLongPoll longPoll(this->bot);
	
	while (true) {
		debugf("Long poll started\n");
		longPoll.start();
    }
}

char* telegram_client_handler::get_name() {
	return (char*) "telegram";
}

bool telegram_client_handler::is_bot_owner(char* id) {
	return strcmp(id, global_config->get_key((char*) "telegram_owner")) == 0;
}
