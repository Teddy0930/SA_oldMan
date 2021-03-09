package com.example.index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.index.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity {
    private Button logout, myevent, myorder, uploadevent, uploadproduct, mysetting;
    private TextView userNameTextView, userMoneyTextView;
    private DatabaseReference AdminRef = FirebaseDatabase.getInstance().getReference().child("Admins");
    private ViewFlipper viewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        viewFlipper = findViewById(R.id.view_flipper);



        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();

        logout = (Button) findViewById(R.id.logout_btn);
        myevent = (Button) findViewById(R.id.my_event_btn);
        myorder = (Button) findViewById(R.id.my_order_btn);
        uploadevent = (Button) findViewById(R.id.upload_event_btn);
        uploadproduct = (Button) findViewById(R.id.upload_product_btn);
        mysetting = (Button) findViewById(R.id.admin_setting_btn);


        userNameTextView = findViewById(R.id.user_profile_name);
        userMoneyTextView = findViewById(R.id.user_profile_money);
        CircleImageView profileImageView = findViewById(R.id.user_profile_image);
        userNameTextView.setText(Prevalent.currentOnlineAdmin.getName());

        Picasso.get().load(Prevalent.currentOnlineAdmin.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        mysetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminSettingsActivity.class);
                startActivity(intent);
            }
        });
        myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminsNewProductRecordActivity.class);
                startActivity(intent);
            }
        });
        myevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminsNewEventRecordActivity.class);
                startActivity(intent);
            }
        });
        uploadproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                startActivity(intent);
            }
        });
        uploadevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewEventActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(AdminCategoryActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void previous(View v) {
        viewFlipper.setInAnimation(this,R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this,R.anim.slide_out_left);
        viewFlipper.showPrevious();
    }

    public void next(View v) {
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
        viewFlipper.showNext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Prevalent.currentOnlineAdmin.setMoney(Integer.parseInt(dataSnapshot.child("Admins" + Prevalent.currentOnlineAdmin.getAccount()).child("money").getValue().toString()));
                userMoneyTextView.setText(String.valueOf(Prevalent.currentOnlineAdmin.getMoney()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
