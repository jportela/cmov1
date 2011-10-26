package pt.up.fe.cmov.display;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class Display {
	
	public static SpannableStringBuilder styleText(String text,int color, int face){
		SpannableStringBuilder displayFormat = new SpannableStringBuilder(text);
        displayFormat.setSpan(new ForegroundColorSpan(color), 0, displayFormat.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        displayFormat.setSpan(new StyleSpan(face), 0, displayFormat.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return displayFormat;
	}

}
