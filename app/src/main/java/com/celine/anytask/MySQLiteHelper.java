package com.celine.anytask;

/**
 * Created by peiwe on 22/07/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME= "data.db";
    private static final String TABLE_NAME = "data";
    private static final String COLUMN__ID = "id";
    private static final String COLUMN__USERNAME = "username";
    private static final String COLUMN__EMAIL = "email";
    private static final String COLUMN__PASSWORD = "password";
    SQLiteDatabase db;
    private static final String TABLE_CREATE =
            "create table data (id integer primary key not null,"
                    + "username text not null, email text not null, password text not null);";

    public MySQLiteHelper (Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    public void insertData (Data d){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "Select * from data";
        Cursor cursor = db.rawQuery(query, null); //count //pass count as ID
        int count = cursor.getCount();

        values.put(COLUMN__ID,count);
        values.put(COLUMN__USERNAME, d.getUsername());
        values.put(COLUMN__EMAIL, d.getEmail());
        values.put(COLUMN__PASSWORD, d.getPassword());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String searchPass(String u_name){
        db = this.getReadableDatabase();
        String query = "select username, password from "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a, b; //a=username. b=password
        b = "Account not available";
        if(cursor.moveToFirst()){
            do{
                a = cursor.getString(0);

                if(a.equals(u_name)){
                    b = cursor.getString(1);
                    break;
                }

            }
            while(cursor.moveToNext());
        }
        return b;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_V, int new_V ){
        String query = "DROP TABLE IF EXIXTS" + TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }


}
