#include "sleepy_discord/sleepy_discord.h"

#include <utils/config.h>

class MyClientClass : public SleepyDiscord::DiscordClient {
public:
	MyClientClass(std::string token, SleepyDiscord::Mode mode) : SleepyDiscord::DiscordClient(token, mode) {}
	void onMessage(SleepyDiscord::Message message) override {
		if (message.startsWith("hello"))
			sendMessage(message.channelID, "Hello " + message.author.username);
	}
};

int main() {
	char* cfg_path = "config.cfg";

	FILE* cfg_file = fopen(cfg_path, "r");
	assert(cfg_file != NULL);

	fseek(cfg_file, 0, SEEK_END);
	int cfg_len = ftell(cfg_file);
	fseek(cfg_file, 0, SEEK_SET);

	char* cfg = (char*) malloc(cfg_len + 1);
	memset(cfg, 0, cfg_len + 1);
	fread(cfg, cfg_len, 1, cfg_file);
	fclose(cfg_file);

	config_loader loader = config_loader(cfg);


	char* discord_token = loader.get_key("discord_token");
	assert(discord_token != nullptr);
	MyClientClass client(discord_token, SleepyDiscord::USER_CONTROLED_THREADS);
	client.setIntents(SleepyDiscord::Intent::SERVER_MESSAGES);
	client.run();
}