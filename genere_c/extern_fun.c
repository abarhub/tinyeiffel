#include "memoire.h"
#include <stdio.h>
#include<assert.h>
#include<string.h>

#include "type_eiffel.h"
#include "programme.h"
#include "interprete.h"
#include "runtime.h"

struct TEIF_Donnee *fun_extern_int(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param)
{
	struct TEIF_Donnee *res;
	assert(objet!=NULL);
	assert(nom!=NULL);
	if(taille_param==1)
	{
	    if(meme_nom_feature(nom,&(liste_nom_feature[0]))!=0)
	    { // operateur binaire +
		    res=defaut_var(&type_integer);
		    res->u.val_int=objet->u.val_int+((*param)[0])->u.val_int;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[1]))!=0)
	    { // operateur binaire -
		    res=defaut_var(&type_integer);
		    res->u.val_int=objet->u.val_int-((*param)[0])->u.val_int;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[2]))!=0)
	    { // operateur binaire *
		    res=defaut_var(&type_integer);
		    res->u.val_int=objet->u.val_int*((*param)[0])->u.val_int;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[4]))!=0)
	    { // operateur binaire //
		    res=defaut_var(&type_integer);
		    res->u.val_int=(int)(objet->u.val_int/((*param)[0])->u.val_int);
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[5]))!=0)
	    { // operateur binaire \\
		    res=defaut_var(&type_integer);
		    res->u.val_int=(int)(objet->u.val_int%((*param)[0])->u.val_int);
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[20]))!=0)
	    { // operateur binaire <
		    res=defaut_var(&type_boolean);
		    if(objet->u.val_int<((*param)[0])->u.val_int)
			    res->u.val_bool=1;
		    else
			    res->u.val_bool=0;
		    return res;
	    }
	}
	else if(taille_param==0)
	{
		if(meme_nom_feature(nom,&(liste_nom_feature[1]))!=0)
		{ // operateur unaire -
			res=defaut_var(&type_integer);
			res->u.val_int=-objet->u.val_int;
			return res;
		}
		else if(meme_nom_feature(nom,&(liste_nom_feature[0]))!=0)
		{ // operateur unaire +
			res=defaut_var(&type_integer);
			res->u.val_int=objet->u.val_int;
			return res;
		}
	}
	assert(0);
	return NULL;
}

struct TEIF_Donnee *fun_extern_bool(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param)
{
	struct TEIF_Donnee *res;
	assert(objet!=NULL);
	assert(nom!=NULL);
	if(taille_param==1)
	{
	    if(meme_nom_feature(nom,&(liste_nom_feature[14]))!=0)
	    { // operateur binaire and
			res=defaut_var(&type_boolean);
			if(objet->u.val_bool&&((*param)[0])->u.val_bool)
				res->u.val_bool=1;
			else
				res->u.val_bool=0;
			return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[13]))!=0)
	    { // operateur binaire or
		    res=defaut_var(&type_boolean);
		    if(objet->u.val_bool||((*param)[0])->u.val_bool)
				res->u.val_bool=1;
			else
				res->u.val_bool=0;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[12]))!=0)
	    { // operateur binaire xor
		    res=defaut_var(&type_boolean);
		    if(((objet->u.val_bool&&!((*param)[0])->u.val_bool))
			||((!objet->u.val_bool&&((*param)[0])->u.val_bool)))
				res->u.val_bool=1;
			else
				res->u.val_bool=0;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[17]))!=0)
	    { // operateur binaire implies
		    res=defaut_var(&type_boolean);
		    if(((objet->u.val_bool&&((*param)[0])->u.val_bool))
			||(!objet->u.val_bool))
				res->u.val_bool=1;
			else
				res->u.val_bool=0;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[15]))!=0)
	    { // operateur binaire and then
		    res=defaut_var(&type_boolean);
		    if(objet->u.val_bool&&((*param)[0])->u.val_bool)
				res->u.val_bool=1;
			else
				res->u.val_bool=0;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[16]))!=0)
	    { // operateur binaire or else
		    res=defaut_var(&type_boolean);
		    if(objet->u.val_bool||((*param)[0])->u.val_bool)
				res->u.val_bool=1;
			else
				res->u.val_bool=0;
		    return res;
	    }
	}
	else if(taille_param==0)
	{
		if(meme_nom_feature(nom,&(liste_nom_feature[7]))!=0)
		{ // operateur unaire not
			res=defaut_var(&type_boolean);
			if(objet->u.val_bool)
				res->u.val_bool=0;
			else
				res->u.val_bool=1;
			return res;
		}
	}
	assert(0);
	return NULL;
}

struct TEIF_Donnee *fun_extern_char(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param)
{
	struct TEIF_Donnee *res;
	assert(objet!=NULL);
	assert(nom!=NULL);
	if(taille_param==1)
	{
	    if(meme_nom_feature(nom,&(liste_nom_feature[20]))!=0)
	    { // operateur binaire <
		    res=defaut_var(&type_boolean);
		    if(objet->u.val_char<((*param)[0])->u.val_char)
			    res->u.val_bool=1;
		    else
			    res->u.val_bool=0;
		    return res;
	    }
	}
	else if(taille_param==0)
	{
		if(meme_nom_feature(nom,&(liste_nom_feature[27]))!=0)
		{ // code
			res=defaut_var(&type_integer);
			res->u.val_int=(int)objet->u.val_char;
			return res;
		}
		/*else if(meme_nom_feature(nom,&(liste_nom_feature[28]))!=0)
		{ // hash_code
			res=defaut_var(&type_integer);
			res->u.val_int=(int)objet->u.val_int;
			return res;
		}*/
	}
	assert(0);
	return NULL;
}

struct TEIF_Donnee *fun_extern_double(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param)
{
	struct TEIF_Donnee *res;
	assert(objet!=NULL);
	assert(nom!=NULL);
	if(taille_param==1)
	{
	    if(meme_nom_feature(nom,&(liste_nom_feature[0]))!=0)
	    { // operateur binaire +
		    res=defaut_var(&type_double);
		    res->u.val_double=objet->u.val_double+((*param)[0])->u.val_double;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[1]))!=0)
	    { // operateur binaire -
		    res=defaut_var(&type_double);
		    res->u.val_double=objet->u.val_double-((*param)[0])->u.val_double;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[2]))!=0)
	    { // operateur binaire *
		    res=defaut_var(&type_double);
		    res->u.val_double=objet->u.val_double*((*param)[0])->u.val_double;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[4]))!=0)
	    { // operateur binaire //
		    res=defaut_var(&type_integer);
		    res->u.val_int=(int)(objet->u.val_double/((*param)[0])->u.val_double);
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[5]))!=0)
	    { // operateur binaire \\
		    res=defaut_var(&type_double);
		    //res->u.val_double=(objet->u.val_double%((*param)[0])->u.val_double);
		    assert(0);
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[20]))!=0)
	    { // operateur binaire <
		    res=defaut_var(&type_boolean);
		    if(objet->u.val_double<((*param)[0])->u.val_double)
			    res->u.val_bool=1;
		    else
			    res->u.val_bool=0;
		    return res;
	    }
	}
	else if(taille_param==0)
	{
		if(meme_nom_feature(nom,&(liste_nom_feature[1]))!=0)
		{ // operateur unaire -
			res=defaut_var(&type_double);
			res->u.val_double=-objet->u.val_double;
			return res;
		}
		else if(meme_nom_feature(nom,&(liste_nom_feature[0]))!=0)
		{ // operateur unaire +
			res=defaut_var(&type_double);
			res->u.val_double=objet->u.val_double;
			return res;
		}
	}
	assert(0);
	return NULL;
}

struct TEIF_Donnee *fun_extern_real(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param)
{
	struct TEIF_Donnee *res;
	assert(objet!=NULL);
	assert(nom!=NULL);
	if(taille_param==1)
	{
	    if(meme_nom_feature(nom,&(liste_nom_feature[0]))!=0)
	    { // operateur binaire +
			float f;
			struct TEIF_Donnee **p;
			//f=((*param)[0])->u.val_real;
			p=(struct TEIF_Donnee **)param;
			f=(*(p+0)/*[0]*/)->u.val_real;
		    res=defaut_var(&type_real);
		    res->u.val_real=objet->u.val_real+((*param)[0])->u.val_real;
		    printf("%f + %f = %f\n",objet->u.val_real,
			  f,res->u.val_real);
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[1]))!=0)
	    { // operateur binaire -
		    res=defaut_var(&type_real);
		    res->u.val_real=objet->u.val_real-((*param)[0])->u.val_real;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[2]))!=0)
	    { // operateur binaire *
		    res=defaut_var(&type_real);
		    res->u.val_real=objet->u.val_real*((*param)[0])->u.val_real;
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[4]))!=0)
	    { // operateur binaire //
		    res=defaut_var(&type_integer);
		    res->u.val_int=(int)(objet->u.val_real/((*param)[0])->u.val_real);
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[5]))!=0)
	    { // operateur binaire \\
		    res=defaut_var(&type_real);
		    //res->u.val_real=(objet->u.val_real%((*param)[0])->u.val_real);
		    assert(0);
		    return res;
	    }
	    else if(meme_nom_feature(nom,&(liste_nom_feature[20]))!=0)
	    { // operateur binaire <
		    res=defaut_var(&type_boolean);
		    if(objet->u.val_real<((*param)[0])->u.val_real)
			    res->u.val_bool=1;
		    else
			    res->u.val_bool=0;
		    return res;
	    }
	}
	else if(taille_param==0)
	{
		if(meme_nom_feature(nom,&(liste_nom_feature[1]))!=0)
		{ // operateur unaire -
			res=defaut_var(&type_real);
			res->u.val_real=-objet->u.val_real;
			return res;
		}
		else if(meme_nom_feature(nom,&(liste_nom_feature[0]))!=0)
		{ // operateur unaire +
			res=defaut_var(&type_real);
			res->u.val_real=objet->u.val_real;
			return res;
		}
	}
	assert(0);
	return NULL;
}

struct TEIF_Donnee *fun_extern_general(struct TEIF_Donnee *objet,
			struct TEIF_NomFeature *nom,
			struct TEIF_Donnee *(*param)[],int taille_param)
{
	struct TEIF_Donnee *res;
	assert(objet!=NULL);
	assert(nom!=NULL);
	if(taille_param==1)
	{
		if(meme_nom_feature(nom,&(liste_nom_feature[29]))!=0||
			meme_nom_feature(nom,&(liste_nom_feature[30]))!=0)
		{ // copy,standard_copy
			copy(objet,((*param)[0]));
			return NULL;
		}
		else if(meme_nom_feature(nom,&(liste_nom_feature[33]))!=0||
			meme_nom_feature(nom,&(liste_nom_feature[34]))!=0)
		{ // is_equal,standard_is_equal
			res=is_equal(objet,((*param)[0]));
			return res;
		}
		else if(meme_nom_feature(nom,&(liste_nom_feature[37]))!=0)
		{ // conforms_to
			res=conforms_to(objet,((*param)[0]));
			return res;
		}
	}
	else if(taille_param==0)
	{
		if(meme_nom_feature(nom,&(liste_nom_feature[31]))!=0||
			meme_nom_feature(nom,&(liste_nom_feature[32]))!=0)
		{ // twin,standard_twin
			//res=defaut_var(&type_integer);
			//res->u.val_int=(int)objet->u.val_char;
			res=twin(objet);
			return res;
		}
		else if(meme_nom_feature(nom,&(liste_nom_feature[35]))!=0)
		{ // generator
			//res=defaut_var(&type_integer);
			//res->u.val_int=(int)objet->u.val_char;
			res=generator(objet);
			return res;
		}
		else if(meme_nom_feature(nom,&(liste_nom_feature[36]))!=0)
		{ // generating_type
			//res=defaut_var(&type_integer);
			//res->u.val_int=(int)objet->u.val_char;
			res=generating_type(objet);
			return res;
		}
		/*else if(meme_nom_feature(nom,&(liste_nom_feature[28]))!=0)
		{ // hash_code
			res=defaut_var(&type_integer);
			res->u.val_int=(int)objet->u.val_int;
			return res;
		}*/
	}
	assert(0);
	return NULL;
}
