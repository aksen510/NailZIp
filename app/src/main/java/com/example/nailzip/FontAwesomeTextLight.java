package com.example.nailzip;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class FontAwesomeTextLight extends androidx.appcompat.widget.AppCompatTextView {
    public FontAwesomeTextLight(Context context){
        super(context);
        init();
    }
    public FontAwesomeTextLight(Context context, AttributeSet attrs){
        super(context,attrs);
        init();
    }
    public  FontAwesomeTextLight(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"font awesome 6 free_solid_900.otf");
        setTypeface(typeface);
    }
}
