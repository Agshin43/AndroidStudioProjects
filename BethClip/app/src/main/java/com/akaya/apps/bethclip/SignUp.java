package com.akaya.apps.bethclip;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";

    EditText nameText;
    EditText emailText;
    EditText passwordText;
    AppCompatButton signupButton;
    TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameText = (EditText)findViewById(R.id.input_name);
        emailText = (EditText)findViewById(R.id.input_email);
        passwordText = (EditText)findViewById(R.id.input_password);
        signupButton = (AppCompatButton)findViewById(R.id.btn_signup);
        loginLink = (TextView)findViewById(R.id.link_login);

        emailText.setHintTextColor(getColor(getApplicationContext(),R.color.ColorWhite));
        nameText.setHintTextColor(getColor(getApplicationContext(),R.color.ColorWhite));
        passwordText.setHintTextColor(getColor(getApplicationContext(),R.color.ColorWhite));

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        startActivity(new Intent(SignUp.this, MainActivity.class));

        finish();
    }

    public void onSignupFailed() {
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("invalid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            passwordText.setError("between 6 and 20 characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}