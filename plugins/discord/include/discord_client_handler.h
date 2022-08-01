#pragma once
#include <sleepy_discord/sleepy_discord.h>
#include <core/client_handler.h>

class discord_client_handler : public client_handler, public SleepyDiscord::DiscordClient {
	public:
		discord_client_handler();
		~discord_client_handler();

		char* get_name() override;
		bool is_bot_owner(char* id) override;
		void onMessage(SleepyDiscord::Message message) override;
};