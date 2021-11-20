package com.android.example.sportyguru.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UniversitydbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "offline";
    public static final String UNIVERSITY_TABLE_NAME = "university_info";
    public static final String UNIVERSITY_NAME = "university_name";
    public static final String UNIVERSITY_COLUMN_ID = "_id";
    public static final String UNIVERSITY_STATE_PROVINCE = "university_state_province";
    public static final String UNIVERSITY_COUNTRY = "university_country";
    public static final String UNIVERSITY_WEB_PAGE= "university_web_page";
    public static final String UNIVERSITY_DOMAIN_NAME= "university_domain_name";
    public static final String UNIVERSITY_CODE= "university_code";
    public static final String UNIVERSITY_ADDRESS="university_address";

    public UniversitydbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + UNIVERSITY_TABLE_NAME + " (" +
                UNIVERSITY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UNIVERSITY_NAME + " TEXT, " +
                UNIVERSITY_STATE_PROVINCE + " TEXT, " +
                UNIVERSITY_COUNTRY + " TEXT, " +
                UNIVERSITY_WEB_PAGE + " TEXT, " +
                UNIVERSITY_DOMAIN_NAME + " TEXT, " +
                UNIVERSITY_CODE + " TEXT ," +
                UNIVERSITY_ADDRESS + " EXT "
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UNIVERSITY_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}