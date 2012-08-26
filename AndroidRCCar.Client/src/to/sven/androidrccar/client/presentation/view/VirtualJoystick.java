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
package to.sven.androidrccar.client.presentation.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Represent one joystick on the {@link VirtualJoystickView}.
 * @author sven
 *
 */
public class VirtualJoystick {
	/**
	 * The numbers of the joysticks, for debugging reasons.
	 */
	private static int num = 0;
	/**
	 * The log tag
	 */
	@SuppressWarnings("unused")
	private final String LOG_TAG = "VirtualJoystick" + num++;
	
	/**
	 * The parent
	 */
	private final VirtualJoystickView view;
	
	/**
	 * The paint for filling the circles.
	 */
	private final Paint paint;
	
	/**
	 * The paint for the borders of the circle.
	 */
	private final Paint borderPaint;
	
	/**
	 * The top position of the static big circle center.
	 */	
	private float top;
	/**
	 * The left position of the static big circle center. 
	 */
	private float left;
	/**
	 * The radius of the static big circle. 
	 */
	private float size;
	
	/**
	 * The size of the stick (radius).
	 */
	private float stickSize;
	/**
	 * The relative left position of the stick center to left.
	 */
	private float stickLeft;
	/**
	 * The relative top position of the stick center to top.
	 */
	private float stickTop;
	
	/**
	 * X-Position of the stick mapped to -1 to +1.
	 */
	private float mappedX;

	/**
	 * Y-Position of the stick mapped to -1 to +1.
	 */
	private float mappedY;
	
	/**
	 * The index of the active touch pointer
	 */
	private int pointerId = -1;
	
	/**
	 * Is the Joystick enabled?
	 */
	private boolean enabled = false;
	
	/**
	 * Default Constructor
	 * @param view The Parent
	 * @param paint The paint for filling the circles.
	 * @param borderPaint The paint for the borders of the circle.
	 */
	public VirtualJoystick(VirtualJoystickView view,
						   Paint paint,
						   Paint borderPaint) {
		this.view = view;
		this.paint = paint;
		this.borderPaint = borderPaint;
	}
	
	/**
	 * Sets/Calculates new drawing positions and sizes.
	 * @param nTop New {@link #top}
	 * @param nLeft New {@link #left}
	 * @param nSize New {@link #size}
	 */
	public void onSizeChanged(float nTop, float nLeft, float nSize) {
		this.top = nTop;
		this.left = nLeft;
		this.size = nSize;
		this.stickSize = 0.4f*size;
	}
	
	/**
	 * Is the Joystick enabled? Default is false.
	 * @see #setEnabled(boolean)
	 * @return Is the Joystick enabled?
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Enables ode disables the joystick. If its not enabled, the joystick can't moved and won't drawn.
	 * @param enabled Enable it?
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * Draw the Joystick on the canvas (if enabled).
	 * @param canvas The canvas.
	 */
	public void draw(Canvas canvas) {
		if(enabled) {
			canvas.drawCircle(left, top, size, paint);
			canvas.drawCircle(left, top, size, borderPaint);
			canvas.drawCircle(left + stickLeft, top + stickTop,
							  stickSize, paint);
			canvas.drawCircle(left + stickLeft, top + stickTop, 
							  stickSize, borderPaint);
		}
	}
	
	/**
	 * Handles the {@link VirtualJoystickView#onTouchEvent(MotionEvent)} of this stick.
	 * @param event A {@link MotionEvent}
	 */
	public void onTouchEvent(MotionEvent event) {
		int pointerIndex = event.getPointerId(event.getActionIndex());
		
		int actionResolved = event.getAction() & MotionEvent.ACTION_MASK;
		if (actionResolved < 7 && actionResolved > 4) {
			actionResolved = actionResolved - 5;
		}
		
		switch(actionResolved)
		{
			case MotionEvent.ACTION_DOWN:
				if(isOverAndHasNoPointer(pointerIndex, event)) {
					this.pointerId = pointerIndex;
					move(event.getX(), event.getY());
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(pointerId != -1) {
					float x = event.getX(pointerId);
					float y = event.getY(pointerId);
					move(x, y);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if(pointerIndex == pointerId) {
					clearPointer();
				}
				break;
		}
	}
	
	/**
	 * Reset to center, because there is no touch anymore.
	 */
	private void clearPointer() {
		pointerId = -1;
		stickTop = 0;
		stickLeft = 0;
		
		updated();
	}
	
	/**
	 * Move stick
	 * @param x Absolute x-Position
	 * @param y Absolute y-Position
	 */
	private void move(float x, float y) {
		stickLeft = x-left;
		stickTop = y-top; // TODO: constrain?
		
		updated();
	}
	
	/**
	 * Stick position has changed calculate new positions 
	 */
	private void updated() {
		float x = stickLeft/size;
		float y = stickTop/size;
		
		x = constrain(x, -1, 1);
		y = constrain(y, -1, 1);
		
		if(x != mappedX || y != mappedY) {
			mappedX = x;
			mappedY = y;
			// Only inform view, if enabled:
			if(enabled) {
				view.updated(this, x ,y);	
			}
		}
	}
	
	/**
	 * Checks if the Touch Event is over the area of this joystick.
	 * @param pointerIndex The Touch Pointer
	 * @param event The {@link MotionEvent}
	 * @return True, if its over.
	 */
	private boolean isOverAndHasNoPointer(int pointerIndex, MotionEvent event) {
		float x = event.getX(pointerIndex)-left;
		float y = event.getY(pointerIndex)-top;

		return (pointerId == -1) &&
			   y >= -size && y <= size &&
			   x >= -size && x <= size;
	}
	
	/**
	 * Constrain the value.
	 * @param value The value
	 * @param min Minimal value.
	 * @param max Maximal value.
	 * @return A value in the constrain.
	 */
	private static float constrain(float value, float min, float max) {
		if(value > max) {
			value = max;
		} else if(value < min) {
			value = min;
		}
		return value;
	}
}
