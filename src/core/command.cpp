#include <core/command.h>
#include <core/core.h>
#include <debug.h>

command_manager::command_manager() : commands(10) {
}

command_manager::~command_manager() {}

void command_manager::add_command(command* cmd) {
	debugf("Adding command %s\n", cmd->get_name());
	commands.add(cmd);
}

void command_manager::on_command(client_command_handler* cmd) {
	char cmd_prefix[0xff] = { 0 };
	char* new_cmd = copy_until(cmd_prefix, cmd->command, ' ');

	if (strcmp(cmd_prefix, prefix) == 0) {
		if (new_cmd[-1] == 0) {
			cmd->message_send((char*) "No command specified.");
		} else {
			char cmd_to_exec[0xff] = { 0 };
			char* args = copy_until(cmd_to_exec, new_cmd, ' ');			
			debugf("searching for command %s\n", cmd_to_exec);

			auto command_executer = commands.find<char*>([](char* name, list<command*>::node* node) {
				return strcmp(name, node->data->get_name()) == 0;
			}, cmd_to_exec);

			if (command_executer == nullptr) {
				char response[0xff * 2] = { 0 };
				snprintf(response, 0xff * 2, "Command %s not found\n", cmd_to_exec);
				cmd->message_send(response);
			} else {
				if (command_executer->data->is_bot_owner_command()) {
					debugf("Command %s is bot owner command\n", cmd_to_exec);

					if (cmd->handler->is_bot_owner(cmd->get_user_id())) {
						command_executer->data->on_command(cmd, args[-1] == 0 ? nullptr : args);
					} else {
						char response[0xff] = { 0 };
						sprintf(response, "You are not bot owner\n");
						cmd->message_send(response);
					}
				} else {
					command_executer->data->on_command(cmd, args[-1] == 0 ? nullptr : args);
				}
			}
		}
	}
}

list<command*>* command_manager::get_commands() {
	return &commands;
}