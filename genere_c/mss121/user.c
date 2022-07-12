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

#define __mss_internal__
#include <stdlib.h>
#include "internal.h"
#include "mss.h"

unsigned int mss_num_scopes;

DCFList mss_scope;

/* mss_enter_scope():
   This function will 'enter a scope' (no shit sherlock), i.e. check that
   all blocks allocated until mss_leave_scope() is called is also freed. */
void mss_enter_scope(const char *filename, const char *function, unsigned long line)
{
	MssScope *new_scope = (MssScope *)mss_malloc_wec(sizeof(MssScope));

	mss_disable_threading();
	mss_check_if_initialized();

	new_scope->filename = filename;
	new_scope->function = function;
	new_scope->line = line;
	new_scope->num_blocks = 0;
	dcflist_append(&mss_scope, new_scope);
	mss_num_scopes++;

	mss_log(MSS_LOG_MODE_NORMAL, "LOG", "Scope entered in %s (line %lu of %s).\n",
		function, line, filename);

	mss_slog("SCOPE: FILENAME=\"%s\" FUNCTION=\"%s\" LINE=\"%lu\" METHOD=\"Enter\"\n",
		 filename, function, line);

	mss_deinitialize_if_restarted();
	mss_enable_threading();
}

/* mss_leave_scope():
   This function will leave the current scope, and print a warning unless
   all blocks allocated within this block is also deallocated. */
void mss_leave_scope(const char *filename, const char *function, unsigned long line)
{
	MssScope *item;
	mss_disable_threading();
	mss_check_if_initialized();

	mss_slog("SCOPE: FILENAME=\"%s\" FUNCTION=\"%s\" LINE=\"%lu\" METHOD=\"Leave\"\n",
		 filename, function, line);

	if (mss_scope.size == 0)
	{
	 	mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR,
		 	"'Leave Scope' requested in %s (line %lu of %s), but no scope was ever entered.\n",
		 	function, line, filename);
		mss_deinitialize_if_restarted();
		mss_enable_threading();
		return;
	}

	dcflist_go_end(&mss_scope);
	item = dcflist_get_item(&mss_scope);
#ifdef MSS_DEBUG
	if (item == NULL)
	{
		fprintf(stderr, "Error: item == NULL in mss_leave_scope(%s, %s, %lu)\n", filename, function, line);
		mss_deinitialize_if_restarted();
		mss_enable_threading();
		return;
	}
#endif

	if (item->num_blocks > 0)
	{
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR,
			"Scope left in %s (line %lu of %s) with %i number of blocks still allocated within that scope.\n",
			function, line, filename, item->num_blocks);
	}
	else if (item->num_blocks < 0)
	{
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR,
			"Scope left in %s (line %lu of %s) with %i more number of blocks deleted than allocated within that scope.\n",
			function, line, filename, item->num_blocks);
	}
	else
		mss_log(MSS_LOG_MODE_NORMAL, "LOG", "Scope left in %s (line %lu of %s) with no blocks allocated.\n",
			function, line, filename);

	dcflist_remove(&mss_scope);
	mss_num_scopes--;
	mss_deinitialize_if_restarted();
	mss_enable_threading();
}

/* This function returns the amount of currently used memory. */
unsigned int mss_currently_used_mem(void)
{
	return mss_used_mem;
}

/* This function returns the maximum amount of memory ever used, since
   program start. */
unsigned int mss_maximum_used_nem(void)
{
	return mss_max_used_mem;
}

/* This function returns the number of allocated blocks... (nah, really!?) */
unsigned int mss_number_of_allocated_blocks(void)
{
	return mss_num_of_blocks;
}

/* mss_register_constant_block():
   This function will let you register a "constant" block, which means
   that a warning will be issued upon deletion of this block (if
   MSS_DO_CHECK_ON_DEALLOC was defined) if it was changed, and also in
   mss_check_block(). */
void mss_register_constant_block(void *ptr, const char *filename, const char *function, unsigned long line)
{
	unsigned char *ptr_aux;
	MssNode *node;
	unsigned int k;

	mss_check_if_initialized();
	mss_disable_threading();
	mss_log(MSS_LOG_MODE_START_SEPARATOR,
		"MSG", "Register constant block at %p requested in function %s (line %lu of %s).\n", ptr, function, line, filename);

	/* I look for the block... */
	node = mss_find_node(ptr);
	if (node == NULL) /* ERROR: The block is not here... */
	{
		mss_warn(MSS_WARN_IF_SHOW, "Cannot execute request; there is no block starting at: %p.\n", ptr);
	}
	else if (node->type & MSS_NODE_TYPE_CONSTANT)
	{
		mss_warn(MSS_WARN_IF_SHOW, "Cannot execute request; Block at %p is already registered as constant.\n", ptr);
	}
	else
	{
		ptr_aux = (unsigned char *)node->ptr; /* Aux. pointer to run through the block */
		node->type |= MSS_NODE_TYPE_CONSTANT;	/* We set bit 0 to 1 = constant block. */

		for (k = 0; k < (node->size); k++)
			node->checksum += *ptr_aux++;	/*  We compute the checksum. */

		mss_log(MSS_LOG_MODE_NORMAL, "MSG", "Block sized " MSS_PRINTF_SIZE_T " bytes at %p, allocated by %s (line %lu of %s) has been registered as CONSTANT.\n",
			(MSS_PTR_SIZE_T)node->size, ptr, node->function, node->line, node->filename);
		mss_log(MSS_LOG_MODE_NORMAL,
			"MSG", "Checksum for this block is %li.\n", node->checksum);
	}
	mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);

	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

/* mss_unregister_constant_block():
   This will remove the specified block as a constant block, hence making
   it possible to change the block (without having a warning thrown at you ;).
   (By the way, isn't it UNregister, Juanje?) */
void mss_unregister_constant_block(void *ptr, const char *filename, const char *function, unsigned long line)
{
	MssNode *node;

	mss_check_if_initialized();
	mss_disable_threading();

	mss_log(MSS_LOG_MODE_START_SEPARATOR,
		"MSG", "Unregister constant block at %p requested in function %s (line %lu of %s).\n",
		ptr, function, line, filename);

	/* Look for the block... */
	node = mss_find_node(ptr);
	if (node == NULL) /* ERROR: The block is not here... */
	{
		mss_warn(MSS_WARN_IF_SHOW, "Cannot execute request; there is no block starting at %p.\n", ptr);
	}
	else if (!(node->type & MSS_NODE_TYPE_CONSTANT))
	{
		mss_warn(MSS_WARN_IF_SHOW, "Cannot execute request; block at %p was not registered as CONSTANT.\n", ptr);
	}
	else
	{
		node->type &= ~MSS_NODE_TYPE_CONSTANT;
		node->checksum = 0; /* We reset the checksum value */
		mss_log(MSS_LOG_MODE_NORMAL,
			"MSG", "Block sized " MSS_PRINTF_SIZE_T " bytes at %p, allocated by %s (line %lu of %s) has been UNregistered as CONSTANT.\n",
			(MSS_PTR_SIZE_T)node->size, ptr, node->function, node->line, node->filename);
	}

	mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);

	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

void mss_set_block_label(void *ptr, const char *message, const char *filename, const char *function, unsigned long line)
{
 	MssNode *node;

	mss_disable_threading();
	mss_check_if_initialized();

	mss_slog("LABEL: FILENAME=\"%s\" FUNCTION=\"%s\" LINE=\"%lu\" PTR=\"" MSS_PRINTF_SIZE_T "\"\n",
		filename, function, line, (MSS_PTR_SIZE_T)ptr);

	node = mss_find_node(ptr);
	if (node == NULL)
	{
		mss_warn(MSS_WARN_SEPARATOR, "Set block label request in %s (line %lu of %s) "
					     "failed. There is no block starting at %p.\n",
					     function, line, filename, ptr);
	}
	else
	{
		if (node->label != NULL)
		{
		 	free(node->label);
			node->label = NULL;
		}

		if (message == NULL)
		{
		 	mss_log(MSS_LOG_MODE_NORMAL, "MSG",
				"Block label removed for %p as requested in %s (line %lu of %s).\n",
				ptr, function, line, filename);
		}
		else
		{
		 	node->label = (char *)mss_malloc_wec(strlen(message) + 1);
			strcpy(node->label, message);
			mss_log(MSS_LOG_MODE_NORMAL, "MSG",
				"Block label set for %p to \"%s\" as requested in %s (line %lu of %s).\n",
				ptr, message, function, line, filename);
		}
	}

	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

void mss_dump_block(void *ptr, const char *file, const char *filename, const char *function, unsigned long line)
{
	MssNode *node;
	FILE *fout;

	mss_disable_threading();
	mss_check_if_initialized();

	node = mss_find_node(ptr);
	if (node == NULL)
	{
		mss_warn(MSS_WARN_SEPARATOR, "Dump memory contents request in %s (line %lu of %s) "
					     "failed. There is no block starting at %p.\n",
					     function, line, filename, ptr);
	}
	else
	{
		fout = fopen(file, "wb");
		if (fout == NULL)
		{
			mss_warn(MSS_WARN_SEPARATOR, "Dump memory contents of %p (%s) request in %s (line %lu of %s) "
					     "failed. Unable to open file \"%s\" for writing.\n",
					     node->ptr, node->label == NULL ? "no label" : node->label, function, line, filename, file);
		}
		else
		{
		 	fwrite(node->ptr, 1, node->size, fout);
			mss_log(MSS_LOG_MODE_ALWAYS | MSS_LOG_MODE_NORMAL,
				"MSG", "Memory dump of block sized " MSS_PRINTF_SIZE_T " starting at %p (%s) to file \"%s\" requested in %s (line %lu of %s). Request successful.\n",
				(MSS_PTR_SIZE_T)node->size, node->ptr, node->label == NULL ? "no label" : node->label, file, function, line, filename);
			fclose(fout);
		}
	}

	mss_enable_threading();
	mss_deinitialize_if_restarted();
}
