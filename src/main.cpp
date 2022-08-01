#include <utils/config.h>
#include <core/core.h>
#include <debug.h>
#include <plugin/plugin.h>
#include <assert.h>
#include <unistd.h>

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

	global_command_manager = new command_manager();

	load_plugin((char*) "plugins/test/libtest.so");
	load_plugin((char*) "plugins/discord/libdiscord.so");

	debugf("Startup complete!\n");

	while (true) {
		sleep(1);
	}
}