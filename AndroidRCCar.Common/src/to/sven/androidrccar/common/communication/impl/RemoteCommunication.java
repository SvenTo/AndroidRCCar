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
package to.sven.androidrccar.common.communication.impl;

import java.io.IOException;
import java.net.Socket;

import to.sven.androidrccar.common.communication.contract.IRemoteCommunication;
import to.sven.androidrccar.common.communication.contract.IRemoteCommunicationListener;
import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.framework.IDependencyContainer;
import android.os.Handler;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This class handles the communication with the other Android device (Host or Client).
 * It serialize the message to send into JSON
 * and deserialize the received messages.
 * @author sven
 */
public class RemoteCommunication implements IRemoteCommunication {
	/**
	 *  A TCP socket to the other Android device (Host/Client).
	 */
	private final Socket socket;
	/**
	 * Will be informed, when something happen here.
	 * @see IRemoteCommunicationListener
	 */
	private final IRemoteCommunicationListener listener;
	/**
	 * @see MessageListenerThread
	 */
	private final MessageListenerThread messageListenerThread;
	/**
	 * For (De-)Serialization of {@link Message}s into JSON.
	 */
	private final ObjectMapper jsonMapper = new ObjectMapper();
	
	/**
	 * Default Constructor.
	 * @param dependencyContainer Needed to get the {@link Socket} and the {@link Handler}.
	 * @param listener Will be informed, when something happen here.
	 */
	public RemoteCommunication(IDependencyContainer<?, ?> dependencyContainer, IRemoteCommunicationListener listener) { 
		this.socket = dependencyContainer.getSocket();
		this.listener = listener;
		jsonMapper.configure(Feature.AUTO_CLOSE_TARGET, false);
		jsonMapper.configure(SerializationFeature.CLOSE_CLOSEABLE, false);
		jsonMapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
		messageListenerThread = new MessageListenerThread(this, dependencyContainer.getHandler());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startMessageListener() {
		messageListenerThread.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		messageListenerThread.interrupt();
		if(!socket.isClosed())
		{
			socket.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(Message message) throws IOException {
		jsonMapper.writeValue(socket.getOutputStream(), message);
	}
	
	/**
	 * Inform the {@link IRemoteCommunicationListener} about a new message.
	 * @param message a new message.
	 */
	void messageRecived(Message message) {
		listener.messageReceived(message);
	}
	
	/**
	 * Inform the {@link IRemoteCommunicationListener} that something went wrong.
	 * @param e What went wrong.
	 */
	void connectionProblem(Exception e) {
		listener.connectionProblem(e);
	}
	
	/**
	 * For (De-)Serialization of {@link Message}s into JSON.
	 * @return A {@link ObjectMapper}
	 */
	ObjectMapper getJsonObjectMapper() {
		return jsonMapper;
	}
	
	/**
	 * Connection to the partner.
	 * @return A {@link Socket}
	 */
	Socket getSocket() {
		return socket;
	}
}
