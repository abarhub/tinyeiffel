#ifndef _PROGRAMME_
#define _PROGRAMME_
#include<stdio.h>
#include "type_eiffel.h"

extern struct TEIF_Classe global_classe[22];

extern const int nb_classe;
extern const char *nom_classe_racine;
extern const char *nom_routine_racine;
extern TEIF_Bool heritage_directe[22][22];
extern TEIF_Bool heritage[22][22];
void initialisation(void);

#endif
