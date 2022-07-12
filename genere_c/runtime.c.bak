#include "memoire.h"
#include <stdio.h>
#include<assert.h>
#include<string.h>
//#include<malloc.h>

#include "type_eiffel.h"
#include "programme.h"
#include "interprete.h"
#include "debug.h"
//#include "extern_fun.h"
#include "erreur.h"
#include "runtime.h"

// creer un booleen a vrai ssi n <> 0
struct TEIF_Donnee *conv_bool(int n)
{
	struct TEIF_Donnee *res=defaut_var(&type_boolean);
	if(n==0)
		res->u.val_bool=0;
	else
		res->u.val_bool=1;
	return res;
}

// retourne un objet STRING contenant s
struct TEIF_Donnee *conv_str(char *s)
{
	struct TEIF_Donnee *res=NULL;
	if(s!=NULL)
	{
		res=defaut_var(&type_string);
		res->u.val_string=strdup(s);
	}
	return res;
}

// copy
void copy(struct TEIF_Donnee *obj,struct TEIF_Donnee *obj2)
{
	assert(obj!=NULL);
	assert(obj2!=NULL);
	assert(obj->type_donnee==obj2->type_donnee);
	if(obj!=obj2)
	{
		switch(obj2->type_donnee)
		{
			case EInt:
				obj->u.val_int=obj2->u.val_int;
				break;
			case EBool:
				obj->u.val_bool=obj2->u.val_bool;
				break;
			case EChar:
				obj->u.val_char=obj2->u.val_char;
				break;
			case EDouble:
				obj->u.val_double=obj2->u.val_double;
				break;
			case EReal:
				obj->u.val_real=obj2->u.val_real;
				break;
			case EString:
				obj->u.val_string=obj2->u.val_string;
				break;
			case ENormal:
				{
					int i,diff;
					struct TEIF_Membre *tmp,*tmp2;
					for(i=0;i<NB_TABLE;i++)
					{
						tmp=(obj2->valeurs)[i];
						if(tmp==NULL)
						{
							
						}
						else
						{
							while(tmp!=NULL)
							{
								struct TEIF_Donnee *t2;
								if(tmp->donnee!=NULL&&est_expended(tmp->type))
								{
									t2=creer_var(tmp->type);
									copy(t2,tmp->donnee);
								}
								else
								{
									t2=tmp->donnee;
								}
								tmp2=donne_var(obj->valeurs,tmp->nom);
								tmp2->donnee=t2;
								tmp=tmp->suivant;
							}
						}
					}
				}
				break;
			default:
				assert(0);
				break;
		}
	}
}

// is_equal
struct TEIF_Donnee *is_equal(struct TEIF_Donnee *obj,struct TEIF_Donnee *obj2)
{
	struct TEIF_Donnee *res;
	assert(obj!=NULL);
	int r=0;
	if(obj2==NULL)
	{
		r=0;
	}
	else
	{
		if(obj->type_donnee==obj2->type_donnee)
		{
			switch(obj->type_donnee)
			{
				case EInt:
					if(obj->u.val_int==obj2->u.val_int)
						r=1;
					break;
				case EBool:
					if(obj->u.val_bool==obj2->u.val_bool)
						r=1;
					break;
				case EChar:
					if(obj->u.val_char==obj2->u.val_char)
						r=1;
					break;
				case EDouble:
					if(obj->u.val_double==obj2->u.val_double)
						r=1;
					break;
				case EReal:
					if(obj->u.val_real==obj2->u.val_real)
						r=1;
					break;
				case EString:
					if(obj->u.val_string==obj2->u.val_string)
						r=1;
					break;
				case ENormal:
					//fprintf(fichier,"Normal\n");
					if(est_expended(obj->type))
					{// objet expanded
						// comparaison si meme type
						// comparaison du contenu des objets
						int i,diff;
						struct TEIF_Membre *tmp,*tmp2;
						//fprintf(fichier,"expanded[\n");
						diff=0;
						for(i=0;i<NB_TABLE&&diff==0;i++)
						{
							tmp=(obj->valeurs)[i];
							tmp2=(obj2->valeurs)[i];
							if(tmp==NULL||tmp2==NULL)
							{
								if(tmp!=tmp2)
									diff=1;
							}
							else
							{
								while(tmp!=NULL&&tmp2!=NULL&&diff==0)
								{
									struct TEIF_Donnee *t2;
									if(tmp->donnee!=NULL)
									{
										if(tmp2->donnee==NULL)
										{
											diff=1;
										}
										else
										{
											t2=is_equal(tmp->donnee,tmp2->donnee);
											if(!t2->u.val_bool)
											{
												//fprintf(fichier,"pas meme elt\n");
												//affiche(tmp->donnee,fichier);
												//fprintf(fichier,"\n");
												//affiche(tmp2->donnee,fichier);
												//fprintf(fichier,"\n");
												diff=1;
											}
										}
									}
									else
									{
										if(tmp2->donnee!=NULL)
										{
											diff=1;
										}
									}
									tmp=tmp->suivant;
									tmp2=tmp2->suivant;
								}
								if(tmp!=NULL||tmp2!=NULL)
								{
									//fprintf(fichier,"pas meme nb elt\n");
									diff=1;
								}
							}
						}
						if(diff==0)
							r=1;
						else
							r=0;
						//fprintf(fichier,"]res=%d(%d)\n",res,diff);
						//affiche(obj,fichier);
						//fprintf(fichier,"\n");
						//affiche(obj2,fichier);
						//fprintf(fichier,"\n");
					}
					else
					{// objet non expanded
						//fprintf(fichier,"normal%d,%d\n",objet1->type->est_like,est_expended(objet1->type));
						if(!est_expended(obj2->type)
							&&obj==obj2)
						{
							r=1;
						}
						else
						{
							r=0;
						}	
					}
					//fprintf(fichier,"res=%d\n",res);
					break;
				default:
					assert(0);
					break;
			}
		}
	}
	res=conv_bool(r);
	//fprintf(fichier,"res2=%d\n",res);
	return res;
}

// equal
struct TEIF_Donnee *equal(struct TEIF_Donnee *obj,struct TEIF_Donnee *obj2)
{
	struct TEIF_Donnee *res;
	int r=0;
	if(obj==NULL)
	{
		if(obj2==NULL)
		{
			r=1;
		}
		res=defaut_var(&type_boolean);
		if(r==0)
			res->u.val_bool=0;
		else
			res->u.val_bool=1;
	}
	else
	{
		res=is_equal(obj,obj2);
	}
	return res;
}

// cree un objet copie de obj
struct TEIF_Donnee *twin(struct TEIF_Donnee *obj)
{
	struct TEIF_Donnee *res;
	assert(obj!=NULL);
	assert(obj->type!=NULL);
	res=creer_var(obj->type);
	assert(res!=NULL);
	copy(res,obj);
	return res;
}

// retourne le nom de la classe de base de obj
struct TEIF_Donnee *generator(struct TEIF_Donnee *obj)
{
	struct TEIF_Donnee *res=NULL;
	assert(obj!=NULL);
	assert(obj->type!=NULL);
	res=conv_str(obj->type->nom);
	return res;
}

// retourne le type effectif de obj
struct TEIF_Donnee *generating_type(struct TEIF_Donnee *obj)
{
	struct TEIF_Donnee *res=NULL;
	assert(obj!=NULL);
	assert(obj->type!=NULL);
	// todo:prendre en compte les parametres generiques
	res=conv_str(obj->type->nom);
	return res;
}

// retourne vrai ssi obj est conforme a obj2
struct TEIF_Donnee *conforms_to(struct TEIF_Donnee *obj,struct TEIF_Donnee *obj2)
{
	struct TEIF_Donnee *res;
	struct TEIF_Type *type1,*type2;
	int i,j;
	assert(obj!=NULL);
	res=defaut_var(&type_boolean);
	res->u.val_bool=0;
	if(obj2!=NULL)
	{
		type1=obj->type;
		assert(type1!=NULL);
		type2=obj2->type;
		assert(type2!=NULL);
		i=no_classe(type1->nom);
		assert(i!=-1);
		j=no_classe(type2->nom);
		assert(j!=-1);
		printf("%s=%s ?\n",type1->nom,type2->nom);
		printf("i=%d,j=%d,h=%d,%d\n",i,j,heritage[j][i],heritage[i][j]);
		if(i==j||heritage[j][i]!=0)
			res->u.val_bool=1;
		else
			res->u.val_bool=0;
	}
	return res;
}

void affiche_heritage()
{
	int i,j,n;
	printf("Heritage directe:\n");
	for(i=0;i<nb_classe;i++)
	{
		printf("%s herite de :",global_classe[i]->type->nom);
		n=0;
		for(j=0;j<nb_classe;j++)
		{
			if(heritage_directe[i][j]!=0)
			{
				if(n>0)
				{
					printf(",");
				}
				printf("%s",global_classe[j]->type->nom);
				n++;
			}
		}
		printf("\n");
	}
	printf("Heritage:\n");
	for(i=0;i<nb_classe;i++)
	{
		printf("%s herite de :",global_classe[i]->type->nom);
		n=0;
		for(j=0;j<nb_classe;j++)
		{
			if(heritage[i][j]!=0)
			{
				if(n>0)
				{
					printf(",");
				}
				printf("%s",global_classe[j]->type->nom);
				n++;
			}
		}
		printf("\n");
	}
}
