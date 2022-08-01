#pragma once

#include <core/client_command_handler.h>
#include <core/client_handler.h>

#include <tgbot/tgbot.h>

class telegram_client_command_handler : public client_command_handler {
	public:
		telegram_client_command_handler(TgBot::Message::Ptr message, client_handler* handler);
		~telegram_client_command_handler();
		
		void message_send(char* message) override;
		
		void picture_upload(char* file_path) override;
		void audio_upload(char* file_path) override;
		void video_upload(char* file_path) override;
		void file_upload(char* file_path) override;
		
		char* get_user_id() override;
		char* get_user_name() override;
	
	private:
		TgBot::Message::Ptr message;
};