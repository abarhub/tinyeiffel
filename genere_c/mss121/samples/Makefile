# Makefile for the MSS test programs version 1.2
# Written by Peter Palotas in 1998
#
# This makefile is written for use with GNU Make, GNU Fileutils, and GCC,
# under Linux or DJGPP.
#
# There are a few rules in this makefile, which are listed here.
#
# make all:
# This will compile the test programs. The library must either be installed
# (`make install' in the main MSS directory) or be present in the directory
# it is placed by `make lib' in the main MSS directory. (lib/djgpp or
# lib/linux).
#
# make clean:
# This will remove all objectfiles.
#
# make cleanall:
# This will remove all rebuildable files.

# CC ofcourse should point to your compiler, preferrably gcc.
CC=gcc

# AR should point to your archiver. (Should be `ar')
AR=ar

# Compiler flags and stuff.
CFLAGS=-O2 -m486 -I.. -ansi -pedantic -DMSS -W -Wall
LFLAGS=-m486 -o
ARFLAGS=rcs

#Uncomment the line below if you're debugging MSS. (if you are not a
#developer, you	don't want to uncomment	this, 'cause it	won't help you
#anything.  (This is not for debugging YOUR program, but for debugging MSS!
#DEBUG=.

#If you	don't have a GCC with support for stabs	debugging information,
#get it! (Or you could replace -gstabs+	below with ex. -g)
DEBUGFLAGS=-DMSS_DEBUG -gstabs+

# The version of MSS
VERSION=1.1

# -------- target OS (djgpp or linux) --------
ifdef DJDIR
TARGET=djgpp
else
TARGET=linux
endif

ifdef DEBUG
DFLAGS=$(DEBUGFLAGS)
else
DFLAGS=
endif

ifeq ($(TARGET),djgpp)
# -------- djgpp specific stuff	-----------
LIBDIR=../lib/djgpp
EXE_SUFFIX=.exe
CXXLIB=-lstdcxx
OBJDIR=../objs/djgpp
else
ifeq ($(TARGET),linux)
LIBDIR=../lib/linux
EXE_SUFFIX=
CXXLIB=-lstdc++
OBJDIR=../objs/linux
else
.PHONY:	badtarget
badtarget:
	@echo Error: target operating system not properly set.
	@echo 	     try running `make TARGET=djgpp' or	`make TARGET=linux'
	@echo	     If you are using another operating system, you probably
	@echo	     need to tweak the makefile a whole lot.
	@echo	     Makefiles for other targets will be included in the
	@echo	     distribution if they are provided to us. (See the docs
	@echo	     for more info).
endif
endif


.PHONY:	all clean cleanall

all: test1_1$(EXE_SUFFIX) test1_2$(EXE_SUFFIX) test1_3$(EXE_SUFFIX) \
     test2_1$(EXE_SUFFIX) test3_1$(EXE_SUFFIX)
	@echo All done.

test1_1$(EXE_SUFFIX): $(OBJDIR)/test1_1.o
	$(CC) $(LFLAGS) test1_1$(EXE_SUFFIX) $(OBJDIR)/test1_1.o $(LIBDIR)/libmss.a $(CXXLIB)

test1_2$(EXE_SUFFIX): $(OBJDIR)/test1_2.o
	$(CC) $(LFLAGS) test1_2$(EXE_SUFFIX) $(OBJDIR)/test1_2.o $(LIBDIR)/libmss.a $(CXXLIB)

test1_3$(EXE_SUFFIX): $(OBJDIR)/test1_3.o
	$(CC) $(LFLAGS) test1_3$(EXE_SUFFIX) $(OBJDIR)/test1_3.o $(LIBDIR)/libmss.a $(CXXLIB)

test2_1$(EXE_SUFFIX): $(OBJDIR)/test2_1.o
	$(CC) $(LFLAGS) test2_1$(EXE_SUFFIX) $(OBJDIR)/test2_1.o $(LIBDIR)/libmss.a

test3_1$(EXE_SUFFIX): $(OBJDIR)/test3_1.o
	$(CC) $(LFLAGS) test3_1$(EXE_SUFFIX) $(OBJDIR)/test3_1.o $(LIBDIR)/libmss.a

$(OBJDIR)/test1_1.o: test1_1.cc
	$(CC) -c $(DFLAGS) $(CFLAGS) -o $(OBJDIR)/test1_1.o test1_1.cc

$(OBJDIR)/test1_2.o: test1_2.cc
	$(CC) -c $(DFLAGS) $(CFLAGS) -o $(OBJDIR)/test1_2.o test1_2.cc

$(OBJDIR)/test1_3.o: test1_3.cc
	$(CC) -c $(DFLAGS) $(CFLAGS) -o $(OBJDIR)/test1_3.o test1_3.cc

$(OBJDIR)/test2_1.o: test2_1.c
	$(CC) -c $(DFLAGS) $(CFLAGS) -o $(OBJDIR)/test2_1.o test2_1.c

$(OBJDIR)/test3_1.o: test3_1.c
	$(CC) -c $(DFLAGS) $(CFLAGS) -o $(OBJDIR)/test3_1.o test3_1.c

clean:
	@rm -f $(OBJDIR)/test1_1.o $(OBJDIR)/test1_2.o $(OBJDIR)/test1_3.o \
	       $(OBJDIR)/test2_1.o $(OBJDIR)/test3_1.o

cleanall: clean
	@rm -f test1_1$(EXE_SUFFIX) test1_2$(EXE_SUFFIX) test1_3$(EXE_SUFFIX) \
               test2_1$(EXE_SUFFIX) test3_1$(EXE_SUFFIX)

