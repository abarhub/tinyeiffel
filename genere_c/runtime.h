#ifndef _RUNTIME_H_
#define _RUNTIME_H_

void copy(struct TEIF_Donnee *obj,struct TEIF_Donnee *obj2);
struct TEIF_Donnee *is_equal(struct TEIF_Donnee *obj,struct TEIF_Donnee *obj2);
struct TEIF_Donnee *twin(struct TEIF_Donnee *obj);
struct TEIF_Donnee *generator(struct TEIF_Donnee *obj);
struct TEIF_Donnee *generating_type(struct TEIF_Donnee *obj);
struct TEIF_Donnee *conforms_to(struct TEIF_Donnee *obj,struct TEIF_Donnee *obj2);

void affiche_heritage();

#endif