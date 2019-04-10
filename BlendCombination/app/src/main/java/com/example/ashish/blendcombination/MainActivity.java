package com.example.ashish.blendcombination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button adminButton , userButton;
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        adminButton = findViewById(R.id.admin);
        userButton = findViewById(R.id.user);

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminEnd.class));
                finish();
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UserEnd.class));
                finish();
            }
        });
    }


}
