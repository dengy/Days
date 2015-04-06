package com.inde.shiningdays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inde.shiningdays.util.SharedPrefsUtil;
import com.inde.shiningdays.util.Utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by dengy on 15/4/5.
 */
public class LockScreen extends Activity implements View.OnClickListener{
    private EditText lockPwd;
    private TextView lockPwdIncorrectHint;
    private String lockOperation;
    private Button lockScreenCancel;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.lock_screen);
        initViews();

        Intent intent = getIntent();
        if(intent != null) {
            lockOperation = getIntent().getStringExtra(Constant.LOCK_OPERATION);
            if(lockOperation != null && !"".equals(lockOperation)) {
                lockScreenCancel.setVisibility(View.VISIBLE);
            } else {
                lockScreenCancel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //这里写你要在用户按下返回键同时执行的动作
            moveTaskToBack(false);            //核心代码：屏蔽返回行为
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initViews() {
        lockPwd = (EditText)findViewById(R.id.lock_pwd);
        lockPwd.requestFocus();
        lockPwdIncorrectHint = (TextView)findViewById(R.id.lock_pwd_incorrect_hint);
        lockScreenCancel = (Button)findViewById(R.id.lock_screen_cancel);
        findViewById(R.id.lock_pwd_forget).setOnClickListener(this);
        lockScreenCancel.setOnClickListener(this);
        lockPwd.addTextChangedListener(new TextWatcher() {
            private String temp;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //lockPwdIncorrectHint.setText("");
                temp = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                verifyLockPwd(temp);
            }
        });
    }

    private void verifyLockPwd(String lockPwdText) {
        if(getLockPwd().equals(lockPwdText)) {
            lockPwd.clearFocus();
            lockPwdIncorrectHint.setText("");
            //other operation
            if(Constant.MODIFY_LOCK.equals(lockOperation)) {
                //to modify view
                toPasswordEdit();
            } else if(Constant.CLOSE_LOCK.equals(lockOperation)) {
                showCloseLockConfirm();
            } else {
                finish();
            }
        } else {
            lockPwdIncorrectHint.setText(R.string.lock_pwd_incorrect);
        }
    }

    private void showCloseLockConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        View layout = inflater.inflate(R.layout.confirm_dialog, null);
        TextView confirmTitle = (TextView)layout.findViewById(R.id.confirmTitle);
        confirmTitle.setText(getResources().getString(R.string.close_lock_dialog_title));

        builder.setView(layout)
                .setPositiveButton(R.string.confirm_label, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearLockPwd();
                    }

                }).setNegativeButton(R.string.go_back_label, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        Dialog d = builder.create();
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    private void toPasswordEdit() {
        finish();
        Intent intent = new Intent(this, PasswordEdit.class);
        startActivity(intent);
    }

    /*private void toMainActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/

    private void toFindLockPwd() {
        Intent intent = new Intent(this, FindLockPwd.class);
        startActivity(intent);
    }

    private String getLockPwd() {
        SharedPreferences prefs = SharedPrefsUtil.getSharedPrefs
                (this, Constant.COUNT_DOWN_SETTING_PREF);
        return prefs.getString(Constant.LOCK_PWD, "");
    }

    private void clearLockPwd() {
        SharedPreferences.Editor editor = SharedPrefsUtil.getSharedPrefs
                (this, Constant.COUNT_DOWN_SETTING_PREF).edit();
        editor.remove(Constant.LOCK_PWD);
        editor.remove(Constant.FIND_PWD_QUESTION);
        editor.remove(Constant.FIND_PWD_ANSWER);
        editor.commit();
        finish();
        Toast.makeText(this, R.string.operationSuccess, Toast.LENGTH_LONG);
    }

    /*private void showSendMailConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        View layout = inflater.inflate(R.layout.confirm_dialog, null);
        TextView confirmTitle = (TextView)layout.findViewById(R.id.confirmTitle);
        final String email = getEmail();
        final String emailContent = getResources().getString(R.string.forget_pwd_email_content).
                toString() + getPassword();
        final String emailTitle = getResources().getString(R.string.forget_pwd_email_title).
                toString();
        String dialogTitle = getResources().getString(R.string.confirm_sent_pwd_to_email_title).
                toString() + email;
        confirmTitle.setText(dialogTitle);

        builder.setView(layout)
                .setPositiveButton(R.string.confirm_label, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMail(email, emailTitle, emailContent);
                    }

                }).setNegativeButton(R.string.go_back_label, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        Dialog d = builder.create();
        d.setCanceledOnTouchOutside(false);
        d.show();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lock_pwd_forget:
                toFindLockPwd();
                break;
            case R.id.lock_screen_cancel:
                finish();
                break;
            default:
                break;
        }

    }

    /*private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.qq.com");
        properties.put("mail.smtp.port", "465");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constant.BACKGROUD_SEND_MAIL,
                        Constant.BACKGROUD_SEND_MAIL_PWD);
            }
        });
    }

    private javax.mail.Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        javax.mail.Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Constant.BACKGROUD_SEND_MAIL,
                getResources().getString(R.string.app_name)));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    private class SendMailTask extends AsyncTask<javax.mail.Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LockScreen.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }*/
}