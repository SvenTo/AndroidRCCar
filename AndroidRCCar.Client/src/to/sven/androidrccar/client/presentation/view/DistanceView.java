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

import to.sven.androidrccar.client.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Draws a circle with a arrow to bearing of the car and writes the distance under it.
 * @author sven
 *
 */
public class DistanceView extends View {
	
	/**
	 * {@link Paint} for drawing of the border
	 */
	private final Paint borderPaint = new Paint();
	/**
	 * {@link Paint} for filling the shapes.
	 */
	private final Paint fillPaint = new Paint();
	/**
	 * {@link Paint} for the text.
	 */
	private final Paint textPaint = new Paint();
	/**
	 * The {@link Path} of the direction indicator.
	 */
	private Path path;
	/**
	 * Center point of the circle.
	 */
	private float mTop;
	/**
	 * Center point of the circle.
	 */
	private float mLeft;
	/**
	 * Radius point of the circle.
	 */
	private float radius;
	/**
	 * north string.
	 */
	private String northStr;
	/**
	 * The top position of the distance string.
	 */
	private float distanceTop;
	/**
	 * The distance as string.
	 */
	private String distanceStr;
	/**
	 * Bearing in degree from north to east. 
	 */
	private float bearing;
	
	/**
	 * @see View#View(Context, AttributeSet, int)
	 */
	public DistanceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * @see View#View(Context, AttributeSet)
	 */
	public DistanceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * @see View#View(Context)
	 */
	public DistanceView(Context context) {
		super(context);
		init();
	}
	
	/**
	 * Initializes the fixed values like colors.  
	 */
	private void init() {
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeJoin(Paint.Join.ROUND);
		borderPaint.setStrokeCap(Paint.Cap.ROUND);
		borderPaint.setAntiAlias(true);
		borderPaint.setShadowLayer(0.2f, 0.0f, 0.0f, 0xFF000000);
		fillPaint.setARGB(50, 255, 255, 255);
		fillPaint.setAntiAlias(true);
		
		textPaint.setAntiAlias(true);
		textPaint.setARGB(255, 255, 255, 255);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_size));
		
		textPaint.setShadowLayer(0.02f, 0.0f, 0.0f, 0xFF000000);

		northStr = getContext().getString(R.string.north);
		distanceStr = "- " + getContext().getString(R.string.meter);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawText(northStr, mLeft, textPaint.getTextSize(), textPaint);
		canvas.drawText(distanceStr, mLeft, distanceTop, textPaint);

		canvas.drawCircle(mLeft, mTop, radius, fillPaint);
		canvas.drawCircle(mLeft, mTop, radius, borderPaint);
		
		canvas.rotate(bearing, mLeft, mTop);
		canvas.drawPath(path, fillPaint);
		canvas.drawPath(path, borderPaint);
	}
	
	/**
	 * Sets the distance and bearing. The view will be redrawn at some point in future.
	 * @param distance Distance in meters
	 * @param bearingTo Bearing in degree from north to east. 
	 */
	public void setDistance(float distance, float bearingTo) {
		this.bearing = bearingTo;
		distanceStr = String.format("%.2f ", distance) + " " + getContext().getString(R.string.meter);
		
		invalidate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		float width = w;
		float strokeWith = width/20;
		
		borderPaint.setStrokeWidth(strokeWith);

		float topMargin = textPaint.getTextSize()+strokeWith;
		radius = w/2-2*strokeWith;
		mTop = w/2+topMargin;
		mLeft = w/2;
		float triangleH = radius*3/2;
		float xDiff = (float) (Math.sqrt(3)*radius/2)/2;
		
		path = new Path();
		path.moveTo(mLeft, topMargin);
		path.lineTo(mLeft+xDiff, triangleH+topMargin);
		path.lineTo(mLeft-xDiff, triangleH+topMargin);
		path.close();
		
		distanceTop = topMargin+w+textPaint.getTextSize();
		
		invalidate();
	}
}
