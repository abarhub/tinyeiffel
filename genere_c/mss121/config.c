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
#include "inifile.h"

/* Below are some default values for the configuration, all set by
   mss_get_configuration(). */
static const char MSS_DEFAULT_LOCAL_CONFIG_FILENAME[] = "mss.cfg";
static const char MSS_DEFAULT_LOG_FILENAME[] = "mss.log";
static const char MSS_DEFAULT_SLOG_FILENAME[] = "slog.mss";

DCFList mss_config_error;

MssConfigOption mss_config_options[] =
{
	{ "LogFileName", MSS_CONFIG_TYPE_STRING, MSS_LOG_FILENAME, MSS_CONFIG_WHERE_DEFAULT },
	{ "GenerateSpecialLog", MSS_CONFIG_TYPE_BOOL, &MSS_DO_SPECIAL_LOG, MSS_CONFIG_WHERE_DEFAULT },
	{ "SpecialLogFileName", MSS_CONFIG_TYPE_STRING, MSS_SLOG_FILENAME, MSS_CONFIG_WHERE_DEFAULT },
	{ "LogToSTDOUT", MSS_CONFIG_TYPE_BOOL, &MSS_DO_LOG_TO_STDOUT, MSS_CONFIG_WHERE_DEFAULT },
	{ "LogToSTDERR", MSS_CONFIG_TYPE_BOOL, &MSS_DO_LOG_TO_STDERR, MSS_CONFIG_WHERE_DEFAULT },
	{ "ShowLogs", MSS_CONFIG_TYPE_BOOL, &MSS_DO_SHOW_LOGS, MSS_CONFIG_WHERE_DEFAULT },
	{ "WatchLimits", MSS_CONFIG_TYPE_BOOL, &MSS_DO_WATCH_LIMITS, MSS_CONFIG_WHERE_DEFAULT },
	{ "WatchSize", MSS_CONFIG_TYPE_INT, &MSS_WATCH_LIMITS_SIZE, MSS_CONFIG_WHERE_DEFAULT },
	{ "WatchValue", MSS_CONFIG_TYPE_HEX, &MSS_WATCH_LIMITS_VALUE, MSS_CONFIG_WHERE_DEFAULT },
	{ "CheckOnDealloc", MSS_CONFIG_TYPE_BOOL, &MSS_DO_CHECK_ON_DEALLOC, MSS_CONFIG_WHERE_DEFAULT },
	{ "CheckAllOnAlloc", MSS_CONFIG_TYPE_BOOL, &MSS_DO_CHECK_ALL_ON_ALLOC, MSS_CONFIG_WHERE_DEFAULT },
	{ "FillMemOnAlloc", MSS_CONFIG_TYPE_BOOL, &MSS_DO_FILL_MEM_ON_ALLOC, MSS_CONFIG_WHERE_DEFAULT },
	{ "FillMemOnAllocValue", MSS_CONFIG_TYPE_HEX, &MSS_FILL_MEM_ON_ALLOC_VALUE, MSS_CONFIG_WHERE_DEFAULT },
	{ "FillMemOnDeAlloc", MSS_CONFIG_TYPE_BOOL, &MSS_DO_FILL_MEM_ON_DEALLOC, MSS_CONFIG_WHERE_DEFAULT },
	{ "FillMemOnDeAllocValue", MSS_CONFIG_TYPE_HEX, &MSS_FILL_MEM_ON_DEALLOC_VALUE, MSS_CONFIG_WHERE_DEFAULT },
	{ "ExitOnWarning", MSS_CONFIG_TYPE_BOOL, &MSS_DO_EXIT_ON_WARNING, MSS_CONFIG_WHERE_DEFAULT },
	{ "FalseAllocFails", MSS_CONFIG_TYPE_INT, &MSS_FALSE_ALLOC_FAIL_PERCENTAGE, MSS_CONFIG_WHERE_DEFAULT },
	{ "ExtraNewline", MSS_CONFIG_TYPE_BOOL, &MSS_DO_EXTRA_NEWLINE, MSS_CONFIG_WHERE_DEFAULT },
	{ "WordWrap", MSS_CONFIG_TYPE_BOOL, &MSS_DO_WORD_WRAP, MSS_CONFIG_WHERE_DEFAULT },
        { "WarnOnAllNULLDeallocs", MSS_CONFIG_TYPE_BOOL, &MSS_DO_ALWAYS_WARN_ON_NULL_DEALLOC, MSS_CONFIG_WHERE_DEFAULT },
	{ "ZeroLenAllocReturnNULL", MSS_CONFIG_TYPE_BOOL, &MSS_DO_RETURN_NULL_ON_ZERO_LENGTH_ALLOC, MSS_CONFIG_WHERE_DEFAULT },
	{ NULL, MSS_CONFIG_TYPE_END, NULL, MSS_CONFIG_WHERE_DEFAULT }
};

/* These are all the config variables.
   I don't like having the arrays fixed at a size of 256 bytes, but there
   is not other way as I see it, since allcating the memory dynamically would
   require freeing it at program exit, and this is hard, because we will never
   know for sure when the program actually exits. */
char 		MSS_LOCAL_CONFIG_FILENAME[256];
char 		MSS_LOG_FILENAME[256];
char 		MSS_SLOG_FILENAME[256];
int 		MSS_DO_SPECIAL_LOG = 0;
int 		MSS_DO_LOG_TO_STDERR = 0;
int 		MSS_DO_LOG_TO_STDOUT = 0;
int 		MSS_DO_WATCH_LIMITS = 1;
unsigned int 	MSS_WATCH_LIMITS_SIZE = 32;
unsigned char 	MSS_WATCH_LIMITS_VALUE = 0xA8;
int 		MSS_DO_CHECK_ON_DEALLOC = 1;
int 		MSS_DO_FILL_MEM_ON_ALLOC = 1;
unsigned char 	MSS_FILL_MEM_ON_ALLOC_VALUE = 0x98;
int 		MSS_DO_FILL_MEM_ON_DEALLOC = 1;
unsigned char 	MSS_FILL_MEM_ON_DEALLOC_VALUE = 0x86;
int 		MSS_DO_EXIT_ON_WARNING = 0;
int 		MSS_FALSE_ALLOC_FAIL_PERCENTAGE = 0;
int 		MSS_DO_CHECK_ALL_ON_ALLOC = 0;
char 		MSS_DO_EXTRA_NEWLINE = 1;
int 		MSS_DO_WORD_WRAP = 1;
int 		MSS_DO_ALWAYS_WARN_ON_NULL_DEALLOC = 1;
int		MSS_DO_RETURN_NULL_ON_ZERO_LENGTH_ALLOC = 1;
int		MSS_CFG_STATUS = 0;
int		MSS_DO_SHOW_LOGS = 1;

/* read_config:
   This function will read the config-variables from the specified DCFIniFile
   if found, and store them in the config-variables declared and initialized
   above. The DCFIniFile must be opened and valid. This function will not
   close the DCFIniFile, so this must be done by the caller.
   (static helper function for mss_get_configuration).
   All resources freed upon exit. */
static void read_config(DCFIniFile *inifile, int config_where)
{
	int i = 0;
	char *s;
	char tmpstr[256];
	DCFIniFileItem *item;

	while (mss_config_options[i].type != MSS_CONFIG_TYPE_END)
	{
		switch (mss_config_options[i].type)
		{
			case MSS_CONFIG_TYPE_BOOL:
			case MSS_CONFIG_TYPE_INT:
			case MSS_CONFIG_TYPE_HEX:
				s = (char *)dcfinifile_get_value(inifile, mss_config_options[i].name, NULL);
				if (s != NULL)
				{
					*((int *)mss_config_options[i].value) = dcfinifile_get_int_value(inifile, mss_config_options[i].name, *((int *)mss_config_options[i].value));
					mss_config_options[i].where = config_where;
				}
				break;
			case MSS_CONFIG_TYPE_STRING:
				s = (char *)dcfinifile_get_value(inifile, mss_config_options[i].name, NULL);
				if (s != NULL)
				{
					strcpy((char *)mss_config_options[i].value, dcfinifile_get_value(inifile, mss_config_options[i].name, (char *)mss_config_options[i].value));
					mss_config_options[i].where = config_where;
				}
				break;
			default:
				fprintf(stderr, "Error in MSS; read_config() encountered an unknown MSS_CONFIG_TYPE in mss_config_options[].\n");
				exit(EXIT_FAILURE);
		};
		i++;
	};

	/* Check for unknown configuration options, and add errors to
	   mss_config_error. */
	dcflist_rewind(&inifile->list);
	while ( (item = dcflist_get_item(&inifile->list)) != NULL)
	{
		int error = 1;
		i = 0;
		while (mss_config_options[i].type != MSS_CONFIG_TYPE_END)
		{
			if (dcfinifile_stricmp(mss_config_options[i].name, item->name) == 0 || (config_where == MSS_CONFIG_WHERE_GLOBAL && dcfinifile_stricmp(item->name, "LocalCFGFile") == 0))
			{
			 	error = 0;
				break;
			}
			i++;
		};
		if (error)
		{
			sprintf(tmpstr, "Error in %s; Unknown option `%s'", config_where == MSS_CONFIG_WHERE_LOCAL ? "LOCAL configurationfile" : "GLOBAL configurationfile", item->name);
			s = (char *)mss_malloc_wec(strlen(tmpstr) + 1);
			strcpy(s, tmpstr);
			dcflist_append(&mss_config_error, s);
		}
		dcflist_go_forward(&inifile->list);
	};
}

/* get_configuration:
   Read the configuration, checking first for global then local files.
   Using read_config() to read the configuration from the files (if any
   was found).
   All resources freed upon exit. */
void mss_get_configuration(void)
{
	DCFIniFile *global_inifile = NULL;
	DCFIniFile *local_inifile = NULL;
	const char *global_inifile_name;

	/* First set up the default values for the string variables */
	strcpy(MSS_LOG_FILENAME, MSS_DEFAULT_LOG_FILENAME);
	strcpy(MSS_SLOG_FILENAME, MSS_DEFAULT_SLOG_FILENAME);
	strcpy(MSS_LOCAL_CONFIG_FILENAME, MSS_DEFAULT_LOCAL_CONFIG_FILENAME);
	dcflist_create(&mss_config_error, &free);

	/* Check if global configuration filename was found in environment
	   variable specified by MSS_CONFIG_ENVVAR, and in that case
 	   read that file into memory. */
	global_inifile_name = getenv(MSS_CONFIG_ENVVAR);
	if (global_inifile_name != NULL)
	{
		global_inifile = dcfinifile_open(global_inifile_name);
	}
	else
	{
	 	MSS_CFG_STATUS |= MSS_CFG_STATUS_NO_ENVVAR;
	}

	/* If global configuration file was found read the filename of the
	   local configuration file (if it can be found, otherwise use
	   default value). */
	if (global_inifile != NULL)
	{
		strcpy(MSS_LOCAL_CONFIG_FILENAME, dcfinifile_get_value(global_inifile, "LocalCFGFile", MSS_DEFAULT_LOCAL_CONFIG_FILENAME));
	}
	else
	{
		MSS_CFG_STATUS |= MSS_CFG_STATUS_NO_GLOBAL_FILE;
	}

	/* Try to open the   local configuration file. */
	local_inifile = dcfinifile_open(MSS_LOCAL_CONFIG_FILENAME);

	/* If the global configurationfile was found (and read), read the
	   configuration options available from that file, overriding the
	   default values, closing the file afterwards. */
	if (global_inifile != NULL)
	{
		read_config(global_inifile, MSS_CONFIG_WHERE_GLOBAL);
		dcfinifile_close(global_inifile);
	}

	/* If the local configurationfile was found (and read), read the
	   configuration options available from that file, overriding any
	   options read from the global file, and the default values.
	   Closing the file afterwards. */
	if (local_inifile != NULL)
	{
		read_config(local_inifile, MSS_CONFIG_WHERE_LOCAL);
		dcfinifile_close(local_inifile);
	}
	else
	{
	 	MSS_CFG_STATUS |= MSS_CFG_STATUS_NO_LOCAL_FILE;
	}
}

