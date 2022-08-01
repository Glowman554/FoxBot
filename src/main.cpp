#include "sleepy_discord/sleepy_discord.h"

#include <utils/config.h>
#include <core/core.h>
#include <core/platform/discord_client_handler.h>
#include <debug.h>

class test_command : public command {
	public:
		char* get_name() override {
			return "test";
		}

		bool is_bot_owner_command() override {
			return true;
		}

		void on_command(client_command_handler* cmd) override {
			cmd->message_send((char*) "test");
			cmd->file_upload((char*) "../LICENSE");
		}
};

int main() {
	char* cfg_path = (char*) "config.cfg";

	FILE* cfg_file = fopen(cfg_path, "r");
	assert(cfg_file != NULL);

	fseek(cfg_file, 0, SEEK_END);
	int cfg_len = ftell(cfg_file);
	fseek(cfg_file, 0, SEEK_SET);

	char* cfg = (char*) clear_alloc(cfg_len + 1);
	fread(cfg, cfg_len, 1, cfg_file);
	fclose(cfg_file);

	global_config = new config_loader(cfg);

	prefix = global_config->get_key((char*) "prefix");
	assert(prefix != nullptr);
	debugf("prefix: %s\n", prefix);

	debugf("here\n");
	global_command_manager = new command_manager();

	global_command_manager->add_command(new test_command());

	discord_client_handler* handler = new discord_client_handler();
	debugf("Startup complete!\n");
}