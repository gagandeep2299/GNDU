package com.example.gndu;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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

public class placement extends Fragment {

    ArrayList<String> images=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    ArrayList<placementView> placementV=new ArrayList<>();

    public static placement newInstance() {
        return new placement();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.placement_fragment, container, false);
        getListOfImages(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PlacementViewModel mViewModel = new ViewModelProvider(this).get(PlacementViewModel.class);
        // TODO: Use the ViewModel
    }

    public void getListOfImages(View view)
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
                CourseGVAdapter adapt=new CourseGVAdapter(requireContext(),placementV);
                GridView gridView=view.findViewById(R.id.gridView);
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
class CourseGVAdapter extends ArrayAdapter<placementView> {
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
}