package com.inde.shiningdays;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inde.shiningdays.util.BaseActivity;
import com.inde.shiningdays.util.SharedPrefsUtil;

/**
 * Created by dengy on 15/4/6.
 */
public class FindLockPwd extends BaseActivity implements View.OnClickListener{
    private TextView findPwdQuestion;
    private EditText findPwdAnswer;
    private TextView oldLockPwd;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.find_lock_pwd);

        initViews();
    }

    private void initViews() {
        findPwdQuestion = (TextView)findViewById(R.id.find_pwd_q);
        findPwdAnswer = (EditText)findViewById(R.id.find_pwd_a);
        oldLockPwd = (TextView)findViewById(R.id.old_lock_pwd);
        findViewById(R.id.find_pwd_cancel).setOnClickListener(this);
        findViewById(R.id.find_pwd_btn).setOnClickListener(this);

        SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs
                (this, Constant.COUNT_DOWN_SETTING_PREF);
        findPwdQuestion.setText(prefs.getString(Constant.FIND_PWD_QUESTION, ""));
    }

    private String getFindPwdAnswer() {
        SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs
                (this, Constant.COUNT_DOWN_SETTING_PREF);
        return prefs.getString(Constant.FIND_PWD_ANSWER, "");
    }

    private void findOldLockPwd() {
        oldLockPwd.setText("");
        String findPwdAnswerCurrent = findPwdAnswer.getText().toString();
        String findPwdAnswer = getFindPwdAnswer();
        if (findPwdAnswer !=null && findPwdAnswer.equals(findPwdAnswerCurrent)) {
            SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs
                    (this, Constant.COUNT_DOWN_SETTING_PREF);
            String msg = getResources().getString(R.string.lock_pwd_hint) + prefs.getString(Constant.LOCK_PWD, "");
            oldLockPwd.setText(msg);
        } else {
            oldLockPwd.setText(getResources().getString(R.string.find_pwd_qa_incorrect));
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.find_pwd_btn:
                findOldLockPwd();
                break;
            case R.id.find_pwd_cancel:
                finish();
                break;
            default:
                break;
        }
    }

}