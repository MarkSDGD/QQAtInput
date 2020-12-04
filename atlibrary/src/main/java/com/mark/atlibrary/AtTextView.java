package com.mark.atlibrary;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import org.json.JSONException;

/**
 * Created by Mark on 2020/12/3.
 * <p>Copyright 2020 ZTZQ.</p>
 */
public class AtTextView extends AppCompatTextView {

    private final AtDelegate atDelegate = new AtDelegate(getContext(), getTextSize());

    public AtTextView(Context context) {
        this(context, null);
    }

    public AtTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AtTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    /**
     * Json string 2 spannable string spannable string.
     *
     * @param strjson the strjson
     * @return the spannable string
     * @throws JSONException the json exception
     */
    public SpannableString jsonString2SpannableString(String strjson) throws JSONException {
        return atDelegate.jsonString2SpannableString(strjson);
    }

}
