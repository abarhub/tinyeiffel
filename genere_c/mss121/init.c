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
#include <stdlib.h>
#include <time.h>
#include "internal.h"
#include "mss.h"

/* mss_initialized, mss_deinitialized:
   These variables are used to check wether mss is initialized or not, i.e.
   if logfile has been opened for writing, and any other tasks are set up
   properly. If `mss_initialized' is true mss_startup() has been called, but
   that doesn't guarantee that logfile is still open, since mss_exit() might
   have been called by the atexit() function or Mss::~Mss().  If exit()
   HAS been called, then `mss_deinitialized' will equal true.  If this is
   true, then a call to mss_startup() has to be made before doing any
   operations involving the logfile, but it also means that the function
   calling mss_startup() is responsible for calling mss_exit() again before
   returning. */
static int mss_initialized = 0;
int mss_deinitialized = 0;
int mss_check_init_count = 0;

/* option flags */
unsigned char mss_options = 0;

/* mss_print_log_info_on_exit:
   If this is true, Mss will call mss_print_log_info on first call to
   mss_exit(). This will be set to false by the Mss destructor if present,
   and MSS_DTOR_AFTER_ATEXIT was defined. */
int mss_print_log_info_on_exit = 1;


/* mss_open_logfiles:
   This function will open the specified logfile(s), or set mss_logfile to
   stdout/stderr, depending on the configuration options read by
   mss_get_configuration(). Is only to be called by mss_startup(). In case
   mss_open_logfiles() fails to open the specified logfile, or both stdout
   and stderr logging are set, it will print an error message, exiting the
   program.
   TODO: Fix exit points, so that they are safe. */
static void mss_open_logfiles(const char *mode)
{
	if (MSS_DO_LOG_TO_STDOUT && MSS_DO_LOG_TO_STDERR)
	{
		/* WARNING! This exit point might cause a bug, or
		   GPF if destructors are called after call to
		   exit(), performing memory actions. */
		fprintf(stderr, "MSS: Both logging to stdout and stderr was specified.\n");
		exit(EXIT_FAILURE);
	}

	if (MSS_DO_SPECIAL_LOG)
	{
		if (!(mss_slogfile = fopen(MSS_SLOG_FILENAME, mode)))
		{
			/* WARNING! This exit point might cause a bug, or
 			   GPF if destructors are called after call to
 			   exit(), performing memory actions. */
			fprintf(stderr, "MSS: Error opening special logfile (%s); check name, attribs or disk space.\n", MSS_SLOG_FILENAME);
			exit(EXIT_FAILURE);
		}
	}

	/* If neither LOG_TO_STDOUT nor LOG_TO_STDERR, we gotta try
	   logging to a file! */
	if (!MSS_DO_LOG_TO_STDOUT && !MSS_DO_LOG_TO_STDERR)
	{
		if ((mss_logfile = fopen(MSS_LOG_FILENAME, mode)) == NULL)
		{
	             /* WARNING! This exit point might cause a bug, or
			GPF if destructors are called after call to
 			exit(), performing memory actions. */
			fprintf(stderr, "MSS: Error opening logfile (%s); check name, attribs or disk space.\n", MSS_LOG_FILENAME);
			exit(EXIT_FAILURE);
		}
	}
	else
	{
		if (MSS_DO_LOG_TO_STDERR)
			mss_logfile = stderr;
		else
			mss_logfile = stdout;
	}
}

/* mss_startup():
   This function will perform initializing of variables, and
   opening the logfile, writing a header to it (if not called before).
   Will also register mss_exit() as an atexit function. Should NOT be called
   by the user! */
void mss_startup(void)
{
	/* If this function hasn't been called before `mss_initialized' will
	   equal false, and in that case we do all neccessary initializing. */
	mss_disable_threading();
	if (!mss_initialized)
	{
		/* set mss_initialized to be true, since we are in fact about
		   to initialize MSS */
		mss_initialized = -1;

		/* Set a random seed for the "allocation false failure"
		   feature. */
		srand((int)time(NULL));

		dcflist_create_sorted(&mss_list, &mss_free_node_object, &mss_compare_node_objects);
		dcflist_create(&mss_scope, &mss_free_scope_object);

		mss_get_configuration();

		/* Set up mss_options, to be enabled (this option has no
		   effect presently), to show all logs, and to show all
		   warnings.
		   MSS_SHOW_LOGS are now read from config file.*/

	 	mss_options = MSS_ENABLED |
			      (MSS_DO_SHOW_LOGS != 0 ? MSS_SHOW_LOGS : 0) |
			      MSS_SHOW_WARNINGS; /* Every flag set at start. */

		mss_open_logfiles("wt");
		mss_log_header();
	 	if (atexit(&mss_exit) != 0)
		{
		 	fprintf(stderr, "MSS: Error registering atexit() function.\n");
			exit(EXIT_FAILURE);
		}
	}

	/* Okay, so this function has been called before, but if mss_exit()
	   also has been called (`mss_deinitialized' != false) this means
	   there will be more output to the logfile, and since mss_exit()
	   closes the logfile we open it (in append mode) again. (If not
	   logging to a standard stream, see above). */
	else if (mss_deinitialized)
	{
		mss_open_logfiles("at");
	}
	mss_enable_threading();
}

/* mss_exit():
   If mss_startup() has never been called, this function will just return.
   However that should never happen, so we also print a little error
   message to stderr in that case.  Otherwise, we print some info if this
   is the first call to this function, and close the logfile. */
void mss_exit(void)
{
	mss_disable_threading();
	if (!mss_initialized) /* This should never happen. */
	{
#ifdef MSS_DEBUG /* So we print a little error message if in DEBUG mode. */
		fprintf(stderr, "DEBUG: mss_exit() was called, though mss_startup() has never been called.\n");
#endif
		mss_enable_threading();
		return;
	}

	/* If this is the first call to mss_exit() we will print some info,
	   (equal to LOG_INFO), since we assume that this is the end of the
	   program. This might, as mentioned before, not be the case, but
	   what can you do.  Atleast the main part of everything should be
	   executed before this happens.  Maybe a #define should be available
	   to turn this off, but as of now I don't see the need for this. */
	if (!mss_deinitialized)
	{
		MssScope *scope;
		dcflist_go_end(&mss_scope);
		scope = dcflist_get_item(&mss_scope);
		mss_deinitialized = -1;

		if (scope != NULL)
		{
			mss_warn(MSS_WARN_IF_SHOW | MSS_WARN_SEPARATOR,
				"Program exited(?) but there are still %u scopes not left."
				" Last scope entered in %s (line %lu of %s).\n",
				mss_num_scopes, scope->function, scope->line, scope->filename);
		}

		/* mss_print_log_info_on_exit will be set to false by
		   Mss::~Mss(), if MSS_DTOR_AFTER_ATEXIT is defined. Since
		   the destructor will then take care of this. */
	      	if (mss_print_log_info_on_exit)
		{
			fputs(mss_separator, mss_logfile);
			fprintf(mss_logfile, "MSG: Listing info about allocated blocks at end(?) of program:\n");
			mss_log_raw_info();
			fputs(mss_separator, mss_logfile);
		}
#ifdef MSS_DEBUG
		mss_log(MSS_LOG_MODE_ALWAYS, "DEBUG", "MSS deinitialized.\n");
#endif
	}
	/* Otherwise we don't print any info, since we don't want that info
	   for every allocation/deallocation that happens after this function
	   has been called for the first time, now do we!? */
	else
	{
#ifdef MSS_DEBUG
	 	mss_log(MSS_LOG_MODE_ALWAYS, "DEBUG", "MSS re-deintialized.\n");
#endif
	}

	/* We also close the logfile ofcourse, unless it's one of the
	   standard streams. */
	if (mss_logfile != stdout && mss_logfile != stderr)
	{
		fclose(mss_logfile);
	  	mss_logfile = NULL;
	}

	if (MSS_DO_SPECIAL_LOG)
		fclose(mss_slogfile);

	mss_enable_threading();
}

/* check_if_initialized():
   This function should be called in the beginning of every function that
   might use the logfile when both `mss_initialized' and `mss_deinitialized'
   are true, i.e. any function that uses the logfile, or calls a function
   that uses the logfile and can/should be called by the user. (i.e. not
   necessary for static functions. */
void mss_check_if_initialized(void)
{
	/* Just call mss_startup for now, it will take care of the rest.
	   Since the function is inlined this will not add any overhead. */
	mss_check_init_count++;
	if (mss_check_init_count == 1)
		mss_startup();
}

/* deinitialize_if_restarted():
   This function should be called at the end of every function that calls the
   above function (`check_if_initialized()') in the beginning. It calls
   mss_exit() if needed. */
void mss_deinitialize_if_restarted(void)
{
	mss_check_init_count--;
	if (mss_check_init_count < 0)
	{
		fprintf(stderr, "MSS: Internal Error, deinitialize_if_restarted called more times than\n"
				"     check_if_initialized.  Please submit a bug report.\n");
		exit(-1);
	}

	if (mss_check_init_count == 0)
		if (mss_deinitialized)
			mss_exit();
}

/* This function will unnest any nested check_if_initialized() calls, and
   call deinitialize_if_restarted. Useful whenever MSS wants to exit the
   program from within a function. */
void mss_forced_deinitialize_if_restarted(void)
{
	if (mss_check_init_count != 0)
	{
		mss_check_init_count = 1; /* Will be decreased by the above func. */
		mss_deinitialize_if_restarted();
	}
}


