package com.example.gndu.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gndu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class examination extends Fragment {

    private ExaminationViewModel mViewModel;
    public static String semester;
    public static examination newInstance() {
        return new examination();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.examination_fragment, container, false);
        //Fragment frag=new Fragment();
        //String semester=getArguments().getString("sem");
        //String semester= getArguments().getString("sem");

        fetchExaminationData(semester,view);
        return view;
    }

    public String[][] fetchExaminationData(String semester,View view)
    {
        String[][] examinationData=new String[10][2];
        ArrayList<String> examDataObject=new ArrayList<>();
        ArrayList<String> examDataValue=new ArrayList<>();
        int[] drawableId={R.drawable.examlidt_foreground,R.drawable.examlidt_foreground,R.drawable.examlidt_foreground,R.drawable.examlidt_foreground,R.drawable.examlidt_foreground,R.drawable.examlidt_foreground};
        ListView examList=view.findViewById(R.id.examListview1);
        ListView examListDate=view.findViewById(R.id.examListview);
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Examination").child("sem"+semester.charAt(0));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot snapShort :snapshot.getChildren()) {

                    examDataObject.add(snapShort.getKey());
                    examDataValue.add(snapShort.getValue(String.class));
                    examinationData[i][0]=snapShort.getKey();
                    examinationData[i][1]=snapShort.getValue(String.class);
                    Log.d("myapp",snapShort.getKey());
                    i++;
                }
                try {
                    fetchSubjectData(examDataObject, view);
                }
                catch(Exception e)
                {
                    Log.d("myapp",e.getMessage());
                }
                ArrayAdapter<String> adapt=new ArrayAdapter<>(getContext(),R.layout.exam_list_item_design,R.id.textView,examDataObject);
                examList.setAdapter(adapt);
                ArrayAdapter<String> adapt1=new ArrayAdapter<>(getContext(),R.layout.exam_list_item_design,R.id.textView,examDataValue);
                examListDate.setAdapter(adapt1);
                Log.d("myapp",examinationData[0][0]+examDataObject);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp","sorry;(");
            }
        });

        return examinationData;
    }
    public void fetchSubjectData(ArrayList<String> examDataObject,View view)
    {
        HashMap<String,String> dataWithobject=new HashMap<>();
        ListView name =view.findViewById(R.id.subjectName);
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference subjectDataReference=FirebaseDatabase.getInstance().getReference().child("subjects");
        ArrayList<String> SubjectName=new ArrayList<>();
        subjectDataReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sp: snapshot.getChildren())
                {
                    dataWithobject.put(sp.getKey(),sp.getValue(String.class));
                    Log.d("myapp",sp.getValue(String.class));

                }
                examDataObject.forEach((n)->SubjectName.add(dataWithobject.get(n)));
               /*for(ArrayList<String> element:examDataObject)
               {
                   if(dataWithobject.get(examDataObject.get(i))!=null)
                       SubjectName.add(dataWithobject.get(examDataObject.get(i)));
                   Log.d("myapp",Integer.toString(i));
                }*/
                SubjectName.remove(null);
                ArrayAdapter<String> nameAdapt=new ArrayAdapter<>(getContext(),R.layout.exam_list_item_design,R.id.textView,SubjectName);
                name.setAdapter(nameAdapt);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExaminationViewModel.class);
        // TODO: Use the ViewModel
    }

}