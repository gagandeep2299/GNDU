package com.example.gndu;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gndu.databinding.ActivityHomeBinding;
import com.example.gndu.ui.examination;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ViewPager2 viewPager2;
    private final Handler sliderHandler = new Handler();
    TextView username;
    TextView email;
    String[] data;
    DrawerLayout drawer;
    public static HashMap<String,String> userdata=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setNavigationViewListener();

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_examination, R.id.nav_Result, R.id.nav_Syllabus, R.id.nav_studentForm, R.id.nav_placementForms, R.id.nav_feesSection)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //get intent userdata

        //drawable data enter
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);
        email = headerView.findViewById(R.id.emailView);
        username.setText(userdata.get("userName"));
        email.setText(userdata.get("email"));
        ImageView ig=headerView.findViewById(R.id.profileIamge);
        Picasso.get().load(userdata.get("imageUrl")).into(ig);
       // Log.d("myapp",userdata.get("email")+" \n"+userdata.get("password")+" \n"+userdata.get("imageUrl")+" \n"+userdata.get("phoneNumber")+" \n"+userdata.get("section")+" \n"+userdata.get("semester")+" \n"+userdata.get("userId")+" \n"+userdata.get("userName"));
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAccountFragment = new Intent(Home.this, Account.class);
                Account.userdata=userdata;
                goToAccountFragment.putExtra("userdata", data);
                startActivity(goToAccountFragment);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAccountFragment = new Intent(Home.this, Account.class);
                Account.userdata=userdata;
                goToAccountFragment.putExtra("userdata", data);
                startActivity(goToAccountFragment);

            }
        });
       /*navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                if (id == R.id.nav_account){
                    Intent goToAccountFragment = new Intent(Home.this, Account.class);
                    Account.userdata=userdata;
                    goToAccountFragment.putExtra("userdata", data);
                    startActivity(goToAccountFragment);
                }
                return true;
            }
        });*/
        LinearLayout recuiter=findViewById(R.id.OurRecuiters);
        recuiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,ourRecuiter.class);
                startActivity(i);
            }
        });



        examination.semester =userdata.get("semester");
        result.semester=userdata.get("semester");
        result.id=userdata.get("userId");
        result.Result_name=userdata.get("userName");
        Syllabus.sem=userdata.get("semester");
        TimeTable.Sem=userdata.get("semester");
        result.ImageUrl=userdata.get("imageUrl");



    //fetchExaminationData(data[4]);


}
public void openTimeTableActivity(View view)
{
    Intent startTimeTableActivity=new Intent(Home.this,TimeTable.class);
    startActivity(startTimeTableActivity);
}
public void gallery(View view)
{
    Intent i=new Intent(Home.this,Gallery.class);
    startActivity(i);
}

@SuppressLint("SetTextI18n")
public void ShowUpdates(View view)
{
    if(Objects.equals(userdata.get("semester"), "null")) {
        Intent i = new Intent(Home.this, updates.class);
        i.putExtra("selected","updates");
        startActivity(i);
    }
    else{
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.updates);
        TextView head = bottomSheetDialog.findViewById(R.id.heading);
        head.setText("Updates");
        ListView updateListview = bottomSheetDialog.findViewById(R.id.updateListView);
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("updates");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String arr[] = Objects.requireNonNull(snapshot.getValue(String.class)).split("@");
                ArrayList<String> updateList = new ArrayList<>();
                Collections.addAll(updateList, arr);
                ArrayAdapter<String> adapt = new ArrayAdapter<>(Home.this, R.layout.update_list_design, R.id.updatesText, updateList);
                assert updateListview != null;
                updateListview.setAdapter(adapt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp", "sorry;(");
            }
        });
        bottomSheetDialog.show();


    }
}
    @SuppressLint("SetTextI18n")
    public void ShowScholarships(View view)
    {
        if(Objects.equals(userdata.get("semester"), "null")) {
            Intent i = new Intent(Home.this, updates.class);
            i.putExtra("selected","Scholarship");
            startActivity(i);
        }
        else {
            ArrayList<String> scholarshipsName = new ArrayList<>();
            ArrayList<String> scholarshipsLinks = new ArrayList<>();
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.updates);
            TextView heading = bottomSheetDialog.findViewById(R.id.heading);
            heading.setText("Scholarships");
            ListView updateListview = bottomSheetDialog.findViewById(R.id.updateListView);
            FirebaseDatabase fb = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Scholarship");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Log.d("myapp", s.getKey());
                        scholarshipsName.add(s.getKey());
                        scholarshipsLinks.add(s.getValue(String.class));
                    }

                    ArrayAdapter<String> adapt1 = new ArrayAdapter<>(Home.this, R.layout.update_list_design, R.id.updatesText, scholarshipsName);
                    assert updateListview != null;
                    updateListview.setAdapter(adapt1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //  data.add("null");
                    Log.d("myapp", "sorry;(");
                }
            });
            bottomSheetDialog.show();
            assert updateListview != null;
            updateListview.setOnItemClickListener((arg0, arg1, position, arg3) -> {
                Log.d("myapp", String.valueOf(position));
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(scholarshipsLinks.get(position)));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(scholarshipsLinks.get(position)));
                    startActivity(i);
                }

            });
        }
    }
    public void StudentCornerContent(View view)
    {
        ArrayList<String> Name=new ArrayList<>();
        ArrayList<String> Links=new ArrayList<>();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.updates);
        TextView heading=bottomSheetDialog.findViewById(R.id.heading);
        heading.setText("Student's Corner");
        ListView updateListview=bottomSheetDialog.findViewById(R.id.updateListView);
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("studentCorner");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s:snapshot.getChildren())
                {
                    Log.d("myapp",s.getKey());
                    Name.add(s.getKey());
                    Links.add(s.getValue(String.class));
                }

                ArrayAdapter<String> adapt1=new ArrayAdapter<>(Home.this,R.layout.update_list_design,R.id.updatesText,Name);
                assert updateListview != null;
                updateListview.setAdapter(adapt1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp","sorry;(");
            }
        });
        bottomSheetDialog.show();
        assert updateListview != null;
        updateListview.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            Log.d("myapp", String.valueOf(position));
            try {
                Intent i = new Intent("android.intent.action.MAIN");
                i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                i.addCategory("android.intent.category.LAUNCHER");
                i.setData(Uri.parse(Links.get(position)));
                startActivity(i);
            }
            catch(ActivityNotFoundException e) {
                // Chrome is not installed
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Links.get(position)));
                startActivity(i);
            }

        });
    }

    //slider settings

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }





}