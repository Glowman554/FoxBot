#include <help_command.h>

#include <string>
#include <debug.h>

char* help_command::get_name() {
	return (char*) "help";
}

bool help_command::is_bot_owner_command() {
	return false;
}

void help_command::on_command(client_command_handler* cmd, char* args) {
	if (args == nullptr) {
		std::string message = "Available commands:\n";
		global_command_manager->get_commands()->foreach<std::string&>([](std::string& message, list<command*>::node* n) {
			message += "> ";
			message += n->data->get_name();
			message += " - ";
			message += n->data->get_help(false);
			message += "\n";
		}, message);

		cmd->message_send((char*) message.c_str());
	} else {
		debugf("%s\n", args);
		auto n = global_command_manager->get_commands()->find<char*>([](char* arg, list<command*>::node* n) {
			return strcmp(arg, n->data->get_name()) == 0;
		}, args);

		if (n == nullptr) {
			cmd->message_send((char*) "Command not found.");
		} else {
			cmd->message_send((char*) n->data->get_help(true));
		}
	}
}

char* help_command::get_help(bool long_desc) {
	if (long_desc) {
		return (char*) "You already figured it out";
	} else {
		return (char*) "Get help about a command.";
	}
}