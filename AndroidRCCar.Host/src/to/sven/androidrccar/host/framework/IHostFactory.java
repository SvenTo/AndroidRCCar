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

import to.sven.androidrccar.common.framework.IFactory;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;
import to.sven.androidrccar.host.logic.impl.HostLogic;
import to.sven.androidrccar.host.service.contract.ICameraStreamingService;
import to.sven.androidrccar.host.service.contract.IHostConfigurationService;
import to.sven.androidrccar.host.service.impl.CameraStreamingService;
import to.sven.androidrccar.host.service.impl.HostConfigurationService;
import android.content.Context;
import android.os.Handler;

/**
 * This factory provides creators for all classes that {@link HostLogic} needed to create.
 * @author sven
 *
 */
public interface IHostFactory extends IFactory {

	/**
	 * Creates a new {@link CameraStreamingService}
	 * @return Created instance as {@link ICameraStreamingService}
	 * @see CameraStreamingService#CameraStreamingService()
	 */
	ICameraStreamingService createCameraStreamingService();

	/**
	 * @see HostConfigurationService#HostConfigurationService(android.content.Context)
	 * @param context see {@link HostConfigurationService#HostConfigurationService}
	 * @return Created instance.
	 */
	IHostConfigurationService createHostConfigurationService(Context context);
	
	/**
	 * @see AccessoryCommunication#AccessoryCommunication(FileDescriptor, Handler)
	 * @param fileDescriptor see {@link AccessoryCommunication#AccessoryCommunication(FileDescriptor, Handler)}
	 * @param mainThreadHandler see {@link AccessoryCommunication#AccessoryCommunication(FileDescriptor, Handler)}
	 * @return Created instance.
	 */
	IAccessoryCommunication createAccessoryCommunication(FileDescriptor fileDescriptor, Handler mainThreadHandler);

}
