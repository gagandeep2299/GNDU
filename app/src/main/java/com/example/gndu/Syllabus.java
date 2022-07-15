package com.example.gndu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Syllabus extends Fragment {

    private SyllabusViewModel mViewModel;
    ListView syllabus;
    ArrayAdapter<String> adapt;
    public static String sem;
    public static ArrayList<ListView> listview=new ArrayList<>();

    public static Syllabus newInstance() {
        return new Syllabus();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.syllabus_fragment, container, false);
        fetchSyllabusData(view,sem);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SyllabusViewModel.class);
        // TODO: Use the ViewModel
    }
    public  void fetchSyllabusData(View view, String sem)
    {
        Log.d("myapp",sem);
        ArrayList<syllabusdata> data=new ArrayList<>();
        ArrayList<String> data1=new ArrayList<>();
        syllabus=view.findViewById(R.id.Subject_list);
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("syllabus").child("sem"+sem.charAt(0));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot snapShort :snapshot.getChildren()) {
                    String subjectName=snapShort.getKey();
                    data1.add(subjectName);
                    Log.d("myapp",subjectName);
                        syllabusdata result = new syllabusdata(subjectName, snapShort.child("Section A").getValue(String.class),snapShort.child("Section B").getValue(String.class),snapShort.child("Section C").getValue(String.class),snapShort.child("Section D").getValue(String.class));
                        data.add(result);
                }
               // data1.remove(null);
                SyllabusAdapter adapt=new SyllabusAdapter(getContext(),data);
                syllabus.setAdapter(adapt);

                Log.d("myapp", String.valueOf(data1.size()));
                /*adapt=new ArrayAdapter<>(getContext(),R.layout.syllabus_list_design,R.id.Subject_name,data1);

                syllabus.setAdapter(adapt);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp","sorry;(");
            }
        });


    }

}


class syllabusdata {
    public String subjectName;
    public String SectionA_syllabus;
    public String SectionB_syllabus;
    public String SectionC_syllabus;
    public String SectionD_syllabus;

    public syllabusdata(String subjectName,String SectionA ,String SectionB,String SectionC,String SectionD) {
        this.subjectName = subjectName;
        this.SectionA_syllabus=SectionA;
        this.SectionB_syllabus=SectionB;
        this.SectionC_syllabus=SectionC;
        this.SectionD_syllabus=SectionD;

    }
}


class SyllabusAdapter extends ArrayAdapter<syllabusdata> {

    @SuppressLint("StaticFieldLeak")
    public SyllabusAdapter(Context context, ArrayList<syllabusdata> syllabus) {
        super(context, 0, syllabus);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        syllabusdata usersyllabus = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.syllabus_list_design, parent, false);
        }
        // Lookup view for data population
        TextView subjectName =  convertView.findViewById(R.id.Subject_name);
        subjectName.setText(usersyllabus.subjectName);
        ListView sectionPart=convertView.findViewById(R.id.subject_data);
        Syllabus.listview.add(sectionPart);
        section secA=new section("Section A",usersyllabus.SectionA_syllabus);
        section secB=new section("Section B",usersyllabus.SectionB_syllabus);
        section secC=new section("Section C",usersyllabus.SectionC_syllabus);
        section secD=new section("Section D",usersyllabus.SectionD_syllabus);
        ArrayList<section> sectionArrayList=new ArrayList<>();

        sectionArrayList.add(secA); sectionArrayList.add(secB);  sectionArrayList.add(secC); sectionArrayList.add(secD);
        SectionAdapter sectionAdapter=new SectionAdapter(getContext(),sectionArrayList);
        sectionPart.setAdapter(sectionAdapter);
        return convertView;
    }
}


class SectionAdapter extends ArrayAdapter<section> {
    public SectionAdapter(Context context, ArrayList<section> section) {
        super(context, 0, section);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        section usersection = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_data_list, parent, false);
        }
        // Lookup view for data population
        //TextView subjectName = (TextView) convertView.findViewById(R.id.Subject_name);
        TextView sectionHead = (TextView) convertView.findViewById(R.id.section);
        sectionHead.setText(usersection.sectionType);
        TextView sectionData= (TextView) convertView.findViewById(R.id.Topics);
        sectionData.setText(usersection.sectionData);
        /*if(usersection.sectionData.equals("null"))
        {
            sectionHead.setVisibility(View.INVISIBLE);
            sectionData.setVisibility(View.INVISIBLE);
        }
        else{
            sectionHead.setVisibility(View.VISIBLE);
            sectionData.setVisibility(View.VISIBLE);
        }*/
        return convertView;
    }

}
class section{
    public String sectionType;
    public String sectionData;
    public section(String sectionType,String sectionData)
    {
        this.sectionData=sectionData;
        this.sectionType=sectionType;
    }
}