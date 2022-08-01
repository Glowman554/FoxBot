#include "sleepy_discord/sleepy_discord.h"

#include <utils/config.h>
#include <core/core.h>
#include <core/platform/discord_client_handler.h>
#include <debug.h>

class MyClientClass : public SleepyDiscord::DiscordClient {
public:
	MyClientClass(std::string token, SleepyDiscord::Mode mode) : SleepyDiscord::DiscordClient(token, mode) {}
	void onMessage(SleepyDiscord::Message message) override {
		if (message.startsWith("test")) {
			sendMessage(message.channelID, "Hello " + message.author.ID.string());
			uploadFile(message.channelID, "../LICENSE", "LICENSE");
		}
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

	global_config = new config_loader(cfg);

	debugf("here\n");

	discord_client_handler* handler = new discord_client_handler();
	debugf("Startup complete!\n");
}