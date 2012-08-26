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
package to.sven.androidrccar.client.test.logic.handler;

import java.lang.reflect.ParameterizedType;

import to.sven.androidrccar.client.framework.ClientFactory;
import to.sven.androidrccar.client.framework.IClientDependencyContainer;
import to.sven.androidrccar.client.logic.contract.IClientLogicListener;
import to.sven.androidrccar.client.logic.handler.AbstractClientMessageHandler;
import to.sven.androidrccar.client.logic.handler.IClientLogicHandlerFacade;
import android.test.AndroidTestCase;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * Provides functionality that is used by every MessageHandler test.
 * Provides initialization, replaying and verifying of 
 * {@link IClientLogicHandlerFacade},
 * {@link IClientLogicListener} and
 * {@link IClientDependencyContainer} mocks.
 * Provides creation of test subject.
 * @author sven
 * @param <THandler> The Handler to test.
 *
 */
@UsesMocks({ IClientLogicHandlerFacade.class,
	 		 IClientLogicListener.class ,IClientDependencyContainer.class })
public abstract class AbstractTestCaseForClientMessageHandler<THandler extends AbstractClientMessageHandler<?>>
	extends AndroidTestCase {

	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	protected IClientLogicHandlerFacade logicMock;
	
	/**
	 * Normal mock (without order checking)
	 * @see AndroidMock#createMock
	 */
	protected IClientDependencyContainer cdcMock;
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	protected IClientLogicListener logicListenerMock;
	
	/**
	 * The {@link ClientFactory} for creating the message handler.
	 */
	private ClientFactory factory = new ClientFactory();
	
	/**
	 * Before Test:
	 * Set up new {@link IClientLogicHandlerFacade},
	 * {@link IClientLogicListener} and
	 * {@link IClientDependencyContainer} mocks. 
	 * @see AndroidMock#createStrictMock
	 */
	@Override
	protected void setUp() {
		logicMock = AndroidMock.createStrictMock(IClientLogicHandlerFacade.class);
		cdcMock = AndroidMock.createMock(IClientDependencyContainer.class);
		logicListenerMock = AndroidMock.createStrictMock(IClientLogicListener.class);
		
		AndroidMock.expect(logicMock.getDependency())
				   .andReturn(cdcMock); 
		AndroidMock.expect(cdcMock.getLogicListener())
				   .andReturn(logicListenerMock)
				   .anyTimes();
	}

	/**
	 * Sets the mocks in replay phase and create a mock injected handler through the {@link ClientFactory}.
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
		AndroidMock.verify(logicMock, cdcMock, logicListenerMock);
	}
	
	/**
	* Sets the mocks in replay phase by {@link #setUp()}.
	* @see AndroidMock#replay
	*/
	protected void replayFieldMocks() {
		AndroidMock.replay(logicMock, cdcMock, logicListenerMock);
	}
}
