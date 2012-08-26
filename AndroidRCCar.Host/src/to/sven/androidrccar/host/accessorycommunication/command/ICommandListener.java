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
package to.sven.androidrccar.host.accessorycommunication.command;

import java.io.InputStream;
import java.io.OutputStream;

import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunicationListener;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;

/**
 * This interface is used to connect the {@link AccessoryCommunication} with the implementors of {@link AbstractCommand}.
 * The commands implementations can streams for the communication with the µController
 * and call the {@link IAccessoryCommunicationListener} when a response is received.
 * The {@link AccessoryCommunication} does thereby post back handling to the main thread (UI thread).
 * @author sven
 *
 */
public interface ICommandListener extends IAccessoryCommunicationListener {
	
	/**
	 * The {@link OutputStream} to send message to the µController.
	 * @return {@link OutputStream}
	 */
	InputStream getInputStream();
	
	/**
	 * The {@link InputStream} to received messages from the µController.  
	 * @return {@link InputStream}
	 */
	OutputStream getOutputStream();

	/**
	 * Set the features that supported by the car.
	 * @param features {@link CarFeatures}
	 */
	void setCarFeatures(CarFeatures features);

	/**
	 * Get the features that supported by the car.
	 * @return {@link CarFeatures}
	 */
	CarFeatures getCarFeatures();
	
	/**
	 * Post a command to the communication message queue.
	 * @param command The command that should send to the µController
	 */
	void postCommand(AbstractCommand command); 
}
