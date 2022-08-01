#include <utils/utils.h>

#include <stdlib.h>
#include <string.h>

void* clear_alloc(size_t size) {
	void* ptr = malloc(size);
	memset(ptr, 0, size);
	return ptr;
}
