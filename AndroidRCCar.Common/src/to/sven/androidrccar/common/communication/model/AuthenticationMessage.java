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

import to.sven.androidrccar.common.communication.model.AuthenticationMessage;
import to.sven.androidrccar.common.communication.model.GreetingMessage;
import to.sven.androidrccar.common.communication.model.Message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This message try to authenticate the Client on the Host. It's send by the Client.
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class AuthenticationMessage extends Message {
	
	/**
	 * Creates a new instance of {@link AuthenticationMessage}.
	 * @param passwordHash {@link #passwordHash}
	 */
	public AuthenticationMessage(@JsonProperty("passwordHash") String passwordHash) {
		this.passwordHash = passwordHash; 
	}
	
	/**
	 * The password combined with the salt from the server and encrypt with SHA-1.
	 * This should ensure that this hash couldn't reused for the next connect by an attacker.
	 * @see GreetingMessage#authSalt
	 */
	public final String passwordHash;
}
