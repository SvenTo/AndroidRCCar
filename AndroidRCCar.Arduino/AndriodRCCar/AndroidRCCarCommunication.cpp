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

#include "AndroidRCCarCommunication.h"
#include <string.h>

// TODO: Not here?
/*!
 * \brief Pure-virtual workaround.
 *
 * The avr-libc does not support a default implementation for handling 
 * possible pure-virtual calls. This is a int and empty workaround for this.
 */
extern "C" {
  void __cxa_pure_virtual()
  {
    // put error handling here
  }
}

void AndroidRCCarCommunication::setUp() {
	accessory.powerOn();
	doSetUp();
}

bool AndroidRCCarCommunication::isConnected() {
	return accessory.isConnected();
}

void AndroidRCCarCommunication::handleNextCommand() {
	byte buffer[requestCommandSize];
	int len = accessory.read(buffer, sizeof(buffer));
	if(len == -1) {
		// Received no nothing, yet.
		return;
	} else if(!validateCommand(buffer, len)) {
		sendError(INVALID_COMMAND_ERROR);
	} else if(answerBatteryNearEmpty()) {
		// Do nothing, because battery is near empty.
		sendResponse(BATTERY_NEAR_EMPTY);
	} else {
		// All OK!
		processCommand(buffer);
	}
}

void AndroidRCCarCommunication::processCommand(byte buffer[]) {
	byte command = buffer[0];
	byte *payload = &buffer[1];
	switch(command) {
		case NOOP:
			noopInternal();
			break;
		case GET_PROTOCOL_VERSION:
			getProtocolVersionInternal();
			break;
		case GET_FEATURES:
			getFeaturesInternal();
			break;
		case TURN_CAR:
			turnCarInternal(payload);
			break;
		case ADJUST_SPEED:
			adjustSpeedInternal(payload);
			break;
		case ROTATE_CAM:
			rotateCamInternal(payload);
			break;
		case GET_BATTERY_STATE:
			getBatteryStateInternal();
			break;
		default :
			sendError(UNKOWN_COMMAND_ERROR);
	}
}

bool AndroidRCCarCommunication::validateCommand(byte buffer[], int len) {
	if(len == requestCommandSize) {
		//Serial.print("Read command : 0x"); // TODO: Debug!
		//for(int i = 0; i < len;i++) { Serial.print(buffer[i], HEX); Serial.print(" "); } // TODO: Debug!
 		//Serial.println(""); // TODO: Debug!
		
		byte checksum = buffer[requestCommandSize-1];
		byte expectedChecksum = crc.from_array(buffer, len-1);
		if(expectedChecksum == checksum) {
			return true;
		} else {
			//Serial.print("Invalid Checksum: 0x"); // TODO: Debug!
			//Serial.print(checksum, HEX); // TODO: Debug!
			//Serial.print("- Expected 0x"); // TODO: Debug!
			//Serial.println(expectedChecksum, HEX); // TODO: Debug!
		}
	}
	return false;
}

void AndroidRCCarCommunication::noopInternal() {
	sendResponse(REQUEST_OK);
}

void AndroidRCCarCommunication::getProtocolVersionInternal() {
	unsigned int len = getPayloadLen(PROTOCOL_VERSION);
	byte payload[len];
	putIntIntoPayload(protocolVersion, payload, 0);
	sendResponse(PROTOCOL_VERSION, payload, len);
}

void AndroidRCCarCommunication::getFeaturesInternal() {
	CarFeatures features = getFeatures();
	unsigned int len = getPayloadLen(FEATURES);
	byte payload[len];
	bitWrite(payload[0], 0, features.adjustableSpeed);
	bitWrite(payload[0], 1, features.driveBackward);
	bitWrite(payload[0], 2, features.batteryPower);
	putIntIntoPayload(features.cameraPanMin, payload, 1);
	putIntIntoPayload(features.cameraPanMax, payload, 3);
	putIntIntoPayload(features.cameraTiltMin, payload, 5);
	putIntIntoPayload(features.cameraTiltMax, payload, 7);
	
	sendResponse(FEATURES, payload, len);
}

void AndroidRCCarCommunication::turnCarInternal(byte payload[]) {
	int16_t rotation = getIntFromPayload(payload, 0);
	if(turnCar(rotation)) {
		sendResponse(REQUEST_OK);
	} else {
		sendError(PROCESS_COMMAND_ERROR);
	}
}

void AndroidRCCarCommunication::adjustSpeedInternal(byte payload[]) {
	int16_t speed = getIntFromPayload(payload, 0);
	if(adjustSpeed(speed)) {
		sendResponse(REQUEST_OK);
	} else {
		sendError(PROCESS_COMMAND_ERROR);
	}
}

void AndroidRCCarCommunication::rotateCamInternal(byte payload[]) {
	int16_t pan = getIntFromPayload(payload, 0);
	int16_t tilt = getIntFromPayload(payload, 2);
	if(rotateCam(pan, tilt)) {
		sendResponse(REQUEST_OK);
	} else {
		sendError(PROCESS_COMMAND_ERROR);
	}
}

void AndroidRCCarCommunication::getBatteryStateInternal() {
	int16_t chargingLevel = getBatteryState();
	
	if(chargingLevel >= 0) {
		unsigned int len = getPayloadLen(BATTERY_STATE);
		byte payload[len];
		putIntIntoPayload(chargingLevel, payload, 0);
		sendResponse(BATTERY_STATE, payload, len);
	} else {
		sendError(PROCESS_COMMAND_ERROR);
	}
}

void AndroidRCCarCommunication::sendError(ErrorId errorId) {
	unsigned int len = getPayloadLen(ERROR);
	byte payload[len];
	putIntIntoPayload(static_cast<int16_t>(errorId), payload, 0);
	sendResponse(ERROR, payload, len);
}

void AndroidRCCarCommunication::sendResponse(ResponseMessage responseMsgType, byte payload[], unsigned int payloadLen) {
	//Serial.print("sendResponse: 0x"); // TODO: Debug.
	
	byte response[responseMsgSize] = { 0x00 };
	
	response[0] = static_cast<byte>(responseMsgType);
	if(payloadLen > 0) {
		memcpy(&response[1], payload, payloadLen);
	}
	response[responseMsgSize-1] = crc.from_array(response, responseMsgSize-1);
	
	accessory.beginTransmission();
	accessory.write(response, sizeof(response));
	accessory.endTransmission();
	
	//for(int i = 0; i < sizeof(response);i++) { Serial.print(response[i], HEX); Serial.print(" "); }  // TODO: Debug.
	//Serial.println(""); // TODO: Debug.
}

void AndroidRCCarCommunication::sendResponse(ResponseMessage responseMsgType) {
	sendResponse(responseMsgType, NULL, 0);
}

const unsigned int AndroidRCCarCommunication::requestPayloadLen[7] = { 0, 0, 0, 2, 2, 4, 0 };
const unsigned int AndroidRCCarCommunication::responsePayloadLen[6] = { 0, 2, 0, 2, 9, 2 };
