package com.dy.fastframework.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextView700Title extends androidx.appcompat.widget.AppCompatTextView {

    public TextView700Title(Context context) {
        super(context);
    }


    public TextView700Title(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, android.R.attr.textViewStyle);
    }
}