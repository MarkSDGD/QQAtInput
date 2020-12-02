package com.mark.atlibrary.listener;

import android.text.Editable;

/**
 * Created by Mark on 2020/11/27.
 * <p>Copyright 2020 ZTZQ.</p>
 * 需要 监听自定义处理，实现以下方法
 */
public interface InputTextListener {

    void beforeInputTextChanged(CharSequence s, int start, int count, int after);

    void onInputTextChanged(CharSequence s, int start, int before, int count);

    void afterInputTextChanged(Editable s);
}