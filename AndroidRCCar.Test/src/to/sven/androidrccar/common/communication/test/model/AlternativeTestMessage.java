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
package to.sven.androidrccar.common.communication.test.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import to.sven.androidrccar.common.communication.model.Message;

/**
 * Another {@link Message} that is only used for the purpose of testing. 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class AlternativeTestMessage extends Message {
	
	/**
	 * Creates a new {@link AlternativeTestMessage}.
	 * @param someInt Some integer for the purpose of testing.
	 */
	@JsonCreator
	public AlternativeTestMessage(@JsonProperty("someInt") int someInt) {
		this.someInt = someInt;
	}
	
	/**
	 * Some integer for the purpose of testing.
	 */
	public final int someInt;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + someInt;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlternativeTestMessage other = (AlternativeTestMessage) obj;
		if (someInt != other.someInt)
			return false;
		return true;
	}
}
