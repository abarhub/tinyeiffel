/*  MSS -- Memory Supervision System version 1.2
 *  Copyright (C) 1998  Juan Jesus Alcolea Picazo and Peter Palotas
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *  You can contact the authors of this library at the following e-mail
 *  addreses. For more information, look in the documentation that came
 *  with this library.
 *
 *  Juan Jesus Alcolea Picazo, a920101@zipi.fi.upm.es
 *  Peter Palotas, blizzar@hem1.passagen.se
 *
 */
#include <stdlib.h>
#include <stdio.h>
#include "list.h"

void dcflist_create(DCFList *list, void (*dtor)(void *item))
{
	list->first_item = NULL;
	list->last_item = NULL;
	list->current_item = NULL;
	list->size = 0;
	list->position = 0;
	list->dtor = dtor;
	list->is_sorted = 0;
}

void dcflist_create_sorted(DCFList *list, void (*dtor)(void *item), int (*cmp)(void *i1, void *i2))
{
	dcflist_create(list, dtor);
 	list->is_sorted = 1;
	list->cmp = cmp;
}

void dcflist_destroy(DCFList *list)
{
	DCFListItem *item, *temp_item;
	if (list == NULL)
		return;

	item = list->first_item;
	while (item != NULL)
	{
		temp_item = item;
	 	item = item->next;
		if (list->dtor != NULL)
		 	list->dtor(temp_item->item);
		free(temp_item);
	}
}

int dcflist_add(DCFList *list, void *item)
{
	list->position = 0;
	list->current_item = list->first_item;

	while (list->current_item != NULL)
	{
	  	if (list->cmp(list->current_item->item, item) > 0)
		{
			int return_value = dcflist_insert(list, item);
			list->current_item = list->first_item;
			return return_value;
		}
		list->current_item = list->current_item->next;
	};
	list->current_item = list->first_item;
	return dcflist_append(list, item);
}

int dcflist_insert(DCFList *list, void *item)
{
	DCFListItem *new_item;

	if (list->current_item == NULL)
	{
		return dcflist_append(list, item);
	}

	new_item = (DCFListItem *)malloc(sizeof(DCFListItem));
	if (new_item == NULL)
		return 0;

	new_item->item = item;
	new_item->next = list->current_item;
	new_item->prev = list->current_item->prev;
	new_item->next->prev = new_item;

	if (new_item->prev != NULL)
	{
		new_item->prev->next = new_item;
	}
	else
	{
		list->first_item = new_item;
	}
	list->position++;
	list->size++;
	return 1;
}

int dcflist_append(DCFList *list, void *item)
{
	DCFListItem *new_item;

	new_item = (DCFListItem *)malloc(sizeof(DCFListItem));
	if (new_item == NULL)
		return 0;

	new_item->item = item;
 	new_item->next = NULL;

	if (list->current_item == NULL && list->first_item != NULL)
		list->position++;

 	list->size++;

	if (list->last_item == NULL)
	{
		new_item->prev = NULL;
		list->first_item = list->last_item = list->current_item = new_item;
	}
	else
	{
		new_item->prev = list->last_item;
		list->last_item->next = new_item;
		list->last_item = new_item;
	}
	return 1;
}

void dcflist_rewind(DCFList *list)
{
	list->current_item = list->first_item;
	list->position = 0;
}

int dcflist_go_end(DCFList *list)
{
#ifdef MSS_DEBUG
	if (list == NULL)
		fprintf(stderr, "Error in dcflist_go_end(), list passed was NULL.\n");
#endif /* !MSS_DEBUG */

	if (list->size == 0)
		return 0;

	list->current_item = list->last_item;
	list->position = list->size - 1;
	return 1;
}

int dcflist_go_back(DCFList *list)
{
 	if (list->current_item == NULL)
	{
		list->current_item = list->last_item;
		if (list->current_item != NULL)
		{
			list->position = list->size - 1;
		 	return 1;
		}
		return 0;
	}

	if (list->current_item->prev == NULL) /* We are at first item */
		return 0;

	list->current_item = list->current_item->prev;
	list->position--;
	return 1;
}

int dcflist_go_forward(DCFList *list)
{
	if (list->current_item == NULL)
		return 0;

	list->current_item = list->current_item->next;
	list->position++;
	return -1;
}

void *dcflist_get_item(DCFList *list)
{
 	if (list->current_item == NULL)
		return NULL;

	return list->current_item->item;
}

int dcflist_remove(DCFList *list)
{
	DCFListItem *tmp;
 	if (list->current_item == NULL)
		return 0;

	if (list->current_item->next != NULL)
		list->current_item->next->prev = list->current_item->prev;

	if (list->current_item->prev != NULL)
		list->current_item->prev->next = list->current_item->next;

	if (list->current_item == list->last_item)
		list->last_item = list->current_item->prev;

	if (list->current_item == list->first_item)
		list->first_item = list->current_item->next;

	if (list->dtor != NULL)
		list->dtor(list->current_item->item);

	tmp = list->current_item;
	list->current_item = list->current_item->next;

	list->size--;

	free(tmp);
	return 1;
}

