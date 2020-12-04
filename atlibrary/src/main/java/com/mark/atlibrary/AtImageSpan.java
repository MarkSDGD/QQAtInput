package com.mark.atlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

/**
 * Created by Mark on 2020/12/4.
 * <p>Copyright 2020 ZTZQ.</p>
 */
public class AtImageSpan extends DynamicDrawableSpan {
    private String showText;
    private String userId;
    private Drawable mDrawable;
    private Context mContext;
    private int mResourceId;
    private int start;
    private int end;
    private int spanBgResId;
    private int textColor;

    /**
     * Instantiates a new My image span.
     *
     * @param context     the context
     * @param mDrawable   the m drawable
     * @param start       the start
     * @param end         the end
     * @param showText    the show text
     * @param spanBgResId the span bg res id
     * @param textColor   the text color
     * @param userId      the user id
     */
    public AtImageSpan(Context context, Drawable mDrawable, int start, int end, String showText, int spanBgResId, int textColor, String userId) {
        this.mContext = context;
        this.mDrawable = mDrawable;
        this.start = start;
        this.end = end;
        this.showText = showText;
        this.userId = userId;
        this.spanBgResId = spanBgResId;
        this.textColor = textColor;
    }

    /**
     * Gets show text.
     *
     * @return the show text
     */
    public String getShowText() {
        return showText;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets start.
     *
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets end.
     *
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * Gets span bg res id.
     *
     * @return the span bg res id
     */
    public int getSpanBgResId() {
        return spanBgResId;
    }

    /**
     * Gets text color.
     *
     * @return the text color
     */
    public int getTextColor() {
        return textColor;
    }

    @Override
    public Drawable getDrawable() {
        return mDrawable;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            //获得文字、图片高度
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY = 0;
        //获得将要显示的文本高度 - 图片高度除2 = 居中位置+top(换行情况)
        transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
        //偏移画布后开始绘制
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}
