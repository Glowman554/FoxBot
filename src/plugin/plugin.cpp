#include <plugin/plugin.h>
#include <dlfcn.h>
#include <debug.h>
#include <dirent.h>
#include <string.h>
#include <utils/utils.h>
#include <stdlib.h>

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

void load_folder(const char* path) {
	DIR* dir = opendir(path);
	if (dir == nullptr) {
		debugf("Failed to open folder: %s\n", path);
		return;
	}
	
	struct dirent* entry;
	while ((entry = readdir(dir)) != nullptr) {
		if (entry->d_type == DT_REG) {
			if (strstr(entry->d_name, ".so") != nullptr) {
				char* full_path = (char*) clear_alloc(strlen(path) + strlen(entry->d_name) + 1);
				sprintf(full_path, "%s/%s", path, entry->d_name);
				debugf("Found plugin: %s\n", full_path);
				
				load_plugin(full_path);
				free(full_path);
			}
		}
	}
	
	closedir(dir);
}