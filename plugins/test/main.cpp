#include <debug.h>
#include <plugin/plugin.h>
#include <core/core.h>

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

PLUGIN() {
	debugf("Hello from plugin!\n");
	global_command_manager->add_command(new test_command());
}