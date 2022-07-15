package com.example.gndu;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recuiter extends AppCompatActivity {

    ArrayList<String> images=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    ArrayList<placementView> placementV=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recuiter);
        getListOfImages();
    }
    public void getListOfImages()
    {
        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("placementData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot s:snapshot.getChildren())
                {
                    placementView p=new placementView(s.getValue(String.class),s.getKey());
                    placementV.add(p);
                    name.add(s.getKey());
                    images.add(s.getValue(String.class));
                }
                CourseGVAdapter adapt=new CourseGVAdapter(Recuiter.this,placementV);
                GridView gridView=findViewById(R.id.gridView);
                gridView.setAdapter(adapt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp","sorry;(");
            }
        });
    }
}
/*class CourseGVAdapter extends ArrayAdapter<placementView> {
    public CourseGVAdapter(@NonNull Context context, ArrayList<placementView> placementViewArrayList) {
        super(context, 0, placementViewArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.placement_grid_design, parent, false);
        }
        placementView courseModel = getItem(position);
        TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
        ImageView courseIV = listitemView.findViewById(R.id.idIVcourse);
        courseTV.setText(courseModel.Text);
        Picasso.get().load(courseModel.image).into(courseIV);
        return listitemView;
    }
}
class  placementView
{
    String image;
    String Text;
    public placementView(String Image,String text)
    {
        this.image=Image;
        this.Text=text;
    }
}*/