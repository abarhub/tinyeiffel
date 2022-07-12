/* test3_1.c
 * This example program shows how to temporarily disable MSS.  Maybe it isn't
 * the best example but you should get the idea - the program allocates 100,
 * 200 and 400 bytes, but in the log you should see only the allocations of 
 * 100 and 400.
 */
#include <mss.h>
#include <stdio.h>
#include <stdlib.h>

int main(void)
{
	void * mem;
	puts("Logged allocation...");
	mem = malloc(100);
	free(mem);
#ifdef MSS
#include <no_mss.h>
#endif
	puts("Unlogged allocation...");
	mem = malloc(200);
	free(mem);
#ifdef __MSS_H_REINCLUDE
#include <mss.h>
#endif
	puts("Logged again...");
	mem = malloc(400);
	free(mem);
	return 0;
}
