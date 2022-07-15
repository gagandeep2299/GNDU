package com.example.gndu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class update_data extends AppCompatActivity {

    String updates;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        Intent i=getIntent();
        final String[] updates = {i.getStringExtra("updatesAdd")};
        if(i.getStringExtra("mode").equals("0")) {
            EditText text = findViewById(R.id.editTextAddData);
            Spinner s = findViewById(R.id.data_spinner);
            s.setVisibility(View.GONE);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            Button add = findViewById(R.id.addbutton);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(text.getText().toString()))
                        updates[0] += "@" + text.getText().toString();
                    databaseReference.child("updates").setValue(updates[0]);
                    finish();
                }
            });
        }
        else
        {
            Log.d("myapp",i.getStringExtra("mode"));

            String[] arr =i.getStringExtra("updatesAdd").split("@");
            EditText text =findViewById(R.id.editTextAddData);
            Spinner s=findViewById(R.id.data_spinner);
            text.setVisibility(View.GONE);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            Button add=findViewById(R.id.addbutton);
            add.setText("Delete");
            //Spinner spinner = new Spinner(this);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                    (update_data.this, android.R.layout.simple_spinner_item,
                            arr); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            s.setAdapter(spinnerArrayAdapter);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("myapp", (String) s.getSelectedItem());
                    ArrayList<String> l=new ArrayList<>();
                    Collections.addAll(l,arr);
                    l.remove((String) s.getSelectedItem());
                    StringBuilder update1= new StringBuilder();
                    for(String l1:l)
                        update1.append(l1).append("@");
                    update1.substring(0,update1.length()-1);
                    String finalUpdate = update1.toString();
                    Log.d("myapp",finalUpdate);
                    databaseReference.child("updates").setValue(finalUpdate);
                    finish();
                }
            });


        }
        Button cancel=findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
/*    */