#include<stdio.h>
#include<assert.h>
#include<stdlib.h>

void erreur_fatal(char *message)
{
	assert(message!=NULL);
	printf("Erreur fatal:%s\n",message);
	exit(1);
}

void lance_exception(char *message)
{
	assert(message!=NULL);
	printf("Exception:%s\n",message);
	erreur_fatal(message);
	return;
}
