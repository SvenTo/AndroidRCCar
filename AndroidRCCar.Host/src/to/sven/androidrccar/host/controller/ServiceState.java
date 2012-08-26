/*******************************************************************************
 * Copyright (C) 2012 Sven Nobis
 * 
 * This file is part of AndroidRCCar (http://androidrccar.sven.to)
 * 
 * AndroidRCCar is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 ******************************************************************************/
package to.sven.androidrccar.host.controller;

/**
 * States of the {@link HostService}
 * @author sven
 *
 */
public enum ServiceState {
	/**
	 * No connection to accessory and other phone.
	 */
	NOT_CONNECTED,
	/**
	 * Connected with accessory, waiting until the other phone connects.
	 */
	WAIT_FOR_OTHER_PHONE,
	/**
	 * Connected to accessory and other phone.
	 */
	CONNECTED,
	/**
	 * Like {@link #WAIT_FOR_OTHER_PHONE}, but a phone was connected before.
	 */
	CONNECTION_LOST,
	
	/**
	 * Some error occurred.
	 */
	ERROR
}
