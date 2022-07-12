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

#ifndef __mss_internal_h__
#define __mss_internal_h__

/**********************************************************************/
/* This file includes static functions that should be inlined mostly. */
/* And also prototypes for non-static functions that should be 	      */
/* internal to MSS only. And any other stuff like structure	      */
/* definitions and such.                                              */
/**********************************************************************/


/* Since we're in MSS define __mss_internal__ */
#define __mss_internal__
#include <stdio.h>
#include "list.h"
#include "inifile.h"

/*************************/
/* Structure definitions */
/*************************/

/* Structure to informaition about scopes. */
typedef struct MssScope
{
	/* Information about where this scope was entered. */
	const char *function;
	const char *filename;
	unsigned long line;

	/* The number of blocks allocated in this scope. */
	int num_blocks;
} MssScope;

#ifdef __cplusplus
extern "C" {
#endif

/*************************/
/*      INTERNAL.C       */
/*************************/
void mss_disable_threading(void);
void mss_enable_threading(void);
char *mss_word_wrap(const char *label, const char *str);
void mss_abort(void);

/*************************/
/*        USER.C         */
/*************************/
extern DCFList mss_scope;
extern unsigned int mss_num_scopes;


/*************************/
/*       ALLOC.C         */
/*************************/
#define MSS_NODE_TYPE_VARIABLE 0
#define MSS_NODE_TYPE_CONSTANT	1

typedef struct MssNode
{
/* Each node contains info about a memory block */
	const char *function;
	const char *filename;
	unsigned long line;

	char *label;

	void *ptr;
	size_t size;

	unsigned long checksum;
	unsigned char type;  /* This is a flag byte.
				Bit 0=1 means this is a CONSTANT block. */

	unsigned int allocated_by; /* This will indicate if block was allocated
	  			      using new, or malloc/realloc/calloc. */
} MssNode;

typedef struct MssNumAllocs
{
	unsigned long calls;
	unsigned long successes;
} MssNumAllocs;

typedef struct MssNum
{
	MssNumAllocs new_arrays;
	MssNumAllocs news;
	MssNumAllocs deletes;
	MssNumAllocs delete_arrays;
	MssNumAllocs mallocs;
	MssNumAllocs frees;
	MssNumAllocs reallocs;
	MssNumAllocs callocs;
	MssNumAllocs xmallocs;
	MssNumAllocs xfrees;
	MssNumAllocs cfrees;
	MssNumAllocs xreallocs;
	MssNumAllocs strdups;
} MssNum;

extern DCFList mss_list;
extern MssNum mss_no;
extern unsigned long mss_num_of_blocks; /* Number of currently allocated blocks */
extern unsigned long mss_used_mem;  /* Amount of currently used memory */
extern unsigned long mss_max_used_mem; /* Maximum amount of used memory */

void mss_increase_mss_no_calls(int called_by);

void mss_free_scope_object(void *item);
int mss_compare_node_objects(void *i1, void *i2);
void mss_free_node_object(void *item);

void *mss_malloc_wec(size_t s);

void *mss_alloc(size_t s, int called_by, const char *filename, const char *function, unsigned long line);
int mss_dealloc(void *ptr, int called_by, const char *filename, const char *function, unsigned long line);
MssNode *mss_find_node(void *ptr);


/*************************/
/*       CHECK.C         */
/*************************/

/* Return codes from mss_check_node() */
#define MSS_CHECK_NODE_OK	0
#define MSS_CHECK_NODE_SUFFIX	1	/* Suffix corrupt */
#define MSS_CHECK_NODE_PREFIX	2	/* Prefix corrupt */
#define MSS_CHECK_NODE_CORRUPT	4 	/* Constant violation */

int mss_check_node(MssNode *node);
void mss_check_all_blocks_on_alloc(const char *message, const char *filename, const char *function, unsigned long line);


/*************************/
/*       CONFIG.C        */
/*************************/
#define MSS_CONFIG_ENVVAR "MSS_CFG"
#define MSS_CONFIG_TYPE_BOOL	1
#define MSS_CONFIG_TYPE_INT	2
#define MSS_CONFIG_TYPE_HEX	3
#define MSS_CONFIG_TYPE_STRING	4
#define MSS_CONFIG_TYPE_END	5

#define MSS_CONFIG_WHERE_DEFAULT	0
#define MSS_CONFIG_WHERE_LOCAL		1
#define	MSS_CONFIG_WHERE_GLOBAL		2

typedef struct MssConfigOption
{
	const char *name;
	int type;
	void *value;
	int where;
} MssConfigOption;

extern MssConfigOption mss_config_options[];
extern DCFList mss_config_error;

extern char 		MSS_LOCAL_CONFIG_FILENAME[256];
extern char 		MSS_CONFIG_FILENAME[256];
extern char 		MSS_LOG_FILENAME[256];
extern char 		MSS_SLOG_FILENAME[256];
extern int 		MSS_DO_SPECIAL_LOG;
extern int 		MSS_DO_LOG_TO_STDERR;
extern int 		MSS_DO_LOG_TO_STDOUT;
extern int 		MSS_DO_WATCH_LIMITS;
extern unsigned int 	MSS_WATCH_LIMITS_SIZE;
extern unsigned char 	MSS_WATCH_LIMITS_VALUE;
extern int 		MSS_DO_CHECK_ON_DEALLOC;
extern int 		MSS_DO_FILL_MEM_ON_ALLOC;
extern unsigned char 	MSS_FILL_MEM_ON_ALLOC_VALUE;
extern int 		MSS_DO_FILL_MEM_ON_DEALLOC;
extern unsigned char 	MSS_FILL_MEM_ON_DEALLOC_VALUE;
extern int 		MSS_DO_EXIT_ON_WARNING;
extern int 		MSS_FALSE_ALLOC_FAIL_PERCENTAGE;
extern int 		MSS_DO_CHECK_ALL_ON_ALLOC;
extern char 		MSS_DO_EXTRA_NEWLINE;
extern int 		MSS_DO_WORD_WRAP;
extern int 		MSS_DO_ALWAYS_WARN_ON_NULL_DEALLOC;
extern int		MSS_DO_RETURN_NULL_ON_ZERO_LENGTH_ALLOC;
extern int		MSS_CFG_STATUS;
extern int		MSS_DO_SHOW_LOGS;

#define MSS_CFG_STATUS_OK		0
#define MSS_CFG_STATUS_NO_ENVVAR	1
#define MSS_CFG_STATUS_NO_GLOBAL_FILE	2
#define MSS_CFG_STATUS_NO_LOCAL_FILE	4

void mss_get_configuration(void);

/*************************/
/*         LOG.C         */
/*************************/
extern FILE *mss_logfile;         /* output file */
extern FILE *mss_slogfile;	  /* Special logfile */
extern const char mss_separator[];
extern const char mss_separator2[];

#define MSS_LOG_MODE_NORMAL		0 /* Print as normal */
#define MSS_LOG_MODE_START_SEPARATOR	1 /* Print separator before string */
#define MSS_LOG_MODE_END_SEPARATOR	2 /* Print separator after string */
#define MSS_LOG_MODE_NO_EXTRA_NEWLINE	4 /* Don't print extra newline */
#define MSS_LOG_MODE_ALWAYS		8 /* Ignore mss_options setting */

#define MSS_WARN_ALWAYS		0 /* Always warn */
#define MSS_WARN_IF_SHOW 	1 /* write warning only if they should be shown */
#define MSS_WARN_SEPARATOR 	2 /* write separators surrounding warning */
#define MSS_WARN_NO_EXIT	4 /* don't exit */

void mss_log(int mode, const char *label, const char *format, ...)
#ifdef __GNUC__
	__attribute__((format (printf, 3, 4)))
#endif
;
void mss_warn(int options, char *format, ...)
#ifdef __GNUC__
	__attribute__((format (printf, 2, 3)))
#endif
;

void mss_slog(char *format, ...)
#ifdef __GNUC__
	__attribute__((format (printf, 1, 2)))
#endif
;

void mss_log_header(void);
void mss_log_raw_info(void);
void mss_log_check_node_warnings(MssNode *node, int result, const char *filename, const char *function, unsigned long line);


/*************************/
/*        INIT.C         */
/*************************/

/* option_flags values. */
#define MSS_ENABLED 		1
#define MSS_SHOW_LOGS 		2
#define MSS_SHOW_WARNINGS 	4

extern int mss_check_init_count;
extern int mss_deinitialized;
extern int mss_print_log_info_on_exit;
extern unsigned char mss_options;

void mss_exit(void);

void mss_check_if_initialized(void);
void mss_deinitialize_if_restarted(void);
void mss_forced_deinitialize_if_restarted(void);


#ifdef __cplusplus
}
#endif /* !__cplusplus */

#endif /* !__mss_internal_h__ */
