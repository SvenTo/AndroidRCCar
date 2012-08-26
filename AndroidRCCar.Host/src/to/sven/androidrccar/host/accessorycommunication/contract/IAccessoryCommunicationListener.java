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

import to.sven.androidrccar.host.accessorycommunication.exception.AccessoryConnectionProblemException;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;

/**
 * This interface is used to tell the implementor when something happens in the {@link AccessoryCommunication}.
 * @author sven
 *
 */
public interface IAccessoryCommunicationListener {
	
	/**
	 * The µController runs an unsupported protocol version.
	 * @param hostVersion Protocol version supported by this application
	 * @param microControllerVersion Protocol version supported by the µController.
	 */
	void protocolVersionNotMatch(short hostVersion, short microControllerVersion);
	
	/**
	 * The connection has been successfully initialized.
	 * This means that the protocol version on µController matches with
	 * this version and we received the features. 
	 */
	void connectionInitialized();
	
	/**
	 * The µController has send the battery state.
	 * @param chargingLevel Battery power level in percent (0-100%).
	 */
	void batteryStateReceived(float chargingLevel);
	
	/**
	 * The µController does not accept commands anymore,
	 * because the battery is near empty.
	 */
	void batteryNearEmpty();
	
	/**
	 * Something went wrong on the µController, so it don't works properly.
	 * @param message Maybe a information about, what went wrong.
	 */
	void errorReceived(String message);
	
	/**
	 * The is a connection problem with the µController.
	 * @param ex The Problem.
	 */
	void connectionProblem(AccessoryConnectionProblemException ex);
}
