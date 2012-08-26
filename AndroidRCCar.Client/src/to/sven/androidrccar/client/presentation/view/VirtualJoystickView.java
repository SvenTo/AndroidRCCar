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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Two multitouch-enabled OnScreen Joysticks in one view.
 * @author sven
 *
 */
public class VirtualJoystickView extends View {

	/**
	 * {@link Paint} for drawing of the border
	 */
	private final Paint borderPaint = new Paint();
	/**
	 * {@link Paint} for filling the shapes.
	 */
	private final Paint paint = new Paint();
	/**
	 * The {@link OnVirtualJoystickMoveListener}
	 */
	private OnVirtualJoystickMoveListener listener;
	/**
	 * The left joystick
	 */
	private final VirtualJoystick leftJoystick =
			new VirtualJoystick(this, paint, borderPaint); 
	/**
	 * The right joystick
	 */
	private final VirtualJoystick rightJoystick = 
			new VirtualJoystick(this, paint, borderPaint);
	/**
	 * A list of both joystick
	 */
	private final VirtualJoystick[] joysticks = { leftJoystick, rightJoystick };
	
	/**
	 * @see View#View(Context, AttributeSet, int)
	 */
	public VirtualJoystickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * @see View#View(Context, AttributeSet)
	 */
	public VirtualJoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * @see View#View(Context)
	 */
	public VirtualJoystickView(Context context) {
		super(context);
		init();
	}
	
	/**
	 * Initializes the fixed values like colors.  
	 */
	private void init() {
		borderPaint.setStyle(Style.STROKE);
		paint.setARGB(100, 255, 255, 255);
		borderPaint.setStrokeWidth(1);
		borderPaint.setAntiAlias(true);
		
		paint.setARGB(100, 255, 255, 255);
		paint.setAntiAlias(true);
	}
	
	/**
	 * Sets the Listener
	 * @param listener {@link OnVirtualJoystickMoveListener}
	 */
	public void setOnVirtualJoystickMoveListener(OnVirtualJoystickMoveListener listener) {
		this.listener = listener;
	}

	/**
	 * Enables the left joystick.
	 * @see VirtualJoystick#setEnabled(boolean)
	 * @param enabled Is enabled?
	 */
	public void enableLeftStick(boolean enabled) {
		leftJoystick.setEnabled(enabled);
		invalidate();
	}

	/**
	 * Enables the right joystick.
	 * @see VirtualJoystick#setEnabled(boolean)
	 * @param enabled Is enabled?
	 */
	public void enableRightStick(boolean enabled) {
		rightJoystick.setEnabled(enabled);
		invalidate();
	}
	
	/**
	 * Draw the two joysticks
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		leftJoystick.draw(canvas);
		rightJoystick.draw(canvas);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		float height = h;
		float width = w;
		
		float top = height/2;
		float left = height/2;
		float right = width-height/2;
		float size = height/2;
		
		leftJoystick.onSizeChanged(top, left, size);
		rightJoystick.onSizeChanged(top, right, size);
		
		borderPaint.setStrokeWidth(size/100);
		invalidate();
	}
	
	/**
	 * The user touches the view. Inform the two Joysticks.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		for(VirtualJoystick joystick : joysticks) {
			joystick.onTouchEvent(event);
		}
		
		return true;
	}
	
	/**
	 * A joystick was moved. Inform the listener.
	 * @param joystick The Joystick that was moved
	 * @param x The new x-position (from -1 to +1)
	 * @param y The new y-position (from -1 to +1)
	 */
	public void updated(VirtualJoystick joystick, float x, float y) {
		invalidate();

		if(joystick == leftJoystick) {
			listener.onLeftMove(x, y);
		} else {
			listener.onRightMove(x, y);
		}
	}
}
