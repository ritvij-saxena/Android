package com.example.ashish.blendcombination;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminEnd extends AppCompatActivity {

    Button login;
    EditText emailID;
    TextInputEditText pass;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_end);

        login = findViewById(R.id.login);
        emailID = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailID.getText().toString().equals(""))
                {
                    emailID.setError("Email ID empty");
                    emailID.setFocusable(true);
                }
                if(pass.getText().toString().equals(""))
                {
                    pass.setError("Password Empty");
                    pass.setFocusable(true);
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(emailID.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            startActivity(new Intent(AdminEnd.this,AdminConsole.class));
                            finish();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Login",e.getMessage());
                                    AlertDialog.Builder build = new AlertDialog.Builder(AdminEnd.this);
                                    build.setTitle("Error").setCancelable(true).setMessage(e.getMessage());
                                    build.show();
                                }
                            });
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminEnd.this,MainActivity.class));
        finish();
    }
}
