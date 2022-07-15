package com.example.gndu;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ourRecuiter extends AppCompatActivity {

    ArrayList<String> images=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    ArrayList<com.example.gndu.placementView> placementV=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_recuiter);

        getListOfImages();


    }

        public void getListOfImages()
        {
            FirebaseDatabase fb=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("placementData");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for(DataSnapshot s:snapshot.getChildren())
                    {
                        com.example.gndu.placementView p=new com.example.gndu.placementView(s.getValue(String.class),s.getKey());
                        placementV.add(p);
                        name.add(s.getKey());
                        images.add(s.getValue(String.class));
                    }
                    com.example.gndu.CourseGVAdapter adapt=new com.example.gndu.CourseGVAdapter(ourRecuiter.this,placementV);
                    GridView gridView=findViewById(R.id.gridView);
                    gridView.setAdapter(adapt);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //  data.add("null");
                    Log.d("myapp","sorry;(");
                }
            });
        }
    }