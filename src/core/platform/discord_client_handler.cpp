#include <core/platform/discord_client_handler.h>
#include <core/platform/discord_client_command_handler.h>
#include <core/core.h>

discord_client_handler::discord_client_handler() : SleepyDiscord::DiscordClient("", SleepyDiscord::USER_CONTROLED_THREADS) {
	char* token = global_config->get_key("discord_token");
	assert(token != nullptr);

	this->setToken(token);
	this->setIntents(SleepyDiscord::Intent::SERVER_MESSAGES);
	this->run();
}

char* discord_client_handler::get_name() {
	return (char*) "discord";
}

void discord_client_handler::onMessage(SleepyDiscord::Message message) {
	if (message.startsWith("test") && !message.author.bot) {
		discord_client_command_handler handler = discord_client_command_handler(message, this);
		handler.message_send("test");
		handler.file_upload("../LICENSE");
	}
};