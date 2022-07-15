package com.example.gndu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Gallery extends AppCompatActivity {

    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> extension=new ArrayList<>();
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    Task<Void> databaseReference;
    String url;
    ArrayList<galleryView> galleryV=new ArrayList<>();
    private FirebaseDatabase Firebasedatabase;
    HashMap<String,String> image=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);
        getListOfImages();
        GridView galleryview=findViewById(R.id.galleryGidView);
        galleryview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in=new Intent(Gallery.this,ImageDetailActivity.class);
                ImageDetailActivity.urls=extension;
                ImageDetailActivity.names=name;
                ImageDetailActivity.imagesData=image;
                in.putExtra("position",i);
                startActivity(in);
            }
        });
        change_account_setting cas=new change_account_setting();
        FloatingActionButton fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                SelectImage();

            }
        });
    }
    public void getListOfImages()
    {
        StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();

        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Gallery");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot s:snapshot.getChildren())
                {
                    image.put(s.getKey(),s.getValue(String.class));
                    name.add(s.getKey());
                    extension.add(s.getValue(String.class));
                    galleryView g1=new galleryView(s.getValue(String.class));
                    galleryV.add(g1);
                }
                galleryAdapter adapt=new galleryAdapter(Gallery.this,galleryV);
                GridView gridView=findViewById(R.id.galleryGidView);
                gridView.setAdapter(adapt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp","sorry;(");
            }
        });
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
        ImageView imageView=findViewById(R.id.imgview);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage(name.size()+1);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage(int number)
    {
        if(filePath != null)
        {
           /* final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();*/
            String name="image"+number;
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference ref =storage.getReference().child("gallery").child(name);
                  //  storageReference.child("gallery").child(name);

            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        //progressDialog.dismiss();
                        getImageUrl(name);
                        Log.d("myapp",filePath.toString());
                        Toast.makeText(Gallery.this, "Added", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // progressDialog.dismiss();
                            Toast.makeText(Gallery.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void getImageUrl(String name)
    {
        Intent getData=getIntent();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference();
        ref.child("gallery/"+name)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'photos/profile.png'
                Log.d("myapp",uri.toString());
                url=uri.toString();
                image.put(name,url);
                startActivity(getIntent());
                Firebasedatabase=FirebaseDatabase.getInstance();
                databaseReference=Firebasedatabase.getReference().child("Gallery").setValue(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast makeToast=Toast.makeText(Gallery.this,"unable to load url",Toast.LENGTH_SHORT);
                makeToast.show();
            }
        });
    }

}
class galleryAdapter extends ArrayAdapter<galleryView> {
    public galleryAdapter(@NonNull Context context, ArrayList<galleryView> galleryViewArrayList) {
        super(context, 0, galleryViewArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_design, parent, false);
        }
        galleryView courseModel = getItem(position);
        ImageView courseIV = listitemView.findViewById(R.id.galleryImageView);
        Picasso.get().load(courseModel.image).into(courseIV);
        return listitemView;
    }
}
class  galleryView
{
    String image;
    public galleryView(String Image)
    {
        this.image=Image;
    }
}