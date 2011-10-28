package pt.up.fe.cmov.display;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class Display {
	
	public static SpannableStringBuilder styleText(String text,int color, int face){
		SpannableStringBuilder displayFormat = new SpannableStringBuilder(text);
        displayFormat.setSpan(new ForegroundColorSpan(color), 0, displayFormat.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        displayFormat.setSpan(new StyleSpan(face), 0, displayFormat.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return displayFormat;
	}
	
	public static TextView displayAppointment(String appointInfo,int size,Context context){
		return displayAppointment(appointInfo,size,context,Color.BLACK);
	}
	
	public static TextView displayAppointment(String appointInfo,int size,Context context,int textColor){
		TextView text = new TextView(context);
        text.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		SpannableStringBuilder displayFormat = Display.styleText(appointInfo,textColor,Typeface.BOLD);
		text.setTextSize(size);
        text.setText(displayFormat);
        text.setGravity(Gravity.FILL_HORIZONTAL);
		return text;
	}

}
