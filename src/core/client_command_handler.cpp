#include <core/client_command_handler.h>

#include <debug.h>

client_command_handler::client_command_handler(client_handler* handler, char* command) {
	this->handler = handler;
	this->command = command;

	debugf("message: %s\n", command);
}