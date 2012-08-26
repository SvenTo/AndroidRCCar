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
package to.sven.androidrccar.common.test.logic;

import java.net.Socket;

import to.sven.androidrccar.common.framework.AbstractDependencyContainer;
import to.sven.androidrccar.common.framework.IFactory;
import to.sven.androidrccar.common.logic.contract.ILogicListener;
import to.sven.androidrccar.common.logic.impl.AbstractLogic;

import com.google.android.testing.mocking.AndroidMock;

/**
 * This dependency container for testing
 * returns strict mock objects for the dependences of {@link AbstractLogic}.
 * @see AndroidMock#createStrictMock
 * @author sven
 *
 */
public class TestDependencyContainer extends AbstractDependencyContainer<IFactory, ILogicListener> {

	/**
	 * Creates the {@link TestDependencyContainer} with mock objects.
	 */
	public TestDependencyContainer() {
		super(AndroidMock.createStrictMock(IFactory.class),
			  AndroidMock.createStrictMock(ILogicListener.class),
			  null);
		setSocket(AndroidMock.createStrictMock(Socket.class));
	}
}
