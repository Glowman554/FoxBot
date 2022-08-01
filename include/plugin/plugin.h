#pragma once

void load_plugin(const char* path);
void load_folder(const char* path);

#define PLUGIN() extern "C" void plugin_init()