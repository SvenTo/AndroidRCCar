/**
 * This file is part of the EDA - The Simple Service Gateway, project.
 * Copyright (C) 2000, Utilator AB, http://www.utilator.com
 * Copyright (C) 2000-2001 ake Hedman, Eurosource, http://www.eurosource.se
 * Project home: eda.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or (at your option) any later version.
 * 
 * This program  is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EDA. see the file COPYING.  If not, write to
 * the Free Software Foundation, 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Authors: the EDA Development Team.  Its members are listed in a
 * file named AUTHORS, in the root directory of this distribution.
*/ 
/**
 * Lookup Table:
 * 
 *  libFLAC - Free Lossless Audio Codec library
 * Copyright (C) 2000,2001,2002,2003  Josh Coalson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

// 8 bit crc calculation, one byte at a time.

#include "CRC8.h"

// Constructor
CRC8::CRC8( void ) 
{
  init();
}

// Destructor
CRC8::~CRC8( void ) 
{
  // Null
}

// Methods
void CRC8::calc ( unsigned char byte_in ) 
{
  	m_byte_count++; 	//bump the count of bytes that we have processed
                        // so far...
  	m_crc = crc_table[ m_crc ^ byte_in ];
}

void CRC8::init ( void ) 
{
  	m_crc = 0;
  	m_byte_count = 0;
}

unsigned char CRC8::get ( void ) 
{
  	return m_crc;
}

int CRC8::return_byte_count( void ) 
{
  	return m_byte_count;
} 

// author: sven
char CRC8::from_array( unsigned char buffer[], unsigned int len ) {
		init();
		for(int i = 0; i < len; i++) {
			calc(buffer[i]);
		}
		return get();
}
