#include <telegram_client_command_handler.h>
#include <telegram_client_handler.h>

#include <string>

telegram_client_command_handler::telegram_client_command_handler(TgBot::Message::Ptr message, client_handler* handler) : client_command_handler(handler, (char*) message->text.c_str()) {
	this->message = message;
}

telegram_client_command_handler::~telegram_client_command_handler() {}

void telegram_client_command_handler::message_send(char* message) {
	((telegram_client_handler*) this->handler)->bot.getApi().sendMessage(this->message->chat->id, message);
}

void telegram_client_command_handler::picture_upload(char* file_path) {
	((telegram_client_handler*) this->handler)->bot.getApi().sendPhoto(this->message->chat->id, TgBot::InputFile::fromFile(file_path, ""));
}

void telegram_client_command_handler::audio_upload(char* file_path) {
	((telegram_client_handler*) this->handler)->bot.getApi().sendAudio(this->message->chat->id, TgBot::InputFile::fromFile(file_path, ""));
}

void telegram_client_command_handler::video_upload(char* file_path) {
	((telegram_client_handler*) this->handler)->bot.getApi().sendVideo(this->message->chat->id, TgBot::InputFile::fromFile(file_path, ""));
}

void telegram_client_command_handler::file_upload(char* file_path) {
	((telegram_client_handler*) this->handler)->bot.getApi().sendDocument(this->message->chat->id, TgBot::InputFile::fromFile(file_path, ""));
}

char tg_id_buffer[64] = { 0 };
char* telegram_client_command_handler::get_user_id() {
	memset(tg_id_buffer, 0, 64);
	sprintf(tg_id_buffer, "%lu", this->message->from->id);
	return tg_id_buffer;
}

char* telegram_client_command_handler::get_user_name() {
	return (char*) this->message->from->username.c_str();
}