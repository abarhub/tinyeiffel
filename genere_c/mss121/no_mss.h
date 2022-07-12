/*  MSS -- Memory Supervision System version 1.2
 *  Copyright (C) 2001  Juan Jesus Alcolea Picazo and Peter Palotas
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
 *    Written by Laurynas Biveinis, lauras@softhome.net
 *
 */

/* If MSS is not defined, we do not use MSS either ;) */
#ifdef MSS

/* Undefine MSS for now, so user can isolate offending code segments.
 * However, define some other internal symbol so the next include of mss.h
 * could redefine MSS
 */
#undef MSS
#undef __MSS_H_REINCLUDE
#define __MSS_H_REINCLUDE

#ifdef __cplusplus
#undef new
#undef delete
#endif /* !__cplusplus */

#undef malloc
#undef xmalloc
#undef free
#undef realloc
#undef xrealloc
#undef calloc
#undef xfree
#undef cfree
#undef strdup

#endif /* MSS */

