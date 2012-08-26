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

#ifndef __AndroidRCReferenceCar_h__
#define __AndroidRCReferenceCar_h__

#define __STDC_LIMIT_MACROS
#include <stdint.h>
#include <AndroidRCCarCommunication.h>
#include <CarFeatures.h>
#include <AFMotor.h>
#include <Servo.h> 
#include<Arduino.h>
#include "TwoCellLiPolBattery.h"

/**
 * @brief The AndroidRCReferenceCar class providing the implementation
 *        of the software for the reference hardware.
 */
class AndroidRCReferenceCar : public AndroidRCCarCommunication {
	public:
        /**
         * @brief Default constructor
         */
		AndroidRCReferenceCar():
			AndroidRCCarCommunication("Reference Car"),
			frontLeftMotor(4),
			frontRightMotor(3),
			backLeftMotor(1),
			backRightMotor(2),
			panServo(Servo()),
			tiltServo(Servo()),
			battery(40, A0, A1),
			currentSpeed(0),
			currentRotation(0)
			{
		}
        /**
         * @brief Stops all motors and rotates the camera to initial position.
         */
		void resetAll();
	protected:
        /**
         * {@inheritDoc}
         */
		virtual bool turnCar(int16_t rotation);
        
        /**
         * {@inheritDoc}
         */
		virtual int16_t getBatteryState();
        
        /**
         * {@inheritDoc}
         * (All Features are supported.)
         */
		virtual CarFeatures getFeatures();
        
        /**
         * {@inheritDoc}
         */
		virtual bool adjustSpeed(int16_t speed);
        
        /**
         * {@inheritDoc}
         */
		virtual bool rotateCam(int16_t pan, int16_t tilt);
        
        /**
         * @brief Attach Servo, resetAll() and check battery power
         */
		virtual void doSetUp();
        
        /**
         * {@inheritDoc}
         */
		virtual bool answerBatteryNearEmpty();
	private:
        /**
         * @brief The speed or rotation wheel has changed. Set new motor speed.
         */
		void updateMotors();
        /**
         * @brief Sets the run of the motor.
         * @param run
         */
		void setMotorRun(int run);
        
        /**
         * @brief Maps a received pan value to the correct value for the servo.
         * @param receivedPan The received pan to map
         * @return The received value mapped to the value for the servo.
         */
		int  mapServoPanValue(int16_t receivedPan);        
        /**
         * @brief Maps a received tilt value to the correct value for the servo.
         * @param receivedPan The received pan to map
         * @return The received value mapped to the value for the servo.
         */
		int  mapServoTiltValue(int16_t receivedTilt);
        /**
         * @brief Returns the mapped speed for the left motors.
         *        The value is mapped from the speed combined with the turnCar value.
         * @return mapped speed for the left motors
         */
		int  mapLeftMotorSpeed();
        /**
         * @brief Returns the mapped speed for the right motors.
         *        The value is mapped from the speed combined with the turnCar value.
         * @return mapped speed for the left motors
         */
		int  mapRightMotorSpeed();

        /**
         * @brief The object for reading the battery state
         */
		TwoCellLiPolBattery battery;
        /**
         * @brief Front left motor
         */
		AF_DCMotor frontLeftMotor;
        /**
         * @brief Front right motor
         */
		AF_DCMotor frontRightMotor;
        /**
         * @brief Back left motor
         */
		AF_DCMotor backLeftMotor;
        /**
         * @brief Back right motor
         */
		AF_DCMotor backRightMotor;
        /**
         * @brief Servo for panning the camera
         */
		Servo panServo;
        /**
         * @brief Servo for tilting the camera
         */
		Servo tiltServo;
        /**
         * @brief currentSpeed of the car as the received value (not mapped!)
         */
		int16_t currentSpeed;
        /**
         * @brief currentRotation of the car as the received value (not mapped!)
         */
		int16_t currentRotation;
};

#endif
