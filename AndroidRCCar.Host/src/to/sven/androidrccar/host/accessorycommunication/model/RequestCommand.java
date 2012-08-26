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
package to.sven.androidrccar.host.accessorycommunication.model;

import to.sven.androidrccar.common.communication.model.AdjustSpeedMessage;
import to.sven.androidrccar.common.communication.model.TurnCarMessage;

/**
 * This enumeration contains all commands that can be send to the µController (Car).
 * 
 * It contains a command type id and the length of the payload in bytes.
 * 
 * @author sven
 *
 */
public enum RequestCommand {
	
	/**
	 * No Operation Command
	 * 
	 * Expected response: {@link ResponseMessage#REQUEST_OK}
	 * 
	 * This command may not changed in newer protocol versions!
	 */
	NOOP ((byte)0x01, (short)0),
	
	/**
	 * Tells the µController to send the protocol version.
	 * 
	 * Expected response: {@link ResponseMessage#PROTOCOL_VERSION}
	 * 
	 * This command may not changed in newer protocol versions!
	 */
	GET_PROTOCOL_VERSION ((byte)0x02, (short)0),
	
	/**
	 * Tells the µController to send the features of the car.
	 * 
	 * Expected response: {@link ResponseMessage#FEATURES}
	 * 
	 * Note: Call this command only if protocol versions match!
	 */
	GET_FEATURES ((byte)0x03, (short)0),

	/**
	 * Tells the µController to "turn the wheels" of the car. 
	 * 
	 * Payload:
	 * rotation as {@link Short}: {@link Short#MAX_VALUE} for maximum rotation to the right,
	 * 							  {@link Short#MIN_VALUE} for maximum rotation to the left,
	 * 								0 for no rotation. 
	 * Expected response: {@link ResponseMessage#REQUEST_OK}
	 * 
	 * Note: Call this command only if protocol versions match!
	 * 
	 * @see TurnCarMessage#rotation
	 */
	TURN_CAR ((byte)0x04, (short)2),
	
	/**
	 * Tells the µController to adjust the speed of the car.
	 * 
	 * Payload:
	 * speed as {@link Short}: {@link Short#MAX_VALUE} for maximum forward speed,
	 * 						   {@link Short#MIN_VALUE} for maximum backward speed (if supported),
	 * 						   0 is stop. 
	 * Expected response: {@link ResponseMessage#REQUEST_OK}
	 * 
	 * Note: Call this command only if protocol versions match! 
	 * 
	 * @see AdjustSpeedMessage#speed
	 * @see CarFeatures#driveBackward
	 */
	ADJUST_SPEED ((byte)0x05, (short)2), 
	
	/**
	 * Tells the µController to rotate the camera.
	 * 
	 * Payload:
	 * pan as {@link Short}: (if supported, else 0)
	 * 						   {@link Short#MAX_VALUE} for maximum pan to the right
	 * 						   {@link Short#MIN_VALUE} for maximum pan to the left
	 * 						   0 no rotation/looking forward
	 * tilt as {@link Short}: same as pan for tilt up (+) and down (-).
	 * Expected response: {@link ResponseMessage#REQUEST_OK}
	 * 
	 * Note: Call this command only if protocol versions match
	 * 		 and camera pan and tilt is supported.
	 * 
	 * @see CarFeatures#supportPanCamera()
	 * @see CarFeatures#supportTiltCamera()
	 */
	ROTATE_CAM ((byte)0x06, (short)(2+2)),
	
	/**
	 * Tells the µController to send the battery state.
	 * 
	 * Expected response: {@link ResponseMessage#BATTERY_STATE}
	 * 
	 * Note: Call this command only if protocol versions match
	 * 		 and battery state is supported.
	 */
	GET_BATTERY_STATE ((byte)0x07, (short)0);
	
	/**
	 * Unique identifier for the message type.
	 */
	public final byte messageId;
	
	/**
	 * Length of the additional data in bytes.
	 */
	public final short payloadLength;
	
	/**
	 * Default constructor
	 * @param commandTypeId Unique identifier for the command type.
	 * @param payloadLength Length of the additional data in bytes.
	 */
	RequestCommand(byte commandTypeId, short payloadLength) {
		this.messageId = commandTypeId;
		this.payloadLength = payloadLength;
	}
}
