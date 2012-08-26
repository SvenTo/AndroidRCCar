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

/**
 * Thrown if an passed argument is outside of its acceptable range.
 * @author sven
 *
 */
public class RangeException extends IllegalArgumentException {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = -5069473850565905232L;
	
	
	/**
	 * @see IllegalArgumentException#IllegalArgumentException(String, Throwable)
	 */
	public RangeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see IllegalArgumentException#IllegalArgumentException(String)
	 */
	public RangeException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @see IllegalArgumentException#IllegalArgumentException(Throwable)
	 */
	public RangeException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Creates a new {@link RangeException}.
	 * @param invalidArgumentName The name of the argument. 
	 * @param actualValue The invalid value.
	 */
	protected RangeException(String invalidArgumentName,
						  String actualValue) {
		super("The passed value \"" + actualValue + "\"" + 
			  " of parameter " + invalidArgumentName + 
			  " is outside its acceptable range.");
	}
	

	/**
	 * Creates a new {@link RangeException}.
	 * @param invalidArgumentName The name of the argument. 
	 * @param actualValue The invalid value.
	 */
	public RangeException(String invalidArgumentName,
						  float actualValue) {
		this(invalidArgumentName, Float.toString(actualValue));
	}
	
	/**
	 * Creates a new {@link RangeException}.
	 * @param invalidArgumentName The name of the argument. 
	 * @param actualValue The invalid value.
	 */
	public RangeException(String invalidArgumentName,
						  short actualValue) {
		this(invalidArgumentName, Short.toString(actualValue));
	}
}
