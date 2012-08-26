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
package to.sven.androidrccar.common.utils;

import android.os.AsyncTask;

/**
 * This data class can be used as a result of {@link AsyncTask}.
 * It provides an normal result oder an exception as result of the task. 
 * @author sven
 *
 * @param <TResult> The type of the normal result
 */
public class AsyncTaskResult<TResult> {
	/**
	 * Contains the result, if task ended normally.
	 */
	private TResult result;
	
	/**
	 * Contains the error, if an exception occurred.
	 */
	private Exception error;
	
	/**
	 * Task ended normally.
	 * @param result The result of the task.
	 */
	public AsyncTaskResult(TResult result) {
	    this.result = result;
	}
	
	/**
	 * An exception occurred while running the task. 
	 * @param error The {@link Exception}
	 */
	public AsyncTaskResult(Exception error) {
	    this.error = error;
	}
	
	/**
	 * Contains the result, if task ended normally.
	 * @return The result.
	 */
	public TResult getResult() {
	    return result;
	}
	
	/**
	 * Contains the error, if an exception occurred.
	 * @return The {@link Exception}
	 */
	public Exception getError() {
	    return error;
	}
}
