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
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include "inifile.h"

int dcfinifile_stricmp(const char *s1, const char *s2)
{
	while (toupper(*s1) == toupper(*s2))
	{
	 	if (*s1 == '\0')
			return 0;
		s1++;
		s2++;
	};
	return *(unsigned const char *)(s1) - *(unsigned const char *)(s2);
}

static void destroy_item(void *item)
{
	if (((DCFIniFileItem *)item)->name != NULL)
		free(((DCFIniFileItem *)item)->name);

	if (((DCFIniFileItem *)item)->value != NULL)
		free(((DCFIniFileItem *)item)->value);

	free(item);
}

static int add_item(DCFIniFile *file, const char *name, const char *value)
{
	DCFIniFileItem *item = (DCFIniFileItem *)malloc(sizeof(DCFIniFileItem));
	if (item == NULL)
		return 0;

	item->name = (char *)malloc(strlen(name) + 1);
	if (item->name == NULL)
	{
	 	free(item);
		return 0;
	}

	item->value = (char *)malloc(strlen(value) + 1);
	if (item->value == NULL)
	{
	 	free(item->name);
		free(item);
		return 0;
	}

	strcpy(item->name, name);
	strcpy(item->value, value);
	if (dcflist_append(&file->list, item) == 0)
	{
	 	free(item->value);
		free(item->name);
		free(item);
		return 0;
	}
	return 1;
}

static char gn_name[256];
static const char *get_name(char *line, char **srcp)
{
	char *src, *dest;

	/* Strip any trailing newlines */
	src = line;
	while (*src != '\0')
	{
	 	if (*src == '\r' || *src == '\n')
			*src = '\0';
		src++;
	};

	/* Find name (if any) */
	dest = gn_name;
	src = line;

	/* Skip leading spaces */
	while (isspace(*src))
	{
		if (*src == '\0') /* Empty line */
			return NULL;
		src++;
	};

	/* Check if comment */
	if (*src == '#')
		return NULL;

	/* Read name */
	while (!isspace(*src) && *src != '=')
	{
		if (*src == '\0')
			return NULL; /* No value, i.e. comment */

		*dest++ = *src++;
	};
	*dest = '\0';
	if (srcp != NULL)
		*srcp = src;
	return gn_name;
}

static int add_line(DCFIniFile *file, char *line)
{
	const char *name;
 	char value[256];
	char *src, *dest;
	int in_quote = 0;

	name = get_name(line, &src);

	if (name == NULL)
		return 0;

	while (*src != '=') /* Move to the equal sign */
	{
		if (*src == '\0')
			return 0;
		src++;
	}
	src++;

	/* Skip spaces */
	while (isspace(*src))
	{
		if (*src == '\0')  /* No value */
			return 0;
		src++;
	};

	/* Read value (yes, spaces ARE allowed in value and also '#' signs).
	   Value may be quoted, but that is not neccessary.
	   The following escape characters are allowed:
	   \\ == backslash (\)
	   \n == newline
	   \r == return
	   \" == quote
	   \t == tab */
	dest = value;
	if (*src == '\"')
	{
		src++;
		in_quote = 1;
	}

	while (*src != '\0' && !(in_quote && *src == '\"'))
	{
		if (*src == '\\')
		{
		 	if (*(src + 1) == '\\')
			{
				src++;
			}
			else if (tolower(*(src + 1)) == 'n')
			{
			 	src++;
				*src = '\n';
			}
			else if (tolower(*(src + 1)) == 'r')
			{
				src++;
				*src = '\r';
			}
			else if (tolower(*(src + 1)) == '\"')
			{
				src++;
				*src = '\"';
			}
			else if (tolower(*(src + 1)) == 't')
			{
				src++;
				*src = '\t';
			}
		}
	 	*dest++ = *src++;
	};
	*dest = '\0';

	/* Skip trailing spaces in value */
	while (isspace(*dest))
	{
	 	*dest = '\0';
		dest--;
	};

	return add_item(file, name, value);
}

DCFIniFile *dcfinifile_open(const char *filename)
{
	FILE *fin;
	DCFIniFile *file;
	char buf[256];

	fin = fopen(filename, "rt");
	if (fin == NULL)
		return NULL;

	file = (DCFIniFile *)malloc(sizeof(DCFIniFile));
	if (file == NULL)
	{
		fclose(fin);
		return NULL;
	}

	file->filename = (char *)malloc(strlen(filename) + 1);
	if (file->filename == NULL)
	{
		fclose(fin);
		free(file);
		return NULL;
	}
	strcpy(file->filename, filename);

	file->has_changed = 0;

	dcflist_create(&file->list, &destroy_item);

	while (fgets(buf, 256, fin) != NULL)
	{
	 	add_line(file, buf);
	};

	fclose(fin);
	return file;
}

static void find_name_in_list(DCFIniFile *file, const char *name)
{
	DCFIniFileItem *item;
	dcflist_rewind(&file->list);
	while ((item = dcflist_get_item(&file->list)) != NULL)
	{
		if (dcfinifile_stricmp(item->name, name) == 0)
			return;
		dcflist_go_forward(&file->list);
	};
}

static int move_file(const char *src, const char *dest)
{
	FILE *fin, *fout;
	char buf[256];
	int items_read;
	fin = fopen(src, "rb");
	if (fin == NULL)
		return 0;
	fout = fopen(dest, "wb");
	if (fout == NULL)
	{
	 	fclose(fin);
		return 0;
	}

	while ( (items_read = fread(buf, 1, 256, fin)) != 0)
		fwrite(buf, 1, items_read, fout);

	fclose(fin);
	fclose(fout);
	remove(src);
	return 1;
}

int dcfinifile_close(DCFIniFile *file)
{
	FILE *fout, *fin;
	char buf[256];
	DCFIniFileItem *item;
	const char *tmp_filename = tmpnam(NULL);
	const char *tmp_p;


	if (file == NULL)
		return 0;

	/* Write file. This dumps any comments and so, and that pretty
	   much sucks I guess, but this will be fixed though. */
	if (file->has_changed)
	{
		fin = fopen(file->filename, "rt");
		if (fin == NULL)
			return 0;

		fout = fopen(tmp_filename, "wt");
		if (fout == NULL)
		{
			fclose(fout);
			return 0;
		}

		while (fgets(buf, 256, fin) != NULL)
		{
			tmp_p = get_name(buf, NULL);
			if (tmp_p != NULL)
				find_name_in_list(file, get_name(buf, NULL));
			else
				dcflist_go_end(&file->list);

			if ( (item = dcflist_get_item(&file->list)) != NULL)
			{
				fprintf(fout, "%s=%s\n", item->name, item->value);
				dcflist_remove(&file->list);
			}
			else
				fprintf(fout, "%s\n", buf);
		};
		dcflist_rewind(&file->list);
		while ( (item = dcflist_get_item(&file->list)) != NULL)
		{
		 	fprintf(fout, "%s=%s\n", item->name, item->value);
			dcflist_go_forward(&file->list);
		};

		fclose(fin);
		fclose(fout);
		move_file(tmp_filename, file->filename);
	}
	dcflist_destroy(&file->list);
	free(file->filename);
	return 1;
}

const char *dcfinifile_get_value(DCFIniFile *file, const char *name, const char *default_value)
{
	DCFIniFileItem *item;
 	dcflist_rewind(&file->list);
	while ( (item = (DCFIniFileItem *)dcflist_get_item(&file->list)) != NULL)
	{
		if (dcfinifile_stricmp(item->name, name) == 0)
		{
		 	return item->value;
		}
		dcflist_go_forward(&file->list);
	};

	return default_value;
}

int dcfinifile_get_int_value(DCFIniFile *file, const char *name, int default_value)
{
	int value = default_value;
	const char *valstr = dcfinifile_get_value(file, name, NULL);

	if (valstr == NULL)
		return value;

	if (dcfinifile_stricmp(valstr, "True") == 0 || dcfinifile_stricmp(valstr, "Yes") == 0)
		return 1;

	if (dcfinifile_stricmp(valstr, "False") == 0 || dcfinifile_stricmp(valstr, "No") == 0)
		return 0;

	sscanf(valstr, "%i", &value);
	return value;
}

int dcfinifile_set_value(DCFIniFile *file, const char *name, const char *value)
{
	DCFIniFileItem *item;
	int success;

	dcflist_rewind(&file->list);
	while ( (item = (DCFIniFileItem *)dcflist_get_item(&file->list)) != NULL)
	{
	 	if (dcfinifile_stricmp(item->name, name) == 0)
		{
	  		free(item->value);
			item->value = (char *)malloc(strlen(value) + 1);
			if (item->value == NULL)
			{
				return 0;
			}
			strcpy(item->value, value);
			strcpy(item->name, name);
			file->has_changed = 1;
			return 1;
		}
		dcflist_go_forward(&file->list);
	};
	success = add_item(file, name, value);
	if (success)
		file->has_changed = 1;
	return success;
}

