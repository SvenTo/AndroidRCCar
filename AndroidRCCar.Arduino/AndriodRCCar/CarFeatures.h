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

#ifndef __CarFeatures_h__
#define __CarFeatures_h__

#define __STDC_LIMIT_MACROS
#include <stdint.h>

/**
 * @brief This scrut is used to define the features of the car.
 */
struct CarFeatures {
    /**
     * @brief Is the speed adjustable. If not the car can only drive with constant speed. 
     */
	bool adjustableSpeed;
    /**
     * @brief Car can drive backward.
     */
	bool driveBackward;
    /**
     * @brief Car provides information about remaining battery power of the car.
     */
	bool batteryPower;
    /**
     * @brief The maximum pan of the camera to the left.
     * 0 is 0 degree,
     * INT16_MAX is 180 degree,
     * negative values are invalid.
     */
	int16_t cameraPanMin;
    /**
     * @brief The maximum pan of the camera to the right.
     * 0 is 0 degree,
     * INT16_MAX is 180 degree,
     * negative values are invalid.
     */
	int16_t cameraPanMax;
    /**
     * @brief The maximum tilt of the camera down.
     * 0 is 0 degree,
     * INT16_MAX is 180 degree,
     * negative values are invalid.
     */
	int16_t cameraTiltMin;
    /**
     * @brief The maximum tilt of the camera up.
     * 0 is 0 degree,
     * INT16_MAX is 180 degree,
     * negative values are invalid.
     */
	int16_t cameraTiltMax;
};

#endif
