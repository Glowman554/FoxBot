#include <debug.h>
#include <plugin/plugin.h>
#include <discord_client_handler.h>

#include <pthread.h>

void thread_func(void* arg) {
	(new discord_client_handler())->run();
}

PLUGIN() {
	debugf("Hello from plugin!\n");
	pthread_t thread;
	pthread_create(&thread, NULL, (void* (*)(void*)) thread_func, NULL);	
}