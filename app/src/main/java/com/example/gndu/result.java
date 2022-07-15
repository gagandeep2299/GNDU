package com.example.gndu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class result extends Fragment {

    private ResultViewModel mViewModel;
    public static String semester;
    public static String id;
    public static String Result_name;
    public static String ImageUrl;
    TextView CGPA,SGPA,DATE,SEMESTER;
    public static int Semester;
    ListView result;
    UsersAdapter adapt;
    ImageView ivStory;

    public static result newInstance() {
        return new result();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.result_fragment, container, false);
        TextView result_name=view.findViewById(R.id.result_name);
        result_name.setText("Hello "+Result_name.split(" ")[0]);
        Semester=Integer.parseInt(Character.toString(semester.charAt(0)));
        if(ImageUrl!="null") {
            ivStory = view.findViewById(R.id.ivStory);
            Picasso.get().load(ImageUrl).into(ivStory);
        }
        if(Semester>1)Semester--;
        fetchResultData(id,view, Semester +"th");
        Button next=view.findViewById(R.id.button2);
        Button Previous=view.findViewById(R.id.button);
        SEMESTER=view.findViewById(R.id.Semester);
        SEMESTER.setText("Semester "+Semester);
        CGPA=view.findViewById(R.id.Cgpa);
        SGPA=view.findViewById(R.id.Sgpa);
        DATE=view.findViewById(R.id.Date);
        result=view.findViewById(R.id.result_list);
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Semester -1 >= 1) {
                    Semester--;
                    Log.d("myapp",Semester+"you got");
                    SEMESTER.setText("Semester "+Semester);
                    fetchResultData(id, view,Semester+"th");
                }

            }
            });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myapp",Integer.parseInt(Character.toString(semester.charAt(0)))+" ");
                if(Semester+1<Integer.parseInt(Character.toString(semester.charAt(0))))
                {
                    Semester++;
                    SEMESTER.setText("Semester "+Semester);
                    fetchResultData(id,view,Integer.toString(Semester));
                Log.d("myapp","data changed");
                }

            }
        });
        return view;
    }

    public  void fetchResultData(String ID, View view, String sem)
    {
        Log.d("myapp",sem);

        final String[] gpa = {"",""};
       // .concat(Character.toString(sem.charAt(0)))
        ArrayList<marks> data=new ArrayList<>();
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        TextView grades_color=view.findViewById(R.id.grades);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("RESULTS").child(ID).child("SEM"+sem.charAt(0));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot snapShort :snapshot.getChildren()) {
                    String subjectName=snapShort.getKey();
                    if(subjectName.equals("CGPA")||subjectName.equals("CPGA"))
                        CGPA.setText("CGPA: "+snapShort.getValue(String.class));
                    else if(subjectName.equals("SGPA"))
                        SGPA.setText("SGPA: "+snapShort.getValue(String.class));
                    else if(subjectName.equals("Result Date:"))
                        DATE.setText("Result Date: "+snapShort.getValue(String.class));
                    else
                    {
                        String gradesPointsCredits = snapShort.getValue(String.class);
                        marks result = new marks(subjectName, Character.toString(gradesPointsCredits.charAt(0)), Character.toString(gradesPointsCredits.charAt(1)), gradesPointsCredits.substring(2));
                        data.add(result);
                    }

                }
                adapt=new UsersAdapter(getContext(),data);
                if(result.getAdapter()!=null)
                {
                    result.setAdapter(null);
                }
                result.setAdapter(adapt);
                adapt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp","sorry;(");
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        // TODO: Use the ViewModel
    }

}
class UsersAdapter extends ArrayAdapter<marks> {
    public UsersAdapter(Context context, ArrayList<marks> mark) {
        super(context, 0, mark);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        marks user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.result_item_list_design, parent, false);
        }
        // Lookup view for data population
        TextView subjectName = (TextView) convertView.findViewById(R.id.subject_name);
        TextView credits = (TextView) convertView.findViewById(R.id.credits);
        TextView grades = (TextView) convertView.findViewById(R.id.grades);
        TextView points = (TextView) convertView.findViewById(R.id.points);
        if(user.grade.equals("F"))
            grades.setBackgroundColor(Color.parseColor("#AA423B"));
        else if(user.grade.equals("P"))
            grades.setBackgroundColor(Color.parseColor("#9B9F53"));
        else
            grades.setBackgroundColor(Color.parseColor("#4B8E4D"));
        // Populate the data into the template view using the data object
        subjectName.setText(user.subjectCode);
        credits.setText(user.credits);
        grades.setText(user.grade);
        points.setText(user.points);
        // Return the completed view to render on screen
        return convertView;
    }
}
class marks {
    public String subjectCode;
    public String credits;
    public String grade;
    public String points;

    public marks(String subjectcode,String credits ,String grade,String points) {
        this.subjectCode = subjectcode;
        this.credits=credits;
        this.grade = grade;
        this.points=points;
    }
}
