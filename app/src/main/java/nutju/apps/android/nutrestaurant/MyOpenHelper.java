package nutju.apps.android.nutrestaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nutju on 9/24/2015 AD.
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    // Explicit
    private static final String DB_NAME = "Restaurant.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_USER = "create table userTABLE (_id integer primary key, " +
            "user text, " +
            "password text, " +
            "name text);";
    private static final String CREATE_FOOD = "create table foodTABLE (_id integer primary key, " +
            "food text, " +
            "source text, " +
            "price text);";

    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }   // Constructor

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_FOOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}   // Main Class
