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
#include <stdio.h>
#include <time.h>
#include <stdarg.h>
#include "internal.h"
#include "mss.h"

/* Temporary string used by some functions... Not reentrant, but
   multi-threading is disabled in all of them anyway...
   If that is possible. */
static char tmpstr[1024];

const char mss_separator[] = "------------------------------------------------------------------------------\n";
const char mss_separator2[] = "******************************************************************************\n";

FILE *mss_logfile = NULL;         /* output file */
FILE *mss_slogfile = NULL;	  /* Special logfile */

/* get_time():
   This function will return a string representating the current time, just
   a little helper function for the rest of the functions. */
static char *get_time(void)
{
	time_t now;
	time(&now);
	return ctime(&now);
}

/* mss_log_header():
   This function will write some start information to the logfile.  It's only
   called by mss_startup(), and doesn't need any further explanation! ;) */
void mss_log_header(void)
{
	int i = 0;
	char *s;
	mss_disable_threading();
	fprintf(mss_logfile, "%s", mss_separator);
	fprintf(mss_logfile, "MSS -- Memory Supervision System version %s\n", MSS_VERSION);
	fprintf(mss_logfile, "Copyright (C) 1998  Juan Jesus Alcolea Picazo and Peter Palotas\n\n");
	fprintf(mss_logfile, "MSS Normal Logfile.\n\n");
	mss_slog("VERSION: MSS=\"" MSS_VERSION "\"\n");
	mss_slog("INIT: ENVVAR=\"%s\" ", getenv(MSS_CONFIG_ENVVAR) == NULL ? "" : getenv(MSS_CONFIG_ENVVAR));

	if (MSS_CFG_STATUS & MSS_CFG_STATUS_NO_ENVVAR)
	{
		fprintf(mss_logfile, mss_word_wrap("WARNING", "Environment variable `MSS_CFG' not set. Unable to use global configuration file.\n"));
	}

	if (MSS_CFG_STATUS & MSS_CFG_STATUS_NO_GLOBAL_FILE)
	{
		mss_slog("GLOBAL=\"False\" ");

		if (!(MSS_CFG_STATUS & MSS_CFG_STATUS_NO_ENVVAR))
		{
	 		fprintf(mss_logfile, mss_word_wrap("WARNING", "Environment variable `MSS_CFG' does not point to an existing file. No global configuration file could be loaded.\n"));
		}
	}
	else /* Global exists */
	{
	 	mss_slog("GLOBAL=\"True\" ");
	}

	if (MSS_CFG_STATUS & MSS_CFG_STATUS_NO_LOCAL_FILE)
	{
		mss_slog("LOCAL=\"False\" ");

		if (MSS_CFG_STATUS & MSS_CFG_STATUS_NO_GLOBAL_FILE)
		{
		 	fprintf(mss_logfile, mss_word_wrap("WARNING", "No local configuration file found either, using hard-coded default values only.\n"));
		}
		else /* Global exists, local doesn't */
		{
		 	fprintf(mss_logfile, mss_word_wrap("WARNING", "No local configuration file found, using options from global configuration file to override hard-coded values.\n"));
		}
	}
	else /* Local exists */
	{
	 	mss_slog("LOCAL=\"True\" ");
		if (!(MSS_CFG_STATUS & MSS_CFG_STATUS_NO_GLOBAL_FILE))
		{ /* Global exists too :) */
			fprintf(mss_logfile, mss_word_wrap("NOTE", "Using global configuration file pointed to by environment variable `MSS_CFG', and local configuration file in current directory to override hard-coded default values.\n"));
		}
	}

	mss_slog("LOCALCFGFILE=\"%s\"\n", MSS_LOCAL_CONFIG_FILENAME);

	fprintf(mss_logfile, "\nOption List:\n\n");
	while (mss_config_options[i].type != MSS_CONFIG_TYPE_END)
	{
		mss_slog("OPTION: NAME=\"%s\" ", mss_config_options[i].name);
		switch (mss_config_options[i].type)
		{
			case MSS_CONFIG_TYPE_BOOL:
				mss_slog("VALUE=\"%s\" ", *((int *)mss_config_options[i].value) == 0 ? "No" : "Yes");
				fprintf(mss_logfile, "%-35s %-14s", mss_config_options[i].name, *((int *)mss_config_options[i].value) == 0 ? "No" : "Yes");
				break;
			case MSS_CONFIG_TYPE_INT:
				mss_slog("VALUE=\"%i\" ", *((int *)mss_config_options[i].value));
				fprintf(mss_logfile, "%-35s %-14i", mss_config_options[i].name, *((int *)mss_config_options[i].value));
				break;
			case MSS_CONFIG_TYPE_HEX:
				mss_slog("VALUE=\"%i\" ", *((int *)mss_config_options[i].value));
				fprintf(mss_logfile, "%-35s 0x%-12X", mss_config_options[i].name, *((int *)mss_config_options[i].value));
				break;
			case MSS_CONFIG_TYPE_STRING:
				mss_slog("VALUE=\"%s\" ", (char *)mss_config_options[i].value);
				fprintf(mss_logfile, "%-35s %-14s", mss_config_options[i].name, (char *)mss_config_options[i].value);
				break;
		};
		mss_slog("FROM=\"%s\"\n", mss_config_options[i].where == MSS_CONFIG_WHERE_DEFAULT ? "Default" : mss_config_options[i].where == MSS_CONFIG_WHERE_LOCAL ? "Local" : "Global");
		fprintf(mss_logfile, " %s\n", mss_config_options[i].where == MSS_CONFIG_WHERE_DEFAULT ? "(default)" : mss_config_options[i].where == MSS_CONFIG_WHERE_LOCAL ? "(local file)" : "(global file)");
		i++;
	};

	/* Check for errors during configuration, and print those to the
	   logfile. */
	fprintf(mss_logfile, "\n");
	dcflist_rewind(&mss_config_error);
	while ( (s = dcflist_get_item(&mss_config_error)) != NULL)
	{
	 	fprintf(mss_logfile, "WARNING: %s\n", s);
		dcflist_go_forward(&mss_config_error);
	};
	dcflist_destroy(&mss_config_error);
#ifdef MSS_DEBUG
	fprintf(mss_logfile, "MSS_DEBUG was defined.\n");
#endif /* !MSS_DEBUG */
	fprintf(mss_logfile, "%s", mss_separator);
	fprintf(mss_logfile, "Logging started at %s\n", get_time());
	mss_enable_threading();
}

void mss_slog(char *format, ...)
{
 	va_list arg;
	if (MSS_DO_SPECIAL_LOG)
	{
		mss_disable_threading();
		va_start(arg, format);
		vsprintf(tmpstr, format, arg);
		va_end(arg);

		fprintf(mss_slogfile, tmpstr);
		mss_enable_threading();
	}
}

/* mss_log():
   This function will output the specified message to the logfile, word
   wrapping its contents and so on. Really nice function really! ;) */
void mss_log(int mode, const char *label, const char *format, ...)
{
	va_list arg;
	if ((!(mss_options & MSS_SHOW_LOGS)) && (!(mode & MSS_LOG_MODE_ALWAYS)))
		return;

	mss_disable_threading();
	va_start(arg, format);
	vsprintf(tmpstr, format, arg);
	va_end(arg);
	if (mode & MSS_LOG_MODE_START_SEPARATOR)
		fputs(mss_separator, mss_logfile);

	fprintf(mss_logfile, "%s", label == NULL ? tmpstr : mss_word_wrap(label, tmpstr));

	if (mode & MSS_LOG_MODE_END_SEPARATOR)
		fputs(mss_separator, mss_logfile);

	if (!(mode & MSS_LOG_MODE_NO_EXTRA_NEWLINE) && MSS_DO_EXTRA_NEWLINE)
		fprintf(mss_logfile, "\n");

	mss_enable_threading();
}

/* mss_warn():
   Internal helper function to print a warning to the logfile, and exit
   the program if EXIT_ON_WARNING was defined. */
void mss_warn(int options, char *format, ...)
{
	va_list arg;
	va_start(arg, format);
	if (((options & MSS_WARN_IF_SHOW) && !MSS_DO_EXIT_ON_WARNING) || MSS_DO_EXIT_ON_WARNING
		|| !(options & MSS_WARN_IF_SHOW))
	{
		if (((mss_options & MSS_SHOW_WARNINGS) && !MSS_DO_EXIT_ON_WARNING) || MSS_DO_EXIT_ON_WARNING)
		{
			mss_disable_threading();

			if (options & MSS_WARN_SEPARATOR)
				fprintf(mss_logfile, mss_separator);

			vsprintf(tmpstr, format, arg);
			fprintf(mss_logfile, "%s", mss_word_wrap("WARNING", tmpstr));

			if (options & MSS_WARN_SEPARATOR)
				fputs(mss_separator, mss_logfile);

			if (MSS_DO_EXTRA_NEWLINE)
				fprintf(mss_logfile, "\n");
			mss_enable_threading();
		}

		if (!(options & MSS_WARN_NO_EXIT) && MSS_DO_EXIT_ON_WARNING)
		{
			mss_abort();
		}
	}
	va_end(arg);
}

/* mss_log_raw_info():
   This function is a helper function to mss_log_info() that prints the
   info only, without separators and stuff. Also used by mss_exit(). */
void mss_log_raw_info(void)
{
	const char format[] = "%-10s   %8lu         %8lu \n";
	mss_disable_threading();
	if (mss_num_of_blocks != 0)
		fprintf(mss_logfile, "INFO: %lu blocks currently allocated.\n", mss_num_of_blocks);
	else
		fprintf(mss_logfile, "INFO: NO blocks currently allocated.\n");

	if (mss_num_of_blocks != 0)
		fprintf(mss_logfile, "INFO: %lu bytes of memory currently used.\n", mss_used_mem);

	fprintf(mss_logfile, "INFO: %lu bytes maximum memory used.\n", mss_max_used_mem);

	fprintf(mss_logfile, "\n");
	fprintf(mss_logfile, "Method       Times Called    Successful Calls\n");
	fprintf(mss_logfile, "---------------------------------------------\n");

	fprintf(mss_logfile, format, "new", mss_no.news.calls, mss_no.news.successes);
   	fprintf(mss_logfile, format, "new[]", mss_no.new_arrays.calls, mss_no.new_arrays.successes);
	fprintf(mss_logfile, format, "delete", mss_no.deletes.calls, mss_no.deletes.successes);
	fprintf(mss_logfile, format, "delete[]", mss_no.delete_arrays.calls, mss_no.delete_arrays.successes);
	fprintf(mss_logfile, format, "malloc", mss_no.mallocs.calls, mss_no.mallocs.successes);
	fprintf(mss_logfile, format, "xmalloc", mss_no.xmallocs.calls, mss_no.xmallocs.successes);
	fprintf(mss_logfile, format, "realloc", mss_no.reallocs.calls, mss_no.reallocs.successes);
	fprintf(mss_logfile, format, "xrealloc", mss_no.xreallocs.calls, mss_no.xreallocs.successes);
	fprintf(mss_logfile, format, "strdup", mss_no.strdups.calls, mss_no.strdups.successes);
	fprintf(mss_logfile, format, "calloc", mss_no.callocs.calls, mss_no.callocs.successes);
	fprintf(mss_logfile, format, "free", mss_no.frees.calls, mss_no.frees.successes);
	fprintf(mss_logfile, format, "cfree", mss_no.cfrees.calls, mss_no.cfrees.successes);
	fprintf(mss_logfile, format, "xfree", mss_no.xfrees.calls, mss_no.xfrees.successes);
	mss_enable_threading();
}

/* mss_log_disable():
   Well, figure this out if you can.  It will disable all log-output. */
void mss_log_disable(const char *filename, const char *function, unsigned long line)
{
	mss_check_if_initialized();
	mss_disable_threading();
	mss_log(MSS_LOG_MODE_START_SEPARATOR | MSS_LOG_MODE_ALWAYS |
		MSS_LOG_MODE_END_SEPARATOR, "MSG", "Logging output DISABLED in function %s (line %lu of %s) at %s", function, line, filename, get_time());
	mss_options = mss_options & (~MSS_SHOW_LOGS);
	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

/* mss_enable_log_output():
   The very opposite of mss_log_disable. */
void mss_log_enable(const char *filename, const char *function, unsigned long line)
{
	mss_check_if_initialized();
	mss_disable_threading();
	mss_log(MSS_LOG_MODE_START_SEPARATOR | MSS_LOG_MODE_END_SEPARATOR |
		MSS_LOG_MODE_ALWAYS, "MSG", "Logging output ENABLED in function %s (line %lu of %s) at %s", function, line, filename, get_time());
	mss_options = mss_options | MSS_SHOW_LOGS;
	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

/* mss_log_msg():
   This function will output the specified message to the logfile.  It will
   only be called by the user. */
void mss_log_msg(const char *msg, const char *filename, const char *function, unsigned long line)
{
	mss_check_if_initialized();
	mss_disable_threading();
	mss_slog("MSG: FILENAME=\"%s\" FUNCTION=\"%s\" LINE=\"%lu\" MSG=\"%s\"\n",
		filename, function, line, msg);
	fprintf(mss_logfile, mss_separator2);
	fprintf(mss_logfile, "MSG: USER MSG requested in %s (line %lu of %s):\n", function, line, filename);
	fprintf(mss_logfile, mss_word_wrap("MSG", msg));
	fprintf(mss_logfile, "\n");
	mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator2);
	mss_enable_threading();
	mss_deinitialize_if_restarted();
}


/* mss_log_info():
   This function prints some information about the current status of the
   program. */
void mss_log_info(const char *filename, const char *function, unsigned long line)
{
	mss_check_if_initialized();
	mss_disable_threading();
	mss_log(MSS_LOG_MODE_START_SEPARATOR | MSS_LOG_MODE_ALWAYS, "MSG", "Info requested in module %s, line %lu, function %s:\n", filename, line, function);
	mss_log_raw_info();
	mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);
	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

/* mss_log_internal_info():
   This function will print some internal info about MSS itself, how much
   memory is being wasted and such. */
void mss_log_internal_info(const char *filename, const char *function, unsigned long line)
{
	mss_check_if_initialized();
	mss_disable_threading();
	mss_log(MSS_LOG_MODE_START_SEPARATOR | MSS_LOG_MODE_ALWAYS, "MSG", "MSS Internal Info requested in %s (line %lu of %s):\n", function, line, filename);
	fprintf(mss_logfile, "INFO: MSS is internally using %li bytes of memory.\n", (long)(mss_num_of_blocks * sizeof(MssNode)));
	if (MSS_DO_WATCH_LIMITS)
		fprintf(mss_logfile, "INFO: WatchLimits guard info is wasting %li bytes of memory.\n", mss_num_of_blocks * MSS_WATCH_LIMITS_SIZE);

	mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);

	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

/* mss_log_list_blocks():
   This function will list all blocks allocated, specifying in what function
   it was allocated, the size, and such. */
void mss_log_list_blocks(const char *filename, const char *function, unsigned long line)
{
	MssNode *node;
	mss_check_if_initialized();
	mss_disable_threading();
	dcflist_rewind(&mss_list);
	mss_log(MSS_LOG_MODE_START_SEPARATOR | MSS_LOG_MODE_ALWAYS, "MSG", "Block listing requested in function %s (line %lu of %s):\n", function, line, filename);
	fprintf(mss_logfile, "LIST: %li blocks currently allocated;\n", mss_num_of_blocks);
	while ( (node = dcflist_get_item(&mss_list)) != NULL)
	{
		sprintf(tmpstr, MSS_PRINTF_SIZE_T " bytes allocated at %p (%s) by function %s (line %lu of %s).\n",
			(MSS_PTR_SIZE_T)node->size, node->ptr, node->label == NULL ? "no label" : node->label, node->function,
			node->line, node->filename);
		fprintf(mss_logfile, mss_word_wrap("LIST", tmpstr));
		dcflist_go_forward(&mss_list);
	}
	mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);
	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

/* mss_log_check_node_warnings:
   This function will write the appropriate warnings (if any) according
   to the result code that mss_check_node() returns. Also takes care
   of special log output. */
void mss_log_check_node_warnings(MssNode *node, int result, const char *filename, const char *function, unsigned long line)
{
	(void)filename;
	(void)line;
	(void)function;
 	if (result & MSS_CHECK_NODE_PREFIX)
	{
		mss_slog("PREFIX: \"Corrupt\" ");
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_NO_EXIT, "Access out of range; PREFIX of block at %p (%s) sized " MSS_PRINTF_SIZE_T " bytes, allocated by %s (line %lu of %s), has been corrupted.\n",
			node->ptr, node->label == NULL ? "no label" : node->label, (MSS_PTR_SIZE_T)node->size, node->function, node->line, node->filename);
	}
	else
	{
	 	mss_slog("PREFIX: \"Ok\" ");
	}

	if (result & MSS_CHECK_NODE_SUFFIX)
	{
	 	mss_slog("SUFFIX: \"Corrupt\" ");
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_NO_EXIT, "Access out of range; SUFFIX of block at %p (%s) sized " MSS_PRINTF_SIZE_T " bytes, allocated by %s (line %lu of %s), has been corrupted.\n",
			node->ptr, node->label == NULL ? "no label" : node->label, (MSS_PTR_SIZE_T)node->size, node->function, node->line, node->filename);
	}
	else
	{
	 	mss_slog("SUFFIX: \"Ok\" ");
	}

	if (result & MSS_CHECK_NODE_CORRUPT)
	{
		mss_slog("CONSTANT=\"Corrupt\" ");
		mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_NO_EXIT, "Block sized " MSS_PRINTF_SIZE_T " bytes at %p (%s), allocated by %s (line %lu of %s) has been CORRUPTED.\n",
		(MSS_PTR_SIZE_T)node->size, node->ptr, node->label == NULL ? "no label" : node->label, node->function, node->line, node->filename);
	}
	else
	{
	 	mss_slog("CONSTANT=\"%s\" ", (node->type & 1) ? "Ok" : "NA");
	}
}

 /* mss_log_query_blocks:
  **
  ** This function traverses the list of allocated blocks and calls
  ** a user-supplied function parameterized with the block pointer,
  ** the name of the function that allocated the block and the line
  ** number that contains the malloc() call, etc. This allows for
  ** selective inspection of block contents or selective block lists.
  **
  */
void mss_log_block_list_filtered(const char *filename, const char *function, unsigned int line, MssCallBackFunc callback_func)
{
	MssNode *node;
	int stop = 0;

	if (callback_func == NULL)
      		return;

	mss_check_if_initialized();
	mss_disable_threading();

	dcflist_rewind(&mss_list);
	mss_log(MSS_LOG_MODE_START_SEPARATOR | MSS_LOG_MODE_ALWAYS,
		"MSG", "Block callback list requested in function %s (line %i of %s):\n",
		function, line, filename);

	while (!stop && ((node = dcflist_get_item(&mss_list)) != NULL))
	{
		tmpstr[0] = '\0';

		stop = (*callback_func) (
				tmpstr,            /* Any output must go here         */
	               		node->ptr,         /* The allocated block             */
        	       		node->size,        /* The block's size                */
               			node->label,       /* The block's label               */
	               		node->filename,    /* The file in which it took place */
				node->function,
	               		node->line         /* The line where it happened      */
             		);

		if (tmpstr[0] != '\0')  /* Function had something to say...     */
			fprintf(mss_logfile, mss_word_wrap("LIST", tmpstr));

		dcflist_go_forward(&mss_list);
	}

	mss_log(MSS_LOG_MODE_ALWAYS, NULL, mss_separator);
	mss_enable_threading();
	mss_deinitialize_if_restarted();
}

