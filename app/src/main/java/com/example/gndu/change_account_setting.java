package com.example.gndu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gndu.Data.MyDbHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class change_account_setting extends AppCompatActivity {

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    FirebaseDatabase Firebasedatabase;
    StorageReference storageReference;
    Task<Void> databaseReference;
    ImageView imageView;
    String email,section,semester,id,name,phonenumber;
    public static HashMap<String,String> userdata=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_account_setting);


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


        //get user data


        //set data to fields
        EditText Id=findViewById(R.id.userID);
        Id.setText(userdata.get("userId"));
        EditText name=findViewById(R.id.Name);
        name.setText(userdata.get("userName"));
        EditText email=findViewById(R.id.Email);
        email.setText(userdata.get("email"));
        EditText phoneNumber=findViewById(R.id.PhoneNumber);
        if(userdata.get("phoneNumber")!=null) {  phoneNumber.setText(userdata.get("phoneNumber"));  }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //hide semester and section for teacher
        if(userdata.get("section")==null)
        {
            section_spinner.setVisibility(View.GONE);
            semester_spinner.setVisibility(View.GONE);
        }
        else{
            if(userdata.get("section").equals("A"))
            section_spinner.setSelection(1);
            else if(userdata.get("section").equals("B"))
            section_spinner.setSelection(2);
            else
            section_spinner.setSelection(3);

            semester_spinner.setSelection(Integer.parseInt(Character.toString(userdata.get("semester").charAt(0))));
        }
        ImageView selectImg=findViewById(R.id.EditImage);
        selectImg.setOnClickListener(v -> SelectImage());
        imageView=findViewById(R.id.ivStory);


    }



    //back button
    public void backActivity(View view)
    {
        finish();
    }

   private void SelectImage()
   { Intent intent = new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage(String name,String Id,String email)
    {
        if(filePath != null)
        {
           /* final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();*/

            StorageReference ref = storageReference.child("ProfileImage").child(name.split(" ")[0]+Id);
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        //progressDialog.dismiss();
                        getImageUrl(name,Id,email);
                        Toast.makeText(change_account_setting.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           // progressDialog.dismiss();
                            Toast.makeText(change_account_setting.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                           // progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    public void getImageUrl(String name,String Id,String email1)
    {
        Intent getData=getIntent();
        String[] data=getData.getStringArrayExtra("userdata");
        StorageReference ref = storageReference;
        ref.child("ProfileImage/" + name.split(" ")[0]+Id)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'photos/profile.png'
                Log.d("myapp",uri.toString());
                url=uri.toString();
                userData a=new userData(email,userdata.get("password"),section,semester,id,name,phonenumber,uri.toString());
                MyDbHandler db=new MyDbHandler(change_account_setting.this);
                db.updateUser(a);
                Firebasedatabase=FirebaseDatabase.getInstance();
                databaseReference=Firebasedatabase.getReference().child("userData").child(email1).setValue(a);
                Toast.makeText(change_account_setting.this,"profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast makeToast=Toast.makeText(change_account_setting.this,"unable to load url",Toast.LENGTH_SHORT);
                makeToast.show();
            }
        });
    }
    String url;
    //action with save button
    public void saveChanges(View view)
    {

        EditText Id=findViewById(R.id.userID);
        EditText name=findViewById(R.id.Name);
        EditText email=findViewById(R.id.Email);
        EditText phoneNumber=findViewById(R.id.PhoneNumber);
        Spinner semester_spinner=findViewById(R.id.Semester_spinner);
        Spinner section_spinner=findViewById(R.id.Section_spinner);
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("email", email);
        map.put("section",section_spinner.getSelectedItem().toString() );
        map.put("semester", semester_spinner.getSelectedItem().toString());
        map.put("userId", Id.getText().toString());
        map.put("userName", name.getText().toString());
        map.put("phoneNumber", phoneNumber.getText().toString());

        //ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        uploadImage(name.getText().toString(),Id.getText().toString(),email.getText().toString().split("@")[0]+"Data");
        this.email=email.getText().toString();
        this.section=section_spinner.getSelectedItem().toString();
        this.semester=semester_spinner.getSelectedItem().toString();
        this.id=Id.getText().toString();
        this.name=name.getText().toString();
        this.phonenumber=phoneNumber.getText().toString();
        // Retrieve the object by id
        /*query.getInBackground(data[6], new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    user.put("phoneNumber", phoneNumber.getText().toString());
                    user.put("name", name.getText().toString());
                    user.put("email", email.getText().toString());
                    user.put("semester", semester_spinner.getSelectedItem().toString());
                    user.put("section", section_spinner.getSelectedItem().toString());
                    //user.put("profilePic",img);
                    user.saveInBackground();
                } else {
                    Log.d("myapp",e.getMessage().toString()+data[5]);
                }
            }
        });*/
    }

}