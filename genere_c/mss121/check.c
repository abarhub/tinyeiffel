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
#include "internal.h"
#include "mss.h"

/* mss_check_pointer_validity():
   Checks wether a pointer points inside a previously allocated block or
   not. */
void mss_check_pointer_validity(void *ptr, const char *filename, const char *function, unsigned long line)
{
	MssNode *node = NULL; /* Shut up GCC warning -- LB */
	MssNode *start = NULL;
	MssNode *end = NULL;
	MSS_PTR_SIZE_T ptr_start = (MSS_PTR_SIZE_T)(-1);
 	MSS_PTR_SIZE_T ptr_end = (MSS_PTR_SIZE_T)(-1);
 	MSS_PTR_SIZE_T diff;
	int done = 0;

	mss_disable_threading();
	mss_check_if_initialized();

	mss_slog("PTRCHECK: FILENAME=\"%s\" FUNCTION=\"%s\" LINE=\"%lu\" PTR=\"" MSS_PRINTF_SIZE_T "\"\n",
		filename, function, line, (MSS_PTR_SIZE_T)ptr);

	dcflist_rewind(&mss_list);

	mss_log(MSS_LOG_MODE_START_SEPARATOR | MSS_LOG_MODE_ALWAYS, "MSG", "Pointer validity check requested for pointer %p, in %s (line %lu of %s):\n", ptr, function, line, filename);

	while (!done && (node = dcflist_get_item(&mss_list)) != NULL)
	{
		if ((diff = (abs((MSS_PTR_SIZE_T)node->ptr - (MSS_PTR_SIZE_T)ptr))) < ptr_start)
		{
			ptr_start = diff;
			start = node;
		}
		if ((diff = (abs(((MSS_PTR_SIZE_T)node->ptr + (MSS_PTR_SIZE_T)node->size) - (MSS_PTR_SIZE_T)ptr))) < ptr_end)
		{
			ptr_end = diff;
			end = node;
		}
		if (!(done = (((MSS_PTR_SIZE_T)node->ptr <= (MSS_PTR_SIZE_T)ptr) && (((MSS_PTR_SIZE_T)node->ptr + (MSS_PTR_SIZE_T)node->size) > (MSS_PTR_SIZE_T)ptr))))
			dcflist_go_forward(&mss_list);
	}

	/* If !done, specified `ptr' does not point to the start of or inside
	   a valid block. */
	if (!done)
	{
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_NO_EXIT, "Pointer %p does not belong to any valid allocated block.\n", ptr);
		if (mss_num_of_blocks > 0)
		{
			mss_log(MSS_LOG_MODE_ALWAYS, "MSG", "Nearest block start is at %p (%s) (distance = " MSS_PRINTF_SIZE_T " bytes), sized " MSS_PRINTF_SIZE_T " bytes, allocated by %s (line %lu of %s)\n",
				start->ptr, start->label == NULL ? "no label" : start->label, ptr_start, (MSS_PTR_SIZE_T)start->size, start->function, start->line, start->filename);
			mss_log(MSS_LOG_MODE_END_SEPARATOR | MSS_LOG_MODE_ALWAYS, "MSG", "Nearest block end is at %p (%s) (distance = " MSS_PRINTF_SIZE_T " bytes), sized " MSS_PRINTF_SIZE_T " bytes, allocated by %s (line %lu of %s)\n",
				(void *)((MSS_PTR_SIZE_T)end->ptr + (MSS_PTR_SIZE_T)end->size - 1),
				end->label == NULL ? "no label" : end->label,
				ptr_end, (MSS_PTR_SIZE_T)end->size, end->function,
				end->line, end->filename);
		}
		if (MSS_DO_EXIT_ON_WARNING)
		{
			mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);
			mss_deinitialize_if_restarted();
			mss_enable_threading();
			mss_abort();
		}
	}
	else
		mss_log(MSS_LOG_MODE_END_SEPARATOR | MSS_LOG_MODE_ALWAYS, "MSG",
			"Pointer p = %p points to position " MSS_PRINTF_SIZE_T " of a block (%s) sized " MSS_PRINTF_SIZE_T " bytes allocated by %s (line %lu of %s).\n",
			ptr, ((MSS_PTR_SIZE_T)ptr - (MSS_PTR_SIZE_T)node->ptr), node->label == NULL ? "no label" : node->label, (MSS_PTR_SIZE_T)node->size, node->function, node->line, node->filename);

	mss_enable_threading();
	mss_deinitialize_if_restarted();
}


/* mss_check_node():
   This function checks `node' for out of bounds writings if
   MSS_DO_WATCH_LIMITS is true. Since we in that case have allocated
   MSS_WATCH_LIMITS_SIZE bytes before and after the actual memory-block and
   filled that memory with a specified value, we just parse through this memory,
   and check if it is still the specified value.  If not, there has been an
   out of bound write. (Or something like that ;)
   The `node' must be a valid node in `mss_list'.
   This function as of version 0.95.2 also checks the checksum if the block
   is registered as a constant block. */
int mss_check_node(MssNode *node)
{
	unsigned int k;
	unsigned char error = 0;
	unsigned char *ptr = (unsigned char *)(node->ptr);
	unsigned long current_checksum = 0;
	unsigned int return_code = 0;

	mss_disable_threading();

	if (MSS_DO_WATCH_LIMITS)
	{
		/* We check the prefixes */
		for (k = 1; k <= MSS_WATCH_LIMITS_SIZE && !error; k++)
			error = (unsigned char)(*(ptr - k) != MSS_WATCH_LIMITS_VALUE);

		if (error)
		{
			return_code |= MSS_CHECK_NODE_PREFIX;
			error = 0;
		}

		/* We check the suffixes */
		ptr = (unsigned char *)node->ptr + node->size;
		for (k = 0; k < MSS_WATCH_LIMITS_SIZE && !error; k++)
			error = (unsigned char)(*(ptr + k) != MSS_WATCH_LIMITS_VALUE);

		if (error)
		{
			return_code |= MSS_CHECK_NODE_SUFFIX;
			error = 0;
		}
	}
	/* And finally we check the checksum if the block was registered as
	   a constant block. */
	if ((node->type) & MSS_NODE_TYPE_CONSTANT)
	{
		ptr = (unsigned char *)node->ptr; /* Aux. pointer to run through the block */
		current_checksum = 0;

		for (k = 0; k < (node->size); k++)
			current_checksum += *ptr++;		/* We compute the checksum. */

		if (current_checksum != node->checksum)
		{
			return_code |= MSS_CHECK_NODE_CORRUPT;
		}
	}

	mss_enable_threading();
	return return_code;
}

/* mss_check_block:
   This function will check the requested block for out of bound writings
   and constant block corruption, using mss_check_node(), writing any
   warning messages (and request notes) to the logfile. */
void mss_check_block(void *ptr, const char *filename, const char *function, unsigned long line)
{
	MssNode *node;
	int result = 0;

	mss_check_if_initialized();
	mss_disable_threading();

	mss_log(MSS_LOG_MODE_START_SEPARATOR | MSS_LOG_MODE_ALWAYS, "MSG", "Check block at %p requested in function %s (line %lu of %s).\n",
		ptr, function, line, filename);

	mss_slog("CHECK: FILENAME=\"%s\" FUNCTION=\"%s\" LINE=\"%lu\" "
		 "METHOD=\"User\" PTR=\"" MSS_PRINTF_SIZE_T "\" ",
		 filename, function, line, (MSS_PTR_SIZE_T)ptr);

	/* Check if the block exists */
	if ((node = mss_find_node(ptr)) == NULL)
	{
		mss_warn(MSS_WARN_NO_EXIT, "WARNING: There is no block starting at: %p", ptr);
		mss_slog("PREFIX=\"Ok\" SUFFIX=\"Ok\" CONSTANT=\"NA\"\n");
	}
	else
	{
		if ((result = mss_check_node(node)) != 0)
		{
			mss_log_check_node_warnings(node, result, filename, function, line);
			mss_slog("\n");
		}
		else
		{
			mss_slog("PREFIX=\"Ok\" SUFFIX=\"Ok\" CONSTANT=\"%s\"\n",
				(node->type & 1) ? "Ok" : "NA");

		 	mss_log(MSS_LOG_MODE_ALWAYS, "LOG", "Block at %p (%s) appears to be correct",
		 		node->ptr, node->label == NULL ? "no label" : node->label);
		}
	}
	mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);

	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

/* mss_check_all_blocks:
   This function will check all currently allocated blocks upon a request from
   the user, for out of bounds writings and constant block corruption, printing
   stuff to the makefile. */
void mss_check_all_blocks(const char *filename, const char *function, unsigned long line)
{
	MssNode *node;
	int result = 0;
	int error = 0;

	mss_check_if_initialized();
	mss_disable_threading();

		mss_log(MSS_LOG_MODE_START_SEPARATOR | MSS_LOG_MODE_ALWAYS,
		"MSG", "Check all blocks requested in function %s (line %lu of %s).\n",
		function, line, filename);

	dcflist_rewind(&mss_list);

	while ( (node = dcflist_get_item(&mss_list)) != NULL)
	{
		mss_slog("CHECK: FILENAME=\"%s\" FUNCTION=\"%s\" LINE=\"%lu\" "
			 "METHOD=\"User\" PTR=\"" MSS_PRINTF_SIZE_T "\" ",
			 filename, function, line, (MSS_PTR_SIZE_T)node->ptr);

		if ((result = mss_check_node(node)) != 0)
		{
			error = 1;
			mss_log_check_node_warnings(node, result, filename, function, line);
		}
		else
		{
			mss_slog("PREFIX=\"Ok\" SUFFIX=\"Ok\" CONSTANT=\"%s\" ",
				(node->type & 1) ? "Ok" : "NA");
		}
		mss_slog("\n");
		dcflist_go_forward(&mss_list);
	}
	if (!error)
	{
	 	mss_log(MSS_LOG_MODE_ALWAYS, "LOG", "All blocks appear to be correct.\n");
	}
	mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);

	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

/* mss_check_all_blocks_on_alloc:
   This functions performs the same action as mss_check_all_blocks() except
   it starts by printing `message' instead of the default user request
   message. Used by mss_alloc, and mss_dealloc if MSS_DO_CHECK_ALL_ON_ALLOC
   was set to true. */
void mss_check_all_blocks_on_alloc(const char *message, const char *filename, const char *function, unsigned long line)
{
	MssNode *node;
	int result = 0;
	int error = 0;

	mss_check_if_initialized();
	mss_disable_threading();

	mss_log(MSS_LOG_MODE_START_SEPARATOR, "MSG", message);

	dcflist_rewind(&mss_list);

	while ( (node = dcflist_get_item(&mss_list)) != NULL)
	{
		if ((result = mss_check_node(node)) != 0)
		{
			error = 1;
			mss_log_check_node_warnings(node, result, filename, function, line);
		}
		dcflist_go_forward(&mss_list);
	}
	if (!error)
	{
	 	mss_log(MSS_LOG_MODE_NORMAL, "LOG", "All blocks appear to be correct.\n");
	}
	mss_log(MSS_LOG_MODE_NORMAL, NULL, mss_separator);

	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

