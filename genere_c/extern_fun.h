#ifndef _EXTERN_FUN_
#define _EXTERN_FUN_

struct TEIF_Donnee *fun_extern_int(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param);
struct TEIF_Donnee *fun_extern_bool(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param);
struct TEIF_Donnee *fun_extern_char(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param);
struct TEIF_Donnee *fun_extern_double(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param);
struct TEIF_Donnee *fun_extern_real(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param);
struct TEIF_Donnee *fun_extern_general(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param);


#endif