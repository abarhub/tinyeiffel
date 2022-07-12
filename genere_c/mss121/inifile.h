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

#ifndef __dcfinifile_h__
#define __dcfinifile_h__

#include "list.h"

typedef struct DCFIniFileItem
{
 	char *name;
	char *value;
} DCFIniFileItem;

typedef struct DCFIniFile
{
	char *filename;
	int has_changed;
	DCFList list;
} DCFIniFile;

/* dcfinifile_open:
   This function will open the specified file, and return a pointer to it.
   If the file couldn't be opened, NULL will be returned, very much like
   fopen().
   Actually this function reads the entire file in, and stores it in an
   internal doubly linked list, specific for that file. The syntax for
   the file is very simple:

   # This is a comment
   variable=value
   variable2 = Yet another value. */
DCFIniFile *dcfinifile_open(const char *filename);

/* dcfinifile_get_value:
   This function will return the value that was assigned to the variable
   specified by `name' in the ini-file, or if that variable name was not
   found it returns `default_value'. */
const char *dcfinifile_get_value(DCFIniFile *file, const char *name, const char *default_value);

/* dcfinifile_get_bool_value:
   This function will return if value equals "false" and 1 if value equals
   "true". Otherwise the integer number represented by value will be returned.
   (if variable does not exist default_value will be returned).
   Hence if variable is any non-number string not "true" 0 will be returned. */
int dcfinifile_get_int_value(DCFIniFile *file, const char *name, int default_value);

/* dcfinifile_set_value:
   This function will change the variable if it exists, or add it if it does
   not exist. */
int dcfinifile_set_value(DCFIniFile *file, const char *name, const char *value);

/* dcfinifile_close:
   This file will write the contents of the specified file back to the
   original filename as specified to dcfinifile_open if any items were
   changed or added, and it also frees all memory allocated by
   dcfinifile_open.
   If you don't want the contents to be written to the file even if the
   have changed, set file->has_changed to zero. */
int dcfinifile_close(DCFIniFile *file);

/* dcfinifile_stricmp:
   Same as strcmp() but ignore case. */
int dcfinifile_stricmp(const char *s1, const char *s2);

#endif /* !__dcfinifile_h__ */

