#pragma once

#include <core/client_command_handler.h>
#include <utils/list.h>

class command {
	public:
		virtual char* get_name() = 0;
		virtual bool is_bot_owner_command() = 0;
		virtual void on_command(client_command_handler* cmd, char* args) = 0;

		virtual char* get_help(bool long_desc) = 0;
};

class command_manager {
	public:
		command_manager();
		~command_manager();

		void add_command(command* cmd);

		void on_command(client_command_handler* cmd);

		list<command*>* get_commands();
	private:
		list<command*> commands;
};