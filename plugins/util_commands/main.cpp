#include <debug.h>
#include <plugin/plugin.h>
#include <core/core.h>

#include <say_command.h>
#include <help_command.h>
#include <whoami_command.h>

PLUGIN() {
	global_command_manager->add_command(new say_command());
	global_command_manager->add_command(new help_command());
	global_command_manager->add_command(new whoami_command());
}