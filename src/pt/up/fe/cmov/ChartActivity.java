package pt.up.fe.cmov;

import pt.up.fe.cmov.display.GraphView;
import android.app.Activity;
import android.os.Bundle;

public class ChartActivity  extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		float[] values = new float[] { 2.0f,1.5f, 2.5f, 1.0f , 3.0f };
		String[] verlabels = new String[] { "great", "ok", "bad" };
		String[] horlabels = new String[] { "today", "tomorrow", "next week", "next month" };
		GraphView graphView = new GraphView(this, values, "GraphViewDemo",horlabels, verlabels, GraphView.BAR);
		setContentView(graphView);
	}
}