#include <debug.h>
#include <plugin/plugin.h>
#include <telegram_client_handler.h>

#include <pthread.h>

void thread_func(void* arg) {
	(new telegram_client_handler())->run();
}

PLUGIN() {
	debugf("Hello from plugin!\n");
	pthread_t thread;
	pthread_create(&thread, NULL, (void* (*)(void*)) thread_func, NULL);	
}