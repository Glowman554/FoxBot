#pragma once

void load_plugin(const char* path);


#define PLUGIN() extern "C" void plugin_init()