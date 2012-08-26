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
package to.sven.androidrccar.host.logic.impl;


/**
 * Interface for a cyclic task.
 * It runs something in a given interval.
 * 
 * @author sven
 */
public interface ICyclicTask {

	/**
	 * Sets the interval how often the task should be called.
	 * It can set at any time. If the task {@link #isRunning()} the interval is
	 * set after the next run of the task.
	 * @param interval Interval in milliseconds.
	 */
	public abstract void setInterval(int interval);

	/**
	 * Is the cyclic task running?
	 * This means that it was started through {@link #start()}
	 * and was not {@link #stop()} after that.
	 * @return true, if it's running
	 */
	public abstract boolean isRunning();

	/**
	 * Run the task in an given interval.
	 */
	public abstract void start();

	/**
	 * Stop running cyclic task.
	 * Does nothing, if its not running.
	 */
	public abstract void stop();

}
