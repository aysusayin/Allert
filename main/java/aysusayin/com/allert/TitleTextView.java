package aysusayin.com.allert;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Aysu on 7.09.2017.
 */

public class TitleTextView extends android.support.v7.widget.AppCompatTextView {
    public TitleTextView(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"modernFont.ttf");
        this.setTypeface(typeface);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"modernFont.ttf");
        this.setTypeface(typeface);
    }

    public TitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"modernFont.ttf");
        this.setTypeface(typeface);
    }
}
