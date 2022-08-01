#pragma once

#include <core/core.h>

class say_command : public command {
	public:
		char* get_name() override;
		bool is_bot_owner_command() override;
		void on_command(client_command_handler* cmd, char* args) override;

		virtual char* get_help(bool long_desc);
};