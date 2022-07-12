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

/* MSS_DEL_LINE_NUMBER, MSS_DEL_FILE_NAME, MSS_DEL_FUNC_NAME:
   These variables are used in the overloaded `delete' operators, since these
   arguments cannot be passed to the function directly.	 This makes the
   delete function non-reentrant, but this is not considered a problem for
   now. */
unsigned int MSS_DEL_LINE;
char *MSS_DEL_FILENAME;
char *MSS_DEL_FUNCTION;

extern "C" int mss_print_log_info_on_exit;
extern "C" const char mss_separator[];
/* class Mss:
   This class actually doesn't do *anything* at all anymore.  Since of the
   new exit.c included from version 0.96.3 of MSS.  So why is it still
   included?  Well, maybe some compilers execute destructors *after* `atexit'
   functions, and therefore we include a new compiler specific option as
   of version 0.96.3, MSS_DTOR_AFTER_ATEXIT, and in that case this class
   will do some stuff... Uhh, what am I rambling about anyway, read the docs
   got damn it! ;) */
class Mss
{
public:
	Mss(void)
        {
#ifdef MSS_DEBUG
		mss_startup(); // Can't print to logfile without this!
		fprintf(mss_logfile, "DEBUG: In Mss Constructor\n");
#endif
#ifdef MSS_DTOR_AFTER_ATEXIT
		mss_print_log_info_on_exit = 0;
#endif
	}

	~Mss(void)
	{
		mss_check_if_initialized();
#ifdef MSS_DEBUG
		fprintf(mss_logfile, "DEBUG: In Mss Destructor\n");
#endif
#ifdef MSS_DTOR_AFTER_ATEXIT
		fputs(mss_separator, mss_logfile);
		fprintf(mss_logfile, "MSG: Listing info about allocated blocks at end(?) of program:\n");
		mss_print_log_info();
		fputs(mss_separator, mss_logfile);
#endif
		mss_deinitialize_if_restarted();
	}
};

/* MSS:
   This is the global object definition, it's never actually used since
   it only contains a constructor and a destructor. */
class Mss MSSGlobalObject;


/* operator new(size_t):
   This overloads the normal operator(s) new to use the ones of MSS specifying
   file-name and function-name as `unknown'.  This makes MSS catch all
   new's, even when `mss.h' was not included in the source file.
   (i.e. library functions using `new' and so on. */
void *operator new(size_t s)
{
	return (operator new(s, "unknown", "unknown", 0));
}

#ifdef MSS_DEFINE_NEW_DELETE_ARRAY
void *operator new[](size_t s)
{
	return (operator new[](s, "unknown", "unknown", 0));
}
#endif

/* operator new(size_t s, filename, line, function_name):
   This is the MSS `new' operators.  These actually only call mss_malloc()
   with the correct filename/line/functionname information. */
void *operator new(size_t s, const char *filename, const char *function, unsigned long line)
{
	return mss_malloc(s, MSS_BY_NEW, filename, function, line);
}

#ifdef MSS_DEFINE_NEW_DELETE_ARRAY
void *operator new[] (size_t s, const char *filename, const char *function, unsigned long line)
{
	return mss_malloc(s, MSS_BY_NEW_ARRAY, filename, function, line);
}
#endif

/* delete(void *):
   Overloads of the `delete' operators.  These uses DEL_XXXX_XXXX to specify
   line/filename/functionname information since these cannot be passed as
   arguments to the function. (see declaration of these for more info) */
void operator delete(void *p)
#ifdef MSS_USE_EXCEPTIONS
throw()
#endif /* !MSS_USE_EXCEPTIONS */
{
        mss_free(p, MSS_BY_DELETE, MSS_DEL_FILENAME, MSS_DEL_FUNCTION, MSS_DEL_LINE);
}

#ifdef MSS_DEFINE_NEW_DELETE_ARRAY
void operator delete[] (void *p)
#ifdef MSS_USE_EXCEPTIONS
throw()
#endif /* !MSS_USE_EXCEPTIONS */
{
        mss_free(p, MSS_BY_DELETE_ARRAY, MSS_DEL_FILENAME, MSS_DEL_FUNCTION, MSS_DEL_LINE);
}
#endif /* !MSS_DEFINE_NEW_DELETE_ARRAY */

