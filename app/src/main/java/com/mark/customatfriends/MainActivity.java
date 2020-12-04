package com.mark.customatfriends;

import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mark.atlibrary.AtEditText;
import com.mark.atlibrary.listener.InputAtListener;
import com.mark.atlibrary.listener.InputTextListener;
import com.mark.customatfriends.bean.Member;
import com.mark.atlibrary.listener.SelectMemberListener;
import com.mark.customatfriends.utils.PreferenceUtils;
import com.mark.customatfriends.widget.SelectMemberPopupWindow;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements InputAtListener, InputTextListener, SelectMemberListener<Member> {

    public static final String WAIT_SEND = "WAIT_SEND";
    private AtEditText mChatEdit;
    private ArrayList<Member> mMembers = new ArrayList<>();
    String[] NAMES = new String[]{"李白", "杜甫", "王勃", "杨万里", "贺知章", "辛弃疾","陆游", "李清照", "白居易"};
    private SelectMemberPopupWindow mSelectMemberPopupWindow;
    private Button mButton;
    private TextView mMemberIdTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        generateTestFriends();
    }

    private void initView() {
        mButton =  findViewById(R.id.button);
        mMemberIdTv =  findViewById(R.id.memberid);
        mChatEdit = (AtEditText) findViewById(R.id.chat_edit);
        mChatEdit.post(() -> {
            mChatEdit.requestFocus();
            softKeyboardControl(true,200);
        });
    }

    private void initListener() {
        mChatEdit.setInputAtListener(this);
        mChatEdit.setInputTextListener(this);
        mButton.setOnClickListener(v -> {
            mMemberIdTv.setText(mChatEdit.getUserIdString());
        });

    }

    private void generateTestFriends() {
        mMembers.clear();
        for (int i = 0; i < NAMES.length; i++) {
            Member member = new Member(NAMES[i], "" + i);
            mMembers.add(member);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }



    /**
     * 恢复草稿
     */
    private void onRestoreDraft() {
        String draft = PreferenceUtils.getString(this, WAIT_SEND, "");
        if (!TextUtils.isEmpty(draft)) {
            try {
                SpannableString ss = mChatEdit.jsonString2SpannableString(draft);
                mChatEdit.setRestoreFromDraft(true);
                mChatEdit.setText(ss);
                mChatEdit.setSelection(ss.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        onRestoreDraft();
    }

    @Override
    protected void onStop() {
        super.onStop();
        onSaveDraft();
    }

    /**
     * 保存草稿
     */
    protected void onSaveDraft() {
        Editable editable = mChatEdit.getText();
        String str = editable.toString();
        if (TextUtils.isEmpty(str.trim())) {
            PreferenceUtils.putString(this, WAIT_SEND, "");
            return;
        }
        SpannableString spannableString = new SpannableString(editable);
        String saveJsonString = "";
        try {
            saveJsonString = mChatEdit.spannableString2JsonString(spannableString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PreferenceUtils.putString(this, WAIT_SEND, saveJsonString);
    }

    /**
     * 跳转到选人界面
     */
    @Override
    public void jumpToSelectMember() {
        mSelectMemberPopupWindow = new SelectMemberPopupWindow(this, mMembers, this);
        mSelectMemberPopupWindow.showAtLocation(findViewById(R.id.root), Gravity.CENTER, 0, 0);
    }


    /**选人接口回调
     * @param member  选择的成员
     */
    @Override
    public void onGetSelectMember(Member member) {
        try {
            Editable editContent = mChatEdit.getText();
            if (member != null) {
                SpannableString atContent = new SpannableString("@" + member.getName() + " ");
                /*if (editContent.toString().contains(atContent)) {
                    dealInputCharAt(editContent);
                    Toast.makeText(MainActivity.this, "已经@了", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                dealInputCharAt(editContent);
                mChatEdit.addSpan(atContent.toString(), AtEditText.DEFAULT_BG, AtEditText.DEFAULT_TEXT_COLOR, member.getId());
                softKeyboardControl(true,200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**去除输入的@
     * @param editContent
     */
    private void dealInputCharAt(Editable editContent) {
        if (mChatEdit.isOnlySupportLastAt()) {
            editContent.delete(editContent.length() - 1, editContent.length());
        } else {
            editContent.delete(mChatEdit.getSelectionStart() - 1, mChatEdit.getSelectionStart());
        }
    }


    /**软键盘显示控制
     * @param isShow  是否显示
     * @param delayMillis 延时间隔
     */
    private void softKeyboardControl(boolean isShow, long delayMillis) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm == null) return;
        if (isShow) {
            mChatEdit.postDelayed(() -> {
                mChatEdit.requestFocus();
                mChatEdit.setSelection(mChatEdit.getSelectionStart());
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }, delayMillis);
        } else {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void beforeInputTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onInputTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterInputTextChanged(Editable s) {

    }
}