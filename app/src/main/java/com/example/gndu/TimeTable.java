package com.example.gndu;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class TimeTable extends AppCompatActivity {

    TimetableView timetable;
    public static String Sem;
    String[] day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_time_table);
        timetable=findViewById(R.id.timetable);
        addNotification();
        FetchData(Sem);
    }
    public void finish(View view)
    {
        finish();
    }
    public void FetchData(String sem)
    {

        FirebaseDatabase fb=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("time table").child("sem"+sem.charAt(0));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                    Map<String,Object> map= (Map<String,Object>) snapshot.getValue();
                    day= new String[]{"Mon", "Tue", "Wed", "Thu", "Fri"};
                    for(int i=0;i<5;i++) {
                        Log.d("myapp",day[i]);
                        String dividedInDays = map.get(day[i]).toString();
                        Log.d("myapp",dividedInDays);
                        String[] dividedInhours = dividedInDays.substring(1, dividedInDays.length() - 1).split(",");
                        for(int j=0;j<dividedInhours.length;j++)
                        {
                            String time=dividedInhours[j].split("=")[0].replace(" ","");
                            String subject=dividedInhours[j].split("=")[1].split("@")[0];
                            String teacher=dividedInhours[j].split("=")[1].split("@")[1];
                            ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                            Schedule schedule = new Schedule();
                            schedule.setClassTitle(subject); // sets subject
                            schedule.setProfessorName(teacher); // sets professor
                            schedule.setDay(i);
                            schedule.setStartTime(new Time(Integer.parseInt(time.substring(0,2)),Integer.parseInt(time.substring(3,5)))); // sets the beginning of class time (hour,minute)
                            schedule.setEndTime(new Time(Integer.parseInt(time.substring(6,8)),Integer.parseInt(time.substring(9,11)))); // sets the end of class time (hour,minute)
                            schedules.add(schedule);
                            timetable.add(schedules);
                            Log.d("myapp",time.substring(0,2)+" "+time.substring(3,5)+" "+subject+" "+teacher+" "+time);
                        }
                        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                                // ...
                                Log.d("myapp",schedules.get(0).getProfessorName());
                                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TimeTable.this);
                                bottomSheetDialog.setContentView(R.layout.updates);

                                TextView head=bottomSheetDialog.findViewById(R.id.heading);
                                assert head != null;
                                head.setText("Details");
                                ListView updateListview=bottomSheetDialog.findViewById(R.id.updateListView);
                                ArrayList<String> updateList=new ArrayList<>();
                                updateList.add(schedules.get(0).getStartTime().getHour()+":"+schedules.get(0).getStartTime().getMinute()+"-"+schedules.get(0).getEndTime().getHour()+":"+schedules.get(0).getEndTime().getMinute()+"  "+day[schedules.get(0).getDay()]);
                                updateList.add("Subject : "+schedules.get(0).getClassTitle());
                                updateList.add("Teacher : "+schedules.get(0).getProfessorName());
                                ArrayAdapter<String> adapt=new ArrayAdapter<>(TimeTable.this,R.layout.update_list_design,R.id.updatesText,updateList);
                                updateListview.setAdapter(adapt);
                                bottomSheetDialog.show();
                            }
                        });
                    }
               /* for(DataSnapshot s:snapshot.getChildren())
                {   int count=0;
                   /* for(DataSnapshot s1:snapshot.getChildren()) {
                        String time=s1.getKey();
                        ArrayList<Schedule> timeList = new ArrayList<>();
                        Schedule schedule = new Schedule();
                        /*schedule.setClassTitle(s1.getValue(String.class).split("@")[0]); // sets subject // sets place
                        schedule.setProfessorName(s1.getValue(String.class).split("@")[1]); // sets professor
                        schedule.setDay(count);
                        schedule.setStartTime(new Time(Integer.parseInt(time.substring(1,3)), Integer.parseInt(time.substring(4,6)))); // sets the beginning of class time (hour,minute)
                        schedule.setEndTime(new Time(Integer.parseInt(time.substring(7,9)), Integer.parseInt(time.substring(10,12)))); // sets the end of class time (hour,minute)
                        timeList.add(schedule);
                        timetable.add(timeList);

                        count++;
                    }
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //  data.add("null");
                Log.d("myapp","sorry;(");
            }
        });
    }
    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.messageicon) //set icon for notification
                        .setContentTitle("Notifications Example") //set title of notification
                        .setContentText("This is a notification message")//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


        Intent notificationIntent = new Intent(this, notification_view.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "This is a notification message");

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}