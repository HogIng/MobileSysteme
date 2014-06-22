package com.example.dbcheck;

import android.content.Context;
import android.graphics.Color;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class Graph extends LineGraphView {

	private GraphViewSeries series;
	private double seconds = 0d;

	public Graph(Context context, String title) {
		super(context, title);

		setManualYAxis(true);
		setManualYAxisBounds(120, 0);

		series = new GraphViewSeries(new GraphViewData[] {});
		series.getStyle().color = Color.rgb(63, 190, 232);

		setScrollable(true);

		getGraphViewStyle().setTextSize(22);
		getGraphViewStyle().setVerticalLabelsWidth(42);
		setDrawBackground(true);
		setVerticalLabels(new String[] { "120", "", "100", "", "80", "", "60",
				"", "40", "", "20", "", "0" });

		setHorizontalLabels(new String[] { "0", "", "", "", "", "", "15", "",
				"", "", "", "", "30" });

		setViewPort(0, 31);

		addSeries(series);

	}

	public void addValue(int dB) {

		if (seconds <= 31) {
			series.appendData(new GraphViewData(seconds, dB), false, 32);
			this.redrawAll();

		} else {
			series.appendData(new GraphViewData(seconds, dB), true, 32);
		}
		seconds += 1d;

	}

}
