#pragma once

#include <core/client_command_handler.h>
#include <core/client_handler.h>

#include <sleepy_discord/sleepy_discord.h>

class discord_client_command_handler : public client_command_handler {
	public:
		discord_client_command_handler(SleepyDiscord::Message message, client_handler* handler);
		~discord_client_command_handler();
		
		void message_send(char* message) override;
		
		void picture_upload(char* file_path) override;
		void audio_upload(char* file_path) override;
		void video_upload(char* file_path) override;
		void file_upload(char* file_path) override;
		
		char* get_user_id() override;
		char* get_user_name() override;
	
	private:
		SleepyDiscord::Message message;
};