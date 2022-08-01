#include "sleepy_discord/sleepy_discord.h"

class MyClientClass : public SleepyDiscord::DiscordClient {
public:
	MyClientClass(std::string token, SleepyDiscord::Mode mode) : SleepyDiscord::DiscordClient(token, mode) {}
	void onMessage(SleepyDiscord::Message message) override {
		if (message.startsWith("hello"))
			sendMessage(message.channelID, "Hello " + message.author.username);
	}
};

int main() {
	MyClientClass client("MTAwMzY0NzcyOTM5MTYzMjUwNA.GAZYQD.y3gIH8zEDULsrWR_saqZF61bu7HspGBTLpOBps", SleepyDiscord::USER_CONTROLED_THREADS);
	client.setIntents(SleepyDiscord::Intent::SERVER_MESSAGES);
	client.run();
}