#include <plugin/plugin.h>
#include <dlfcn.h>
#include <debug.h>

void load_plugin(const char* path) {
	debugf("Loading plugin: %s\n", path);

	void* handle = dlopen(path, RTLD_LAZY);
	if (handle == nullptr) {
		debugf("Failed to load plugin: %s\n", dlerror());
		return;
	}
	
	void* func = dlsym(handle, "plugin_init");
	if (func == nullptr) {
		debugf("Failed to load plugin: %s\n", dlerror());
		return;
	}
	
	debugf("Going to call plugin_init for plugin: %s\n", path);
	auto init_func = (void (*)()) func;
	init_func();
}