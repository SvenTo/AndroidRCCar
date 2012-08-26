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

import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.communication.model.TurnCarMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Message is send to Host:
 * Change the direction of the car or better
 * change the rotation of the "steering wheel".
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class TurnCarMessage extends Message {
	
	/**
	 * Creates a new instance of {@link TurnCarMessage}.
	 * @param rotation {@link #rotation}
	 */
	@JsonCreator
	public TurnCarMessage(@JsonProperty("rotation") float rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Rotation of the "steering wheel":
	 * -1.0f: maximum to the left
	 * 0: no rotation (drive forward)
	 * +1.0f: maximum to the right
	 * (How fast the car turns depends on car.)
	 */
	public final float rotation;
}
