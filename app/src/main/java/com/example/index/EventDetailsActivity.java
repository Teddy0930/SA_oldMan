package com.example.index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.index.Model.Events;
import com.example.index.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EventDetailsActivity extends AppCompatActivity {

    private ImageView eventImage;
    private TextView eventPrice, eventDescription, eventName, eventPeople, eventDate, eventPlace;
    private String eventID = "", adminID = "", FinalEventName, FinalEventDate, FinalEventPlace;
    private Button YesEvent, NoEvent;
    private ProgressDialog loadingBar;
    private DatabaseReference eventListRef = FirebaseDatabase.getInstance().getReference().child("EventsList");
    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);


        eventID = getIntent().getStringExtra("eid");
        adminID = getIntent().getStringExtra("adminid");
        YesEvent = (Button) findViewById(R.id.event_Yes);
        NoEvent = (Button) findViewById(R.id.event_No);
        eventImage = (ImageView) findViewById(R.id.event_image_details);
        eventName = (TextView) findViewById(R.id.event_name_details);
        eventDescription = (TextView) findViewById(R.id.event_description_details);
        eventPrice = (TextView) findViewById(R.id.event_price_details);
        eventPeople = (TextView) findViewById(R.id.event_quantity_details);
        eventDate = (TextView) findViewById(R.id.event_date_details);
        eventPlace = (TextView) findViewById(R.id.event_place_details);
        loadingBar = new ProgressDialog(this);

        getEventDetails(eventID);

        YesEvent.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (Integer.parseInt(eventPeople.getText().toString()) != 0) {
                    YesEvent.setVisibility(View.INVISIBLE);
                    NoEvent.setVisibility(View.VISIBLE);

                    loadingBar.setTitle("???????????????");
                    loadingBar.setMessage("?????????,????????????.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();


                    final DatabaseReference eventListRef = FirebaseDatabase.getInstance().getReference().child("EventsList");


                    final HashMap<String, Object> EventsList = new HashMap<>();
                    EventsList.put("eid", eventID);  //??????
                    EventsList.put("ePlace", FinalEventPlace);
                    EventsList.put("eName", FinalEventName);
                    EventsList.put("eUser", "Users" + Prevalent.currentOnlineUser.getAccount());
                    EventsList.put("eUserName", Prevalent.currentOnlineUser.getName());
                    EventsList.put("eUserID", "Users" + Prevalent.currentOnlineUser.getAccount() + eventID);
                    EventsList.put("eStatus","?????????");
                    EventsList.put("eDate", FinalEventDate);


                    eventListRef.child("User  View").child("Users" + Prevalent.currentOnlineUser.getAccount())
                            .child("Events").child("Users" + Prevalent.currentOnlineUser.getAccount() + eventID)
                            .updateChildren(EventsList)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        eventListRef.child("Admin  View").child(adminID)
                                                .child("Events").child(eventID).child("Users" + Prevalent.currentOnlineUser.getAccount())
                                                .updateChildren(EventsList)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {
                                                            eventsRef.child("User  View").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    HashMap<String, Object> EventsMap = new HashMap<>();
                                                                    EventsMap.put("ePeople", Integer.parseInt(dataSnapshot.child(eventID).child("ePeople").getValue().toString()) - 1);  //??????
                                                                    eventsRef.child("User  View").child(eventID).updateChildren(EventsMap);
                                                                    eventsRef.child("Admin  View").child(adminID).child(eventID).updateChildren(EventsMap);
                                                                    loadingBar.dismiss();
                                                                    Toast.makeText(EventDetailsActivity.this, "????????????!", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(EventDetailsActivity.this, HomeActivity.class);
                                                                    startActivity(intent);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                } else {
                    Toast.makeText(EventDetailsActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }

        });
        NoEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBar.setTitle("???????????????");
                loadingBar.setMessage("?????????,????????????.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();


                YesEvent.setVisibility(View.VISIBLE);
                NoEvent.setVisibility(View.INVISIBLE);
                FirebaseDatabase.getInstance().getReference().child("EventsList")
                        .child("User  View")
                        .child("Users" + Prevalent.currentOnlineUser.getAccount()).child("Events").child("Users" + Prevalent.currentOnlineUser.getAccount() + eventID)
                        .removeValue();
                FirebaseDatabase.getInstance().getReference().child("EventsList")
                        .child("Admin  View")
                        .child(adminID).child("Events").child(eventID).child("Users" + Prevalent.currentOnlineUser.getAccount())
                        .removeValue();

                eventsRef.child("User  View").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String, Object> EventsMap = new HashMap<>();
                        EventsMap.put("ePeople", Integer.parseInt(dataSnapshot.child(eventID).child("ePeople").getValue().toString()) + 1);  //??????
                        eventsRef.child("User  View").child(eventID).updateChildren(EventsMap);
                        eventsRef.child("Admin  View").child(adminID).child(eventID).updateChildren(EventsMap);
                        loadingBar.dismiss();
                        Toast.makeText(EventDetailsActivity.this, "??????????????????!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EventDetailsActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        eventListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Calendar mCal = Calendar.getInstance();
                String dateformat = "yyyy/MM/dd";
                SimpleDateFormat df = new SimpleDateFormat(dateformat);
                String today = df.format(mCal.getTime());
                if (dataSnapshot.child("User  View").child("Users" + Prevalent.currentOnlineUser.getAccount()).child("Events").child("Users" + Prevalent.currentOnlineUser.getAccount() + eventID).exists()) {
                if (dataSnapshot.child("User  View").child("Users" + Prevalent.currentOnlineUser.getAccount()).child("Events").child("Users" + Prevalent.currentOnlineUser.getAccount() + eventID).child("eStatus").getValue().toString().equals("?????????")) {
                        YesEvent.setText("???????????????");
                        YesEvent.setEnabled(false);
                        YesEvent.setVisibility(View.VISIBLE);
                        NoEvent.setVisibility(View.INVISIBLE);
                    }
                else {
                    NoEvent.setText("????????????");
                    YesEvent.setVisibility(View.INVISIBLE);
                    NoEvent.setVisibility(View.VISIBLE);
                }
                } else if (Integer.parseInt(eventPeople.getText().toString()) == 0) {
                    YesEvent.setText("????????????????????????");
                    YesEvent.setVisibility(View.VISIBLE);
                    NoEvent.setVisibility(View.INVISIBLE);
                }
                else {
                    YesEvent.setText("????????????");
                    YesEvent.setVisibility(View.VISIBLE);
                    NoEvent.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getEventDetails(String eventID) {


        eventsRef.child("User  View").child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Events events = dataSnapshot.getValue(Events.class);

                    eventName.setText("????????????: " + events.geteName());
                    eventPrice.setText(String.valueOf(events.geteMoney()));
                    eventDescription.setText("????????????: " + events.geteDescription());
                    eventPeople.setText(String.valueOf(events.getePeople()));
                    eventPlace.setText(events.getePlace());
                    eventDate.setText( events.geteDate());
                    Picasso.get().load(events.geteImage()).into(eventImage);
                    FinalEventName = events.geteName();
                    FinalEventDate = events.geteDate();
                    FinalEventPlace = events.getePlace();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
