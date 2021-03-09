package com.example.index;

import android.content.Intent;
import android.os.Bundle;

import com.example.index.Model.Events;
import com.example.index.Prevalent.Prevalent;
import com.example.index.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView userNameTextView, userMoneyTextView;
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference EventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        EventsRef = FirebaseDatabase.getInstance().getReference().child("Events").child("User  View");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("活動列表");  //新寫的程式碼

        setSupportActionBar(toolbar);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        View headerView = navigationView.getHeaderView(0);

        userNameTextView = headerView.findViewById(R.id.user_profile_name);
        userMoneyTextView = headerView.findViewById(R.id.user_profile_money);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);
        userNameTextView.setText(Prevalent.currentOnlineUser.getName());

        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        //圖片

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_share
                , R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_home, R.id.nav_send,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        //用戶點擊Paper.book().destroy();
        // Intent intent=new Intent(HomeActivity.this,MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //staractivity(intent);
        //finish();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Prevalent.currentOnlineUser.setMoney(Integer.parseInt(dataSnapshot.child("Users" + Prevalent.currentOnlineUser.getAccount()).child("money").getValue().toString()));
                userMoneyTextView.setText(String.valueOf(Prevalent.currentOnlineUser.getMoney()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>()
                        .setQuery(EventsRef, Events.class)
                        .build();

        FirebaseRecyclerAdapter<Events, EventViewHolder> adapter =
                new FirebaseRecyclerAdapter<Events, EventViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull final Events model) {

                        holder.txteventName.setText("活動名稱:" + model.geteName());
                        holder.txteventDescription.setText("活動敘述:" + model.geteDescription());

                        Picasso.get().load(model.geteImage()).into(holder.eImageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(HomeActivity.this, EventDetailsActivity.class);
                                intent.putExtra("eid", model.getEid());
                                intent.putExtra("adminid", model.getAdmin());
                                startActivity(intent);
                            }
                        });
                    }


                    @NonNull
                    @Override
                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_items_layout, parent, false);
                        EventViewHolder holder = new EventViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
