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

import to.sven.androidrccar.common.exception.RangeException;
import android.os.Handler;

/**
 * Abstract implementation of a cyclic task.
 * It calls the abstract method {@link #runTask()}
 * in a given interval.
 * A {@link Handler} is used to call the task after the given interval.
 * @see Handler#postDelayed(Runnable, long)
 * @author sven
 *
 */
public abstract class AbstractCyclicTask implements Runnable, ICyclicTask {
	
	/**
	 * {@link Handler} that is used to call the task after the given {@code interval}.
	 */
	private final Handler handler;
	
	/**
	 * {@link #setInterval}
	 */
	private int interval = 0;
	
	/**
	 * {@link #isRunning}
	 */
	private boolean started;
	
	/**
	 * Default constructor.
	 * @param handler A {@link Handler} that is used to call the task after the given {@code interval}.
	 */
	public AbstractCyclicTask(Handler handler) {
		this.handler = handler;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInterval(int interval) {
		if(interval < 0) {
			throw new RangeException("Interval can not lesser than 0.");
		}
		this.interval = interval;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRunning() {
		return started;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		if(started) {
			throw new IllegalStateException("Allready started.");
		}
		started = true;
		run();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		started = false;
		handler.removeCallbacks(this);
	}
	
	/**
	 * Don't call this method directly. Call {@link #start()}!.
	 */
	@Override
	public void run() {
		if(!started) {
			throw new IllegalStateException("Don't call this method directly. Call start()!.");
		}
		
		runTask();
		handler.postDelayed(this, interval);
	}
	
	/**
	 * The task that should processed in an given interval. 
	 */
	protected abstract void runTask();
}
