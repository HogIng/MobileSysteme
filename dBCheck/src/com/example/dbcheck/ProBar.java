package com.example.dbcheck;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ProBar extends ProgressBar {

	public ProBar(Context context) {
		super(context);
		setMax(240);
	}

	public ProBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);

	}

	@Override
	protected void onDraw(Canvas c) {
		c.rotate(-90);
		c.translate(-getHeight(), 0);
		super.onDraw(c);
	}
	
	

}
