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
package to.sven.androidrccar.host.accessorycommunication.exception;

import java.io.IOException;

import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunicationListener;

/**
 * This Exception is used to tell the {@link IAccessoryCommunicationListener} that a problem occurred.
 * @author sven
 *
 */
public class AccessoryConnectionProblemException extends IOException {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = -4923883899559810787L;

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public AccessoryConnectionProblemException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public AccessoryConnectionProblemException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public AccessoryConnectionProblemException(Throwable cause) {
		super(cause);
	}
}
