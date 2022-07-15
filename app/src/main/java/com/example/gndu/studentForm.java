package com.example.gndu;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class studentForm extends Fragment {

    private StudentFormViewModel mViewModel;

    public static studentForm newInstance() {
        return new studentForm();
    }
    ArrayList<String> formsName=new ArrayList<>();
    ArrayList<String> formsLinks=new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.student_form_fragment, container, false);
        getListOfForms(view);
        ListView list=view.findViewById(R.id.student_form_listview);
        list.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            Log.d("myapp", String.valueOf(position));
            download(formsLinks.get(position));
            Toast download=Toast.makeText(getContext(),"Start Downloading......",Toast.LENGTH_SHORT);
            download.show();
        });
        return view;
    }
    public void download(String formsName)
    {
        StorageReference stg= FirebaseStorage.getInstance().getReference();
        StorageReference ref=stg.child("forms/"+formsName+".pdf");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadFile(requireContext(),formsName,".pdf",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentFormViewModel.class);
        // TODO: Use the ViewModel
    }

    public void getListOfForms(View view)
    {
        ListView listview=view.findViewById(R.id.student_form_listview);
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("students forms");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot s:snapshot.getChildren())
                {
                   formsName.add(s.getKey());
                   formsLinks.add(s.getValue(String.class));
                }
                /*String[] listArray= new String[0];
                if (list != null) {
                    listArray = list.split("@");
                }*/

                 ArrayAdapter<String> adapt=new ArrayAdapter<>(getContext(),R.layout.student_form_list_design,R.id.form,formsName);
                listview.setAdapter(adapt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp","sorry;(");
            }
        });
    }

}