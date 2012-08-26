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

// TODO: Test thiz?

/**
 * This {@link Exception} is thrown, when a {@link Message} contains invalid data.
 * @author sven
 *
 */
public class InvalidMessageException extends IOException {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = 9007008839627983700L;

	/**
	 * @see IOException#IOException(String, Throwable)
	 */
	public InvalidMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see IOException#IOException(String)
	 */
	public InvalidMessageException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @see IOException#IOException(Throwable)
	 */
	public InvalidMessageException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Creates a new {@link InvalidMessageException}.
	 * @param invalidMessage The message that was invalid.
	 * @param invalidMemberName The member of the message that contains an invalid value. 
	 * @param value The invalid value.
	 */
	public InvalidMessageException(Message invalidMessage,
								   String invalidMemberName, Object value) {
		super("A message of type " + invalidMessage.getClass()
												   .toString() + 
			  " contains a invalid value ("+value.toString()+") in member "+invalidMemberName+".");
	}

}
