package com.inde.shiningdays;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inde.shiningdays.util.BaseActivity;
import com.inde.shiningdays.util.SharedPrefsUtil;
import com.inde.shiningdays.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dengy on 15/4/4.
 */
public class PasswordEdit extends BaseActivity implements View.OnClickListener {
    private EditText newLockPwd;
    private EditText newLockPwdConfirm;
    private EditText findPwdQuestion;
    private EditText findPwdAnswer;
    private TextView errorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.password_edit);

        initViews();
    }

    public void initViews() {
        newLockPwd = (EditText)findViewById(R.id.lock_pwd);
        newLockPwd.findFocus();
        newLockPwdConfirm = (EditText)findViewById(R.id.lock_pwd_confirm);
        findPwdQuestion = (EditText)findViewById(R.id.find_pwd_q);
        findPwdAnswer = (EditText)findViewById(R.id.find_pwd_a);
        errorMsg = (TextView)findViewById(R.id.error_msg);
        findViewById(R.id.lock_cancel).setOnClickListener(this);
        findViewById(R.id.lock_save).setOnClickListener(this);

        SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs
                (this, Constant.COUNT_DOWN_SETTING_PREF);
        findPwdQuestion.setText(prefs.getString(Constant.FIND_PWD_QUESTION, ""));
        findPwdAnswer.setText(prefs.getString(Constant.FIND_PWD_ANSWER, ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //umeng sdk
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //umeng sdk
        MobclickAgent.onPause(this);
    }

    private boolean saveLockPwd() {
        errorMsg.setText("");
        String newLockPwdText = newLockPwd.getText().toString();
        if((newLockPwdText == null) || (newLockPwdText.length() != 4)) {
            errorMsg.setText(R.string.lock_pwd_cannot_empty);
        } else {
            String newLockPwdConfirmText = newLockPwdConfirm.getText().toString();

            if(!newLockPwdText.equals(newLockPwdConfirmText)) {
                errorMsg.setText(R.string.must_same_pwd);
            } else {
                String findPwdQuestionText = findPwdQuestion.getText().toString();
                String findPwdAnswerText = findPwdAnswer.getText().toString();
                if(findPwdQuestionText == null || "".equals(findPwdQuestionText) ||
                        findPwdAnswerText == null || "".equals(findPwdAnswerText)) {
                    errorMsg.setText(R.string.find_pwd_qa_cannot_empty);
                } else {
                    SharedPreferences.Editor editor = SharedPrefsUtil.getSharedPrefs
                            (this, Constant.COUNT_DOWN_SETTING_PREF).edit();
                    editor.putString(Constant.LOCK_PWD, newLockPwdText);
                    editor.putString(Constant.FIND_PWD_QUESTION, findPwdQuestionText);
                    editor.putString(Constant.FIND_PWD_ANSWER, findPwdAnswerText);
                    editor.commit();
                    Toast.makeText(this, R.string.lock_pwd_save_succ, Toast.LENGTH_LONG).show();
                    return true;
                }

            }

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.lock_save:
                if(saveLockPwd()) {
                    finish();
                }
                break;
            case R.id.lock_cancel:
                finish();
                break;
            default:
                break;
        }
    }

}