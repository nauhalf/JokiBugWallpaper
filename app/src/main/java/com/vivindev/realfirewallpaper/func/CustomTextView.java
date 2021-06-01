package com.vivindev.realfirewallpaper.func;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by kingdov on 17/01/2017.
 */

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);
        setFont();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
