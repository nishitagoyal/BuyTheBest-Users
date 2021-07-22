package com.something.groceryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.something.groceryapp.R;
import com.something.groceryapp.model.Shared;
import com.something.groceryapp.model.UserHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText nameET, phoneNoET, address1ET, address2ET, address3ET, usernameET, passwordET;
    Button registerButton;
    String name, phoneNo, address, username, password;
    private ProgressDialog addUserDialog;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(registerButton.getWindowToken(), 0);
                 name = nameET.getText().toString();
                 phoneNo = phoneNoET.getText().toString();
                 address = address1ET.getText().toString() + " " + address2ET.getText().toString() + " - " + address3ET.getText().toString();
                 username = usernameET.getText().toString();
                 password = passwordET.getText().toString();
                 if(fieldValidation())
                 {
                     addUserDialog.show();
                     registerButton.setEnabled(false);
                     authenticateUser();

                 }

            }
        });
    }

    private void authenticateUser() {
        firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    addUser();
                }
                else {
                    registerButton.setEnabled(true);
                    addUserDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "This email is already registered. Try with different email.", Toast.LENGTH_LONG).show();
                }
            } });
    }

    private void addUser() {
        String user_key = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());
        UserHelper userHelper = new UserHelper(name, phoneNo, username, password, address,user_key);
        reference.child(user_key)
                .setValue(userHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    addUserDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_LONG).show();
                    shared.setFirstTimeLaunched(false);
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    intent.putExtra("userName", name);
                    intent.putExtra("userNumber", phoneNo);
                    intent.putExtra("userAddress", address);
                    intent.putExtra("userKey", user_key);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        registerButton.setEnabled(true);
                        addUserDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Failed. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean fieldValidation() {
        boolean validate = false;
        if(!nameET.getText().toString().isEmpty() && !phoneNoET.getText().toString().isEmpty() && !usernameET.getText().toString().isEmpty() && !passwordET.getText().toString().isEmpty() && !address1ET.getText().toString().isEmpty() && !address2ET.getText().toString().isEmpty() && !address3ET.getText().toString().isEmpty())
        {
            return true;
        }
        if(nameET.getText().toString().isEmpty() && phoneNoET.getText().toString().isEmpty() && usernameET.getText().toString().isEmpty() && passwordET.getText().toString().isEmpty() && address1ET.getText().toString().isEmpty() && address2ET.getText().toString().isEmpty() && address3ET.getText().toString().isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"All fields are mandatory",Toast.LENGTH_LONG).show();
            validate = false;
        }
        if(nameET.getText().toString().isEmpty())
        {
            nameET.setError(getString(R.string.field_required));
            nameET.setFocusable(true);
            validate=false;
        }
        if(phoneNoET.getText().toString().isEmpty())
        {
            phoneNoET.setError(getString(R.string.field_required));
            phoneNoET.setFocusable(true);
            validate=false;
        }
        if(usernameET.getText().toString().isEmpty())
        {
            usernameET.setError(getString(R.string.field_required));
            usernameET.setFocusable(true);
            validate=false;
        }
        if(passwordET.getText().toString().isEmpty())
        {
            passwordET.setError(getString(R.string.field_required));
            passwordET.setFocusable(true);
            validate=false;
        }
        if(address1ET.getText().toString().isEmpty())
        {
            address1ET.setError(getString(R.string.field_required));
            address1ET.setFocusable(true);
            validate=false;
        }
        if(address2ET.getText().toString().isEmpty())
        {
            address2ET.setError(getString(R.string.field_required));
            address2ET.setFocusable(true);
            validate=false;
        }
        if(address3ET.getText().toString().isEmpty())
        {
            address3ET.setError(getString(R.string.field_required));
            address3ET.setFocusable(true);
            validate=false;
        }

        return validate;
    }

    private void initViews() {
        addUserDialog = new ProgressDialog(RegisterActivity.this, R.style.AlertDialogStyle);
        addUserDialog.setTitle("Registering...");
        addUserDialog.setCancelable(false);
        addUserDialog.setProgress(5);
        firebaseAuth = FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();
        reference= rootnode.getReference("users");
        nameET = findViewById(R.id.user_name);
        phoneNoET = findViewById(R.id.user_phone);
        address1ET = findViewById(R.id.user_address1);
        address2ET = findViewById(R.id.user_address2);
        address3ET = findViewById(R.id.user_address3);
        usernameET = findViewById(R.id.user_username);
        passwordET = findViewById(R.id.user_password);
        registerButton = findViewById(R.id.register_button);
        shared = new Shared(getApplicationContext());
    }
}