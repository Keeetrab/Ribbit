package com.example.bartosz.ribbit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.userNameField) EditText mUsernameField;
    @Bind(R.id.passwordField) EditText mPasswordField;
    @Bind(R.id.signUpText) TextView mSignUpTextView;
    @Bind(R.id.loginButton) Button mLoginButton;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    @OnClick(R.id.loginButton)
    public void logingIn() {
        String username = mUsernameField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if (username.length() < 3) {
            Toast.makeText(LoginActivity.this, "Your username should contain at least 3 characters", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please, enter your password", Toast.LENGTH_SHORT).show();
        }
        else {
            mProgressBar.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    if (e == null){
                        //Success
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle(R.string.login_error_title);
                        builder.setMessage("Sorry, " + e.getMessage());
                        builder.setPositiveButton(android.R.string.ok, null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }
    }

    @OnClick(R.id.signUpText)
    public void goToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
