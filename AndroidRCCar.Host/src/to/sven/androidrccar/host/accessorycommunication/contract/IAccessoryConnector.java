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
package to.sven.androidrccar.host.accessorycommunication.contract;


/**
 * Interface for initialization of the connection to the accessory.
 * @author sven
 */
public interface IAccessoryConnector {
	
	/**
	 * Performs final cleanups:
	 * - Closes connection to the accessory (, if there is one).
	 * - Unregisters receivers 
	 * - Cancel pending intents. 
	 */
	void close();
	
	/**
	 * Check if any accessory is connected to the phone at the moment.
	 * @return True, if an accessory is connected.
	 */
	boolean accessoryConnected();
	
	/**
	 * Opens the accessory if there is any.
	 * If the application has not the permission to open it,
	 * the permission will be requested.
	 */
	void tryOpenAccessory();
}
