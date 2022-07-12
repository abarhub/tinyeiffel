#ifndef _INTERPRETE_
#define _INTERPRETE_

#include "type_eiffel.h"
#include "programme.h"

#define NB_TABLE (10)

struct TEIF_Donnee;

struct TEIF_Membre
{
	char *nom;
	struct TEIF_Donnee *donnee;
	struct TEIF_Type *type;
	struct TEIF_Membre *suivant;
};

enum ETypeDonne {ENormal,EInt,EDouble,EChar,EString,ETab,EBool,EReal};

struct TEIF_Donnee
{
	enum ETypeDonne type_donnee;
	struct TEIF_Type *type;
	TEIF_Bool type_special,expanded;
	int no_objet;
	union tu {
		int val_int;
		int val_bool;
		char val_char;
		double val_double;
		float val_real;
		char *val_string;
		void *val_tab;
	} u;
	struct TEIF_Membre **(valeurs)/* *(*x)[] */;
	struct TEIF_Donnee *(*fun_extern)(struct TEIF_NomFeature *nom,struct TEIF_Donnee **(param) /* *(*x)[] */);
}; 

struct TEIF_Couple
{
	struct TEIF_Donnee *donnee;
	struct TEIF_Type *type;
};

struct TEIF_Frame
{
	char *nom_classe;
	struct TEIF_NomFeature *nom_fonction;
	struct TEIF_Feature *f;
	struct TEIF_Membre **(local) /* *(*x)[] */, **(param) /* *(*x)[] */;
	struct TEIF_Donnee *objet_courant;
	struct TEIF_Frame *precedant;
};

void run(void);

struct TEIF_Classe *cherche_classe(char *nom);
struct TEIF_Donnee *defaut_var(struct TEIF_Type *type);
struct TEIF_Couple *evalue(struct TEIF_Donnee *objet_courant,
					struct TEIF_Expr *expr);
void ajoute_var(struct TEIF_Membre *tab[],struct TEIF_Donnee *d,
		char *nom,struct TEIF_Type *type);
char *convertie_chaine(struct TEIF_Chaine *str);
struct TEIF_NomFeature *trouve_nom_feature(struct TEIF_Type *type1,
				struct TEIF_Type *type2,struct TEIF_NomFeature *nom);
struct TEIF_Feature *cherche_feature(struct TEIF_Classe *classe,
						struct TEIF_NomFeature *nom);

TEIF_Bool meme_nom_feature(struct TEIF_NomFeature *nom,
					struct TEIF_NomFeature *nom2);
struct TEIF_Type *get_var_type(struct TEIF_Frame *frame,char *nom);
int get_denier_no();
struct TEIF_Membre *donne_var(struct TEIF_Membre **(tab)/* *(*x)[] */,char *nom);
struct TEIF_Donnee *appel_routine(struct TEIF_Donnee *objet,
		struct TEIF_Feature *f,struct TEIF_NomFeature *nf,
		struct TEIF_Expr *(*param)[],int taille);
struct TEIF_Donnee *creer_var(struct TEIF_Type *type);
TEIF_Bool est_expended(struct TEIF_Type *type);
int no_classe(char *nom);

extern struct TEIF_Type type_integer,type_boolean,type_any,
		type_character,type_double,type_real,type_string;
extern struct TEIF_NomFeature liste_nom_feature[];

#endif
