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
package to.sven.androidrccar.host.accessorycommunication.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import to.sven.androidrccar.common.exception.RangeException;
import to.sven.androidrccar.common.utils.CRC8;
import to.sven.androidrccar.host.accessorycommunication.exception.AccessoryConnectionProblemException;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.model.ErrorId;
import to.sven.androidrccar.host.accessorycommunication.model.RequestCommand;
import to.sven.androidrccar.host.accessorycommunication.model.ResponseMessage;

/**
 * Abstract implementation for a command that should be send to the µController.
 * The implementation provides sending of the command and receiving the response.
 * It also handles the exceptional response types and the IO exception handling.
 * @author sven
 *
 */
public abstract class AbstractCommand implements Runnable {

	/**
	 * The {@link ICommandListener}
	 */
	protected final ICommandListener commandListener;
	
	/**
	 * The {@link OutputStream} to send message to the µController.
	 */
	private final OutputStream outputStream;
	
	/**
	 * The {@link InputStream} to received messages from the µController.  
	 */
	private final InputStream inputStream;
	
	/**
	 * A {@link Charset} for converting {@link Byte}s to a {@link String}.
	 */
	protected final static Charset utf8 = Charset.forName("UTF-8");
	
	/**
	 * Default Constructor
	 * @param commandListener The {@link ICommandListener}
	 */
	protected AbstractCommand(ICommandListener commandListener) {
		this.commandListener = commandListener;
		this.inputStream = commandListener.getInputStream();
		this.outputStream = commandListener.getOutputStream();
	}
	
	/**
	 * Sends the command; receives and processes the response.
	 * If a exception occurs, it will be delegated to {@link ICommandListener#connectionProblem}. 
	 */
	@Override
	public void run() {
		try {
			sendCommand();
			receiveAndProcessResponse();
		} catch (IOException e) {
			commandListener.connectionProblem(new AccessoryConnectionProblemException(e));
		}
	}
	
	/**
	 * Send the command with its payload.
	 * @throws IOException A exception occurs on write.
	 */
	private void sendCommand() throws IOException {
		int len = AccessoryCommunication.COMMAND_LENGTH;
		byte[] buffer = new byte[len];
		RequestCommand command = getRequestCommandType();
		buffer[0] = command.messageId;
		if(command.payloadLength > 0) {
			ByteBuffer payload = ByteBuffer.allocate(command.payloadLength); 
			fillPayload(payload);
			System.arraycopy(payload.array(), 0, buffer, 1, command.payloadLength);
		}
		buffer[len-1] = CRC8.calc(buffer, len-1);
		outputStream.write(buffer);
	}

	/**
	 * Gets the {@link RequestCommand} type that should send.
	 * @return {@link RequestCommand}
	 */
	protected abstract RequestCommand getRequestCommandType();
	
	/**
	 * Gets the {@link ResponseMessage} type that is expected as response.
	 * @return {@link ResponseMessage}
	 */
	protected abstract ResponseMessage getExpectedResponseMessageType();
	
	/**
	 * Fills the additional data that should be send to the µController. 
	 * @param payload {@link ByteBuffer} that will be filled with additional data.
	 */
	protected abstract void fillPayload(ByteBuffer payload);
	
	/**
	 * Reads the response from the {@link InputStream},
	 * validates it, and processes it depending on the received type.
	 * @throws IOException Something is wrong with the connection or the received message.
	 */
	private void receiveAndProcessResponse() throws IOException {
		byte[] responseBuffer = receiveResponse();
		validateMessage(responseBuffer);
		ResponseMessage responseMessage = mapResponseMessage(responseBuffer);
		ByteBuffer payload = getPayload(responseMessage, responseBuffer);
		processResponse(responseMessage, payload);
	}
	
	/**
	 * Calculates a CRC8 Checksum of the first 15 bytes
	 * and check if it equals with last byte (the checksum byte). 
	 * @param responseBuffer The complete response
	 * @throws IOException Is thrown if the validation failed.
	 */
	private void validateMessage(byte[] responseBuffer) throws IOException {
		int pos = AccessoryCommunication.REPSONE_MESSAGE_LENGTH-1;
		byte exceptedChecksum = CRC8.calc(responseBuffer, pos);
		if(responseBuffer[pos] != exceptedChecksum) {
			throw new IOException("Checksum validation failed.");
		}
	} // TODO: Test thiz!

	/**
	 * Extract the payload and and wrap it into a {@link ByteBuffer}
	 * @param responseMessage The type with the payload length
	 * @param responseBuffer The complete response
	 * @return The payload
	 */
	private ByteBuffer getPayload(ResponseMessage responseMessage,
								  byte[] responseBuffer) {
		short payloadLength = responseMessage.payloadLength;
		ByteBuffer payload = null;
		if(payloadLength > 0) {
			payload = ByteBuffer.wrap(responseBuffer, 1, payloadLength);
		}
		return payload;
	}

	/**
	 * Receives the response message from the µController.
	 * @return The complete response
	 * @throws IOException If a exception on read occurs or the stream is closed.
	 */
	private byte[] receiveResponse() throws IOException {
		byte[] buffer = new byte[AccessoryCommunication.REPSONE_MESSAGE_LENGTH];
		int len = inputStream.read(buffer);
		if(len == -1) {
			throw new IOException("Stream is closed.");
		} else if(len != AccessoryCommunication.REPSONE_MESSAGE_LENGTH) {
			throw new IOException("Invalid response packet length."); // TODO: Wait for other bytes?
		}
		
		return buffer;
	}
	
	/**
	 * Maps the response byte to a {@link ResponseMessage}.
	 * @param responseBuffer The complete response
	 * @return A {@link ResponseMessage}
	 * @throws IOException If the response byte contains a unknown response message type.
	 */
	private ResponseMessage mapResponseMessage(byte[] responseBuffer)
		throws IOException {
		byte messageTypeId = responseBuffer[0];
		
		ResponseMessage response =  ResponseMessage.get(messageTypeId);
		if(response == null) {
			throw new IOException("A response message with the type id "+messageTypeId+" is unkown."); 
		}
		return response;
	}
	
	/**
	 * Process the response depending on {@code responseMessage}.
	 * 
	 * @param responseMessage The received {@link ResponseMessage}.
	 * @param payload The Payload
	 * @throws IOException A invalid response type was received. 
	 */
	private void processResponse(ResponseMessage responseMessage, ByteBuffer payload)
			throws IOException {
		switch (responseMessage) {
		case ERROR:
			processError(payload);
			break;
		case BATTERY_NEAR_EMPTY:
			processBatteryNearEmpty();
			break;
		default:
			if(responseMessage == getExpectedResponseMessageType()) {
				processExpectedResponse(payload);
			} else {
				throw new IOException("The response type " + responseMessage.name() +
									  " is not valid answer for request command type " +
									  getRequestCommandType().name() + ".");
			}
			break;
		}
	}
	
	/**
	 * The received response message is a {@link ResponseMessage#ERROR}
	 * @param payload The Payload
	 */
	private void processError(ByteBuffer payload) {
		short errorId = payload.getShort();
		ErrorId error = ErrorId.get(errorId);
		String message;
		if(error != null) {
			message = "Received an error from type " + error.name() + ".";
		} else {
			message = "Received an unknown error.";
		}
		
		commandListener.errorReceived(message);
	}
	
	/**
	 * The received response message is a {@link ResponseMessage#BATTERY_NEAR_EMPTY}.
	 */
	private void processBatteryNearEmpty() {
		commandListener.batteryNearEmpty();
	}
	
	/**
	 * The received response message is from the expected type.
	 * @param payload Additional data attached to the response. If response has no: {@code null}.
	 */
	protected abstract void processExpectedResponse(ByteBuffer payload);
	
	/**
	 * Converts {@value} to a {@link Float} and maps the value
	 * to a given Range:
	 * short 0 to {@link Short#MAX_VALUE}
	 * is mapped to
	 * float 0 to {@code max}).
	 * @param value Value to convert
	 * @param max The maximum of the range (If {@value} is {@link Short#MAX_VALUE})
	 * @return The converted and mapped value
	 * @throws RangeException If {@value} or {@code max} is negative.
	 */
	protected float rangeConvert(short value, float max) {
		if(value < 0) {
			throw new RangeException("value", value);
		}
		if(max < 0) {
			throw new RangeException("max", max);
		}
		return ((float)value / (float)Short.MAX_VALUE) * max;
	}
}
