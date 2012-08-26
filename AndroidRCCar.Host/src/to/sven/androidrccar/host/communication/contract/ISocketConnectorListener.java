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
package to.sven.androidrccar.host.communication.contract;

import java.net.Socket;

import to.sven.androidrccar.host.communication.impl.SocketConnector;

/**
 * A listener interface for the {@link SocketConnector} 
 * to report, that a somebody has connected
 * or an error occurred.
 * @author sven
 *
 */
public interface ISocketConnectorListener {
	/**
	 * Somebody has connected
	 * @param socket The socket to the "somebody".
	 */
	void connectionEstablished(Socket socket);
	
	/**
	 * An error occurred.
	 * @param resId A string resource Id to a error message.
	 * @param args Some objects, that will be formatted with the string. 
	 */
	void error(int resId, Object... args);
}
