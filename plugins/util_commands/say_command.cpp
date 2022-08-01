#include <say_command.h>

char* say_command::get_name() {
	return (char*) "say";
}

bool say_command::is_bot_owner_command() {
	return false;
}

void say_command::on_command(client_command_handler* cmd, char* args) {
	if (args == nullptr) {
		cmd->message_send((char*) "No message specified.");
	} else {
		cmd->message_send(args);
	}
}

char* say_command::get_help(bool long_desc) {
	if (long_desc) {
		return (char*) "Use 'say <message>' to send a message in the chat.";
	} else {
		return (char*) "Sends a message to the chat.";
	}
}