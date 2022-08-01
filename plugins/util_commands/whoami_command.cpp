#include <whoami_command.h>

#include <string>

char* whoami_command::get_name() {
	return (char*) "whoami";
}

bool whoami_command::is_bot_owner_command() {
	return false;
}

void whoami_command::on_command(client_command_handler* cmd, char* args) {
	if (args != nullptr) {
		cmd->message_send((char*) "Command does not take any arguments.");
	} else {
		std::string message = "You are ";
		message += cmd->get_user_name();
		message += " (";
		message += cmd->get_user_id();
		message += ")";

		cmd->message_send((char*) message.c_str());
	}
}

char* whoami_command::get_help(bool long_desc) {
	if (long_desc) {
		return (char*) "Use 'whoami' to get your username / id.";
	} else {
		return (char*) "Get your username / id.";
	}
}