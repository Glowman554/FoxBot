#include <core/platform/discord_client_command_handler.h>
#include <core/platform/discord_client_handler.h>

discord_client_command_handler::discord_client_command_handler(SleepyDiscord::Message message, client_handler* handler) : client_command_handler(handler, (char*) message.content.c_str()) {
	this->message = message;
}

void discord_client_command_handler::message_send(char* message) {
	((discord_client_handler*) this->handler)->sendMessage(this->message.channelID, message);
}

void discord_client_command_handler::picture_upload(char* file_path) {
	((discord_client_handler*) this->handler)->uploadFile(this->message.channelID, file_path, "");
}

void discord_client_command_handler::audio_upload(char* file_path) {
	((discord_client_handler*) this->handler)->uploadFile(this->message.channelID, file_path, "");
}

void discord_client_command_handler::video_upload(char* file_path) {
	((discord_client_handler*) this->handler)->uploadFile(this->message.channelID, file_path, "");
}

void discord_client_command_handler::file_upload(char* file_path) {
	((discord_client_handler*) this->handler)->uploadFile(this->message.channelID, file_path, "");
}
		
char* discord_client_command_handler::get_user_id() {
	return (char*) this->message.author.ID.string().c_str();
}

char* discord_client_command_handler::get_user_name() {
	return (char*) this->message.author.username.c_str();
}