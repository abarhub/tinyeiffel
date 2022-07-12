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
#include <string.h>
#include "internal.h"
#include "mss.h"

/* This will be increased for every mss_disable_threading(), and it won't be
   enabled again until mss_threads_disabled == 0. (Decreased every
   mss_enable_threading() ofcourse. */
static int mss_threads_disabled = 0;

void mss_disable_threading(void)
{
	mss_threads_disabled++;
	if (mss_threads_disabled == 1)
	{
		MSS_DISABLE_THREADING_FUNC;
	}
}

void mss_enable_threading(void)
{
	if (mss_threads_disabled == 0)
		fprintf(stderr, "MSS:  Error in mss_enable_threading(). Threads enabled more times than\n"
 				"      they were disabled. Please submit a bug report.\n");
	else
		mss_threads_disabled--;

	if (mss_threads_disabled == 0)
	{
		MSS_ENABLE_THREADING_FUNC;
	}
}

/* word_wrap():
   Internal helper function that word-wraps a string to 78 characters, so
   that a newline and strlen(label) spaces are inserted in every newline.
   (Maximum length of final string should be 1024 characters).
   (Okay, I know, it's crappy code, but it works anyway. Don't feel like
    improving quality right now, since the main issue is that the function
    works, no!?). */
static char ww_str[1024];

char *mss_word_wrap(const char *label, const char *str)
{
	int label_len = 0;
	int position = 0;
	int last_space = 0;
	const char *src, *src_temp;
	char *dest;

	if (!MSS_DO_WORD_WRAP)
	{
		if (strlen(label) > 0)
		{
	     		strcpy(ww_str, label);
			strcat(ww_str, ": ");
			strcat(ww_str, str);
		}
		else
			strcpy(ww_str, str);

		return ww_str;
	}

	/* Now we start word wrapping the text. */
	if (label != NULL)
	{
		label_len = strlen(label) + 2;
		if (strlen(label) > 0)
		{
			strcpy(ww_str, label);
			strcat(ww_str, ": ");
		}
		else
			ww_str[0] = '\0';
	}
	else
		label_len = 0;

	position = label_len;
	src = str;
	dest = ww_str + label_len;

	while (*src != '\0')
	{
		position = label_len;
		last_space = -1;
		src_temp = src;
		while (position < 77)
		{
		  	if (*src == ' ')
				last_space = position;

			if (*src == '\0')
			{
				last_space = position;
				break;
			}
			src++;
			position++;
		};
		src = src_temp;
		position = last_space;
		if (last_space == -1)
			last_space = 77;

		position = label_len;
		while (position <= last_space)
		{
		 	*dest++ = *src++;
			position++;
		};
		if (*(src - 1) == '\0')
			return ww_str;

		*dest++ = '\n';

		position = 0;
		while (position < label_len)
		{
			*dest++ = ' ';
			position++;
		};
	};
	*dest = '\0';


	return ww_str;
}

/* mss_abort:
   Internal helper function, which will abort program execution (as fast as
   possible), and print a message. Probably only called by mss_warn(), when
   `ExitOnWarning == True'. */
void mss_abort(void)
{
	mss_disable_threading();
	fprintf(mss_logfile, "---------------------------------------------------\n");
	fprintf(mss_logfile, "MSS IS ABORTING EXECUTION DUE TO THE WARNING ABOVE.\n");
	fprintf(mss_logfile, "---------------------------------------------------\n");
	mss_enable_threading(); /* Why enable threading here!? */

	/* Deinitialize mss, just to make sure, because we have to break out
	   of the nested functions which may have called check_if_initialized()
	   a number of times, hence fooling any function that mss is initialized
	   while its not.  Two seconds ago this made sense, now it doesn't, it's
	   03:00 at night, maybe I should just go to bed and debug tomorrow, huh!?

	   Hmm... This solved the bug though... Right now I don't know why, because
	   uhh... Geez... I just can't think right now... Goodnight! */
	mss_forced_deinitialize_if_restarted();
	exit(255);
}

