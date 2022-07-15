package com.example.gndu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gndu.Data.MyDbHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class loginOrSignup extends AppCompatActivity {

    //FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_or_signup);

        TextView signup =findViewById(R.id.textView2);
        Intent goTOSignUpPage=new Intent(this,signup.class);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(goTOSignUpPage);
            }
        });

        TextView email=findViewById(R.id.login_detail);
        //firebaseDatabase =FirebaseDatabase.getInstance();


    }
    public void loginSubmit(View view)
    {
        EditText login=findViewById(R.id.login_detail);
        EditText password=findViewById(R.id.password_detail);

        //fetching Firebase data
        getData(login.getText().toString(),password.getText().toString());

        //fetching back4app data
      /*  ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        query.whereEqualTo("email", login.getText().toString());
        query.whereEqualTo("password",password.getText().toString());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    // login successful
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "login successful",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    /*String userName=user.getString("name");
                    String email=user.getString("email");
                    String semester=user.getString("semester");
                    String section=user.getString("section");
                    String[] data={user.getString("name"),user.getString("email"), user.getString("ID"),user.getString("phoneNumber"),user.getString("semester"),user.getString("section"),user.getString("objectId")};
                    Intent goToHome=new Intent(loginOrSignup.this,Home.class);
                    goToHome.putExtra("userdata",data);
                    startActivity(goToHome);
                } else {
                    // Something is wrong
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Something went wrong",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });*/
    }
    HashMap<String,String> userdata=new HashMap<>();
    private String[] getData(String login,String password) {
        String[] data=new String[8];

        // calling add value event listener method
        // for getting the values from database.

        databaseReference= FirebaseDatabase.getInstance().getReference().child("userData").child(login.split("@")[0]+"Data");
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
               for(DataSnapshot snapShort :snapshot.getChildren()){
                   userdata.put(snapShort.getKey(),snapShort.getValue(String.class));
                   String value=((String)snapShort.getValue(String.class));
                   data[i]=value;
                   Log.d("myapp",value);
                   i++;
               }
               userData user=new userData(userdata.get("email"),userdata.get("password"),userdata.get("section"),userdata.get("semester"),userdata.get("userId"),userdata.get("userName"),userdata.get("phoneNumber"),userdata.get("imageUrl"));
        if(!user.getEmail().equals("null")) {
            if (userdata.get("email").equals(login) && userdata.get("password").equals(password) ){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "login successful",
                        Toast.LENGTH_SHORT);
                toast.show();
                MyDbHandler DB=new MyDbHandler(loginOrSignup.this);
                DB.addUser(user);
                Intent goToHome=new Intent(loginOrSignup.this,Home.class);
                Home.userdata=userdata;
                goToHome.putExtra("userdata",data);
                startActivity(goToHome);
                finish();
            } else if (userdata.get("email").equals(login) && !userdata.get("password").equals(password) ){
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Password mismatch",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "user authentication failed,Please retry..",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "user not found,Please register yourself",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
             //  data.add("null");
            Log.d("myapp","sorry;(");
           }
       });

       return data;
    }
}
