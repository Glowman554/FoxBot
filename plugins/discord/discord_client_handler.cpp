#include <discord_client_handler.h>
#include <discord_client_command_handler.h>
#include <core/core.h>

discord_client_handler::discord_client_handler() : SleepyDiscord::DiscordClient("", SleepyDiscord::USER_CONTROLED_THREADS) {
	char* token = global_config->get_key((char*) "discord_token");
	assert(token != nullptr);

	this->setToken(token);
	this->setIntents(SleepyDiscord::Intent::SERVER_MESSAGES);
}

discord_client_handler::~discord_client_handler() {}

char* discord_client_handler::get_name() {
	return (char*) "discord";
}

bool discord_client_handler::is_bot_owner(char* id) {
	return strcmp(id, global_config->get_key((char*) "discord_owner")) == 0;
}

void discord_client_handler::onMessage(SleepyDiscord::Message message) {
	if (!message.author.bot) {
		global_command_manager->on_command(new discord_client_command_handler(message, this));
	}
};