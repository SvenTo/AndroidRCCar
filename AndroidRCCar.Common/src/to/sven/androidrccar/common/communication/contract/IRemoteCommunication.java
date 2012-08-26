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
package to.sven.androidrccar.common.communication.contract;

import java.io.Closeable;
import java.io.IOException;

import to.sven.androidrccar.common.communication.contract.IRemoteCommunicationListener;
import to.sven.androidrccar.common.communication.model.Message;

/**
 * This interface handles the communication with the other Android device (Host or Client).
 * It converts also 
 * @author sven
 *
 */
public interface IRemoteCommunication extends Closeable {

	/**
	 * Start the {@link Thread} that wait for messages from the Host/Client.
	 */
	public abstract void startMessageListener();

	/**
	 * Closes the connection to the partner and
	 * if the message listener {@link Thread} is running, it will be stopped, too.
	 * The {@link IRemoteCommunicationListener#connectionProblem} callback will not executed.
	 */
	@Override
	public abstract void close() throws IOException;

	/**
	 * Encodes the {@link Message} as JSON and sends it to the partner.
	 * @param message {@link Message} to Encode
	 * @throws IOException Thrown if sending failed
	 */
	public abstract void sendMessage(Message message) throws IOException;

}
