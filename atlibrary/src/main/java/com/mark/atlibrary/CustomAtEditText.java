package com.mark.atlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.mark.atlibrary.listener.InputAtListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * author: MARK
 * company: ZTZQ
 * created on: 2020/11/16 16:16
 * description: CustomAtEditText.java
 */
public class CustomAtEditText extends AppCompatAutoCompleteTextView {
    public static final int DEFAULT_BG =0;
    public static final int DEFAULT_TEXT_COLOR=0;
    private InputAtListener inputAtListener;
    private boolean isRestoreFromDraft =false;

    private boolean isOnlySupportLastAt =false;
    /**
     * Instantiates a new Custom at edit text.
     *
     * @param context the context
     */
    public CustomAtEditText(Context context) {
        this(context,null);
    }


    /**
     * Instantiates a new Custom at edit text.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomAtEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * Instantiates a new Custom at edit text.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CustomAtEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(defaultTextWatcher);
    }


    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        MyImageSpan[] spans = getText().getSpans(0, getText().length(), MyImageSpan.class);
        for (MyImageSpan myImageSpan : spans) {
            if (getText().getSpanEnd(myImageSpan) - 1 == selStart) {
                selStart = selStart + 1;
                setSelection(selStart);
                break;
            }
        }
        super.onSelectionChanged(selStart, selEnd);
    }


    /**
     * 添加一个span块.
     *
     * @param showText    生成span块的文字
     * @param spanBgResId span块的背景
     * @param textColor   span块文字颜色
     * @param userId      span块对应的成员id
     */
    public void addSpan(String showText, int spanBgResId, int textColor, String userId) {
        if(isOnlySupportLastAt){
            getText().append(showText);
            SpannableString spannableString = new SpannableString(getText());
            generateSpan(spannableString, spannableString.length() - showText.length(), spannableString.length(), showText,spanBgResId,textColor, userId);
            setText(spannableString);
            setSelection(spannableString.length());
        }else{
            int insertPos=getSelectionStart();
            getText().insert(getSelectionStart(),showText);
            SpannableString spannableString = new SpannableString(getText());
            generateSpan(spannableString, getSelectionStart()- showText.length(), getSelectionStart(), showText,spanBgResId,textColor, userId);
            setText(spannableString);
            setSelection(insertPos+showText.length());
        }
    }

    private void generateSpan(Spannable spannableString, int start, int end, String showText, int spanBgResId, int textColor, String userId) {
        View spanView = getSpanView(getContext(), showText, spanBgResId,textColor);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) convertViewToDrawable(spanView);
        bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());
        MyImageSpan what = new MyImageSpan(getContext(), bitmapDrawable,start,end,showText,spanBgResId,textColor, userId);
        spannableString.setSpan(what, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * Convert view to drawable drawable.
     *
     * @param view the view
     * @return the drawable
     */
    public Drawable convertViewToDrawable(View view) {
        int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        cacheBmp.recycle();
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }

    /**
     * Gets span view.
     *
     * @param context     the context
     * @param text        the text
     * @param spanBgResId the span bg res id
     * @param textColor   the text color
     * @return the span view
     */
    public View getSpanView(Context context, String text, int spanBgResId, int textColor) {
        TextView view = new TextView(context);
        //view.setMaxWidth(maxWidth);
        view.setText(text);
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setSingleLine(true);
        //设置文字框背景色
        if(spanBgResId==DEFAULT_BG){
            view.setBackgroundResource(R.drawable.shape_corner_rectangle);
        }else{
            view.setBackgroundResource(spanBgResId);
        }
        view.setTextSize(getTextSize());
        //设置文字颜色
        if(spanBgResId==DEFAULT_TEXT_COLOR){
            view.setTextColor(Color.BLUE);
        }else{
            view.setTextColor(textColor);
        }
        return view;
    }

    public void setInputAtListener(InputAtListener inputAtListener) {
        this.inputAtListener = inputAtListener;
    }
    public void setRestoreFromDraft(boolean restoreFromDraft) {
        isRestoreFromDraft = restoreFromDraft;
    }

    /** 获取@模式
     * @return
     */
    public boolean isOnlySupportLastAt() {
        return isOnlySupportLastAt;
    }

    /** 设置@模式
     * @param onlySupportLastAt  true 最后输入@有效；false,任何位置输入@有效
     */
    public void setOnlySupportLastAt(boolean onlySupportLastAt) {
        isOnlySupportLastAt = onlySupportLastAt;
    }

    private class MyImageSpan extends DynamicDrawableSpan {
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
        public MyImageSpan(Context context, Drawable mDrawable, int start, int end, String showText, int spanBgResId, int textColor, String userId) {
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


    /**
     * 获取输入文本中所有成员的id.
     *
     * @return the user id string
     */
    public String getUserIdString() {
        MyImageSpan[] spans = getText().getSpans(0, getText().length(), MyImageSpan.class);
        StringBuilder builder = new StringBuilder();
        for (MyImageSpan myTextSpan : spans) {
            String realText = getText().toString().substring(getText().getSpanStart(myTextSpan), getText().getSpanEnd(myTextSpan));
            String showText = myTextSpan.getShowText();
            if (realText.equals(showText)) {
                builder.append(myTextSpan.getUserId()).append(",");
            }
        }
        if (!TextUtils.isEmpty(builder.toString())) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


    /**
     * Spannable string 2 json string string.
     *
     * @param ss the ss
     * @return the string
     * @throws JSONException the json exception
     */
    public String spannableString2JsonString(SpannableString ss) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("text", ss.toString());
        JSONArray jsonArray = new JSONArray();
        MyImageSpan[] spans = ss.getSpans(0, ss.length(), MyImageSpan.class);
        for (int i = 0; i < spans.length; i++) {
            String showtext = spans[i].getShowText();
            String userid = spans[i].getUserId();
            int start = ss.getSpanStart(spans[i]);
            int end = ss.getSpanEnd(spans[i]);
            int spanBgResId = spans[i].getSpanBgResId();
            int textColor = spans[i].getTextColor();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("start", start);
            jsonObject.put("end", end);
            jsonObject.put("showtext", showtext);
            jsonObject.put("userid", userid);
            jsonObject.put("spanBgResId", spanBgResId);
            jsonObject.put("textColor", textColor);
            jsonArray.put(jsonObject);
        }
        json.put("spans", jsonArray);
        return json.toString();
    }

    /**
     * Json string 2 spannable string spannable string.
     *
     * @param strjson the strjson
     * @return the spannable string
     * @throws JSONException the json exception
     */
    public SpannableString jsonString2SpannableString(String strjson) throws JSONException {
        JSONObject json = new JSONObject(strjson);
        SpannableString ss = new SpannableString(json.getString("text"));
        JSONArray jsonArray = json.getJSONArray("spans");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String showtext = jsonObject.getString("showtext");
            String userid = jsonObject.getString("userid");
            int start = jsonObject.getInt("start");
            int end = jsonObject.getInt("end");
            int spanBgResId = jsonObject.getInt("spanBgResId");
            int textColor = jsonObject.getInt("textColor");
            generateSpan(ss,start, end, showtext,spanBgResId,textColor, userid);
        }
        return ss;
    }



    TextWatcher defaultTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(isRestoreFromDraft){
                isRestoreFromDraft = false;
            }else{
                if(isOnlySupportLastAt){
                    if (count == 1 && start == s.length() - 1 && s.charAt(start) == '@') {   // 判断输入框内最后一位s为@时跳转
                        if(inputAtListener !=null){
                            inputAtListener.jumpToSelectMember();
                        }
                    }
                }else{
                    if (count == 1 && s.charAt(start) == '@') {  // 判断输入框内输入一位@时跳转
                        if(inputAtListener !=null){
                            inputAtListener.jumpToSelectMember();
                        }
                    }
                }

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
