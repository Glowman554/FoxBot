#pragma once
#include <stddef.h>

void* clear_alloc(size_t size);

char* copy_until(char* dst, char* src, char delim, int limit);
