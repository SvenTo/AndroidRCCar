/*
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
 */

#ifndef __AndroidRCCarCommunication_h__
#define __AndroidRCCarCommunication_h__

#define __STDC_LIMIT_MACROS
#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>
#include <stdint.h>
#include "CarFeatures.h"
#include "CRC8.h"

#define	ANDROID_RC_CAR_PROTOCOL_VERSION		1
#define	ANDROID_RC_CAR_REQUEST_CMD_SIZE		16
#define	ANDROID_RC_CAR_RESPONE_MSG_SIZE		16
/**
 * This data will be given to AndroidAccessory:
 */
#define	ANDROID_RC_CAR_MANUFACTURER			"sven.to"
#define	ANDROID_RC_CAR_MODEL				"AndroidRCCar"
#define	ANDROID_RC_CAR_DEFAULT_DESCRIPTION	"Arduino Car"
#define	ANDROID_RC_CAR_VERSION				"1.0"
#define	ANDROID_RC_CAR_URI					"http://androidrccar.sven.to"
#define	ANDROID_RC_CAR_SERIAL				"513200042424242"

/**
 * @brief This abstract class provides the communication to the Android App. It depends on the AndroidAccessory Library provided by Google (ADK Version 1.0).
 *        It's a generic implementation, the concrete class must provide the supported features and the logic to drive the motors and other features. 
 * @abstract
 * @note A virtual destructor (~AndroidRCCarCommunication() {}) is not defined, because arduino provides no new and delete.
 *       (Although it is possible to defined it.) Due to the limited memory, we should avoid using the heap in Arduino environment.
 *       (see also: http://code.google.com/p/arduino/issues/detail?id=523)
 */
class AndroidRCCarCommunication {
	private:
        /**
         * @brief The RequestCommand enum contains the command types that can be received from the Android App.
         *        For a complete description of the RequestCommands see AndroidRCCar.Host-Enum RequestCommand
         *        (package to.sven.androidrccar.host.accessorycommunication.model). 
         */
		enum RequestCommand {
			NOOP = 0x01,
			GET_PROTOCOL_VERSION = 0x02,
			GET_FEATURES = 0x03,
			TURN_CAR = 0x04,
			ADJUST_SPEED = 0x05, 
			ROTATE_CAM = 0x06,
			GET_BATTERY_STATE = 0x07 
		};
        
        /**
         * @brief The ResponseMessage enum contains the response message types that can be send to the Android App.
         *        For a complete description of the ResponseMessages see AndroidRCCar.Host-Enum ResponseMessage
         *        (package to.sven.androidrccar.host.accessorycommunication.model). 
         */
		enum ResponseMessage {
			REQUEST_OK = 0x01,
			ERROR = 0x02,
			BATTERY_NEAR_EMPTY = 0x03,
			PROTOCOL_VERSION = 0x04,
			FEATURES = 0x05,
			BATTERY_STATE = 0x06
		};
        
        /**
         * @brief The ErrorId enum contains the error ids that can be send with a ERROR Response.
         *        For a complete description of the ErrorIds see AndroidRCCar.Host-Enum ErrorId
         *        (package to.sven.androidrccar.host.accessorycommunication.model). 
         */
		enum ErrorId {
			UNDEFINED_ERROR = 0x00,
			INVALID_COMMAND_ERROR = 0x01,
			UNKOWN_COMMAND_ERROR = 0x02,
			PROCESS_COMMAND_ERROR = 0x03
		};
	public:
        /**
         * @brief Constructor that using the default device description
         * @see ANDROID_RC_CAR_DEFAULT_DESCRIPTION
         */
		AndroidRCCarCommunication():
			accessory(ANDROID_RC_CAR_MANUFACTURER, ANDROID_RC_CAR_MODEL, ANDROID_RC_CAR_DEFAULT_DESCRIPTION,
					  ANDROID_RC_CAR_VERSION, ANDROID_RC_CAR_URI, ANDROID_RC_CAR_SERIAL), crc() {
			}
        
        /**
         * @brief Constructor that using the passed device description
         * @param deviceDescription A description for the car
         */
		AndroidRCCarCommunication(const char *deviceDescription):
			accessory(ANDROID_RC_CAR_MANUFACTURER, ANDROID_RC_CAR_MODEL, deviceDescription,
					  ANDROID_RC_CAR_VERSION, ANDROID_RC_CAR_URI, ANDROID_RC_CAR_SERIAL), crc() {
			}
        
        /**
         * @brief Initializes the accessory and calls the virtual doSetUp
         */
		void setUp();
        
        /**
         * @brief Are we connected and can communicate to Android phone?
         * @return True, if we can communicate
         */
		bool isConnected();
        
        /**
         * @brief Reads the next command from stream and processes it. If nothing is on stream, the method does nothing.
         * @note Please check first, if we are connected to the Android phone
         * @see isConnected()
         */
		void handleNextCommand();
	protected:
        /**
         * @brief Will be called with setUp();. Do some initialization here.
         * @see setUp();
         * @abstract
         */
		virtual void doSetUp() = 0;
        
        /**
         * @brief Will be called, when the Android phone asked for the supported Features.
         * @return Should return the CarFeatures supported by this car.
         * @abstract
         */
		virtual CarFeatures getFeatures() = 0;
        
        /**
         * @brief "turn the wheels" of the car.
         * @param rotation INT16_MAX for maximum rotation to the right,
         *                 INT16_MIN for maximum rotation to the left,
         *                 0 for no rotation. 
         * @return 
         * @abstract
         */
		virtual bool turnCar(int16_t rotation) = 0;
        
        /**
         * @brief Adjust the speed of the car.
         * @param speed INT16_MAX for maximum forward speed,
         *              INT16_MIN for maximum backward speed.
         * @return Was the command executed successfully?
         * @abstract
         */
        virtual bool adjustSpeed(int16_t speed) = 0;
        
        /**
         * @brief Rotate the camera
         * @param pan INT16_MAX for maximum pan to the right,
         *            INT16_MIN for maximum pan to the left 
         *            (if supported, else 0)
         * @param tilt same as pan for tilt up (+) and down (-).
         * @return Was the command executed successfully?
         * @note The default implementation does nothing than return false.
         *       So you don't need to override this method, if you don't support rotation of the cam.
         *       (It should be never called if you return the correct CarFeatures)
         */
		virtual bool rotateCam(int16_t pan, int16_t tilt) { return false; }
        /**
         * @brief Send the battery state
         * @return The charging level of the battery.
         *         0 is 0%,
         *         INT16_MAX is 100%,
         *         negative values are invalid.
         * @note The default implementation does nothing than return -1 (invalid value)
         *       So you don't need to override this method, if you don't support battery state.
         *       (It should be never called if you return the correct CarFeatures)
         */
		virtual int16_t getBatteryState() { return -1; }
        /**
         * @brief If this method returns true, all RequestCommands will be answered with BATTERY_NEAR_EMPTY.
         *        The user will be warned that he should charge the batteries.
         * @return True if the batteries near empty, else false.
         * @note The default implementation returns false.
         *       So you don't need to override this method, if you don't support it.
         */
		virtual bool answerBatteryNearEmpty() { return false; }
	private:
        /**
         * @brief Validates the crc8 checksum and the length.
         * @param buffer The command, that was read.
         * @param len The length of the buffer.
         * @return ture, if the command is valid.
         */
		bool validateCommand(byte buffer[], int len);
        /**
         * @brief Processes a valid command. It extract the command type from the buffer.
         *        executes the matching internal command method.
         * @param buffer The command, that was read.
         */
		void processCommand(byte buffer[]);
        
        /**
         * @brief Sends the given response message with a payload.
         * @param responseMsgType The type of the response message.
         * @param payload The additional data
         * @param payloadLen The length of the additional data
         */
		void sendResponse(ResponseMessage responseMsgType, byte payload[], unsigned int payloadLen);
        /**
         * @brief Sends the given response message without payload.
         * @param responseMsgType The type of the response message.
         */
		void sendResponse(ResponseMessage responseMsgType);
        /**
         * @brief Sends that a error occured. (A ERROR-Response)
         * @param errorId The Id of the error.
         */
        void sendError(ErrorId errorId);
        
        /**
         * @brief Process a NOOP-Command
         */
		void noopInternal();
        /**
         * @brief Process a GET_PROTOCOL_VERSION command: Send the protocolVersion in a PROTOCOL_VERSION response message.
         */
		void getProtocolVersionInternal();
        /**
         * @brief Process a GET_FEATURES command: Calls the getFeatures() and send the result in a FEATURES response message.
         */
		void getFeaturesInternal();
        /**
         * @brief Process a TURN_CAR command: Reads the additional data and passes it to turnCar()
         * @param payload Additional data from the command.
         */
		void turnCarInternal(byte payload[]);
        /**
         * @brief Process a ADJUST_SPEED command: Reads the additional data and passes it to adjustSpeed()
         * @param payload Additional data from the command.
         */
		void adjustSpeedInternal(byte payload[]);
        /**
         * @brief Process a ROTATE_CAM command: Reads the additional data and passes it to rotateCam()
         * @param payload Additional data from the command.
         */
		void rotateCamInternal(byte payload[]);
        /**
         * @brief Process a GET_BATTERY_STATE command: Calls the getBatteryState() and send the result in a BATTERY_STATE response message (or a PROCESS_COMMAND_ERROR, a invalid charging level is returned).
         */
		void getBatteryStateInternal();
        
        /**
         * @brief Read a int16_t from payload at pos.
         * @param payload The payload.
         * @param pos The pos where the int16_t should be read.
         * @return The read int.
         */
		inline int16_t getIntFromPayload(byte payload[], int pos) { 
			return word(payload[pos], payload[pos+1]); // TODO: Better
		}
        
        /**
         * @brief Puts a int16_t to the payload at pos.
         * @param value The int16_t.
         * @param payload The Payload
         * @param pos The position where the int6_t should be written.
         */
		inline void putIntIntoPayload(int16_t value, byte payload[], unsigned int pos) { 
			payload[pos] = (byte)(value >> 8); // TODO: Better
			payload[pos+1] = (byte)(value & 0xff);
		}
        
        /**
         * @brief Returns the expected payload length of the command type.
         * @param command The command type.
         * @return The payload length of the command type.
         */
		inline unsigned int getPayloadLen(RequestCommand command) {
			return requestPayloadLen[static_cast<int>(command)-1];
		}
        
        /**
         * @brief Returns the expected payload length of the response type.
         * @param responseMsg The response type.
         * @return The payload length of the response type.
         */
		inline unsigned int getPayloadLen(ResponseMessage responseMsg) {
			return responsePayloadLen[static_cast<int>(responseMsg)-1];
		}
		
        /**
         * @brief The communication interface with the smartphone.
         */
		AndroidAccessory accessory;
        /**
         * @brief For generating checksum.
         */
		CRC8 crc;
        /**
         * @brief The implemented version of the protocol
         */
		static const int16_t protocolVersion = ANDROID_RC_CAR_PROTOCOL_VERSION;
        /**
         * @brief The length of a commands (not the payload only).
         */
		static const unsigned int requestCommandSize = ANDROID_RC_CAR_REQUEST_CMD_SIZE;
        /**
         * @brief The length of a response (not the payload only).
         */
		static const unsigned int responseMsgSize = ANDROID_RC_CAR_RESPONE_MSG_SIZE;
        /**
         * @brief The list contains the lengths of the commands.
         */
		static const unsigned int requestPayloadLen[7];
        /**
         * @brief The list contains the lengths of the responses.
         */
		static const unsigned int responsePayloadLen[6];
};


#endif
