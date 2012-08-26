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

/**
 * This enumeration contains the types of errors that can send with a 
 * {@link ResponseMessage#ERROR}.
 * @author sven
 *
 */
public enum ErrorId {
	/**
	 * An error occurred that is not defined below.
	 */
	UNDEFINED_ERROR ((short)0x00),
	/**
	 * The Host has send a invalid command.
	 */
	INVALID_COMMAND_ERROR ((short)0x01),
	/**
	 * The Host has send a command that is unknown by the µController.
	 */
	UNKOWN_COMMAND_ERROR ((short)0x02),
	/**
	 * Something went wrong while processing this command.
	 */
	PROCESS_COMMAND_ERROR ((short)0x03);
	
	/**
	 * Length of the additional data in bytes.
	 */
	public final short errorId;
	
	/**
	 * Default constructor
	 * @param errorTypeId Unique identifier for the error type.
	 */
	ErrorId(short errorTypeId) {
		this.errorId = errorTypeId;
	}
	
	/**
	 * Maps the error id to its appropriate {@link ErrorId}.
	 */
	private final static SortedMap<Short, ErrorId> map = new TreeMap<Short, ErrorId>();

	static {
		for(ErrorId error : ErrorId.values()) {
			map.put(error.errorId, error);
		}
	}
	
	/**
	 * Returns the {@link ErrorId} that belong to the {@code errorId}.
	 * @param errorId error id of the {@link ErrorId}.
	 * @return The {@link ErrorId} or null, if no appropriate {@link ErrorId} is found.
	 */
	public static ErrorId get(short errorId) {
		return map.get(errorId);
	}
}
