#include "memoire.h"
#include <stdio.h>
#include<assert.h>
#include<string.h>
//#include<malloc.h>

#include "type_eiffel.h"
#include "programme.h"
#include "interprete.h"
#include "debug.h"
#include "extern_fun.h"
#include "erreur.h"
#include "runtime.h"

struct TEIF_Type type_integer={"integer",0,0,0,NULL,0,NULL},
			type_boolean={"boolean",0,0,0,NULL,0,NULL},
			type_any={"any",0,0,0,NULL,0,NULL},
			type_character={"character",0,0,0,NULL,0,NULL},
			type_double={"double",0,0,0,NULL,0,NULL},
			type_real={"real",0,0,0,NULL,0,NULL},
			type_string={"string",0,0,0,NULL,0,NULL};

/*struct TEIF_Classe *cherche_classe(char *nom);
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
struct TEIF_Membre *donne_var(struct TEIF_Membre **(tab)/* *(*x)[] * /,char *nom);
struct TEIF_Donnee *appel_routine(struct TEIF_Donnee *objet,
		struct TEIF_Feature *f,struct TEIF_NomFeature *nf,
		struct TEIF_Expr *(*param)[],int taille);
struct TEIF_Donnee *creer_var(struct TEIF_Type *type);*/

static FILE *fichier=NULL;

static char *liste_char0[]={"+"};
static char *liste_char1[]={"-"};
static char *liste_char2[]={"*"};
static char *liste_char3[]={"/"};
static char *liste_char4[]={"//"};
static char *liste_char5[]={"\\"};
static char *liste_char6[]={"old"};
static char *liste_char7[]={"not"};
static char *liste_char8[]={"+"};
static char *liste_char9[]={"-"};
static char *liste_char10[]={"."};
static char *liste_char11[]={"^"};
static char *liste_char12[]={"xor"};
static char *liste_char13[]={"or"};
static char *liste_char14[]={"and"};
static char *liste_char15[]={"and then"};
static char *liste_char16[]={"or else"};
static char *liste_char17[]={"implies"};
static char *liste_char18[]={"="};
static char *liste_char19[]={"/="};
static char *liste_char20[]={"<"};
static char *liste_char21[]={"<="};
static char *liste_char22[]={">="};
static char *liste_char23[]={">"};
static char *liste_char24[]={" "};
static char *liste_char25[]={" "};
static char *liste_char26[]={"$"};

static struct TEIF_Chaine liste_nom_op[]=
{
{&(liste_char0),1},
{&(liste_char1),1},
{&(liste_char2),1},
{&(liste_char3),1},
{&(liste_char4),1},
{&(liste_char5),1},
{&(liste_char6),1},
{&(liste_char7),1},
{&(liste_char8),1},
{&(liste_char9),1},
{&(liste_char10),1},
{&(liste_char11),1},
{&(liste_char12),1},
{&(liste_char13),1},
{&(liste_char14),1},
{&(liste_char15),1},
{&(liste_char16),1},
{&(liste_char17),1},
{&(liste_char18),1},
{&(liste_char19),1},
{&(liste_char20),1},
{&(liste_char21),1},
{&(liste_char22),1},
{&(liste_char23),1},
{&(liste_char24),1},
{&(liste_char25),1},
{&(liste_char26),1}
};

struct TEIF_NomFeature liste_nom_feature[]=
{
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[0])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[1])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[2])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[3])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[4])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[5])},
{TEIF_Nom_Operateur,1,0,0,NULL,&(liste_nom_op[6])},
{TEIF_Nom_Operateur,1,0,0,NULL,&(liste_nom_op[7])},
{TEIF_Nom_Operateur,1,0,0,NULL,&(liste_nom_op[8])},
{TEIF_Nom_Operateur,1,0,0,NULL,&(liste_nom_op[9])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[10])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[11])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[12])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[13])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[14])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[15])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[16])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[17])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[18])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[19])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[20])}, // operateur binaire <
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[21])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[22])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[23])},
{TEIF_Nom_Operateur,0,1,0,NULL,&(liste_nom_op[24])},
{TEIF_Nom_Operateur,1,0,0,NULL,&(liste_nom_op[25])},
{TEIF_Nom_Operateur,1,0,0,NULL,&(liste_nom_op[26])}, // no 26
{TEIF_Nom_Normal,0,0,0,"code",NULL}, // code
{TEIF_Nom_Normal,0,0,0,"hash_code",NULL}, // hash_code
{TEIF_Nom_Normal,0,0,0,"copy",NULL},
{TEIF_Nom_Normal,0,0,0,"standard_copy",NULL}, // no 30
{TEIF_Nom_Normal,0,0,0,"twin",NULL},
{TEIF_Nom_Normal,0,0,0,"standard_twin",NULL},
{TEIF_Nom_Normal,0,0,0,"is_equal",NULL},
{TEIF_Nom_Normal,0,0,0,"standard_is_equal",NULL},
{TEIF_Nom_Normal,0,0,0,"generator",NULL},
{TEIF_Nom_Normal,0,0,0,"generating_type",NULL},
{TEIF_Nom_Normal,0,0,0,"conforms_to",NULL}     //  no 37
};


/*struct TEIF_Donnee *fun_extern_int2(struct TEIF_NomFeature *nom,struct TEIF_Donnee *(*param)[])
{
	
	assert(nom!=NULL);

	assert(0);
	return NULL;
}*/

char *donne_token(struct TEIF_Token *t)
{// todo: enlever l'allocation de memoire
	if(t==NULL)
	{
		return strdup("(null)");
	}
	else
	{
		char buf[512],buf2[200],c;
		int i;
		/*sprintf(buf,"(%d,%d,'",t->x,t->y);
		buf2[0]='\0';
		strcpy(buf2,t->text);
		for(i=0;i<(int)strlen(buf2);i++)
		{
			c=buf2[i];
			if(c=='\\')
				buf2[i]='/';
		}
		strcat(buf,buf2);
		strcat(buf,"'!,?'");
		buf2[0]='\0';
		strcpy(buf2,t->file);
		for(i=0;i<(int)strlen(buf2);i++)
		{
			c=buf2[i];
			if(c=='\\')
				buf2[i]='/';
		}
		strcat(buf,buf2);
		strcat(buf,"')");
		//printf("init:%s,res:%s!\n",t->file,buf2);
		return strdup(buf);*/
		sprintf(buf,"(%d,%d,'%s','%s')",t->x,t->y,t->text,t->file);
		return strdup(buf);
	}
}


struct TEIF_Donnee *convDouble(struct TEIF_Donnee *v)
{
	struct TEIF_Donnee *res;
	double d;
	assert(v!=NULL);
	assert(v->type_donnee==EReal||
		v->type_donnee==EInt);
	res=defaut_var(&type_double);
	switch(v->type_donnee)
	{
		case EReal:
			{
				float f;
				f=v->u.val_real;
				d=f;
				res->u.val_double=d;
			}
			break;
		case EInt:
			{
				int i;
				i=v->u.val_int;
				d=i;
				res->u.val_double=d;
			}
			break;
		default:
			assert(0);
	}
	return res;
}

struct TEIF_Donnee *convReal(struct TEIF_Donnee *v)
{
	struct TEIF_Donnee *res;
	float f;
	assert(v!=NULL);
	assert(v->type_donnee==EDouble||
		v->type_donnee==EInt);
	res=defaut_var(&type_real);
	switch(v->type_donnee)
	{
		case EDouble:
			{
				double d;
				d=v->u.val_double;
				f=d;
				res->u.val_real=f;
			}
			break;
		case EInt:
			{
				int i;
				i=v->u.val_int;
				f=i;
				res->u.val_real=f;
			}
			break;
		default:
			assert(0);
	}
	return res;
}

struct TEIF_Donnee *convInt(struct TEIF_Donnee *v)
{
	struct TEIF_Donnee *res;
	int i;
	assert(v!=NULL);
	assert(v->type_donnee==EDouble||
		v->type_donnee==EReal);
	res=defaut_var(&type_integer);
	switch(v->type_donnee)
	{
		case EDouble:
			{
				double d;
				d=v->u.val_double;
				i=d;
				res->u.val_real=i;
			}
			break;
		case EReal:
			{
				float f;
				f=v->u.val_real;
				i=f;
				res->u.val_int=i;
			}
			break;
		default:
			assert(0);
	}
	return res;
}

void make_compatible(struct TEIF_Donnee **tab)
{
	struct TEIF_Donnee *d1,*d2;
	struct TEIF_Donnee *tmp1,*tmp2;
	d1=tab[0];
	d2=tab[1];
	if(d2->type_donnee==EDouble)
	{
		if(d1->type_donnee==EReal||
			d1->type_donnee==EInt)
		{
			d1=convDouble(d1);
		}
	}
	else if(d2->type_donnee==EReal)
	{
		if(d1->type_donnee==EInt)
		{
			d1=convReal(d1);
		}
		else if(d1->type_donnee==EDouble)
		{
			d2=convDouble(d2);
		}
	}
	else if(d2->type_donnee==EInt)
	{
		if(d1->type_donnee==EReal)
		{
			d2=convReal(d2);
		}
		else if(d1->type_donnee==EDouble)
		{
			d2=convDouble(d2);
		}
	}
	tab[0]=d1;
	tab[1]=d2;
}


//#include "extern_fun.c"

TEIF_Bool est_expended(struct TEIF_Type *type)
{
	struct TEIF_Classe *cl;
	assert(type!=NULL);
	if(type->expanded)
		return 1;
	cl=cherche_classe(type->nom);
	if(cl!=NULL)
	{
		if(cl->type->expanded||cl->expanded)
			return 1;
		else
			return 0;
	}
	return 0;
}

int no_classe(char *nom)
{
	int i;
	struct TEIF_Classe *cl;
	assert(nom!=NULL);
	for(i=0;i<nb_classe;i++)
	{
		cl=&(global_classe[i]);
		if(stricmp(cl->type->nom,nom)==0)
			return i;
	}
	return -1;
}

typedef struct TEIF_Donnee *(TEIF_Tab_Donnee)[];

struct TEIF_Frame *derniere_frame;

struct TEIF_Membre *donne_var_membre(struct TEIF_Frame *frame,char *nom)
{
	//struct TEIF_Membre **(tab)/* *(*x)[] */;
	//int i;
	struct TEIF_Membre *tmp;
	assert(frame!=NULL);
	assert(nom!=NULL);
	/*for(i=0;i<NB_TABLE;i++)
	{
		tmp=(tab)[i];
		while(tmp!=NULL)
		{
			if(stricmp(tmp->nom,nom)==0)
				return tmp;
			tmp=tmp->suivant;
		}
	}*/
	if(frame->param!=NULL)
	{
		tmp=donne_var(frame->param,nom);
		if(tmp!=NULL)
		{
			return tmp;
		}
	}
	if(frame->local!=NULL)
	{
		tmp=donne_var(frame->local,nom);
		if(tmp!=NULL)
		{
			return tmp;
		}
	}
	if(frame->objet_courant!=NULL)
	{
		struct TEIF_Donnee *objet;
		objet=frame->objet_courant;
		tmp=donne_var(objet->valeurs,nom);
		if(tmp!=NULL)
		{
			return tmp;
		}
	}
	return NULL;
}

/*struct TEIF_Membre *donne_var_membre2(struct TEIF_Frame *frame,
			struct TEIF_Donnee *objet_courant,char *nom)
{
	//struct TEIF_Membre **(tab)/* *(*x)[] * /;
	//int i;
	struct TEIF_Membre *tmp;
	assert(frame!=NULL);
	assert(objet!=NULL);
	assert(nom!=NULL);
	/*for(i=0;i<NB_TABLE;i++)
	{
		tmp=(tab)[i];
		while(tmp!=NULL)
		{
			if(stricmp(tmp->nom,nom)==0)
				return tmp;
			tmp=tmp->suivant;
		}
	}* /
	if(frame->param!=NULL)
	{
		tmp=donne_var(frame->param,nom);
		if(tmp!=NULL)
		{
			return tmp;
		}
	}
	if(frame->local!=NULL)
	{
		tmp=donne_var(frame->local,nom);
		if(tmp!=NULL)
		{
			return tmp;
		}
	}
	if(frame->objet_courant!=NULL)
	{
		struct TEIF_Donnee *objet;
		objet=frame->objet_courant;
		tmp=donne_var(objet->valeurs,nom);
		if(tmp!=NULL)
		{
			return tmp;
		}
	}
	return NULL;
}*/


struct TEIF_Membre *donne_var(struct TEIF_Membre **(tab)/* *(*x)[] */,char *nom)
{
	int i;
	struct TEIF_Membre *tmp;
	assert(tab!=NULL);
	assert(nom!=NULL);
	for(i=0;i<NB_TABLE;i++)
	{
		tmp=(tab)[i];
		while(tmp!=NULL)
		{
			if(stricmp(tmp->nom,nom)==0)
				return tmp;
			tmp=tmp->suivant;
		}
	}
	return NULL;
}

void set_var(struct TEIF_Frame *frame,char *nom,struct TEIF_Donnee *d)
{
	struct TEIF_Membre *tmp;
	assert(frame!=NULL);
	assert(nom!=NULL);
	tmp=donne_var_membre(frame,nom);
	if(tmp!=NULL)
	{
		tmp->donnee=d;
		return;
	}
	/*if(frame->param!=NULL)
	{
		tmp=donne_var(frame->param,nom);
		if(tmp!=NULL)
		{
			tmp->donnee=d;
			return;
		}
	}
	if(frame->local!=NULL)
	{
		tmp=donne_var(frame->local,nom);
		if(tmp!=NULL)
		{
			tmp->donnee=d;
			return;
		}
	}*/
	assert(0);
}

struct TEIF_Donnee *get_var(struct TEIF_Frame *frame,char *nom)
{
	struct TEIF_Membre *tmp;
	struct TEIF_Donnee *d;
	assert(frame!=NULL);
	assert(nom!=NULL);
	tmp=donne_var_membre(frame,nom);
	if(tmp!=NULL)
	{
		return tmp->donnee;
	}
	if(stricmp(nom,"current")==0)
	{
		return frame->objet_courant;
	}
	/*if(frame->param!=NULL)
	{
		tmp=donne_var(frame->param,nom);
		if(tmp!=NULL)
		{
			return tmp->donnee;
		}
	}
	if(frame->local!=NULL)
	{
		tmp=donne_var(frame->local,nom);
		if(tmp!=NULL)
		{
			return tmp->donnee;
		}
	}*/
	printf("var inconnue:%s!\n",nom);
	assert(0);
	return NULL;
}

struct TEIF_Type *get_var_type(struct TEIF_Frame *frame,char *nom)
{
	struct TEIF_Membre *tmp;
	struct TEIF_Donnee *d;
	assert(frame!=NULL);
	assert(nom!=NULL);
	tmp=donne_var_membre(frame,nom);
	if(tmp!=NULL)
	{
		//tmp->donnee=d;
		return tmp->type;
	}
	if(stricmp(nom,"current")==0)
	{
		return frame->objet_courant->type;
	}
	/*if(frame->param!=NULL)
	{
		tmp=donne_var(frame->param,nom);
		if(tmp!=NULL)
		{
			return tmp->type;
		}
	}
	if(frame->local!=NULL)
	{
		tmp=donne_var(frame->local,nom);
		if(tmp!=NULL)
		{
			return tmp->type;
		}
	}*/
	printf("n=%s!\n",nom);
	affiche_stack(frame);
	assert(0);
	return NULL;
}

char *donne_chaine(struct TEIF_Chaine *t)
{
	if(t==NULL)
	{
		return NULL;
	}
	else
	{
		char buf[500],tmp[2]="\"",*p;
		int i,j;
		buf[0]='\0';
		strcat(buf,tmp);
		for(i=0;i<t->taille_liste;i++)
		{
			strncat(buf,(*t->liste)[i],sizeof(buf)-2);
		}
		strcat(buf,tmp);
		// todo : effacer ce malloc
		p=(char*)malloc(strlen(buf)+1);
		memset(p,0,strlen(buf)+1);
		p[0]='\0';
		strcat(p,buf);
		return p;
	}
}

char *donne_type(struct TEIF_Type *t)
{
	if(t==NULL)
	{
		return NULL;
	}
	else
	{
		return t->nom;
	}
}

char *donne_nfeature(struct TEIF_NomFeature *t)
{
	if(t==NULL)
	{
		return NULL;
	}
	else
	{
		if(t->prefix||t->infix)
		{
			return donne_chaine(t->nom2);
		}
		else
			return t->nom;
	}
}

void eval_instr(struct TEIF_Frame *frame,
		struct TEIF_Instr *(*liste_instr)[],int taille_instr)
{
	struct TEIF_Donnee *objet;
	assert(frame!=NULL);
	assert(taille_instr>=0);
	objet=frame->objet_courant;
	if(taille_instr>0)
	{
		struct TEIF_Instr *instr;
		struct TEIF_Couple *c;
		struct TEIF_Donnee *d;
		int i;
		for(i=0;i<taille_instr;i++)
		{
			instr=(*liste_instr)[i];
			if(instr->type==TEIF_Affect)
			{
				struct TEIF_Type *t1,t2;
				/*if(i>=21)
				{
					printf("%d)",i);
				}*/
				c=evalue(objet,instr->expr);
				/*if(i>=21)
				{
					printf("Eval Ok",i);
				}*/
				d=c->donnee;
				/*if(d1->type_donnee==EInt)
				{
					d1=convReal(d1);
				}*/
				/*{
				struct TEIF_Donnee *tmp1,*tmp2,*tab[2];
				tab[0]=d1;
				tab[1]=d2;
				make_compatible(tab);
				d1=tab[0];
				d2=tab[1];
				}*/
				t1=get_var_type(frame,instr->nom);
				if(t1!=NULL&&stricmp(t1->nom,"double")==0)
				{
					if(d!=NULL&&(d->type_donnee==EReal||
						d->type_donnee==EInt))
					{
						d=convDouble(d);
					}
				}
				else if(t1!=NULL&&stricmp(t1->nom,"real")==0)
				{
					if(d!=NULL&&d->type_donnee==EInt)
					{
						d=convReal(d);
					}
				}
				assert(c!=NULL);
				set_var(frame,instr->nom,d);
				fprintf(fichier,"instr : %s:=",instr->nom);
				affiche(d,fichier);
				fprintf(fichier,"( loc=%s)",donne_token(instr->token1));
				fprintf(fichier,"\n");
				fflush(fichier);
			}
			else if(instr->type==TEIF_Appel)
			{
				struct TEIF_Feature *f;
				struct TEIF_Donnee *d;
				struct TEIF_NomFeature nf={TEIF_Nom_Normal,0,0,0,NULL,NULL};
				struct TEIF_Classe *classe;
				struct TEIF_Donnee *objet_courant;
				struct TEIF_Instr *ins;
				struct TEIF_Membre *membre;
				//sscanf(expr->str,"%d",&i);
				assert(objet!=NULL);
				objet_courant=objet;
				ins=instr;
				while(ins!=NULL)
				{
					assert(objet_courant!=NULL);
					assert(ins->nom!=NULL);
					assert(instr->type==TEIF_Appel);
					if(stricmp(ins->nom,"current")==0)
					{
						d=derniere_frame->objet_courant;
					}
					else
					{
						membre=donne_var_membre(frame,ins->nom);
						if(membre!=NULL)
						{// c'est une variable
							assert(instr->taille_parametre==0);
							d=membre->donnee;
							//assert(d!=NULL);
						}
						else
						{// c'est un appel de routine
							nf.nom=ins->nom;
							//printf("nom=%s:\n",nf.nom);
							classe=cherche_classe(objet_courant->type->nom);
							assert(classe!=NULL);
							f=cherche_feature(classe,&nf);
							assert(f!=NULL);
							//printf("app:(%p)\n",expr->parametre);
							if(ins->parametre==NULL)
								assert(instr->taille_parametre==0);
							fprintf(fichier,"instr :%d.%s",objet->no_objet,ins->nom);
							//affiche(d,fichier);
							fprintf(fichier,"( loc=%s)",donne_token(ins->token1));
							fprintf(fichier,"\n");
							fflush(fichier);
							d=appel_routine(objet_courant,f,&nf,
								ins->parametre,ins->taille_parametre);
						}
					}
					objet_courant=d;
					//fprintf(fichier,"fin appel(%p)\n",d);
					fflush(fichier);
					/*assert(expr->str2!=NULL);
					p2=convertie_chaine(expr->str2);
					assert(p2!=NULL);
					p=(char*)malloc(strlen(p2)+1);
					memset(p,0,strlen(p2)+1);
					strcpy(p,p2);
					d=defaut_var(&type_string);
					d->u.val_string=p;*/
					/*res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
					memset(res,0,sizeof(struct TEIF_Couple));
					res->donnee=d;
					res->type=f->type_retour;*/
					ins=ins->suivant;
					if(ins!=NULL)
					{
						//assert(objet_courant!=NULL);
						if(objet_courant==NULL)
						{
							char buf[512];
							sprintf(buf,"Appel sur objet Void a la position %d,%d,%s",ins->token1->x,ins->token1->y,ins->token1->file);
							lance_exception(buf);
						}
					}
				}
				assert(d==NULL);
				assert(f->type_retour==NULL);
			}
			else if(instr->type==TEIF_Creation)
			{
				struct TEIF_Feature *f;
				struct TEIF_Donnee *d;
				struct TEIF_NomFeature nf={TEIF_Nom_Normal,0,0,0,NULL,NULL};
				struct TEIF_Classe *classe;
				struct TEIF_Donnee *objet_courant;
				struct TEIF_Type *type_creation;
				char *var,*nom_routine=NULL;
				//sscanf(expr->str,"%d",&i);
				//assert(0);
				var=instr->nom;
				assert(var!=NULL);
				if(instr->nom2!=NULL)
				{
					nom_routine=instr->nom2;
				}
				if(instr->type_creation!=NULL)
				{
					type_creation=instr->type_creation;
				}
				else
				{
					type_creation=get_var_type(frame,var);
					//printf("type=%s!\n",type_creation);
				}
				assert(type_creation!=NULL);
				d=creer_var(type_creation);
				assert(d!=NULL);
				set_var(frame,var,d);
				objet_courant=d;
				assert(objet_courant!=NULL);
				fprintf(fichier,"instr : !%s!%s",type_creation->nom,var);
				if(nom_routine!=NULL)
				{
					fprintf(fichier,".%s",nom_routine);
				}
				//affiche(d,fichier);
				fprintf(fichier,"(obj=%d, loc=%s)",objet_courant->no_objet,donne_token(instr->token1));
				fprintf(fichier,"\n");
				fflush(fichier);
				if(nom_routine!=NULL)
				{// c'est un appel
					nf.nom=nom_routine;
					//printf("nom=%s:\n",nf.nom);
					classe=cherche_classe(objet_courant->type->nom);
					assert(classe!=NULL);
					f=cherche_feature(classe,&nf);
					assert(f!=NULL);
					//printf("app:(%p)\n",expr->parametre);
					if(instr->parametre==NULL)
						assert(instr->taille_parametre==0);
					d=appel_routine(objet_courant,f,&nf,
						instr->parametre,instr->taille_parametre);
					//printf("fin appel\n");
					/*assert(expr->str2!=NULL);
					p2=convertie_chaine(expr->str2);
					assert(p2!=NULL);
					p=(char*)malloc(strlen(p2)+1);
					memset(p,0,strlen(p2)+1);
					strcpy(p,p2);
					d=defaut_var(&type_string);
					d->u.val_string=p;*/
					/*res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
					memset(res,0,sizeof(struct TEIF_Couple));
					res->donnee=d;
					res->type=f->type_retour;*/
					assert(d==NULL);
					assert(f->type_retour==NULL);
				}
			}
			else if(instr->type==TEIF_If)
			{
				struct TEIF_Type *t1,t2;
				struct TEIF_Instr *ins;
				int fin=0;
				ins=instr;
				assert(ins!=NULL);
				while(fin==0&&ins!=NULL)
				{
					switch(ins->type)
					{
						case TEIF_If:
						case TEIF_ElseIf:
							if(ins->type==TEIF_If)
							{
								fprintf(fichier,"instr : if ");
							}
							else
							{
								fprintf(fichier,"instr : elseif ");
							}
							fprintf(fichier,"( loc=%s)",donne_token(ins->token1));
							fprintf(fichier,"\n");
							fflush(fichier);
							c=evalue(objet,ins->expr);
							d=c->donnee;
							assert(d!=NULL);
							assert(d->type_donnee==EBool);
							//fprintf(fichier,"val=%d\n",d->u.val_bool);
							if(d->u.val_bool!=0)
							{
								eval_instr(frame,ins->liste_instr,ins->taille_liste_instr);
								fin=1;
							}
							break;
						case TEIF_Else:
							fprintf(fichier,"instr : else ");
							fprintf(fichier,"( loc=%s)",donne_token(ins->token1));
							fprintf(fichier,"\n");
							fflush(fichier);
							eval_instr(frame,ins->liste_instr,ins->taille_liste_instr);
							assert(ins->suivant==NULL);
							break;
						default:
							assert(0);
							break;
					}
					ins=ins->suivant;
				}
			}
			else if(instr->type==TEIF_Loop)
			{
				struct TEIF_Type *t1,t2;
				//int fin=0;
				fprintf(fichier,"instr : from ");
				fprintf(fichier,"( loc=%s)",donne_token(instr->token1));
				fprintf(fichier,"\n");
				fflush(fichier);
				eval_instr(frame,instr->from,instr->taille_from);
				//fprintf(fichier,"fin instr from\n");
				c=evalue(objet,instr->expr);
				//fprintf(fichier,"fin expr1\n");
				d=c->donnee;
				assert(d!=NULL);
				assert(d->type_donnee==EBool);
				//fprintf(fichier,"debut boucle\n");
				while(d->u.val_bool==0)
				{
					//fprintf(fichier,"loop 1\n");
					eval_instr(frame,instr->loop,instr->taille_loop);
					//fprintf(fichier,"loop 2\n");
					c=evalue(objet,instr->expr);
					d=c->donnee;
					assert(d!=NULL);
					assert(d->type_donnee==EBool);
				}
			}
			else
			{
				printf("Instruction inconnue\n");
				assert(0);
			}
		}
	}
}

struct TEIF_Donnee *lance_routine(struct TEIF_Donnee *objet,
		struct TEIF_Feature *f,struct TEIF_NomFeature *nf,
		struct TEIF_Donnee *(*param)[],int taille)
{
	struct TEIF_Donnee *resultat=NULL;
	int i;
	struct TEIF_Membre *param2[NB_TABLE];
	struct TEIF_Frame *frame;
	fprintf(fichier,"prologue(%d:%s.%s)\n",objet->no_objet,donne_type(objet->type),donne_nfeature(nf));
	assert(objet!=NULL);
	assert(f!=NULL);
	assert(nf!=NULL);
	//printf("type=%d\n",f->type);
	assert(f->type==TEIF_FeatureExternal||
			f->type==TEIF_FeatureRoutine);
	if(nf->prefix==0)
	{// todo: a remettre pb si c'est prefix
		assert(f->taille_param==taille);
	}
	assert(taille>=0);
	//printf("Lance:\n");
	memset(&param2,0,sizeof(param2));
	if(taille==0)
	{
		memset(&param2,0,sizeof(param2));
	}
	else
	{
		struct TEIF_DeclareVar *dv;
		assert(f->param!=NULL);
		for(i=0;i<taille;i++)
		{
			dv=(*f->param)[i];
			//printf("Debut3\n");
			ajoute_var(param2,(*param)[i],dv->nom,dv->type);
			//printf("Fin3\n");
		}
	}
	frame=derniere_frame;
	derniere_frame=(struct TEIF_Frame *)malloc(sizeof(struct TEIF_Frame));
	memset(derniere_frame,0,sizeof(struct TEIF_Frame));
	derniere_frame->nom_classe=objet->type->nom;
	derniere_frame->nom_fonction=nf;
	derniere_frame->f=NULL;
	derniere_frame->local=NULL; // todo: a calculer
	{
	struct TEIF_Membre **(p);
	p=(struct TEIF_Membre **)param2;
	derniere_frame->param=p;
	//derniere_frame->param=&param2;
	}
	derniere_frame->objet_courant=objet;
	derniere_frame->precedant=frame;
	if(f->type==TEIF_FeatureRoutine)
	{
		if(f->local!=NULL&&f->taille_local>0)
		{// todo:a corriger
			struct TEIF_Donnee *d;
			struct TEIF_DeclareVar *dv;
			struct TEIF_Membre **(tab);
			tab=(struct TEIF_Membre **)malloc(sizeof(struct TEIF_Membre *)*NB_TABLE);
			memset(tab,0,sizeof(struct TEIF_Membre *)*NB_TABLE);
			for(i=0;i<f->taille_local;i++)
			{
				dv=(*f->local)[i];
				d=defaut_var(dv->type);//(struct TEIF_Membre **)*
				//printf("Debut2\n");
				ajoute_var(tab,d,dv->nom,dv->type);
				//printf("Fin2\n");
			}
			derniere_frame->local=tab;
		}
		if(f->type_retour!=NULL)
		{// création de la variable result
			if(derniere_frame->local==NULL)
			{
				struct TEIF_Membre **(tab);
				tab=(struct TEIF_Membre **)malloc(sizeof(struct TEIF_Membre *)*NB_TABLE);
				memset(tab,0,sizeof(struct TEIF_Membre *)*NB_TABLE);
				derniere_frame->local=tab;
			}
			ajoute_var(derniere_frame->local,defaut_var(f->type_retour),
				"Result",f->type_retour);
		}
		eval_instr(derniere_frame,f->liste_instr,f->taille_liste_instr);
		/*if(f->taille_liste_instr>0)
		{
			struct TEIF_Instr *instr;
			struct TEIF_Couple *c;
			struct TEIF_Donnee *d;
			for(i=0;i<f->taille_liste_instr;i++)
			{
				instr=(*f->liste_instr)[i];
				if(instr->type==TEIF_Affect)
				{
					struct TEIF_Type *t1,t2;
					/*if(i>=21)
					{
						printf("%d)",i);
					}* /
					c=evalue(objet,instr->expr);
					/*if(i>=21)
					{
						printf("Eval Ok",i);
					}* /
					d=c->donnee;
					/*if(d1->type_donnee==EInt)
					{
						d1=convReal(d1);
					}*/
					/*{
					struct TEIF_Donnee *tmp1,*tmp2,*tab[2];
					tab[0]=d1;
					tab[1]=d2;
					make_compatible(tab);
					d1=tab[0];
					d2=tab[1];
					}* /
					t1=get_var_type(derniere_frame,instr->nom);
					if(t1!=NULL&&stricmp(t1->nom,"double")==0)
					{
						if(d!=NULL&&(d->type_donnee==EReal||
							d->type_donnee==EInt))
						{
							d=convDouble(d);
						}
					}
					else if(t1!=NULL&&stricmp(t1->nom,"real")==0)
					{
						if(d!=NULL&&d->type_donnee==EInt)
						{
							d=convReal(d);
						}
					}
					assert(c!=NULL);
					set_var(derniere_frame,instr->nom,d);
					fprintf(fichier,"instr : %s:=",instr->nom);
					affiche(d,fichier);
					fprintf(fichier,"( loc=%s)",donne_token(instr->token1));
					fprintf(fichier,"\n");
				}
				else if(instr->type==TEIF_Appel)
				{
					struct TEIF_Feature *f;
					struct TEIF_Donnee *d;
					struct TEIF_NomFeature nf={TEIF_Nom_Normal,0,0,0,NULL,NULL};
					struct TEIF_Classe *classe;
					struct TEIF_Donnee *objet_courant;
					struct TEIF_Instr *ins;
					struct TEIF_Membre *membre;
					//sscanf(expr->str,"%d",&i);
					assert(objet!=NULL);
					objet_courant=objet;
					ins=instr;
					while(ins!=NULL)
					{
						assert(objet_courant!=NULL);
						assert(ins->nom!=NULL);
						assert(instr->type==TEIF_Appel);
						if(stricmp(ins->nom,"current")==0)
						{
							d=derniere_frame->objet_courant;
						}
						else
						{
						    membre=donne_var_membre(derniere_frame,ins->nom);
						    if(membre!=NULL)
						    {// c'est une variable
							    assert(instr->taille_parametre==0);
							    d=membre->donnee;
							    assert(d!=NULL);
						    }
						    else
						    {// c'est un appel de routine
							  nf.nom=ins->nom;
							  //printf("nom=%s:\n",nf.nom);
							  classe=cherche_classe(objet_courant->type->nom);
							  assert(classe!=NULL);
							  f=cherche_feature(classe,&nf);
							  assert(f!=NULL);
							  //printf("app:(%p)\n",expr->parametre);
							  if(ins->parametre==NULL)
								  assert(instr->taille_parametre==0);
							  fprintf(fichier,"instr :%d.%s",objet->no_objet,ins->nom);
							  //affiche(d,fichier);
							  fprintf(fichier,"( loc=%s)",donne_token(ins->token1));
							  fprintf(fichier,"\n");
							  d=appel_routine(objet_courant,f,&nf,
								  ins->parametre,ins->taille_parametre);
						    }
						}
						objet_courant=d;
						//printf("fin appel\n");
						/*assert(expr->str2!=NULL);
						p2=convertie_chaine(expr->str2);
						assert(p2!=NULL);
						p=(char*)malloc(strlen(p2)+1);
						memset(p,0,strlen(p2)+1);
						strcpy(p,p2);
						d=defaut_var(&type_string);
						d->u.val_string=p;*/
						/*res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
						memset(res,0,sizeof(struct TEIF_Couple));
						res->donnee=d;
						res->type=f->type_retour;* /
						ins=ins->suivant;
						if(ins!=NULL)
						{
							//assert(objet_courant!=NULL);
							if(objet_courant==NULL)
							{
								lance_exception("Appel sur objet Void");
							}
						}
					}
					assert(d==NULL);
					assert(f->type_retour==NULL);
				}
				else if(instr->type==TEIF_Creation)
				{
					struct TEIF_Feature *f;
					struct TEIF_Donnee *d;
					struct TEIF_NomFeature nf={TEIF_Nom_Normal,0,0,0,NULL,NULL};
					struct TEIF_Classe *classe;
					struct TEIF_Donnee *objet_courant;
					struct TEIF_Type *type_creation;
					char *var,*nom_routine=NULL;
					//sscanf(expr->str,"%d",&i);
					//assert(0);
					var=instr->nom;
					assert(var!=NULL);
					if(instr->nom2!=NULL)
					{
						nom_routine=instr->nom2;
					}
					if(instr->type_creation!=NULL)
					{
						type_creation=instr->type_creation;
					}
					else
					{
						type_creation=get_var_type(derniere_frame,var);
						//printf("type=%s!\n",type_creation);
					}
					assert(type_creation!=NULL);
					d=creer_var(type_creation);
					assert(d!=NULL);
					set_var(derniere_frame,var,d);
					objet_courant=d;
					assert(objet_courant!=NULL);
					fprintf(fichier,"instr : !%s!%s",type_creation->nom,var);
					if(nom_routine!=NULL)
					{
						fprintf(fichier,".%s",nom_routine);
					}
					//affiche(d,fichier);
					fprintf(fichier,"(obj=%d, loc=%s)",objet_courant->no_objet,donne_token(instr->token1));
					fprintf(fichier,"\n");
					if(nom_routine!=NULL)
					{// c'est un appel
					    nf.nom=nom_routine;
					    //printf("nom=%s:\n",nf.nom);
					    classe=cherche_classe(objet_courant->type->nom);
					    assert(classe!=NULL);
					    f=cherche_feature(classe,&nf);
					    assert(f!=NULL);
					    //printf("app:(%p)\n",expr->parametre);
					    if(instr->parametre==NULL)
						    assert(instr->taille_parametre==0);
					    d=appel_routine(objet_courant,f,&nf,
						    instr->parametre,instr->taille_parametre);
					    //printf("fin appel\n");
					    /*assert(expr->str2!=NULL);
					    p2=convertie_chaine(expr->str2);
					    assert(p2!=NULL);
					    p=(char*)malloc(strlen(p2)+1);
					    memset(p,0,strlen(p2)+1);
					    strcpy(p,p2);
					    d=defaut_var(&type_string);
					    d->u.val_string=p;*/
					    /*res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
					    memset(res,0,sizeof(struct TEIF_Couple));
					    res->donnee=d;
					    res->type=f->type_retour;* /
					    assert(d==NULL);
					    assert(f->type_retour==NULL);
					}
				}
				else
				{
					assert(0);
				}
			}
		}*/
		{
			struct TEIF_Membre *(m);
			m=donne_var_membre(derniere_frame,"result");
			if(m!=NULL)
				resultat=m->donnee;
		}
	}
	else
	{
		char *chaine;
		assert(f->type==TEIF_FeatureExternal);
		chaine=convertie_chaine(f->str);
		if(stricmp(chaine,"tinyeiffel")==0)
		{
			TEIF_Fun_extern *fun;
			fun=f->function_ext;
			assert(fun!=NULL);
			resultat=fun(objet,nf,param,taille);
			/*if(stricmp(f->classe->type->nom,"integer")==0)
			{
				resultat=fun_extern_int(objet,nf,param,taille);
			}
			else if(stricmp(f->classe->type->nom,"boolean")==0)
			{
				resultat=fun_extern_bool(objet,nf,param,taille);
			}
			else if(stricmp(f->classe->type->nom,"character")==0)
			{
				resultat=fun_extern_char(objet,nf,param,taille);
			}
			else if(stricmp(f->classe->type->nom,"double")==0)
			{
				resultat=fun_extern_double(objet,nf,param,taille);
			}
			else if(stricmp(f->classe->type->nom,"real")==0)
			{
				resultat=fun_extern_real(objet,nf,param,taille);
			}
			else
			{
				printf("type=%s",f->classe->type->nom);
				assert(0);
			}*/
			//resultat=objet->fun_extern(nf,param);
		}
		else
		{
			assert(0);
		}
	}
	fprintf(fichier,"epilogue(%d:%s.%s)\n",objet->no_objet,donne_type(objet->type),donne_nfeature(nf));
	derniere_frame=derniere_frame->precedant;
	return resultat;
}

struct TEIF_Donnee *appel_routine(struct TEIF_Donnee *objet,
		struct TEIF_Feature *f,struct TEIF_NomFeature *nf,
		struct TEIF_Expr *(*param)[],int taille)
{
	struct TEIF_Donnee /* *param2,*/*res;
	TEIF_Tab_Donnee *param2;
	struct TEIF_Couple *e;
	assert(objet!=NULL);
	assert(f!=NULL);
	assert(f->type==TEIF_FeatureExternal||
			f->type==TEIF_FeatureRoutine);
	assert(taille>=0);
	assert(f->taille_param==taille);
	if(param==NULL||taille==0)
	{
		assert(taille==0);
		param2=NULL;
	}
	else
	{
		int i;
		param2=(TEIF_Tab_Donnee *)malloc(sizeof(struct TEIF_Donnee*)*taille);
		for(i=0;i<taille;i++)
		{
			//printf("evalue:\n");
			e=evalue(derniere_frame->objet_courant,(*param)[i]);
			//printf("evalue2:\n");
			(*param2)[i]=e->donnee;
			//printf("evalue3:\n");
		}
	}
	//printf("lance:\n");
	res=lance_routine(objet,f,nf,(/*(TEIF_Tab_Donnee)*/param2),taille);
	//printf("fin lance\n");
	return res;
}


struct TEIF_NomFeature *donne_nom_feature(int type)
{
	struct TEIF_NomFeature *res=NULL;
	assert(type>=Plus);
	assert(type<=Dollard);
	switch(type)
	{
		case Plus:
			res=&liste_nom_feature[0];
			break;
		case Moins:
			res=&liste_nom_feature[1];
			break;
		case Fois:
			res=&liste_nom_feature[2];
			break;
		case Div:
			res=&liste_nom_feature[3];
			break;
		case Div_entier:
			res=&liste_nom_feature[4];
			break;
		case Mod:
			res=&liste_nom_feature[5];
			break;
		case Old:
			res=&liste_nom_feature[6];
			break;
		case Not:
			res=&liste_nom_feature[7];
			break;
		case PlusU:
			res=&liste_nom_feature[8];
			break;
		case MoinsU:
			res=&liste_nom_feature[9];
			break;
		case Point:
			res=&liste_nom_feature[10];
			break;
		case Puiss:
			res=&liste_nom_feature[11];
			break;
		case Xor:
			res=&liste_nom_feature[12];
			break;
		case Or:
			res=&liste_nom_feature[13];
			break;
		case And:
			res=&liste_nom_feature[14];
			break;
		case And_Then:
			res=&liste_nom_feature[15];
			break;
		case Or_Else:
			res=&liste_nom_feature[16];
			break;
		case Implies:
			res=&liste_nom_feature[17];
			break;
		case Egal:
			res=&liste_nom_feature[18];
			break;
		case Diff:
			res=&liste_nom_feature[19];
			break;
		case Infs:
			res=&liste_nom_feature[20];
			break;
		case Inf:
			res=&liste_nom_feature[21];
			break;
		case Sup:
			res=&liste_nom_feature[22];
			break;
		case Sups:
			res=&liste_nom_feature[23];
			break;
		case Free_Op:
			res=&liste_nom_feature[24];
			break;
		case Free_OpU:
			res=&liste_nom_feature[25];
			break;
		case Dollard:
			res=&liste_nom_feature[26];
			break;
		default:
			assert(0);
	}
	assert(res!=NULL);
	return res;
}

TEIF_Bool type_binaire(int type)
{
	assert(type>=Plus);
	assert(type<=Dollard);
	switch(type)
	{
		case Plus:
		case Moins:
		case Fois:
		case Div:
		case Div_entier:
		case Mod:
		case Point:
		case Puiss:
		case Xor:
		case Or:
		case And:
		case And_Then:
		case Or_Else:
		case Implies:
		case Egal:
		case Diff:
		case Infs:
		case Inf:
		case Sup:
		case Sups:
		case Free_Op:
			return 1;
		default:
			return 0;
	}
}

TEIF_Bool type_unaire(int type)
{
	assert(type>=Plus);
	assert(type<=Dollard);
	switch(type)
	{
		case PlusU:
		case MoinsU:
		case Not:
		case Free_OpU:
			return 1;
		default:
			return 0;
	}
}

// comparaison =
int egal(struct TEIF_Donnee *objet1,struct TEIF_Donnee *objet2)
{
	int res=0;
	if(objet1==NULL||objet2==NULL)
	{
		if(objet1==NULL&&objet2==NULL)
		{
			res=1;
		}
	}
	else
	{
		if(objet1->type_donnee==objet2->type_donnee)
		{
			switch(objet1->type_donnee)
			{
				case EInt:
					if(objet1->u.val_int==objet2->u.val_int)
						res=1;
					break;
				case EBool:
					if(objet1->u.val_bool==objet2->u.val_bool)
						res=1;
					break;
				case EChar:
					if(objet1->u.val_char==objet2->u.val_char)
						res=1;
					break;
				case EDouble:
					if(objet1->u.val_double==objet2->u.val_double)
						res=1;
					break;
				case EReal:
					if(objet1->u.val_real==objet2->u.val_real)
						res=1;
					break;
				case EString:
					if(objet1->u.val_string==objet2->u.val_string)
						res=1;
					break;
				case ENormal:
					//fprintf(fichier,"Normal\n");
					if(est_expended(objet1->type))
					{// objet expanded
						// comparaison si meme type
						// comparaison du contenu des objets
						int i,diff;
						struct TEIF_Membre *tmp,*tmp2;
						//fprintf(fichier,"expanded[\n");
						diff=0;
						for(i=0;i<NB_TABLE&&diff==0;i++)
						{
							tmp=(objet1->valeurs)[i];
							tmp2=(objet2->valeurs)[i];
							if(tmp==NULL||tmp2==NULL)
							{
								if(tmp!=tmp2)
									diff=1;
							}
							else
							{
								while(tmp!=NULL&&tmp2!=NULL&&diff==0)
								{
									if(!egal(tmp->donnee,tmp2->donnee))
									{
										/*fprintf(fichier,"pas meme elt\n");
										affiche(tmp->donnee,fichier);
										fprintf(fichier,"\n");
										affiche(tmp2->donnee,fichier);
										fprintf(fichier,"\n");*/
										diff=1;
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
							res=1;
						else
							res=0;
						/*fprintf(fichier,"]res=%d(%d)\n",res,diff);
						affiche(objet1,fichier);
						fprintf(fichier,"\n");
						affiche(objet2,fichier);
						fprintf(fichier,"\n");*/
					}
					else
					{// objet non expanded
						//fprintf(fichier,"normal%d,%d\n",objet1->type->est_like,est_expended(objet1->type));
						if(!est_expended(objet2->type)
							&&objet1==objet2)
						{
							res=1;
						}
						else
						{
							res=0;
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
	//fprintf(fichier,"res2=%d\n",res);
	return res;
}

struct TEIF_Couple *evalue(struct TEIF_Donnee *objet_courant,
					struct TEIF_Expr *expr)
{
	struct TEIF_Couple *res=NULL;
	struct TEIF_Donnee *d;
	assert(expr!=NULL);
	assert(objet_courant!=NULL);
	if(expr->op==Entier)
	{
		int i;
		sscanf(expr->str,"%d",&i);
		d=defaut_var(&type_integer);
		d->u.val_int=i;
		res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
		memset(res,0,sizeof(struct TEIF_Couple));
		res->donnee=d;
		res->type=&type_integer;
	}
	else if(expr->op==Vrai)
	{
		d=defaut_var(&type_boolean);
		d->u.val_bool=1;
		res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
		memset(res,0,sizeof(struct TEIF_Couple));
		res->donnee=d;
		res->type=&type_boolean;
	}
	else if(expr->op==Faux)
	{
		d=defaut_var(&type_boolean);
		d->u.val_bool=0;
		res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
		memset(res,0,sizeof(struct TEIF_Couple));
		res->donnee=d;
		res->type=&type_boolean;
	}
	else if(expr->op==Var)
	{// TODO: a optimiser
		struct TEIF_Membre *m;
		if(stricmp(expr->nom,"current")==0)
		{
			res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
			memset(res,0,sizeof(struct TEIF_Couple));
			res->donnee=derniere_frame->objet_courant;
			res->type=derniere_frame->objet_courant->type;
		}
		else
		{//objet_courant
			//struct TEIF_Donnee *obj=NULL;
			m=NULL;
			if(objet_courant==derniere_frame->objet_courant)
			{
			    /*struct TEIF_Donnee *d;
			struct TEIF_DeclareVar *dv;
			struct TEIF_Membre **(tab);
			tab=(struct TEIF_Membre **)malloc(sizeof(struct TEIF_Membre *)*NB_TABLE);
			memset(tab,0,sizeof(struct TEIF_Membre *)*NB_TABLE);
			for(i=0;i<f->taille_local;i++)
			{
				dv=(*f->local)[i];
				d=defaut_var(dv->type);//(struct TEIF_Membre **)*
				//printf("Debut2\n");
				ajoute_var(tab,d,dv->nom,dv->type);
				//printf("Fin2\n");
			}
			derniere_frame->local=tab;*/
				/*if(derniere_frame->local!=NULL)
					m=donne_var(derniere_frame->local,expr->nom);
				if(m==NULL)*/
					m=donne_var_membre(derniere_frame,expr->nom);
			}
			else
			{
				if(objet_courant!=NULL)
				{
					struct TEIF_Donnee *objet;
					objet=objet_courant;
					m=donne_var(objet->valeurs,expr->nom);
				}
			}
			if(m!=NULL)
			{// c'est une variable
				//d=get_var(derniere_frame,expr->nom);
				d=m->donnee;
				res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
				memset(res,0,sizeof(struct TEIF_Couple));
				res->donnee=d;
				res->type=&type_any;
			}
			else
			{
				struct TEIF_Feature *f;
				struct TEIF_Donnee *d;
				struct TEIF_NomFeature nf={TEIF_Nom_Normal,0,0,0,NULL,NULL};
				struct TEIF_Classe *classe;
				//sscanf(expr->str,"%d",&i);
				//assert(0);
				nf.nom=expr->nom;
				//printf("nom2=%s:\n",nf.nom);
				classe=cherche_classe(objet_courant->type->nom);
				assert(classe!=NULL);
				f=cherche_feature(classe,&nf);
				assert(f!=NULL);
				assert(f->type==TEIF_FeatureExternal||
					f->type==TEIF_FeatureRoutine);
				//printf("entre\n");
				d=lance_routine(objet_courant,f,&nf,
					NULL,0);
				//printf("sort\n");
				//printf("fin appel\n");
				res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
				memset(res,0,sizeof(struct TEIF_Couple));
				res->donnee=d;
				res->type=f->type_retour;
			}
		}
	}
	else if(expr->op==Reel)
	{
		double v,v2,v3,v4;
		float f;
		char buf1[512],buf2[512];
		sscanf(expr->str,"%lf",&v);
		f=v;
		v2=f;
		//v3=v2;
		//v4=v;
		sprintf(buf1,"%f",f);
		sprintf(buf2,"%lf",v);
		//if(v3==v4)
		if(strcmp(buf1,buf2)==0)
		{// il est compatible avec un type real
			//printf("nombre(%s)=%lf,%f\n",expr->str,v,f);
			d=defaut_var(&type_real);
			d->u.val_real=f;
			//d->u.val_real=f;
			res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
			memset(res,0,sizeof(struct TEIF_Couple));
			res->donnee=d;
			res->type=&type_real;
			//printf("nb f=%f!(%d,%lf,%lf)\n",f,(v2==v),v2,v);
		}
		else
		{// il est trop grand pour le type real
			//printf("nombre(%s)=%lf,%f\n",expr->str,v,f);
			d=defaut_var(&type_double);
			d->u.val_double=v;
			//d->u.val_real=f;
			res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
			memset(res,0,sizeof(struct TEIF_Couple));
			res->donnee=d;
			res->type=&type_double;
			//printf("nb d=%lf!(%d,%lf,%lf)\n",v,(v2==v),v2,v);
		}
	}
	else if(expr->op==Char)
	{
		char c;
		//sscanf(expr->str,"%d",&i);
		c=expr->str[1];
		d=defaut_var(&type_character);
		d->u.val_char=c;
		res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
		memset(res,0,sizeof(struct TEIF_Couple));
		res->donnee=d;
		res->type=&type_character;
	}
	else if(expr->op==Chaine)
	{
		char *p,*p2;
		//sscanf(expr->str,"%d",&i);
		assert(expr->str2!=NULL);
		p2=convertie_chaine(expr->str2);
		assert(p2!=NULL);
		p=(char*)malloc(strlen(p2)+1);
		memset(p,0,strlen(p2)+1);
		strcpy(p,p2);
		d=defaut_var(&type_string);
		d->u.val_string=p;
		res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
		memset(res,0,sizeof(struct TEIF_Couple));
		res->donnee=d;
		res->type=&type_string;
	}
	else if(type_binaire(expr->op))
	{
		struct TEIF_Couple *exp1,*exp2;
		struct TEIF_Donnee *d1,*d2;
		struct TEIF_NomFeature *nf;
		struct TEIF_Classe *cl;
		struct TEIF_Feature *f;
		int v;
		switch(expr->op)
		{
			case Plus:
			case Moins:
			case Fois:
			case Div:
			case Div_entier:
			case Mod:
			case Puiss:
			case Xor:
			case Or:
			case And:
			case And_Then:
			case Or_Else:
			case Implies:
			case Infs:
			case Inf:
			case Sup:
			case Sups:
			case Free_Op:
				exp1=evalue(objet_courant,expr->expr1);
				assert(exp1!=NULL);
				exp2=evalue(objet_courant,expr->expr2);
				assert(exp2!=NULL);
				d1=exp1->donnee;
				assert(d1!=NULL);
				d2=exp2->donnee;
				assert(d2!=NULL);
				{
					struct TEIF_Donnee *tmp1,*tmp2,*tab[2];
					tab[0]=d1;
					tab[1]=d2;
					make_compatible(tab);
					d1=tab[0];
					d2=tab[1];
				}
				nf=donne_nom_feature(expr->op);
				assert(nf!=NULL);
				//nf=trouve_nom_feature(exp1->type,exp2->type,nf);
				assert(nf!=NULL);
				cl=cherche_classe(d1->type->nom);
				assert(cl!=NULL);
				f=cherche_feature(cl,nf);
				assert(f!=NULL);
				{
					struct TEIF_Donnee *p[1];
					p[0]=d2;					
					//printf("entre2\n");
					d1=lance_routine(d1,f,nf,&p,1);
					//printf("sort2\n");
				}
				res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
				memset(res,0,sizeof(struct TEIF_Couple));
				res->donnee=d1;
				res->type=f->type_retour;
				break;
			case Point:
				exp1=evalue(objet_courant,expr->expr1);
				assert(exp1!=NULL);
				d1=exp1->donnee;
				assert(d1!=NULL);

				exp2=evalue(d1,expr->expr2);
				assert(exp2!=NULL);
				d2=exp2->donnee;
				assert(d2!=NULL);
				res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
				memset(res,0,sizeof(struct TEIF_Couple));
				res->donnee=d2;
				res->type=exp2->type;
				break;
			case Egal:
			case Diff:
				exp1=evalue(objet_courant,expr->expr1);
				assert(exp1!=NULL);
				d1=exp1->donnee;
				exp2=evalue(objet_courant,expr->expr2);
				assert(exp2!=NULL);
				d2=exp2->donnee;
				//v=0;
				if(expr->op==Egal)
				{
					v=egal(d1,d2);
					//fprintf(fichier,"v0=%d\n",v);
				}
				else
				{
					if(egal(d1,d2)==0)
					{
						v=1;
					}
					else
					{
						v=0;
					}
				}
				//fprintf(fichier,"v=%d\n",v);
				d1=defaut_var(&type_boolean);
				d1->u.val_bool=v;
				res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
				memset(res,0,sizeof(struct TEIF_Couple));
				res->donnee=d1;
				res->type=&type_boolean;
				break;
			default:
				assert(0);
		}
		/*d=get_var(derniere_frame,expr->nom);
		res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
		res->donnee=d;
		res->type=&type_any;*/
	}
	else if(type_unaire(expr->op))
	{
		struct TEIF_Couple *exp1,*exp2;
		struct TEIF_Donnee *d1,*d2;
		struct TEIF_NomFeature *nf;
		struct TEIF_Classe *cl;
		struct TEIF_Feature *f;
		switch(expr->op)
		{
			case PlusU:
			case MoinsU:
			case Not:
			case Free_OpU:
				exp1=evalue(objet_courant,expr->expr1);
				assert(exp1!=NULL);
				d1=exp1->donnee;
				assert(d1!=NULL);
				nf=donne_nom_feature(expr->op);
				assert(nf!=NULL);
				//nf=trouve_nom_feature(exp1->type,exp2->type,nf);
				assert(nf!=NULL);
				cl=cherche_classe(d1->type->nom);
				assert(cl!=NULL);
				f=cherche_feature(cl,nf);
				assert(f!=NULL);
				{
					//struct TEIF_Donnee *p[1];
					//p[0]=d2;
					//printf("taille1=%d,taille2=%d\n",f->taille_param,0);
					//printf("entre3\n");
					d1=lance_routine(d1,f,nf,NULL,0);
					//printf("sort3\n");
				}
				res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
				memset(res,0,sizeof(struct TEIF_Couple));
				res->donnee=d1;
				res->type=f->type_retour;
				break;
			default:
				assert(0);
		}
		/*d=get_var(derniere_frame,expr->nom);
		res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
		res->donnee=d;
		res->type=&type_any;*/
	}
	else if(expr->op==Appel)
	{
		struct TEIF_Feature *f;
		struct TEIF_Donnee *d;
		struct TEIF_NomFeature nf={TEIF_Nom_Normal,0,0,0,NULL,NULL};
		struct TEIF_Classe *classe;
		//sscanf(expr->str,"%d",&i);
		//assert(0);
		assert(objet_courant!=NULL);
		assert(expr!=NULL);
		assert(expr->nom!=NULL);
		nf.nom=expr->nom;
		//printf("nom=%s:\n",nf.nom);
		classe=cherche_classe(objet_courant->type->nom);
		assert(classe!=NULL);
		f=cherche_feature(classe,&nf);
		assert(f!=NULL);
		//printf("app:(%p)\n",expr->parametre);
		if(expr->parametre==NULL)
			assert(expr->taille_parametre==0);
		d=appel_routine(objet_courant,f,&nf,
		    expr->parametre,expr->taille_parametre);
		//printf("fin appel\n");
		/*assert(expr->str2!=NULL);
		p2=convertie_chaine(expr->str2);
		assert(p2!=NULL);
		p=(char*)malloc(strlen(p2)+1);
		memset(p,0,strlen(p2)+1);
		strcpy(p,p2);
		d=defaut_var(&type_string);
		d->u.val_string=p;*/
		res=(struct TEIF_Couple *)malloc(sizeof(struct TEIF_Couple));
		memset(res,0,sizeof(struct TEIF_Couple));
		res->donnee=d;
		res->type=f->type_retour;
	}
	else
	{
		assert(0);
	}
	return res;
}

struct TEIF_NomFeature *trouve_nom_feature(struct TEIF_Type *type1,
				struct TEIF_Type *type2,struct TEIF_NomFeature *nom)
{
	TEIF_Bool *h1;
	int i,no1,no2,j,n,k;
	struct TEIF_Classe *cl,*cl2;
	struct TEIF_NomFeature *res=NULL;
	struct TEIF_Heritage *h;
	assert(type1!=NULL);
	assert(type2!=NULL);
	no1=no_classe(type1->nom);
	no2=no_classe(type2->nom);
	h1=(TEIF_Bool *)malloc(sizeof(TEIF_Bool)*nb_classe);
	memset(h1,0,sizeof(TEIF_Bool)*nb_classe);
	for(i=0;i<nb_classe;i++)
	{
		if(heritage[i][no1]&&heritage[no2][i])
			h1[i]=1;
		else
			h1[i]=0;
	}
	n=no1;
	for(i=0;i<nb_classe;i++)
	{
		//cl2=&(global_classe[i]);
		for(j=0;j<nb_classe;j++)
		{
			if(heritage[n][j])
				break;
		}
		if(j>=nb_classe)
		{
			break;
		}
		cl=&(global_classe[j]);
		// la classe d'indice j est descendante directe de n
		if(cl->heritage!=NULL&&cl->taille_heritage>0)
		{
			h=NULL;
			for(k=0;k<cl->taille_heritage;k++)
			{
				h=(*cl->heritage)[k];
				if(h->type->nom!=NULL&&
					stricmp(h->type->nom,cl->type->nom)==0)
				{
					break;
				}
				h=NULL;
			}
			if(h!=NULL)
			{
				/*if(h->rename!=NULL&&h->taille_rename>0)
				{ // TODO: rename
					for(k=0;k<h->taille_rename;k++)
					{
						if(h->rename[k]->
					}
				}*/ 
			}
		}
		n=j;
	}
	return res;
}

struct TEIF_Donnee *defaut_var(struct TEIF_Type *type)
{
	struct TEIF_Donnee *res=NULL;
	assert(type!=NULL);
	if(stricmp(type->nom,"integer")==0)
	{
		res=(struct TEIF_Donnee *)malloc(sizeof(struct TEIF_Donnee));
		memset(res,0,sizeof(struct TEIF_Donnee));
		res->type_donnee=EInt;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_int=0;
		res->valeurs=NULL;
		res->fun_extern=NULL;
		res->no_objet=get_denier_no();
		//res->fun_extern=&fun_extern_int;
		res->type=type;
	}
	else if(stricmp(type->nom,"boolean")==0)
	{
		res=(struct TEIF_Donnee *)malloc(sizeof(struct TEIF_Donnee));
		memset(res,0,sizeof(struct TEIF_Donnee));
		res->type_donnee=EBool;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_bool=0;
		res->valeurs=NULL;
		res->fun_extern=NULL;
		res->type=type;
		res->no_objet=get_denier_no();
	}
	else if(stricmp(type->nom,"character")==0)
	{
		res=(struct TEIF_Donnee *)malloc(sizeof(struct TEIF_Donnee));
		memset(res,0,sizeof(struct TEIF_Donnee));
		res->type_donnee=EChar;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_char='\0';
		res->valeurs=NULL;
		res->fun_extern=NULL;
		res->type=type;
		res->no_objet=get_denier_no();
	}
	else if(stricmp(type->nom,"double")==0)
	{
		res=(struct TEIF_Donnee *)malloc(sizeof(struct TEIF_Donnee));
		memset(res,0,sizeof(struct TEIF_Donnee));
		res->type_donnee=EDouble;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_double=0.0;
		res->valeurs=NULL;
		res->fun_extern=NULL;
		res->type=type;
		res->no_objet=get_denier_no();
	}
	else if(stricmp(type->nom,"real")==0)
	{
		res=(struct TEIF_Donnee *)malloc(sizeof(struct TEIF_Donnee));
		memset(res,0,sizeof(struct TEIF_Donnee));
		res->type_donnee=EReal;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_real=0.0;
		res->valeurs=NULL;
		res->fun_extern=NULL;
		res->type=type;
		res->no_objet=get_denier_no();
	}
	else if(stricmp(type->nom,"string")==0)
	{
		res=(struct TEIF_Donnee *)malloc(sizeof(struct TEIF_Donnee));
		memset(res,0,sizeof(struct TEIF_Donnee));
		res->type_donnee=EString;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_string=NULL;
		res->valeurs=NULL;
		res->fun_extern=NULL;
		res->type=type;
		res->no_objet=get_denier_no();
	}
	else if(!est_expended(type))
	{// un objet reference => Void
		
	}
	else
	{// todo: eviter la recursivite infinie
		// creation d'un objet expanded
		struct TEIF_Classe *cl;
		res=(struct TEIF_Donnee *)malloc(sizeof(struct TEIF_Donnee));
		memset(res,0,sizeof(struct TEIF_Donnee));
		res->type=type;
		res->no_objet=get_denier_no();
		res->valeurs=NULL;
		res->type_donnee=ENormal;
		res->type_special=0;
		res->expanded=est_expended(type);
		res->fun_extern=NULL;
		cl=cherche_classe(type->nom);
		if(cl->taille_attr>0)
		{
			int i;
			struct TEIF_Attribut *attr;
			struct TEIF_Feature *f;
			res->valeurs=(struct TEIF_Membre **)malloc(sizeof(struct TEIF_Membre*)*NB_TABLE);
			memset((res->valeurs),0,sizeof(struct TEIF_Membre *)*NB_TABLE);
			for(i=0;i<cl->taille_attr;i++)
			{
				attr=(*cl->attr)[i];
				if(attr->feature_directe!=NULL)
					f=attr->feature_directe;
				else
					f=attr->feature_reel;
				if(f!=NULL&&f->type==TEIF_FeatureAttr)
				{// un attribut
					struct TEIF_Donnee *d;
					char *nom;
					d=defaut_var(f->type_retour);
					nom=attr->nom->nom;
					//printf("Debut1\n");
					ajoute_var((res->valeurs),d,nom,f->type_retour);
					//printf("Fin1\n");
				}
			}
		}
	}
	return res;
}

int hashcode(char *nom)
{
	int i,res=0;
	assert(nom!=NULL);
	for(i=0;i<(int)strlen(nom);i++)
	{
		res=(res+(int)nom[i])%(NB_TABLE);
	}
	assert(res>=0);
	assert(res<NB_TABLE);
	return res;
}

void ajoute_var(struct TEIF_Membre *tab[],struct TEIF_Donnee *d,
			char *nom,struct TEIF_Type *type)
{
	int i,hash;
	struct TEIF_Membre *p,*p2;
	/*if(tab==NULL)
	{
		int j;
		j=5;
		i=j+1;
	}*/
	assert(tab!=NULL);
	assert(nom!=NULL);
	hash=hashcode(nom);
	p=(struct TEIF_Membre *)malloc(sizeof(struct TEIF_Membre));
	memset(p,0,sizeof(struct TEIF_Membre));
	p->nom=nom;
	p->donnee=d;
	p->type=type;
	p->suivant=tab[hash];
	tab[hash]=p;
}

int get_denier_no()
{
	static int no_dernier=1;
	int tmp;
	tmp=no_dernier;
	no_dernier++;
	return tmp;
}

struct TEIF_Donnee *creer_var(struct TEIF_Type *type)
{
	struct TEIF_Donnee *res;
	assert(type!=NULL);
	res=(struct TEIF_Donnee *)malloc(sizeof(struct TEIF_Donnee));
	memset(res,0,sizeof(struct TEIF_Donnee));
	res->type=type;
	res->no_objet=get_denier_no();
	if(stricmp(type->nom,"integer")==0)
	{
		res->type_donnee=EInt;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_int=0;
		res->valeurs=NULL;
		//res->fun_extern=&fun_extern_int;
		res->fun_extern=NULL;
	}
	else if(stricmp(type->nom,"boolean")==0)
	{
		res->type_donnee=EBool;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_bool=0;
		res->valeurs=NULL;
		res->fun_extern=NULL;
	}
	else if(stricmp(type->nom,"character")==0)
	{
		res->type_donnee=EChar;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_char='\0';
		res->valeurs=NULL;
		res->fun_extern=NULL;
	}
	else if(stricmp(type->nom,"double")==0)
	{
		res->type_donnee=EDouble;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_double=0.0;
		res->valeurs=NULL;
		res->fun_extern=NULL;
	}
	else if(stricmp(type->nom,"real")==0)
	{
		res->type_donnee=EReal;
		res->type_special=1;
		res->expanded=est_expended(type);
		res->u.val_real=0.0;
		res->valeurs=NULL;
		res->fun_extern=NULL;
	}
	else
	{
		struct TEIF_Classe *cl;
		res->type_donnee=ENormal;
		res->type_special=0;
		res->expanded=est_expended(type);
		res->fun_extern=NULL;
		cl=cherche_classe(type->nom);
		if(cl->taille_attr>0)
		{
			int i;
			struct TEIF_Attribut *attr;
			struct TEIF_Feature *f;
			res->valeurs=(struct TEIF_Membre **)malloc(sizeof(struct TEIF_Membre*)*NB_TABLE);
			memset((res->valeurs),0,sizeof(struct TEIF_Membre *)*NB_TABLE);
			for(i=0;i<cl->taille_attr;i++)
			{
				attr=(*cl->attr)[i];
				if(attr->feature_directe!=NULL)
					f=attr->feature_directe;
				else
					f=attr->feature_reel;
				if(f!=NULL&&f->type==TEIF_FeatureAttr)
				{// un attribut
					struct TEIF_Donnee *d;
					char *nom;
					d=defaut_var(f->type_retour);
					nom=attr->nom->nom;
					//printf("Debut1\n");
					ajoute_var((res->valeurs),d,nom,f->type_retour);
					//printf("Fin1\n");
				}
			}
		}
	}
	return res;
}

struct TEIF_Classe *cherche_classe(char *nom)
{
	int i;
	struct TEIF_Classe *cl;
	
	assert(nom!=NULL);
	for(i=0;i<nb_classe;i++)
	{
		cl=&(global_classe[i]);
		if(stricmp(cl->type->nom,nom)==0)
		{
			return cl;
		}
	}
	return NULL;
}

char *convertie_chaine(struct TEIF_Chaine *str)
{
	int i,len;
	char *buf;
	assert(str!=NULL);
	len=0;
	for(i=0;i<str->taille_liste;i++)
	{
		len+=strlen((*(str->liste))[i]);
	}
	if(len==0)
		return NULL;
	else
	{
		buf=(char*)malloc(len+1);
		memset(buf,0,len+1);
		buf[0]='\0';
		for(i=0;i<str->taille_liste;i++)
		{
			strcat(buf,(*(str->liste))[i]);
		}
		return buf;
	}
}

TEIF_Bool meme_nom_feature(struct TEIF_NomFeature *nom,
					struct TEIF_NomFeature *nom2)
{
	assert(nom!=NULL);
	assert(nom2!=NULL);
	if(nom->type==TEIF_Nom_Normal)
	{
		if(nom2->type!=TEIF_Nom_Normal)
			return 0;
		if(stricmp(nom->nom,nom2->nom)==0)
			return 1;
		else
			return 0;
	}
	else
	{
		char *buf1,*buf2;
		int res;
		if(nom2->type==TEIF_Nom_Normal)
			return 0;
		buf1=convertie_chaine(nom->nom2);
		assert(buf1!=NULL);
		buf2=convertie_chaine(nom2->nom2);
		assert(buf2!=NULL);
		if(stricmp(buf1,buf2)==0)
			res=1;
		else
			res=0;
		free(buf1);
		free(buf2);
		return res;
	}
}

struct TEIF_Attribut *donne_attribut(struct TEIF_Classe *classe,
						struct TEIF_NomFeature *nom)
{
	struct TEIF_Attribut *tmp;
	int i;
	assert(nom!=NULL);
	assert(classe!=NULL);
	for(i=0;i<classe->taille_attr;i++)
	{
		tmp=(*classe->attr)[i];
		if(meme_nom_feature(tmp->nom,nom))
		{
			return tmp;
		}
	}
	return NULL;
}

struct TEIF_Feature *cherche_feature(struct TEIF_Classe *classe,
						struct TEIF_NomFeature *nom)
{
	struct TEIF_Attribut *attr;
	struct TEIF_Feature *f;
	assert(nom!=NULL);
	assert(classe!=NULL);
	attr=donne_attribut(classe,nom);
	if(attr==NULL)
		return NULL;
	if(attr->feature_directe!=NULL)
		f=attr->feature_directe;
	else
		f=attr->feature_reel;
	assert(f!=NULL);
	return f;
}

void run(void)
{
	struct TEIF_Classe *classe_racine;
	struct TEIF_Feature *routine_racine;
	struct TEIF_NomFeature nf={TEIF_Nom_Normal,0,0,0,NULL,NULL};
	struct TEIF_Donnee *objet_racine;

	fichier=fopen("trace.log","w");
	if(fichier==NULL)
	{
		printf("Erreur:Impossible de créer le fichier trace.log");
		return;
	}
	fprintf(fichier,"Initialisation\n");
	printf("pointer:%p,%p,\n",liste_char0,liste_char0[0]);
	printf("suite:\n");
	printf("plus:%s\n",liste_char0[0]);
	printf("plus2:%p\n",(liste_nom_op[0]));
	printf("plus2:%p\n",*(liste_nom_op+0));
	printf("plus2:%p\n",(*(liste_nom_op+0)).liste);
	printf("plus2:%p\n",((liste_nom_op[0])).liste);
	printf("plus2:%p\n",liste_nom_op[0].liste);
	printf("plus2:%p\n",*(liste_nom_op[0].liste));
	printf("plus2:%p\n",(*(liste_nom_op[0].liste))[0]);
	printf("res=%s",convertie_chaine(liste_nom_op+0));
	//assert(0);
	//printf("plus3:%p",*(liste_nom_op[0]));
	//printf("plus2:%s",(*(liste_nom_op[0])).liste[0]);
	//return;
	nf.nom=(char *)nom_routine_racine;
	affiche_heritage();
	printf("Debut du programme\n");
	classe_racine=cherche_classe((char*)nom_classe_racine);
	assert(classe_racine!=NULL);
	routine_racine=cherche_feature(classe_racine,&nf);
	assert(routine_racine!=NULL);
	objet_racine=creer_var(classe_racine->type);
	//printf("entre4\n");
	lance_routine(objet_racine,routine_racine,&nf,NULL,0);
	//printf("sort4\n");
	fprintf(fichier,"Terminaison\n");
	fclose(fichier);
}
