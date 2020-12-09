package com.mark.atlibrary;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.mark.atlibrary.listener.InputAtListener;
import com.mark.atlibrary.listener.InputTextListener;

import org.json.JSONException;

/**
 * author: MARK
 * company: ZTZQ
 * created on: 2020/11/16 16:16
 * description: CustomAtEditText.java
 */
public class AtEditText extends AppCompatAutoCompleteTextView {
    public static final int DEFAULT_BG = 0;
    public static final int DEFAULT_TEXT_COLOR = 0;
    private InputAtListener inputAtListener;
    private InputTextListener inputTextListener;
    private boolean isRestoreFromDraft = false;
    private boolean isOnlySupportLastAt = false;
    private AtDelegate atDelegate = new AtDelegate(getContext(),getTextSize());
    /**
     * Instantiates a new Custom at edit text.
     *
     * @param context the context
     */
    public AtEditText(Context context) {
        this(context, null);
    }


    /**
     * Instantiates a new Custom at edit text.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public AtEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Custom at edit text.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public AtEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(defaultTextWatcher);
    }


    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        AtImageSpan[] spans = getText().getSpans(0, getText().length(), AtImageSpan.class);
        for (AtImageSpan atImageSpan : spans) {
            if (getText().getSpanEnd(atImageSpan) - 1 == selStart) {
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
    public void addSpan(String showText, int spanBgResId, int textColor, String userId,int maxEms) {
        if (isOnlySupportLastAt) {
            getText().append(showText);
            SpannableString spannableString = new SpannableString(getText());
            atDelegate.generateSpan(spannableString, spannableString.length() - showText.length(), spannableString.length(), showText, spanBgResId, textColor, userId,maxEms);
            setText(spannableString);
            setSelection(spannableString.length());
        } else {
            int insertPos = getSelectionStart();
            getText().insert(getSelectionStart(), showText);
            SpannableString spannableString = new SpannableString(getText());
            atDelegate.generateSpan(spannableString, getSelectionStart() - showText.length(), getSelectionStart(), showText, spanBgResId, textColor, userId,maxEms);
            setText(spannableString);
            setSelection(insertPos + showText.length());
        }
    }

    public void setInputAtListener(InputAtListener inputAtListener) {
        this.inputAtListener = inputAtListener;
    }

    public void setRestoreFromDraft(boolean restoreFromDraft) {
        isRestoreFromDraft = restoreFromDraft;
    }

    public InputTextListener getInputTextListener() {
        return inputTextListener;
    }

    public void setInputTextListener(InputTextListener inputTextListener) {
        this.inputTextListener = inputTextListener;
    }

    /**
     * 获取@模式
     *
     * @return
     */
    public boolean isOnlySupportLastAt() {
        return isOnlySupportLastAt;
    }

    /**
     * 设置@模式
     *
     * @param onlySupportLastAt true 最后输入@有效；false,任何位置输入@有效
     */
    public void setOnlySupportLastAt(boolean onlySupportLastAt) {
        isOnlySupportLastAt = onlySupportLastAt;
    }

    /**
     * 获取输入文本中所有成员的id.
     *
     * @return the user id string
     */
    public String getUserIdString() {
       return  atDelegate.getUserIdString(getText());
    }


    /**
     * Spannable string 2 json string string.
     *
     * @param ss the ss
     * @return the string
     * @throws JSONException the json exception
     */
    public String spannableString2JsonString(SpannableString ss) throws JSONException {
        return atDelegate.spannableString2JsonString(ss);
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


    TextWatcher defaultTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(inputTextListener!=null){
                inputTextListener.beforeInputTextChanged(s, start, count, after);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isRestoreFromDraft) {
                isRestoreFromDraft = false;
            } else {
                if (isOnlySupportLastAt) {
                    if (count == 1 && start == s.length() - 1 && s.charAt(start) == '@') {   // 判断输入框内最后一位s为@时跳转
                        if (inputAtListener != null) {
                            inputAtListener.jumpToSelectMember();
                        }
                    }
                } else {
                    if (count == 1 && s.charAt(start) == '@') {  // 判断输入框内输入一位@时跳转
                        if (inputAtListener != null) {
                            inputAtListener.jumpToSelectMember();
                        }
                    }
                }
            }
            if(inputTextListener!=null){
                inputTextListener.onInputTextChanged(s, start, before, count);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(inputTextListener!=null){
                inputTextListener.afterInputTextChanged(s);
            }
        }
    };

    public AtDelegate getAtDelegate() {
        return atDelegate;
    }


}
