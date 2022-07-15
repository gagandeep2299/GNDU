package com.example.gndu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gndu.Data.MyDbHandler;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Account extends AppCompatActivity {


    ImageView ivStory;
    public static HashMap<String,String> userdata=new HashMap<>();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account);
        Intent intent = getIntent();
        String[] data = intent.getStringArrayExtra("userdata");
        TextView name=findViewById(R.id.Name_Account);
        TextView email=findViewById(R.id.Email_account);
        String currentGreeting= new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        String[] textString = {"Edit your profile here", "Contact us", "Rate Us","Sign Out"};
        int[] drawableIds = {R.drawable.account_update, R.drawable.notification_setting, R.drawable.share_feedback, R.drawable.rate_us,R.drawable.signout};
        MyDbHandler db=new MyDbHandler(Account.this);
        CustomAdapter adapter = new CustomAdapter(this,  textString, drawableIds);
        ListView listView1 = (ListView)findViewById(R.id.menuList);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            Log.d("myapp", String.valueOf(position));
            if(position==0)
            {
                Intent goToAccountSetting=new Intent(Account.this,change_account_setting.class);
                change_account_setting.userdata=userdata;
                goToAccountSetting.putExtra("userdata",data);
                startActivity(goToAccountSetting);
            }
            else if(position==1)
            {
                Intent contactus=new Intent(Account.this,ContactUs.class);
                startActivity(contactus);

            }
            else if(position==3)
            {
                Intent Logout=new Intent(Account.this,loginOrSignup.class);
                db.deleteUser(userdata.get("userId"));
                startActivity(Logout);
                finish();
            }

        });
        if(Integer.parseInt(currentGreeting)<12)
            currentGreeting="Good Morning ";
        else
            currentGreeting="Good Afternoon ";

        name.setText(currentGreeting+userdata.get("userName")+"!");
        email.setText(userdata.get("email"));
        /*try {
            getImageIfAvailable(data[6],data[5]);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        ivStory=findViewById(R.id.ivStory);
            try {
                Picasso.get().load(userdata.get("imageUrl")).into(ivStory);
            }
            catch(Exception e)
            {
                Log.d("myapp",e.getMessage());
            }
    }


    public void backToHome(View view)
    {
      finish();
    }
   /*protected void getImageIfAvailable(@androidx.annotation.NonNull String name, String Id) throws IOException {
            ivStory=findViewById(R.id.ivStory);
            progressDialog=new ProgressDialog(Account.this);
            progressDialog.setMessage("fetching data");
            progressDialog.setCancelable(false);
            progressDialog.show();
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

       DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("ProfileImage").child(name+Id);
            File localFile= File.createTempFile("tempFile",".jpeg");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String link = dataSnapshot.getValue(String.class);
                    Picasso.get().load(link).into(ivStory);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // we are showing that error message in toast
                    Toast.makeText(Account.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
                }
            });
    }*/
}