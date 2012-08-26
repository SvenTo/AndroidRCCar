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
package to.sven.androidrccar.common.exception;

import java.io.IOException;

import to.sven.androidrccar.common.communication.model.Message;

/**
 * This {@link Exception} is thrown, when the {@link Message}-Type is not supported in the current state.
 * @author sven
 *
 */
public class UnsupportedMessageException extends IOException {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = 8597803411423812786L;

	/**
	 * @see IOException#IOException(String, Throwable)
	 */
	public UnsupportedMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see IOException#IOException(String)
	 */
	public UnsupportedMessageException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @see IOException#IOException(Throwable)
	 */
	public UnsupportedMessageException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Creates a new {@link UnsupportedMessageException}.
	 * @param unsupportedMessage The {@link Message} that is not supported.
	 */
	public UnsupportedMessageException(Message unsupportedMessage) {
		super("The message type " + unsupportedMessage.getClass().toString() + " is not supported in this state.");
	}

}
