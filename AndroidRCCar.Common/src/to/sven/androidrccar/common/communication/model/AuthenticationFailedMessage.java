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
package to.sven.androidrccar.common.communication.model;

import to.sven.androidrccar.common.communication.model.AuthenticationFailedMessage;
import to.sven.androidrccar.common.communication.model.Message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Message is send to the client, if the authentication failed.
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class AuthenticationFailedMessage extends Message {
	
	/**
	 * Creates a new instance of {@link AuthenticationFailedMessage}.
	 * @param reason {@link #reason}
	 */
	@JsonCreator
	public AuthenticationFailedMessage(@JsonProperty("reason") String reason) {
		this.reason = reason;
	}
	
	/**
	 * Human readable message with the reason. Currently only password is wrong.
	 */
	public final String reason;
}
