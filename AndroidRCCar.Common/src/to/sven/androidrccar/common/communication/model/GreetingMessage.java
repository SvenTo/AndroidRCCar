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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * First Message.
 * It's send to Client and contains protocol version of the host and a salt for authentication.
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class GreetingMessage extends Message {
	
	/**
	 * Creates a new instance of {@link GreetingMessage}.
	 * @param version {@link #version}
	 * @param authSalt {@link #authSalt}
	 */
	@JsonCreator
	public GreetingMessage(@JsonProperty("version") int version, @JsonProperty("authSalt") String authSalt) {
		this.version = version;
		this.authSalt= authSalt;
	}
	
	/**
	 * Contains protocol version of the host.
	 */
	public final int version;
	
	/**
	 * Salt for authentication. It will be used to encrypt the password before sending it to the host.
	 * @see AuthenticationMessage#passwordHash
	 */
	public final String authSalt;
}
