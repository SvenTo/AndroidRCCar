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
package to.sven.androidrccar.client.logic.contract;

import to.sven.androidrccar.common.communication.model.AdjustSpeedMessage;
import to.sven.androidrccar.common.communication.model.RotateCameraMessage;
import to.sven.androidrccar.common.communication.model.TurnCarMessage;
import to.sven.androidrccar.common.logic.contract.ILogic;

/**
 * Logic for the Client:
 * The "missing" part between the user interface and the host.
 * @author sven
 *
 */
public interface IClientLogic extends ILogic {
	
	/**
	 * Tell the Host that the speed should be adjusted. 
	 * @param speed {@link AdjustSpeedMessage#speed}
	 */
	public void adjustSpeed(float speed);
	
	/**
	 * Tell the Host to turn the car. 
	 * @param rotation {@link TurnCarMessage#rotation}
	 */
	public void turnCar(float rotation);
	
	/**
	 * Tell the Host to rotate the camera
	 * @param pan {@link RotateCameraMessage#pan}
	 * @param tilt {@link RotateCameraMessage#tilt} 
	 */
	public void rotateCamera(float pan, float tilt);
}
