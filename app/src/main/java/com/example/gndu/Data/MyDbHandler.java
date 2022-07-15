package com.example.gndu.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gndu.Paramaters.params;
import com.example.gndu.userData;

public class MyDbHandler extends SQLiteOpenHelper {
    public MyDbHandler(Context context)
    {
        super(context, params.DB_NAME,null,params.DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create="CREATE TABLE "+params.TABLE_NAME+"("+params.KEY_ID+" INTEGER PRIMARY KEY,"+params.KEY_NAME+
                " TEXT, "+params.KEY_EMAIL+" TEXT,"+params.KEY_IMAGEURL+" TEXT ,"+params.KEY_PASSWORD+" TEXT ,"+params.KEY_PHONE+" TEXT,"+params.KEY_SECTION+" TEXT,"
                +params.KEY_SEMESTER+" TEXT,"+params.KEY_USERID+" TEXT )";
        Log.d("myapp",create);
        sqLiteDatabase.execSQL(create);
    }
    public void addUser(userData userdata)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(params.KEY_EMAIL,userdata.getEmail());
        values.put(params.KEY_PASSWORD,userdata.getPassword());
        values.put(params.KEY_IMAGEURL,userdata.getImageUrl());
        values.put(params.KEY_PHONE,userdata.getPhoneNumber());
        values.put(params.KEY_SECTION,userdata.getSection());
        values.put(params.KEY_SEMESTER,userdata.getSemester());
        values.put(params.KEY_USERID,userdata.getUserId());
        values.put(params.KEY_NAME,userdata.getUserName());
        db.insert(params.TABLE_NAME,null,values);
        Log.d("myapp","successfully inserted data");
        db.close();
    }
    public userData getUser()
    {
        
        SQLiteDatabase db=this.getReadableDatabase();
        userData ur = null;
        //query to read data
        String select ="SELECT * FROM "+params.TABLE_NAME;
        Cursor sr=db.rawQuery(select,null);

        if(sr.moveToFirst())
        {
            ur=new userData();
            ur.setEmail(sr.getString(2));
            ur.setPassword(sr.getString(4));
            ur.setImageUrl(sr.getString(3));
            ur.setPhoneNumber(sr.getString(5));
            ur.setSection(sr.getString(6));
            ur.setSemester(sr.getString(7));
            ur.setUserId(sr.getString(8));
            ur.setUserName(sr.getString(1));
        }
        return ur;
    }
    public int updateUser(userData userdata)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(params.KEY_EMAIL,userdata.getEmail());
        values.put(params.KEY_PASSWORD,userdata.getPassword());
        values.put(params.KEY_IMAGEURL,userdata.getImageUrl());
        values.put(params.KEY_PHONE,userdata.getPhoneNumber());
        values.put(params.KEY_SECTION,userdata.getSection());
        values.put(params.KEY_SEMESTER,userdata.getSemester());
        values.put(params.KEY_USERID,userdata.getUserId());
        values.put(params.KEY_NAME,userdata.getUserName());
        return db.update(params.TABLE_NAME,values,params.KEY_USERID+"=?",new String[]{String.valueOf(userdata.getUserId())});
    }
    public void deleteUser(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(params.TABLE_NAME,params.KEY_USERID+"=?",new String[]{id});
        db.close();
    }

}
