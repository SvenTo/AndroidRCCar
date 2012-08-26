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

import java.util.SortedMap;
import java.util.TreeMap;

import to.sven.androidrccar.common.communication.model.BatteryPowerMessage;

// TODO: Command -> Negative values not equal MIN_VALUE?

/**
 * This enumeration contains response message that can be received from the µController (Car).
 * 
 * It contains a message type id and the length of the payload in bytes.
 * 
 * 
 * 
 * @author sven
 *
 */
@SuppressWarnings("boxing")
public enum ResponseMessage {
	 
	/**
	 * The send request was processed and everything is OK.
	 * Can only received when expected by the request command.
	 * 
	 * This response may not changed in newer protocol versions!
	 */
	REQUEST_OK((byte)0x01, (short)0),
	
	/**
	 * Something went wrong.
	 * 
	 * Payload:
	 * short: A error id want went wrong. See {@link ErrorId} for available error types.
	 * 
	 * Exceptional response message: Can received after every request.
	 * 
	 * This response may not changed in newer protocol versions!
	 */
	ERROR((byte)0x02, (short)2),
	
	/**
	 * The battery power is near empty. The µController does not process any requests.
	 * 
	 * Exceptional response message: Can received after every request.
	 * 
	 * This response may not changed in newer protocol versions!
	 */
	BATTERY_NEAR_EMPTY((byte)0x03, (short)0),
	
	/**
	 * Contains the version of the protocol that the µController speaks. 
	 * 
	 * Can only received when the {@link RequestCommand#GET_PROTOCOL_VERSION} was send.
	 * 
	 * Payload:
	 * version as {@link Short}: A version number.
	 *  
	 * This response may not changed in newer protocol versions!
	 */
	PROTOCOL_VERSION((byte)0x04, (short)2), // TODO: Need thiz? -> No, protocol version is defined in ADK Protocol
	
	/**
	 * Contains the features of the car.
	 * 
	 * Payload:
	 *  first byte:
	 * 		bit 0: adjustableSpeed as {@link Boolean}: {@link CarFeatures#adjustableSpeed};
	 * 		bit 1: driveBackward as {@link Boolean}: {@link CarFeatures#driveBackward}
	 *  	bit 2: batteryPower as {@link Boolean}: {@link CarFeatures#batteryPower}
	 *  	bit 3-7: unused, can used in future.
	 *	cameraPanMin as {@link Short}: 0 is 0 degree.
	 *								   {@link Short#MAX_VALUE} is 180 degree. 
	 *	cameraPanMax as {@link Short}: 0 is 0 degree.
	 *								   {@link Short#MAX_VALUE} is 180 degree. 
	 *  cameraTiltMin as {@link Short}: 0 is 0 degree.
	 *								    {@link Short#MAX_VALUE} is 180 degree. 
	 *  cameraTiltMax as {@link Short}: 0 is 0 degree.
	 *								    {@link Short#MAX_VALUE} is 180 degree. 
	 *  If cameraPanMin and cameraPanMax is both 0 this means that camera pan is not supported.
	 *  The same applies to camera tilt with cameraTiltMin and cameraTiltMax.
	 * 
	 * Can only received when the {@link RequestCommand#GET_FEATURES} was send.
	 */
	FEATURES((byte)0x05, (short)(1+4*2)),
	
	/**
	 * Contains battery charging level.
	 * 
	 * Payload:
	 * chargingLevel as {@link Short}: {@link BatteryPowerMessage#chargingLevel}
	 * 								  0 is 0%
	 * 								  {@link Short#MAX_VALUE} is 100%
	 * 
	 * Can only received when the {@link RequestCommand#GET_BATTERY_STATE} was send.
	 */
	BATTERY_STATE((byte)0x06, (short)2);
	
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
	 * @param messageTypeId Unique identifier for the message type.
	 * @param payloadLength Length of the additional data in bytes.
	 */
	ResponseMessage(byte messageTypeId, short payloadLength) {
		this.messageId = messageTypeId;
		this.payloadLength = payloadLength;
	}
	
	/**
	 * Maps the message type id to its appropriate {@link ResponseMessage}.
	 */
	private final static SortedMap<Byte, ResponseMessage> map = new TreeMap<Byte, ResponseMessage>();

	static {
		for(ResponseMessage msg : ResponseMessage.values()) {
			map.put(msg.messageId, msg);
		}
	}
	
	/**
	 * Returns the {@link ResponseMessage} that belong to the {@code messageTypeId}.
	 * @param messageTypeId message type id of the {@link ResponseMessage}.
	 * @return The {@link ResponseMessage} or null, if no appropriate {@link ResponseMessage} is found.
	 */
	public static ResponseMessage get(byte messageTypeId) {
		return map.get(messageTypeId);
	}
}
