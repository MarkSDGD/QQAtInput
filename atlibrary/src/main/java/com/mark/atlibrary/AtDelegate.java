package com.mark.atlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AtDelegate {
    public static final int DEFAULT_BG = 0;
    public static final int DEFAULT_TEXT_COLOR = 0;
    Context context;
    float textSize;
    public AtDelegate(Context context, float textSize) {
        this.context=context;
        this.textSize=textSize;
    }

    public String spannableString2JsonString(SpannableString ss) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("text", ss.toString());
        JSONArray jsonArray = new JSONArray();
        AtImageSpan[] spans = ss.getSpans(0, ss.length(), AtImageSpan.class);
        for (int i = 0; i < spans.length; i++) {
            String showtext = spans[i].getShowText();
            String userid = spans[i].getUserId();
            int start = ss.getSpanStart(spans[i]);
            int end = ss.getSpanEnd(spans[i]);
            int spanBgResId = spans[i].getSpanBgResId();
            int textColor = spans[i].getTextColor();
            int maxEms = spans[i].getMaxEms();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("start", start);
            jsonObject.put("end", end);
            jsonObject.put("showtext", showtext);
            jsonObject.put("userid", userid);
            jsonObject.put("spanBgResId", spanBgResId);
            jsonObject.put("textColor", textColor);
            jsonObject.put("maxEms", maxEms);
            jsonArray.put(jsonObject);
        }
        json.put("spans", jsonArray);
        return json.toString();
    }

    public SpannableString jsonString2SpannableString(String strjson) throws JSONException{
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
            int maxEms = jsonObject.getInt("maxEms");
            generateSpan(ss, start, end, showtext, spanBgResId, textColor, userid,maxEms);
        }
        return ss;
    }

    public void generateSpan(Spannable spannableString, int start, int end, String showText, int spanBgResId, int textColor, String userId,int maxEms) {
        View spanView = getSpanView(context, showText, spanBgResId, textColor,maxEms);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) convertViewToDrawable(spanView);
        bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());
        AtImageSpan what = new AtImageSpan(context, bitmapDrawable, start, end, showText, spanBgResId, textColor, userId,maxEms);
        spannableString.setSpan(what, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * Convert view to drawable drawable.
     *
     * @param view the view
     * @return the drawable
     */
    public Drawable convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
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
    public View getSpanView(Context context, String text, int spanBgResId, int textColor,int maxEms) {
        TextView view = new TextView(context);

        if(maxEms>0){
            view.setMaxEms(maxEms);
        }else{

        }

        view.setText(text);
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setSingleLine(true);
        //设置文字框背景色
        if (spanBgResId == DEFAULT_BG) {
            view.setBackgroundResource(R.drawable.shape_corner_rectangle);
        } else {
            view.setBackgroundResource(spanBgResId);
        }
        view.setTextSize(textSize);
        //设置文字颜色
        if (spanBgResId == DEFAULT_TEXT_COLOR) {
            view.setTextColor(Color.BLUE);
        } else {
            view.setTextColor(textColor);
        }
        return view;
    }

    /**
     * 获取输入文本中所有成员的id.
     *
     * @return the user id string
     * @param editable
     */
    public String getUserIdString(Editable editable) {
        AtImageSpan[] spans = editable.getSpans(0, editable.length(), AtImageSpan.class);
        StringBuilder builder = new StringBuilder();
        for (AtImageSpan myTextSpan : spans) {
            String realText = editable.toString().substring(editable.getSpanStart(myTextSpan), editable.getSpanEnd(myTextSpan));
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
}