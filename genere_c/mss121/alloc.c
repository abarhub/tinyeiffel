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
#include <string.h>
#include "internal.h"
#include "mss.h"

MssNum mss_no;

/* Data variables which MSS uses to store internal information. */
unsigned long mss_num_of_blocks; /* Number of allocated blocks */
unsigned long mss_used_mem;      /* Amount of currently allocated memory */
unsigned long mss_max_used_mem;  /* Maximum amount of memory
 				    allocated since program start */
DCFList mss_list;

void mss_free_scope_object(void *item)
{
	free(item);
}

void mss_free_node_object(void *item)
{
	if (((MssNode *)item)->label != NULL)
		free (((MssNode *)item)->label);
	free(item);
}

/* mss_compare_node_objects:
   Error prone. If long can't contain a pointer, this function will fail. */
int mss_compare_node_objects(void *i1, void *i2)
{
	return (int)((long)(((MssNode *)i1)->ptr) - (long)(((MssNode *)i2)->ptr));
}

/* malloc_wec():
   An internal helper function, malloc with error checking.  Will warn and
   exit upon out of memory errors. (Not if the user program gets an out of
   memory error, but if it occurs within Mss). */
void *mss_malloc_wec(size_t s)
{
	void *ptr = malloc(s);
	if (ptr == NULL)
	{
	 	fprintf(stderr, "Fatal error:  Out of memory in MSS internal function.\n");
		mss_forced_deinitialize_if_restarted();
	 	exit(EXIT_FAILURE);
	}
	return ptr;
}

/* mss_called_by_to_string:
   Converts a MSS_BY_XXXX value to a string. Will store the string in
   `str'. If `str' is NULL the string will be stored in an internal static
   array, and returned to the caller. */
const char *mss_called_by_to_string(int called_by, char *str)
{
	char *method;
	static char method_str[32];

	if (str == NULL)
		method = method_str;
	else
		method = str;

	switch (called_by)
	{
		case MSS_BY_NEW:
			strcpy(method, "new");
			break;
		case MSS_BY_NEW_ARRAY:
			strcpy(method, "new[]");
			break;
		case MSS_BY_DELETE:
			strcpy(method, "delete");
			break;
		case MSS_BY_DELETE_ARRAY:
			strcpy(method, "delete[]");
			break;
		case MSS_BY_MALLOC:
			strcpy(method, "malloc");
			break;
		case MSS_BY_REALLOC:
			strcpy(method, "realloc");
			break;
		case MSS_BY_CALLOC:
			strcpy(method, "calloc");
			break;
		case MSS_BY_FREE:
			strcpy(method, "free");
			break;
		case MSS_BY_XMALLOC:
			strcpy(method, "xmalloc");
			break;
		case MSS_BY_XREALLOC:
			strcpy(method, "xrealloc");
			break;
		case MSS_BY_XFREE:
			strcpy(method, "xfree");
			break;
		case MSS_BY_CFREE:
			strcpy(method, "cfree");
			break;
		case MSS_BY_STRDUP:
			strcpy(method, "strdup");
			break;
		default:
#ifdef MSS_DEBUG
			fprintf(stderr, "mss_called_by_to_string() called with an unknown called_by value (was %i).", called_by);
#endif
			strcpy(method, "unknown");
			break;
	}
	return method;
}

/* mss_insert_node():
   This function will add a memory block to the internal linked list
   (`mss_list') as specified by the parameters. It does not perform any
   error checking, so the calling function should make sure the parameters is
   valid.  (The memory block specified by `ptr' should be allocated by the
   *calling* function).
   It will also update the mss_used_mem, mss_max_used_mem and mss_num_blocks.
   And also the mss_num_mallocs family of variables.

   Return value:
   	non-zero upon success, or zero (false) if it fails. (Currently it
   	never fails. */
int mss_insert_node(void *ptr, size_t size, int called_by, const char *filename, const char *function, unsigned long line)
{
	MssNode *new_node;

	mss_disable_threading();
	mss_check_if_initialized();

	/* We create the new Node and fill it with data */
	new_node = (MssNode *)mss_malloc_wec(sizeof(MssNode));
	new_node->function = function;
	new_node->filename = filename;
	new_node->line = line;

	new_node->label = NULL;

	new_node->ptr = ptr;
	new_node->size = size;

	new_node->checksum = 0;
	new_node->type = MSS_NODE_TYPE_VARIABLE; /* Block is non-constant by default */

	new_node->allocated_by = called_by;

	dcflist_add(&mss_list, new_node);

	/* let's update the information */
	mss_num_of_blocks++;
	mss_used_mem += size;

	switch (called_by)
	{
		case MSS_BY_NEW:
			mss_no.news.successes++;
			break;
		case MSS_BY_NEW_ARRAY:
			mss_no.new_arrays.successes++;
			break;
		case MSS_BY_MALLOC:
			mss_no.mallocs.successes++;
			break;
		case MSS_BY_CALLOC:
			mss_no.callocs.successes++;
			break;
		case MSS_BY_XMALLOC:
			mss_no.xmallocs.successes++;
			break;
		case MSS_BY_REALLOC:
			mss_no.reallocs.successes++;
			break;
		case MSS_BY_XREALLOC:
			mss_no.xreallocs.successes++;
			break;
		case MSS_BY_STRDUP:
			mss_no.strdups.successes++;
			break;
	};

	dcflist_go_end(&mss_scope);

	if (dcflist_get_item(&mss_scope) != NULL)
	 	((MssScope *)dcflist_get_item(&mss_scope))->num_blocks++;

	if (mss_used_mem > mss_max_used_mem)
		mss_max_used_mem = mss_used_mem;

	mss_deinitialize_if_restarted();
	mss_enable_threading();
	return 1; /* Return success */
}

/* mss_false_fail:
   This function will take a random number and compare it to
   MSS_ALLOC_FALSE_FAIL_PERCENTAGE, and return a true value (non-zero) if
   a false failure should occur, or a false value (zero) otherwise. */
int mss_false_fail(void)
{
	if ( (rand() % 100) < MSS_FALSE_ALLOC_FAIL_PERCENTAGE)
		return 1;
	return 0;
}

/* mss_alloc:
   This function will allocate the memory specified. Performing some error
   checking, i.e. if allocation was successful, and/or if size == 0. If
   any of these fails, an error message will be written to the logfile.
   It will add the memory allocated to the internal list if allocation was
   successful. It will NOT intialize the memory to anything, nor print
   anything to the logfile (unless some of the error checking fails).
   TODO: Add check all on alloc */
void *mss_alloc(size_t s, int called_by, const char *filename, const char *function, unsigned long line)
{
	void *ptr;

	mss_disable_threading();
	mss_check_if_initialized();

	if (s == 0)
	{
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR, "%s (line %lu of %s) tried to allocate a 0-length block.\n", function, line, filename);
		if (MSS_DO_RETURN_NULL_ON_ZERO_LENGTH_ALLOC)
		{
			mss_deinitialize_if_restarted();
			mss_enable_threading();
			return NULL;
		}
	}

	if (MSS_DO_WATCH_LIMITS)
	{
		ptr = (void *)malloc(sizeof(unsigned char) * s + 2 * MSS_WATCH_LIMITS_SIZE);
		if (ptr == NULL) /* Out of memory */
		{
			mss_enable_threading();
			mss_deinitialize_if_restarted();
			return NULL;
		}
		else /* Allocation was successful */
		{
			/* Fill the fortification sections with WatchValue */
			memset(ptr, MSS_WATCH_LIMITS_VALUE, MSS_WATCH_LIMITS_SIZE);	/* Prefix */
			memset((void *)((MSS_PTR_SIZE_T)ptr + s + MSS_WATCH_LIMITS_SIZE), MSS_WATCH_LIMITS_VALUE, MSS_WATCH_LIMITS_SIZE); /* Suffix */
			/* Adjust pointer location */
			ptr = (void *)((MSS_PTR_SIZE_T)ptr + MSS_WATCH_LIMITS_SIZE);
		}
	} /* Don't watch limits */
	else
	{
		ptr = (void *)malloc(sizeof(unsigned char) * s);
		if (ptr == NULL) /* Out of memory */
		{
			mss_deinitialize_if_restarted();
			mss_enable_threading();
			return NULL;
		}
	}

	mss_insert_node(ptr, s, called_by, filename, function, line);

	if (MSS_DO_CHECK_ALL_ON_ALLOC)
	{
	 	mss_check_all_blocks_on_alloc("Check all blocks being performed upon allocation.\n", filename, function, line);
	}

	mss_deinitialize_if_restarted();
	mss_enable_threading();
	return ptr;
}

/* mss_strdup:
   The mss wrapper function for strdup(). */
char *mss_strdup(const char *source, const char *filename, const char *function, unsigned long line)
{
	char *dest;

	if (source == NULL)
	{
		mss_increase_mss_no_calls(MSS_BY_STRDUP);
		return NULL;
	}

	dest = mss_malloc(strlen(source) + 1, MSS_BY_STRDUP, filename, function, line);
	if (dest != NULL)
		strcpy(dest, source);
	return dest;
}

/* mss_find_node:
   This function searches `mss_list' for a node containing the specified
   pointer. If one is found, it is returned, and the list is left in that
   position. (Hence, a call to `dcflist_remove(mss_list)' will remove
   the item searched for). If no item was found, NULL is returned and
   the list is left in a "one past end" state. */
MssNode *mss_find_node(void *ptr)
{
	MssNode *node;
	dcflist_rewind(&mss_list);
	while ( (node = dcflist_get_item(&mss_list)) != NULL)
	{
	 	if (node->ptr == ptr)
			return node;
		dcflist_go_forward(&mss_list);
	};
	return NULL;
}

/* mss_realloc:
   This function is the MSS wrapper for realloc and xrealloc. It takes care
   of the complete allocation task for these functions.
   TODO: Add check all on alloc, and check on dealloc, warn on
         reallocation on newed blocks. */
void *mss_realloc(void *ptr, size_t size, int called_by, const char *filename, const char *function, unsigned long line)
{
	void *new_ptr;
	MssNode *node;
	char method[32];

	/* Check if ptr == NULL in which case this function should act
	   just as malloc/xmalloc. */
	if (ptr == NULL)
		return mss_malloc(size, called_by, filename, function, line);

	mss_disable_threading();
	mss_check_if_initialized();

	mss_called_by_to_string(called_by, method);

	mss_increase_mss_no_calls(called_by);

	/* Check if `ptr' is a valid allocated block. */
	node = mss_find_node(ptr);
	if (node == NULL)
	{
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR, "%s (line %lu of %s) tried to reallocate a block starting at %p using `%s' that was not previously allocated.",
			function, line, filename, ptr, method);

		mss_enable_threading();
		mss_deinitialize_if_restarted();
		return NULL;
	}

	if (called_by == MSS_BY_REALLOC)
	{
		if (mss_false_fail())
		{
			mss_warn(MSS_WARN_SEPARATOR | MSS_WARN_NO_EXIT,
				"Call to `%s' failed, due to FalseAllocFailPercentage.\n", mss_called_by_to_string(called_by, NULL));
			mss_deinitialize_if_restarted();
			mss_enable_threading();
			return NULL;
		}
	}

	new_ptr = mss_alloc(size, called_by, filename, function, line);

	if (new_ptr == NULL)
	{
		if (size == 0)
		{
			mss_enable_threading();
			mss_deinitialize_if_restarted();
			return NULL;
		}

		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR | MSS_WARN_NO_EXIT,
			"Out of memory in call to %s in %s (line %lu of %s).\n",
			method, function, line, filename);

		/* xrealloc() should exit upon allocation failure. */
		if (called_by == MSS_BY_XREALLOC)
		{
		 	fprintf(stderr, "Out of memory in `%s'\n", method);
			mss_forced_deinitialize_if_restarted();
			mss_enable_threading();
			exit(EXIT_FAILURE);
		}
	}

	memcpy(new_ptr, ptr, size < node->size ? size : node->size);

	mss_log(MSS_LOG_MODE_NORMAL,
		"LOG", "%s (line %lu of %s) reallocated a block sized " MSS_PRINTF_SIZE_T " bytes at %p (%s) using `%s' "
	        "into a block sized " MSS_PRINTF_SIZE_T " bytes at %p.\n",
		function, line, filename, (MSS_PTR_SIZE_T)node->size, ptr, node->label == NULL ? "no label" : node->label, method, (MSS_PTR_SIZE_T)size, new_ptr);

	mss_dealloc(ptr, called_by, filename, function, line);

	mss_deinitialize_if_restarted();
	mss_enable_threading();
	return new_ptr;
}

/* mss_free:
   This function will take care of the complete task of deallocating memory
   for the following functions; free(), xfree(), cfree(),
   delete and delete[].
   TODO: Add check all on alloc */
void mss_free(void *ptr, int called_by, const char *filename, const char *function, unsigned long line)
{
	char method[32];
	char dealloc_str[64];
	MssNode *node;

	mss_disable_threading();
	mss_check_if_initialized();

	mss_called_by_to_string(called_by, method);
	mss_increase_mss_no_calls(called_by);

	/* Check if a warning about deallocating a NULL pointer
	   should be issued.  */
	if (!ptr)
	{
		if (MSS_DO_ALWAYS_WARN_ON_NULL_DEALLOC)
		{
			mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR,
			"%s (line %lu of %s) tried to deallocate NULL pointer using `%s'.\n",
			function, line, filename, method);
		}
		mss_enable_threading();
		mss_deinitialize_if_restarted();
		return;
	}
	/* Try finding the node */
	node = mss_find_node(ptr);
	if (node == NULL)
	{
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR,
		 	"%s (line %lu of %s) tried to deallocate a block at %p using `%s' that wasn't allocated.\n",
			function, line, filename, ptr, method);
		mss_enable_threading();
		mss_deinitialize_if_restarted();
		return;
	}

	/* Check that a memory block is deallocated using the appropriate
	   function. */
	switch (node->allocated_by)
	{
		case MSS_BY_NEW:
			strcpy(dealloc_str, "`delete'");
			break;
		case MSS_BY_NEW_ARRAY:
			strcpy(dealloc_str, "`delete[]'");
			break;
		case MSS_BY_MALLOC:
		case MSS_BY_XMALLOC:
		case MSS_BY_REALLOC:
		case MSS_BY_XREALLOC:
		case MSS_BY_CALLOC:
		case MSS_BY_STRDUP:
			strcpy(dealloc_str, "`free', `xfree' or `cfree'");
			break;
	}

	if (called_by == MSS_BY_DELETE && node->allocated_by != MSS_BY_NEW)
	{
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR,
			"%s (line %lu of %s) deallocated a block sized " MSS_PRINTF_SIZE_T " bytes at %p (%s), "
			"using `%s' previously allocated by `%s' in %s (line %lu of %s). "
			"(Should be deallocated using %s).\n",
			function, line, filename, (MSS_PTR_SIZE_T)node->size, ptr, node->label == NULL ? "no label" : node->label, method,
			mss_called_by_to_string(node->allocated_by, NULL), node->function,
			node->line, node->filename, dealloc_str);
	}
	else if (called_by == MSS_BY_DELETE_ARRAY && node->allocated_by != MSS_BY_NEW_ARRAY)
	{
	 	mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR,
			"%s (line %lu of %s) deallocated a block sized " MSS_PRINTF_SIZE_T " bytes at %p (%s), "
			"using `%s' previously allocated by `%s' in %s (line %lu of %s). "
			"(Should be deallocated using %s).\n",
			function, line, filename, (MSS_PTR_SIZE_T)node->size, ptr, node->label == NULL ? "no label" : node->label, method,
			mss_called_by_to_string(node->allocated_by, NULL), node->function,
			node->line, node->filename, dealloc_str);
	}
	else if ((called_by == MSS_BY_FREE || called_by == MSS_BY_XFREE ||
	    called_by == MSS_BY_CFREE) &&
	    (node->allocated_by != MSS_BY_MALLOC &&
	     node->allocated_by != MSS_BY_REALLOC &&
	     node->allocated_by != MSS_BY_XMALLOC &&
	     node->allocated_by != MSS_BY_XREALLOC &&
	     node->allocated_by != MSS_BY_CALLOC &&
	     node->allocated_by != MSS_BY_STRDUP))
	{
	 	mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR,
			"%s (line %lu of %s) deallocated a block sized " MSS_PRINTF_SIZE_T " bytes at %p (%s) "
			"using `%s', previously allocated by `%s' in %s (line %lu of %s). "
			"(Should be deallocated using %s).\n",
			function, line, filename, (MSS_PTR_SIZE_T)node->size, ptr, node->label == NULL ? "no label" : node->label, method,
			mss_called_by_to_string(node->allocated_by, NULL), node->function,
			node->line, node->filename, dealloc_str);
	}
	else
	{
		mss_log(MSS_LOG_MODE_NORMAL,
			"LOG", "%s (line %lu of %s) deallocated a block sized " MSS_PRINTF_SIZE_T " bytes at %p (%s) "
			"using `%s' previously allocated by `%s' in %s (line %lu of %s).\n",
			function, line, filename, (MSS_PTR_SIZE_T)node->size, ptr, node->label == NULL ? "no label" : node->label, method,
			mss_called_by_to_string(node->allocated_by, NULL), node->function,
			node->line, node->filename);
	}

	/* Fill memory with MSS_FILL_MEM_ON_DEALLOC_VALUE if
	   MSS_DO_FILL_MEM_ON_DEALLOC was set to true. */
	if (MSS_DO_FILL_MEM_ON_DEALLOC)
	{
		memset(ptr, MSS_FILL_MEM_ON_DEALLOC_VALUE, node->size);
	}

	mss_dealloc(ptr, called_by, filename, function, line);
	mss_deinitialize_if_restarted();
	mss_enable_threading();
}

/* mss_malloc:
   This function takes care of the complete task of allocating memory,
   for the following functions: malloc, calloc, xmalloc, new, new[].
   the memory allocating part of strdup. It also handles requests from
   realloc and xrealloc if NULL was passed as the pointer to these
   functions. */
void *mss_malloc(size_t s, int called_by, const char *filename, const char *function, unsigned long line)
{
	void *ptr;
	char method[32];

	mss_disable_threading();
	mss_check_if_initialized();

	mss_called_by_to_string(called_by, method);

	mss_increase_mss_no_calls(called_by);

	if (called_by == MSS_BY_MALLOC || called_by == MSS_BY_CALLOC || called_by == MSS_BY_STRDUP)
	{
		if (mss_false_fail())
		{
			mss_warn(MSS_WARN_SEPARATOR | MSS_WARN_NO_EXIT,
				"Call to `%s' failed, due to FalseAllocFailPercentage.\n", mss_called_by_to_string(called_by, NULL));
			mss_deinitialize_if_restarted();
			mss_enable_threading();
			return NULL;
		}
	}

	ptr = mss_alloc(s, called_by, filename, function, line);
	if (ptr == NULL)
	{
		if (s == 0)
		{
			mss_enable_threading();
			mss_deinitialize_if_restarted();
			return NULL;
		}

		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR | MSS_WARN_NO_EXIT,
			"Out of memory in call to %s in %s (line %lu of %s).\n",
			method, function, line, filename);

		/* xmalloc() should exit upon allocation failure. */
		if (called_by == MSS_BY_XMALLOC || called_by == MSS_BY_NEW || called_by == MSS_BY_NEW_ARRAY || called_by == MSS_BY_XREALLOC)
		{
			/* `new' must not return NULL */
		 	fprintf(stderr, "Out of memory in `%s'\n", method);
			mss_forced_deinitialize_if_restarted();
			mss_enable_threading();
			exit(EXIT_FAILURE);
		}
		mss_forced_deinitialize_if_restarted();
		mss_enable_threading();
		return NULL;
	}

	if (called_by == MSS_BY_CALLOC)
	{
		memset(ptr, 0, s);
	}
	else if (MSS_DO_FILL_MEM_ON_ALLOC && called_by != MSS_BY_STRDUP)
	{
	 	memset(ptr, MSS_FILL_MEM_ON_ALLOC_VALUE, s);
	}

	mss_log(MSS_LOG_MODE_NORMAL,
		"LOG", "%s (line %lu of %s) allocated " MSS_PRINTF_SIZE_T " bytes at %p using `%s'.\n",
		function, line, filename, (MSS_PTR_SIZE_T)s, ptr, method);

	mss_deinitialize_if_restarted();
	mss_enable_threading();
	return ptr;
}

/* mss_increase_mss_no_calls:
   This function will increase the mss_no.XXX.calls for the appropriate
   method, based upon CALLED_BY passed to the function. */
void mss_increase_mss_no_calls(int called_by)
{
	switch (called_by)
	{
		case MSS_BY_NEW:
			mss_no.news.calls++;
			break;
		case MSS_BY_NEW_ARRAY:
			mss_no.new_arrays.calls++;
			break;
		case MSS_BY_DELETE:
			mss_no.deletes.calls++;
			break;
		case MSS_BY_DELETE_ARRAY:
			mss_no.delete_arrays.calls++;
			break;
		case MSS_BY_MALLOC:
			mss_no.mallocs.calls++;
			break;
		case MSS_BY_REALLOC:
			mss_no.reallocs.calls++;
			break;
		case MSS_BY_CALLOC:
			mss_no.callocs.calls++;
			break;
		case MSS_BY_FREE:
			mss_no.frees.calls++;
			break;
		case MSS_BY_XMALLOC:
			mss_no.xmallocs.calls++;
			break;
		case MSS_BY_XREALLOC:
			mss_no.xreallocs.calls++;
			break;
		case MSS_BY_XFREE:
			mss_no.xfrees.calls++;
			break;
		case MSS_BY_CFREE:
			mss_no.cfrees.calls++;
			break;
		case MSS_BY_STRDUP:
			mss_no.strdups.calls++;
			break;
		default:
#ifdef MSS_DEBUG
			fprintf(stderr, "mss_increase_mss_no_calls() called with an unknown called_by value (was %i).", called_by);
#endif
			break;
	}
}

/* mss_dealloc:
   This function will deallocate the block starting at `ptr' if it exists,
   and remove node from `mss_list'. It returns a non-zero value upon
   success, and zero (false) otherwise. (If `ptr' doesn't exist it will fail
   and return zero). It also updates the mss_num_deletes family of variables,
   and updates mss_used_mem. */
int mss_dealloc(void *ptr, int called_by, const char *filename, const char *function, unsigned long line)
{
	MssNode *node;
	int result;

	mss_disable_threading();

	node = mss_find_node(ptr);
	if (node == NULL)
	{
		mss_enable_threading();
		return 0;
	}

	mss_num_of_blocks--;
	mss_used_mem -= node->size;

	switch (called_by)
	{
	 	case MSS_BY_DELETE:
			mss_no.deletes.successes++;
			break;
		case MSS_BY_DELETE_ARRAY:
			mss_no.delete_arrays.successes++;
			break;
		case MSS_BY_FREE:
			mss_no.frees.successes++;
			break;
		case MSS_BY_XFREE:
			mss_no.xfrees.successes++;
			break;
		case MSS_BY_CFREE:
			mss_no.cfrees.successes++;
			break;
		case MSS_BY_REALLOC:
		case MSS_BY_XREALLOC:
			break;
		default:
#ifdef MSS_DEBUG
			fprintf(stderr, "mss_dealloc() called with an unknown `called_by', was %i\n", called_by);
#endif
			break;
	}

	if (MSS_DO_CHECK_ALL_ON_ALLOC)
	{
	 	mss_check_all_blocks_on_alloc("Check all blocks being performed upon deallocation.\n", filename, function, line);
		node = mss_find_node(ptr);
	}
	else if (MSS_DO_CHECK_ON_DEALLOC)
	{
		result = mss_check_node(node);
		if (result != MSS_CHECK_NODE_OK)
		{
			mss_log(MSS_LOG_MODE_ALWAYS | MSS_LOG_MODE_NO_EXTRA_NEWLINE, NULL, mss_separator);
			mss_log_check_node_warnings(node, result, filename, function, line);
			mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);
		}
	}


	if (MSS_DO_WATCH_LIMITS)
	{
		node->ptr = (void *)((MSS_PTR_SIZE_T)node->ptr - MSS_WATCH_LIMITS_SIZE);
	}
	free(node->ptr);
	dcflist_remove(&mss_list);

	mss_enable_threading();
	return 1;
}
