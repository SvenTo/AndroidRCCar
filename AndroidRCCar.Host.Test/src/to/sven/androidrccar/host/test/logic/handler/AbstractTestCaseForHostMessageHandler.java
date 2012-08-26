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
package to.sven.androidrccar.host.test.logic.handler;

import java.lang.reflect.ParameterizedType;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunication;
import to.sven.androidrccar.host.framework.HostFactory;
import to.sven.androidrccar.host.framework.IHostDependencyContainer;
import to.sven.androidrccar.host.logic.handler.AbstractHostMessageHandler;
import to.sven.androidrccar.host.logic.handler.IHostLogicHandlerFacade;
import android.test.AndroidTestCase;

/**
 * Provides functionality that is used by every MessageHandler test.
 * Provides initialization, replaying and verifying of 
 * {@link IHostLogicHandlerFacade},
 * {@link IAccessoryCommunication} and
 * {@link IHostDependencyContainer} mocks.
 * Provides creation of test subject.
 * @author sven
 * @param <THandler> The Handler to test.
 *
 */
@UsesMocks({ IHostLogicHandlerFacade.class,
	 		 IAccessoryCommunication.class ,IHostDependencyContainer.class })
public class AbstractTestCaseForHostMessageHandler<THandler extends AbstractHostMessageHandler<?>>
	extends AndroidTestCase {

	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	protected IHostLogicHandlerFacade logicMock;
	
	/**
	 * Normal mock (without order checking)
	 * @see AndroidMock#createMock
	 */
	protected IHostDependencyContainer hdcMock;
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	protected IAccessoryCommunication accessoryCommunicationMock;
	
	/**
	 * The {@link HostFactory} for creating the message handler.
	 */
	private HostFactory factory = new HostFactory();
	
	/**
	 * Before Test:
	 * Set up new {@link IHostLogicHandlerFacade},
	 * {@link IHostDependencyContainer} and
	 * {@link IAccessoryCommunication} mocks. 
	 * @see AndroidMock#createStrictMock
	 */
	@Override
	protected void setUp() {
		logicMock = AndroidMock.createStrictMock(IHostLogicHandlerFacade.class);
		hdcMock = AndroidMock.createMock(IHostDependencyContainer.class);
		accessoryCommunicationMock = AndroidMock.createStrictMock(IAccessoryCommunication.class);
		
		AndroidMock.expect(logicMock.getDependency())
				   .andReturn(hdcMock);
		AndroidMock.expect(hdcMock.getAccessoryCommunication())
				   .andReturn(accessoryCommunicationMock)
				   .anyTimes();
	}

	/**
	 * Sets the mocks in replay phase and create a mock injected handler through the {@link HostFactory}.
	 * @see AndroidMock#replay
	 * @return mock injected handler
	 */
	protected THandler aHandlerForTest() {
		replayFieldMocks();
		
		return factory.createMessageHandler(getMessageHandlerClass(), logicMock);
	}
	
	/**
	 * The the {@link Class} of the message handler under test.
	 * @return {@link Class} of the message handler under test.
	 */
	@SuppressWarnings("unchecked")
	private Class<THandler> getMessageHandlerClass() {
		return (Class<THandler>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * Verifies all mock that created by {@link #setUp()}
	 * @see AndroidMock#verify
	 */
	protected void verifyFieldMocks() {
		AndroidMock.verify(logicMock, hdcMock, accessoryCommunicationMock);
	}
	
	/**
	* Sets the mocks in replay phase by {@link #setUp()}.
	* @see AndroidMock#replay
	*/
	protected void replayFieldMocks() {
		AndroidMock.replay(logicMock, hdcMock, accessoryCommunicationMock);
	}
}
