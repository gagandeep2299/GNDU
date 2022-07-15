package com.example.gndu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        TextView map=findViewById(R.id.openMap);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location="http://maps.google.com/maps?q=loc:"+"Guru Nanak Dev University, JRMG+J92, Chheharta Rd, Makka Singh Colony, Amritsar, Punjab 143005";
                Uri gmmIntentUri = Uri.parse(location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        TextView callAdministration=findViewById(R.id.callAdministrativeOffice);
        callAdministration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phno="0000000000";

                Intent i=new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse(phno));
                startActivity(i);
            }
        });
        TextView calladmission=findViewById(R.id.callOnlineAdmissionEnquiry);
        calladmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phno="+91183225880209";

                Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse(phno));
                startActivity(i);
            }
        });
    }
}