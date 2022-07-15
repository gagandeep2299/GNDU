package com.example.gndu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gndu.Data.MyDbHandler;

import java.sql.Connection;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

     Connection con;
     String ConnectionResult="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ImageView logo = findViewById(R.id.profileIamge);
        TextView textLogo = findViewById(R.id.emailView);
        MyDbHandler DB=new MyDbHandler(MainActivity.this);
        userData ur=DB.getUser();
        //getTExtFromSql();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ur==null) {
                    Intent i = new Intent(MainActivity.this, loginOrSignup.class);
                    startActivity(i);
                    finish();
                }
                else {
                    HashMap<String,String> usr=new HashMap<>();
                    usr.put("email",ur.getEmail());
                    usr.put("password",ur.getPassword());
                    usr.put("imageUrl",ur.getImageUrl());
                    usr.put("phoneNumber",ur.getPhoneNumber());
                    usr.put("section",ur.getSection());
                    usr.put("semester",ur.getSemester());
                    usr.put("userId",ur.getUserId());
                    usr.put("userName",ur.getUserName());
                    Home.userdata=usr;
                    Intent i = new Intent(MainActivity.this, Home.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 3000);

        /*CheckLogin ch=new CheckLogin();
        ch.execute("");

       /* Intent login=new Intent(this,loginOrSignup.class);
        startActivity(login);*/
        //getTExtFromSql();
    }
    /*public void getTExtFromSql()
    {
        try {
            CheckLogin c=new CheckLogin();
            con=c.connectionclass();
            if(con!=null)
            {
                Log.e("myapp","under if");
                String query="select * from User_Cridentials";
                Statement str=con.createStatement();
                ResultSet rs= str.executeQuery(query);
                con.close();

                Log.d("myapp",rs.getString(1)+" "+rs.getString(2));
            }
            else {
                ConnectionResult="check Connection";
            }

        }
        catch(Exception e)
        {
                Log.d("myapp","get Text"+e.getMessage());
        }
    }


    /*public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
               // con = connectionclass();
                if(con==null)
                {
                    z="checkYour Internet Access!";
                }
                else{
                    String query="select * from Customer";
                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    if(rs.next())
                    {
                        name1=rs.getString("FirstName");
                        Log.d("myapp",name1);
                        isSuccess=true;
                        con.close();
                    }
                    else{
                        z="invalid Query";
                        isSuccess=false;
                    }
                }
            } catch (Exception e) {
                isSuccess=false;
                z=e.getMessage();
                e.printStackTrace();
                Log.d("myapp",e.getMessage());
            }
            return z;
        }


    }*/
}