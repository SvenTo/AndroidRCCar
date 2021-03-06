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
package to.sven.androidrccar.host.framework;

import java.io.FileDescriptor;

import android.content.Context;
import android.os.Handler;
import to.sven.androidrccar.common.framework.AbstractFactory;
import to.sven.androidrccar.common.logic.handler.ILogicHandlerFacade;
import to.sven.androidrccar.common.logic.handler.IMessageHandler;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;
import to.sven.androidrccar.host.logic.handler.IHostLogicHandlerFacade;
import to.sven.androidrccar.host.logic.impl.HostLogic;
import to.sven.androidrccar.host.service.contract.ICameraStreamingService;
import to.sven.androidrccar.host.service.contract.IHostConfigurationService;
import to.sven.androidrccar.host.service.impl.CameraStreamingService;
import to.sven.androidrccar.host.service.impl.HostConfigurationService;

/**
 * This factory provides creators for all classes that {@link HostLogic} needed to create.
 * @author sven
 *
 */
public class HostFactory extends AbstractFactory implements IHostFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICameraStreamingService createCameraStreamingService() {
		return new CameraStreamingService(); // TODO: Do something.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <THandler extends IMessageHandler<?>> THandler createMessageHandler(
			Class<THandler> type, ILogicHandlerFacade<?> logic)
		throws IllegalArgumentException {
		try {
			return type.getDeclaredConstructor(IHostLogicHandlerFacade.class).newInstance(logic); 
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create an instance of the given handler type.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IHostConfigurationService createHostConfigurationService(Context context) {
		return new HostConfigurationService(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAccessoryCommunication createAccessoryCommunication(
			FileDescriptor fileDescriptor, Handler mainThreadHandler) {
		return new AccessoryCommunication(fileDescriptor, mainThreadHandler);
	}
}
