package com.something.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.something.groceryapp.R;
import com.something.groceryapp.model.Shared;
import com.something.groceryapp.model.UserHelper;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET, passwordET;
    Button signInButton;
    TextView registerTV, forgotPassTV;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootnode;
    DatabaseReference reference, reference1;
    ProgressBar login_progress;
    Shared shared;
    DisplayMetrics metrics;
    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldValidation())
                {
                    signInButton.setEnabled(false);
                    login_progress.setVisibility(View.VISIBLE);
                    loginUser();
                }
            }
        });
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        forgotPassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }

    private void forgotPassword() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.forgot_pass_dialog);
        dialog.setTitle("Confirm Email");
        dialog.getWindow().setLayout((6 * width)/7, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button button = (Button) dialog.findViewById(R.id.confirmEmailButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextInputEditText edit = dialog.findViewById(R.id.forgot_email_et);
                if(edit.getText().toString().isEmpty())
                {
                    edit.setError(getString(R.string.field_required));
                }
                else {
                    String text = edit.getText().toString();
                    firebaseAuth.sendPasswordResetEmail(text).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Password Reset Link Sent!",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this,"This Email does not exist",Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
        dialog.show();
    }

    private void initViews() {
        usernameET = findViewById(R.id.username_et);
        passwordET = findViewById(R.id.password_et);
        signInButton = findViewById(R.id.sign_in_button);
        registerTV = findViewById(R.id.register_text);
        forgotPassTV = findViewById(R.id.forget_pass_et);
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        login_progress = findViewById(R.id.login_progress);
        shared = new Shared(getApplicationContext());
        metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;


    }

    public boolean fieldValidation()
    {

        if ((usernameET.getText().toString().isEmpty()) && (passwordET.getText().toString().isEmpty()))
        {
            usernameET.setError(getString(R.string.field_required));
            passwordET.setError(getString(R.string.field_required));
            return false;
        }
        if(usernameET.getText().toString().isEmpty()) {
            usernameET.setError(getString(R.string.field_required));
            return false;
        }
        if(passwordET.getText().toString().isEmpty()) {
            passwordET.setError(getString(R.string.field_required));
            return false;
        }
        else if ((!usernameET.getText().toString().isEmpty()) && (!passwordET.getText().toString().isEmpty()))
        {
            return true;
        }
        return false;
    }

    private void loginUser() {
        firebaseAuth.signInWithEmailAndPassword(usernameET.getText().toString(), passwordET.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                extractUserInfo(authResult.getUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                signInButton.setEnabled(true);
                login_progress.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,"Please check username and password!",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void extractUserInfo(String Uid)
    {
        reference1 = rootnode.getReference("users").child(Uid);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelper userHelper = snapshot.getValue(UserHelper.class);
                shared.setUserKeyShared(userHelper.getUserKey());
                shared.setUserNameShared(userHelper.getUserName());
                shared.setUserAddressShared(userHelper.getUserAddress());
                shared.setUserPhoneShared(userHelper.getUserPhoneno());
                shared.setFirstTimeLaunched(false);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}