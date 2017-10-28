package aysusayin.com.allert;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Aysu on 6.09.2017.
 */

public class LoginTextView extends android.support.v7.widget.AppCompatTextView {
    public LoginTextView(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "monoglyceridebold.ttf");
        this.setTypeface(typeface);
    }

    public LoginTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "monoglyceridebold.ttf");
        this.setTypeface(typeface);
    }

    public LoginTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "monoglyceridebold.ttf");
        this.setTypeface(typeface);
    }
}
