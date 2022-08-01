#include <utils/config.h>
#include <core/core.h>
#include <debug.h>
#include <plugin/plugin.h>
#include <assert.h>
#include <unistd.h>

int main(int argc, char** argv) {
	if (argc < 2) {
		debugf("Usage: %s <config_file>\n", argv[0]);
		return 1;
	}

	char* cfg_path = argv[1];

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

	load_folder(global_config->get_key((char*) "plugin_path"));

	debugf("Startup complete!\n");

	while (true) {
		sleep(1);
	}
}