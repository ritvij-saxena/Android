package com.example.ashish.blendcombination;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class UserEnd extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference databaseReference;        //for messages only
    //Spinner blend1,blend2;
    String towerName;
    TextView mixText;
    String blend1_Choice , blend2_Choice;
    FloatingActionButton mix;
    String CHANNEL_ID = "USEREND_CLASS";
    AutoCompleteTextView autoComplete1, autoComplete2;
    Spinner forTower;
    String[] towerNamesArray ={"Tower 1", "Tower 2"};
    final static ArrayList<String> keyValues =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_end);

        mix = findViewById(R.id.mixbutton);
        mixText = findViewById(R.id.blendmix);
        database=FirebaseDatabase.getInstance();
        reference = database.getReference("BLENDS");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //blend1 = findViewById(R.id.blend1);
        //blend2 = findViewById(R.id.blend2);
        autoComplete1 = findViewById(R.id.auto1);
        autoComplete2 = findViewById(R.id.auto2);
        forTower = findViewById(R.id.spinnerTower);

       // checkForAdminBroadCasts();
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getApplication(),R.layout.support_simple_spinner_dropdown_item, towerNamesArray);
        forTower.setAdapter(stringArrayAdapter);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checkForAdminBroadCasts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    keyValues.add(child.getKey());
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,keyValues);
               // adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                //blend1.setAdapter(adapter);
               // blend2.setAdapter(adapter);
                autoComplete1.setAdapter(adapter);
                autoComplete2.setAdapter(adapter);
                autoComplete1.setThreshold(1);
                autoComplete2.setThreshold(1);
                autoComplete1.setFocusable(true);
                autoComplete2.setFocusable(true);


                autoComplete1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autoComplete1.showDropDown();
                    }
                });

                /*autoComplete1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        blend1_Choice = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/

                autoComplete2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autoComplete2.showDropDown();
                    }
                });

                /*autoComplete2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        blend2_Choice = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       /* blend1.setPrompt("Blend 1");
        blend1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blend1_Choice = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



        /*blend2.setPrompt("Blend 2");
        blend2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blend2_Choice = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        forTower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                towerName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blend1_Choice = autoComplete1.getText().toString();
                blend2_Choice = autoComplete2.getText().toString();
                if(blend1_Choice.equals(""))
                {
                    autoComplete1.setError("Choose a Blend");
                    autoComplete1.setFocusable(true);
                }
                if(blend2_Choice.equals(""))
                {
                    autoComplete2.setError("Choose a Blend");
                    autoComplete2.setFocusable(true);
                }
                else
                {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BLENDS").child(blend1_Choice);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String mixStr = (String) dataSnapshot.child(blend2_Choice).getValue();
                            mixText.setText(mixStr);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });




    }

    private void checkForAdminBroadCasts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists())
              {
                  for(DataSnapshot child : dataSnapshot.getChildren())
                  {
                      if(child.getKey().equals("ADMIN_MESSAGE"))
                      {
                         DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ADMIN_MESSAGE");
                         ref.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 for(DataSnapshot innerChild : dataSnapshot.getChildren())
                                 {
                                     createNotificationChannel();
                                     Intent intent = new Intent(UserEnd.this, UserEnd.class);
                                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                     PendingIntent pendingIntent = PendingIntent.getActivity(UserEnd.this, 0, intent, 0);
                                     NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(UserEnd.this,CHANNEL_ID);
                                     nBuilder.setContentTitle(innerChild.getKey())
                                             .setContentText(String.valueOf(innerChild.getValue()))
                                             .setAutoCancel(true)
                                             .setPriority(NotificationCompat.PRIORITY_HIGH)
                                             .setSmallIcon(R.drawable.cup)
                                             .setContentIntent(pendingIntent);
                                     NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                     notificationManager.notify(new Random().nextInt(5),nBuilder.build());
                                     break;
                                 }
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });
                      }
                      break;
                  }
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = CHANNEL_ID + "_Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserEnd.this,MainActivity.class));
        finish();
    }
}
