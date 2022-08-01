#pragma once

class client_handler {
	public:
		virtual char* get_name() = 0;
		virtual bool is_bot_owner(char* id) = 0;
};