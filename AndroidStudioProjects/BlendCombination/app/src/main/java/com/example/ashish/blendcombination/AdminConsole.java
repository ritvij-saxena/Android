package com.example.ashish.blendcombination;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class AdminConsole extends AppCompatActivity {

    DatabaseReference databaseReference;
    Button addORUpdate , notifyAll, deleteMsg;
    EditText blend1,blend2,blend_desc;
    String CHANNEL_ID = "ADMIN_CLASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_console);

        addORUpdate = findViewById(R.id.buttonUpdate);
        notifyAll = findViewById(R.id.buttonNotification);
        deleteMsg = findViewById(R.id.buttonClearNotification);

        addORUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AdminConsole.this);
                alert.setTitle("Updating Database");
                LayoutInflater layoutInflater = LayoutInflater.from(AdminConsole.this);
                View view = layoutInflater.inflate(R.layout.updatingdbvalues,null);
                alert.setView(view);
                blend1 = view.findViewById(R.id.blend1_Text);                       //updatingdbvalues.xml
                blend2 = view.findViewById(R.id.blend2_Text);
                blend_desc = view.findViewById(R.id.desecription);
                alert.setPositiveButton("Update Values", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(blend1.getText().toString().equals(""))
                        {
                            blend1.setError("Blend 1 Empty");
                            blend1.setFocusable(true);
                        }
                        if(blend2.getText().toString().equals(""))
                        {
                            blend2.setError("Blend 2 Empty");
                            blend2.setFocusable(true);
                        }
                        if(blend_desc.getText().toString().equals(""))
                        {
                            blend_desc.setError("Blend Description Empty");
                            blend_desc.setFocusable(true);
                        }
                        else
                        {
                            databaseReference = FirebaseDatabase.getInstance().getReference("BLENDS"); //change to blends
                            HashMap<String , String> map = new HashMap<>();
                            map.put(blend2.getText().toString(),blend_desc.getText().toString());
                            databaseReference.child(blend1.getText().toString()).setValue(map);
                            Toast.makeText(AdminConsole.this, "Database Updated", Toast.LENGTH_LONG).show();
                        }
                       /* */

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alert.create();
                alert.show();
            }
        });


        notifyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder build = new AlertDialog.Builder(AdminConsole.this);
                build.setTitle("Broadcast Message");
                LinearLayout layout = new LinearLayout(AdminConsole.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText messageSubject = new EditText(AdminConsole.this);
                messageSubject.setHint("Enter Message Subject Here ");
                messageSubject.setTypeface(Typeface.DEFAULT_BOLD);
                layout.addView(messageSubject);

                final EditText messageContent = new EditText(AdminConsole.this);
                messageContent.setHint("Enter Message Content Here ");
                messageContent.setTypeface(Typeface.DEFAULT_BOLD);
                layout.addView(messageContent);

                build.setView(layout);

                build.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference = FirebaseDatabase.getInstance().getReference("ADMIN_MESSAGE");
                        databaseReference.removeValue();
                        databaseReference.child(messageSubject.getText().toString()).setValue(messageContent.getText().toString());
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminConsole.this);
                        builder.setTitle("Message Sent");
                        builder.setMessage("Message broadcasted");
                        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkForAdminBroadCasts();
                                return;
                            }
                        });
                        builder.show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                build.show();

            }
        });

        deleteMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("ADMIN_MESSAGE");
                databaseReference.removeValue();
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminConsole.this);
                builder.setTitle("Broadcast Message Deleted");
                builder.setMessage("No Messages Left");
                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();

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
                                        Intent intent = new Intent(AdminConsole.this, UserEnd.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(AdminConsole.this, 0, intent, 0);
                                        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(AdminConsole.this,CHANNEL_ID);
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
        startActivity(new Intent(AdminConsole.this,MainActivity.class));
        finish();
    }
}
