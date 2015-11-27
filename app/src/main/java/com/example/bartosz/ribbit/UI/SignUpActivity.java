package com.example.bartosz.ribbit.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.bartosz.ribbit.R;
import com.example.bartosz.ribbit.Utilities.RibbitApplication;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.emailField) EditText mEmailField;
    @Bind(R.id.usernameField) EditText mUsernameField;
    @Bind(R.id.passwordField) EditText mPasswordField;
    @Bind(R.id.signupButton) Button mSignUpButton;
    @Bind(R.id.cancelButton) Button mCancelButton;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);
        getSupportActionBar().hide();
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPassword(String password) {
        //return true if and only if password:
        //1. have at least six characters.
        //2. consists of only letters and digits.
        //3. must contain at least one digit.
        if (password.length() < 5) {
            return false;
        } else {
            char c;
            int count = 0;
            for (int i = 0; i < password.length() - 1; i++) {
                c = password.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    return false;
                } else if (Character.isDigit(c)) {
                    count++;
                }
            }
            if (count == 0){
                return false;
            }
        }
        return true;
    }


    @OnClick(R.id.signupButton)
    public void signUpOnClick(){
        String username = mUsernameField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();

        if (username.length() < 3){
            Toast.makeText(SignUpActivity.this, "Your username should contain at least 3 characters", Toast.LENGTH_SHORT).show();
        } else if (!isValidPassword(password)){
            Toast.makeText(SignUpActivity.this, "Your password should be at least 6 characters long, contains only digits and letters, with at least one digit ", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)){
            Toast.makeText(SignUpActivity.this, "Sorry, this email address does not seem to be correct", Toast.LENGTH_SHORT).show();
        }
        else{
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);

            mProgressBar.setVisibility(View.VISIBLE);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    if(e == null){
                        RibbitApplication.updateParseInstallation(ParseUser.getCurrentUser());

                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                              .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(SignUpActivity.this, "Sorry, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @OnClick(R.id.cancelButton)
    public void cancelOnClick(){
        finish();
    }
}
