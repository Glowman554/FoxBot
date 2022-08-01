#include <utils/utils.h>

#include <stdlib.h>
#include <string.h>

void* clear_alloc(size_t size) {
	void* ptr = malloc(size);
	memset(ptr, 0, size);
	return ptr;
}

char* copy_until(char* dst, char* src, char delim) {
	while (*src != delim && *src != '\0') {
		*dst = *src;
		dst++;
		src++;
	}
	*dst = '\0';
	return src + 1;
}