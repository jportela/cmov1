package pt.up.fe.cmov.display;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;


public class GraphView extends View {

	public static boolean BAR = true;
	public static boolean LINE = false;

	private Paint paint;
	private ArrayList<Float> values;
	private ArrayList<String> horlabels;
	private ArrayList<String> verlabels;
	private String title;
	private boolean type;

	public GraphView(Context context, ArrayList<Float> values, String title, ArrayList<String> horlabels, ArrayList<String> verlabels, boolean type) {
		super(context);
		this.values = values;
		this.title = title;
		this.horlabels = horlabels;
		this.verlabels = verlabels;
		this.type = type;
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float border = 20;
		float horstart = border * 2;
		float height = getHeight();
		float width = getWidth() - 1;
		float max = getMax();
		float min = getMin();
		float diff = max - min;
		float graphheight = height - (2 * border);
		float graphwidth = width - (2 * border);

		paint.setTextAlign(Align.LEFT);
		
	   for (int i = 0; i < verlabels.size(); i++) {
			
	   }
		
	   int hors = horlabels.size() - 1;
	   for (int i = 0; i < horlabels.size(); i++) {
			paint.setColor(Color.LTGRAY);
			float x = ((graphwidth / hors) * i) + horstart;
			canvas.drawLine(x, height - border, x, border, paint);
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(14);
			if (i==horlabels.size()-1)
				paint.setTextAlign(Align.RIGHT);
			if (i==0)
				paint.setTextAlign(Align.LEFT);
			paint.setColor(Color.BLACK);
			canvas.drawText(horlabels.get(i), x, height - 4, paint);
		}

		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(20);
		canvas.drawText(title, (graphwidth / 2) + horstart, border - 4, paint);

		if (max != min) {
			paint.setColor(Color.BLACK);
			if (type == BAR) {
				float datalength = values.size();
				float colwidth = (width - (2 * border)) / datalength;
				for (int i = 0; i < values.size(); i++) {
					float val = values.get(i) - min;
					float rat = val / diff;
					float h = graphheight * rat;
					paint.setColor(Color.BLACK);
					canvas.drawText(verlabels.get(i), 10, (border - h) + graphheight, paint);
					paint.setColor(Color.LTGRAY);
					canvas.drawLine(horstart, (border - h) + graphheight, width, (border - h) + graphheight, paint);
					paint.setColor(Color.BLACK);
					canvas.drawRect((i * colwidth) + horstart, (border - h) + graphheight, ((i * colwidth) + horstart) + (colwidth - 1), height - (border - 1), paint);
				}
			} else {
				float datalength = values.size();
				float colwidth = (width - (2 * border)) / datalength;
				float halfcol = colwidth / 2;
				float lasth = 0;
				for (int i = 0; i < values.size(); i++) {
					paint.setColor(Color.BLACK);
					float val = values.get(i) - min;
					float rat = val / diff;
					float h = graphheight * rat;
					canvas.drawText(verlabels.get(i), 10, (border - h) + graphheight, paint);
					paint.setStrokeWidth(1);
					paint.setColor(Color.LTGRAY);
					canvas.drawLine(horstart, (border - h) + graphheight, width, (border - h) + graphheight, paint);
					if (i > 0){
						paint.setStrokeWidth(8);
						paint.setColor(Color.BLACK);
						canvas.drawLine(((i - 1) * colwidth) + (horstart + 1) + halfcol, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
					}
					lasth = h;
				}
			}
		}
	}

	private float getMax() {
		float largest = Integer.MIN_VALUE;
		for (int i = 0; i < values.size(); i++)
			if (values.get(i) > largest)
				largest = values.get(i);
		return largest;
	}

	private float getMin() {
		float smallest = Integer.MAX_VALUE;
		for (int i = 0; i < values.size(); i++)
			if (values.get(i) < smallest)
				smallest = values.get(i);
		return smallest;
	}

}
