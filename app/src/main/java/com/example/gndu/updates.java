package com.example.gndu;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class updates extends AppCompatActivity {

    String updates;
    String arr1[];
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates);
        Intent get=getIntent();
        String selectedActivity=get.getStringExtra("selected");
        if(selectedActivity.equals("Scholarship"))
        {
            showScholarships();
        }
        else
        showUpdates();
        FloatingActionButton addFab=findViewById(R.id.addData);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(updates.this,update_data.class);
                i.putExtra("updatesAdd",updates);
                i.putExtra("mode","0");
                startActivity(i);
                /*bottomSheetDialog.setContentView(R.layout.add_update);
                EditText text = bottomSheetDialog.findViewById(R.id.editTextAddData);
                Spinner s=bottomSheetDialog.findViewById(R.id.spinner);
                s.setVisibility(View.GONE);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                Button add=bottomSheetDialog.findViewById(R.id.addData);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!TextUtils.isEmpty(text.getText().toString()))
                            updates+="@"+text.getText().toString();
                        databaseReference.child("updates").setValue(updates);
                    }
                });
                Button cancel=bottomSheetDialog.findViewById(R.id.deleteData);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(updates.this);
                        bottomSheetDialog.setContentView(R.layout.add_update);
                        EditText text = bottomSheetDialog.findViewById(R.id.editTextAddData);
                        Spinner s=bottomSheetDialog.findViewById(R.id.spinner);
                        text.setVisibility(View.GONE);
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference();
                        Button add=bottomSheetDialog.findViewById(R.id.addData);
                        add.setText("Delete");

                    }
                });
                bottomSheetDialog.show();
            }*/
            }});
        FloatingActionButton deleteFab=findViewById(R.id.deleteData);
        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(updates.this,update_data.class);
                i.putExtra("mode","1");
                i.putExtra("updatesAdd",updates);
                startActivity(i);
            }
        });

    }
    public void showScholarships()
    {
        ArrayList<String> scholarshipsName = new ArrayList<>();
        ArrayList<String> scholarshipsLinks = new ArrayList<>();
        ListView updateListview =findViewById(R.id.listview);
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

                ArrayAdapter<String> adapt1 = new ArrayAdapter<>(updates.this, R.layout.update_list_design, R.id.updatesText, scholarshipsName);
                assert updateListview != null;
                updateListview.setAdapter(adapt1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp", "sorry;(");
            }
        });
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
    public void showUpdates()
    {
        ListView updateListview=findViewById(R.id.listview);
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("updates");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updates=snapshot.getValue(String.class);
                arr1= Objects.requireNonNull(snapshot.getValue(String.class)).split("@");

                ArrayList<String> updateList=new ArrayList<>();
                Collections.addAll(updateList,arr1);
                ArrayAdapter<String> adapt=new ArrayAdapter<>(updates.this,R.layout.update_list_design,R.id.updatesText,updateList);
                assert updateListview != null;
                updateListview.setAdapter(adapt);
                Spinner spinner = new Spinner(updates.this);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (updates.this, android.R.layout.simple_spinner_item,
                                updateList); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp","sorry;(");
            }
        });

    }

}