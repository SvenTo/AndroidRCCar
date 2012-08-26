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

#include "AndroidRCReferenceCar.h"

void AndroidRCReferenceCar::doSetUp() {
	panServo.attach(10);
	tiltServo.attach(9);
	
	resetAll();
	battery.setUp();
	battery.update();
}

void AndroidRCReferenceCar::resetAll() {
	adjustSpeed(0);
	rotateCam(0, 0);
}

int AndroidRCReferenceCar::getBatteryState() {
	battery.update();
	if(battery.batteryNearEmpty()) {
		resetAll();
	}
	return battery.getValue();
}

CarFeatures AndroidRCReferenceCar::getFeatures() { 
	CarFeatures carFeatures;
	carFeatures.adjustableSpeed = true;
	carFeatures.driveBackward = true;
	carFeatures.batteryPower = true;
    carFeatures.cameraPanMin = map(90, 0, 180, 0, INT16_MAX); // TODO: Const?  // TODO: 180°-23°
	carFeatures.cameraPanMax = map(90, 0, 180, 0, INT16_MAX); // TODO: Const?
	carFeatures.cameraTiltMin = 0;//map(23, 0, 180, 0, INT16_MAX); 
	carFeatures.cameraTiltMax = INT16_MAX;
	return carFeatures;
}

bool AndroidRCReferenceCar::adjustSpeed(int16_t speed) {
	if(speed == INT16_MIN) { speed = -INT16_MAX; }
	currentSpeed = speed;
	updateMotors();
	return true;
} 

bool AndroidRCReferenceCar::turnCar(int16_t rotation) {  
	if(rotation == INT16_MIN) { rotation = -INT16_MAX; }
	currentRotation = rotation;
	updateMotors();
	return true;
}

bool AndroidRCReferenceCar::rotateCam(int16_t pan, int16_t tilt) {
	if(tilt < 0) {
		return false;
	}
	int panS = mapServoPanValue(pan);
	int tiltS = mapServoTiltValue(tilt);
	panServo.write(panS);
	tiltServo.write(tiltS);
	
	return true;
}

void AndroidRCReferenceCar::updateMotors() {
	if(currentSpeed > 0) {
		setMotorRun(FORWARD);
	} else if(currentSpeed < 0) {
		setMotorRun(BACKWARD);
	} else {
		setMotorRun(RELEASE);
	}
	int leftWheelSpeed = mapLeftMotorSpeed();
	int rightWheelSpeed = mapRightMotorSpeed();
	frontLeftMotor.setSpeed(leftWheelSpeed);
	backLeftMotor.setSpeed(leftWheelSpeed);
	frontRightMotor.setSpeed(rightWheelSpeed);
	backRightMotor.setSpeed(rightWheelSpeed);
}

void AndroidRCReferenceCar::setMotorRun(int run) {
		frontLeftMotor.run(run);
		frontRightMotor.run(run);
		backLeftMotor.run(run);
		backRightMotor.run(run);
}

int AndroidRCReferenceCar::mapServoPanValue(int16_t receivedPan) {
	return map(receivedPan, INT16_MIN, INT16_MAX, 23, 180);
}

int AndroidRCReferenceCar::mapServoTiltValue(int16_t receivedTilt) {
	return map(receivedTilt, 0, INT16_MAX, 10, 180);
}

int AndroidRCReferenceCar::mapLeftMotorSpeed() {
	int toMax = 255;
	if(currentRotation < 0) {
		toMax = map(abs(currentRotation), 0, INT16_MAX, toMax-100, 60);
	}
	return map(abs(currentSpeed), 0, INT16_MAX, 0, toMax);
}

int AndroidRCReferenceCar::mapRightMotorSpeed() {
	int toMax = 255;
	if(currentRotation > 0) {
		toMax = map(abs(currentRotation), 0, INT16_MAX, toMax-100, 60);
	}
	return map(abs(currentSpeed), 0, INT16_MAX, 0, toMax);
}

bool AndroidRCReferenceCar::answerBatteryNearEmpty() {
	return battery.batteryNearEmpty();
}
