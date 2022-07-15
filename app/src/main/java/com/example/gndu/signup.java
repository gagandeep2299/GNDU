package com.example.gndu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {


    private static final int REQUEST_GET_SINGLE_FILE =0 ;
    final int[] modeInt = {1};  //mode 1 for teacher and mode 2 for student
    Uri selectedImageUri;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    userData userdata;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        final String[] mode = {"For Faculty","For Students"};
        final String[] idMode = {"   Unique id","   Student id"};
        final String[] clickForChangeMode={"Sign up as Faculty","Sign up as Student"};
        TextView signIn=findViewById(R.id.textView10);
        TextView signUpForFacility=findViewById(R.id.textView13);
        TextView signUpMode=findViewById(R.id.textView12);
        EditText id=findViewById(R.id.id);

        //semester spinner
        Spinner semester_spinner=findViewById(R.id.Semester_spinner);
        ArrayAdapter<CharSequence> semester_spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.semester_spinner, android.R.layout.simple_spinner_item);
        semester_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester_spinner.setAdapter(semester_spinner_adapter);
        //section spinner
        Spinner section_spinner=findViewById(R.id.Section_spinner);
        ArrayAdapter<CharSequence> section_spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.section_spinner, android.R.layout.simple_spinner_item);
        section_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        section_spinner.setAdapter(section_spinner_adapter);

        //listener to change mode of registration
        signUpForFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (modeInt[0] == 1) {
                    signUpMode.setText(mode[0]);
                    id.setHint(idMode[0]);
                    signUpForFacility.setText(clickForChangeMode[0]);
                    modeInt[0] =2;
                    semester_spinner.setVisibility(View.GONE);
                    section_spinner.setVisibility(View.GONE);
                }
                else{
                    signUpMode.setText(mode[1]);
                    id.setHint(idMode[1]);
                    signUpForFacility.setText(clickForChangeMode[1]);
                    modeInt[0]=1;
                    semester_spinner.setVisibility(View.VISIBLE);
                    section_spinner.setVisibility(View.VISIBLE);
                }

            }
        });
        Intent signInTo=new Intent(this,loginOrSignup.class);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(signInTo);
            }
        });
    }

        //registered data send
    public void registrationButton(View view)
    {
        EditText id=findViewById(R.id.id);
        EditText name=findViewById(R.id.name);
        EditText email=findViewById(R.id.email);
        EditText password=findViewById(R.id.password);
        EditText rePassword=findViewById(R.id.rePassword);
        Spinner semester_spinner=findViewById(R.id.Semester_spinner);
        Spinner section_spinner=findViewById(R.id.Section_spinner);
        //firebase data parsing
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        userData a;
        //userdata=new userData();
        if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(id.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(rePassword.getText().toString())) {
            // if the text fields are empty
            // then show the below message.
            Toast.makeText(signup.this, "Please add some data.", Toast.LENGTH_SHORT).show();
        } else {
            // else call the method to add
            // data to our database.
            if(modeInt[0]==2) {
                if (!password.getText().toString().equals(rePassword.getText().toString())) {
                    Toast warning = Toast.makeText(signup.this, "Password doesn't match", Toast.LENGTH_SHORT);
                    warning.show();
                } else {
                    a = new userData(email.getText().toString(), password.getText().toString(), "null", "null", id.getText().toString(), name.getText().toString(), "null", "null");
                    databaseReference.child("userData").child(email.getText().toString().split("@")[0] + "Data").setValue(a);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Registered Successfully ,please login into the system",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    Intent toLogin = new Intent(this, loginOrSignup.class);
                    startActivity(toLogin);
                    finish();
                }
            }
            else{
                 if(!password.getText().toString().equals(rePassword.getText().toString()))
                {
                    Toast warning=Toast.makeText(signup.this,"Password doesn't match",Toast.LENGTH_SHORT);
                    warning.show();
                }
                else{
                    a = new userData(email.getText().toString(), password.getText().toString(), section_spinner.getSelectedItem().toString(), semester_spinner.getSelectedItem().toString(), id.getText().toString(), name.getText().toString(), "null", "null");
                    databaseReference.child("userData").child(email.getText().toString().split("@")[0]+"Data").setValue(a);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Registered Successfully ,please login into the system",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    Intent toLogin=new Intent(this,loginOrSignup.class);
                    startActivity(toLogin);
                    finish();
                }

            }


        }
        //back4app data parsing
      /* ParseObject userDataObject = new  ParseObject("UserData");

        userDataObject.put("ID",id.getText().toString());
        userDataObject.put("name",name.getText().toString());
        userDataObject.put("email",email.getText().toString());
        userDataObject.put("password",password.getText().toString());
        if(modeInt[0]==2)
        {
        userDataObject.put("semester","null");
        userDataObject.put("section","null");
        }
        else{
        userDataObject.put("semester",semester_spinner.getSelectedItem().toString());
        userDataObject.put("section",section_spinner.getSelectedItem().toString());
        }
        userDataObject.saveInBackground(e -> {
            if (e != null){
                Log.e("MainActivity", e.getLocalizedMessage());
            }else{
                Log.d("MainActivity","Object saved.");
            }
        });
       **/
    }
    private void addDatatoFirebase(String name, String email, String userid,String password,String semester,String section) {
        // below 3 lines of code is used to set
        // data in our object class.
        /*userdata.setUserName(name);
        userdata.setEmail(email);
        userdata.setUserId(userid);
        userdata.setPassword(password);
        userdata.setSection(section);
        userdata.setSemester(semester);*/

        // we are use add value event listener method
        // which is called with database reference.

        /*databaseReference.child(email.split("@")[0]+"Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.setValue(a);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Registered Successfully ,please login into the system",
                        Toast.LENGTH_SHORT);

                toast.show();
                Intent toLogin=new Intent(signup.this,loginOrSignup.class);
                startActivity(toLogin);
                // after adding this data we are showing toast message.
                Toast.makeText(signup.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(signup.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

}
