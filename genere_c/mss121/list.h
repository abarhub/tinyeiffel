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
#ifndef __dcflist_h__
#define __dcflist_h__

/* A doubly linked list system. Still to do is an INSERT function. */
struct DCFListItemStruct
{
	void *item;
	struct DCFListItemStruct *next;
	struct DCFListItemStruct *prev;
};

typedef struct DCFListItemStruct DCFListItem;

typedef struct DCFList
{
	DCFListItem *first_item;
	DCFListItem *last_item;
	DCFListItem *current_item;

	unsigned int is_sorted;
	unsigned int size;
	unsigned int position;

	void (*dtor)(void *item);
	int (*cmp)(void *i1, void *i2);
} DCFList;

/* dcflist_create:
   This function will create a linked list, in the object pointed to by
   `list'. It is required to call this function before performing any other
   function on a list.
   * dtor should be a pointer to a function destroying an item (freeing any
     allocated memory and stuff, or be NULL in which no action will be
     performed upon removal of an item. But if any memory in an item is
     dynamically allocated, it is strongly recommended to use a dtor. */
void dcflist_create(DCFList *list, void (*dtor)(void *item));

/* dcflist_create_sorted:
   This function works just like dcflist_create() but it creates a sorted
   list insted. cmp should point to a function comparing two items, returning
   a value < 0 if i1 < i2, 0 if i1 == i2, and > 0 if i1 > i2, just like
   strcmp(). dcflist_append() must NOT be used on a sorted list. */
void dcflist_create_sorted(DCFList *list, void (*dtor)(void *item), int (*cmp)(void *i1, void *i2));

/* dcflist_destroy:
   This function destroys a list, freeing all memory allocated, and calling
   dtor for any item still in the list. Must be called for each item, or
   memory leakage will occur. */
void dcflist_destroy(DCFList *list);

/* dcflist_append:
   Will append the specified item to the list. (Only the pointer will be
   copied, not all the data in the item, so the item should not be freed).
   Will return non-zero upon success. */
int dcflist_append(DCFList *list, void *item);

/* dcflist_insert:
   Will insert the specified item to the list at the current point. If at
   one past end, the item will be appended to the end, and the current item
   will be set to the item appended.
   Must NOT be used with sorted lists. */
int dcflist_insert(DCFList *list, void *item);

/* dcflist_add:
   Will add an item into a sorted list, must NOT be used with unsorted
   lists. */
int dcflist_add(DCFList *list, void *item);


/* dcflist_rewind:
   This function will move the list pointer to the beginning of the list. */
void dcflist_rewind(DCFList *list);

/* dcflist_go_back:
   This function will move the list pointer one item backwards in the list.
   If already at first item, or if there are no items in the list this
   function will return zero. */
int dcflist_go_back(DCFList *list);

/* dcflist_go_forward:
   This function will move the list pointer one item forwards in the list.
   If already at ONE-PAST-END this function will return zero (or if no items
   in the list). */
int dcflist_go_forward(DCFList *list);

/* dcflist_get_item:
   This function will return a pointer to the item pointed to by the list
   pointer. This function will return NULL if at ONE-PAST-END or no items
   in the list. */
void *dcflist_get_item(DCFList *list);

/* dcflist_remove:
   This function will remove the current item, and the next item (if any)
   will become the current item. */
int dcflist_remove(DCFList *list);

/* dcflist_go_end:
   This function will move the list pointer to one past the end of the list,
   hence making a call to dcflist_get_item() right after a call to this
   function returning NULL. */
int dcflist_go_end(DCFList *list);

#endif /* !__dcflist_h__ */

