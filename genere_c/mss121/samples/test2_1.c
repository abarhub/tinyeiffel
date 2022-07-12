/* test2_1.c
 * This example program illustrates how the MSS_LOG_BLOCK_LIST_FILTERED might
 * be used.  There also is a small bug in this program that mss detects.
 * (Okay, it is a bug not too hard to find, but the program will work without
 * fixing it! Can you find it?)
 */

#include <mss.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

static int strfilter(char *out, void *blk, unsigned len, const char *label, const char *file, const char *func, unsigned line)
{
	if (label != NULL)
		if (strcmp(label, "make_string") == 0)
			sprintf(out, "Block at %p, allocated in function %s contains: '%s'.\n",
	        		blk, func, (char *)blk);
	
	/* Remove warnings about unused variables... */
	file++;
	line++;
	len++;	
	return 0;
}

char *make_string(char *str, int reverse)
{
	char *p = (char *)malloc(strlen(str) + 1);
	MSS_SET_BLOCK_LABEL(p, "make_string");
	/* Omitted check for NULL */
	if (reverse)
	{
		unsigned int s_i = 0;
		unsigned int p_i = strlen(str) - 1;
		while (s_i < strlen(str))
		{
			p[p_i--] = str[s_i++];
		}
		p[strlen(str)] = '\0';
	}
	else
		strcpy(p, str);
	return p;
}

int main(void)
{
	int no_values, i;
	char **sarr;
	char *s = (char *)malloc(256 * sizeof(char));
	MSS_SET_BLOCK_LABEL(s, "main tmpstring");

	printf("Enter the number of strings you want to enter: ");
	gets(s);
	no_values = atoi(s);

	sarr = (char **)malloc(no_values * sizeof(char *));
	for (i = 0; i < no_values; i++)
	{
		printf("Enter string %i: ", i);
		gets(s);
		sarr[i] = make_string(s, i % 2);
	}

	for (i = 0; i < no_values; i++)
	 	printf("String %i: %s\n", i, sarr[i]);

	MSS_LOG_BLOCK_LIST_FILTERED(&strfilter);

	for (i = 0; i < no_values; i++)
		free(sarr[i]);
	free(sarr);
	/* s isn't freed, which will be discovered in the logfile */
	return 0;
}
