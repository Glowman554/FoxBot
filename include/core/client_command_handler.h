#pragma once

#include <core/client_handler.h>

class client_command_handler {
	public:
		client_command_handler(client_handler* handler, char* command);

		virtual void message_send(char* message) = 0;

		virtual void picture_upload(char* file_path) = 0;
		virtual void audio_upload(char* file_path) = 0;
		virtual void video_upload(char* file_path) = 0;
		virtual void file_upload(char* file_path) = 0;

		virtual char* get_user_id() = 0;
		virtual char* get_user_name() = 0;
	
		client_handler* handler;
		char* command;
};