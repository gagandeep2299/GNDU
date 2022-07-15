package com.example.gndu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gndu.Data.MyDbHandler;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageDetailActivity extends AppCompatActivity {

    public static ArrayList<String> urls;
    public static ArrayList<String> names;
    int position;
    public static String mode;
    public static HashMap<String,String> imagesData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_detail);
        Intent in=getIntent();
        position=in.getIntExtra("position",1);
        ImageView ig=findViewById(R.id.ImageGet);
        Picasso.get().load(urls.get(position)).into(ig);
        ImageButton delete =findViewById(R.id.deleteButton);
        MyDbHandler db=new MyDbHandler(ImageDetailActivity.this);
        userData ur=db.getUser();
        if(!ur.getSemester().equals("null"))
            delete.setVisibility(View.GONE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagesData.remove(names.get(position));
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            Task<Void> databaseReference=firebaseDatabase.getReference().child("Gallery").setValue(imagesData);
                Toast ts=Toast.makeText(ImageDetailActivity.this,"Reload your gallery to see changes",Toast.LENGTH_SHORT);
                ts.show();
                finish();
            }
        });

    }
    public void finish(View view)
    {
        finish();
    }
    public void previousImage(View view)
    {
        ImageView ig=findViewById(R.id.ImageGet);
        if(position>1)
        {
            position--;
            Picasso.get().load(urls.get(position)).into(ig);
        }
    }
    public void nextImage(View view)
    {
        ImageView ig=findViewById(R.id.ImageGet);
        if(position<urls.size()-1)
        {
            position++;
            Picasso.get().load(urls.get(position)).into(ig);
        }
    }
}