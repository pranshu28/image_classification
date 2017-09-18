package com.github.nf1213.camera;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by avinaash on 1/4/17.
 */

public class MySqlHelper extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "DATAWALA";
    public static final String COLUMN_KINGDOM = "kingdom";
    public static final String COLUMN_COMMON_NAME = "common_name";
    public static final String COLUMN_ORDER = "order";
    public static final String COLUMN_CLASS_NAME = "class_name";
    public static final String COLUMN_PHYLUM = "phylum";
    public static final String COLUMN_SCI_NAME = "sci_name";
    public static final String COLUMN_SPECIES_NAME= "species_name";
    public static final String COLUMN_WIKI_STR = "wiki_str";
    public static final String COLUMN_FAMILY = "family";
    public static final String COLUMN_GENUS = "genus";
    public static final String COLUMN_LOC = "loc";




    private static final String DATABASE_NAME = "Butterfly.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_KINGDOM
            + " text not null," + COLUMN_COMMON_NAME +" text not null,"+COLUMN_ORDER+" text not null,"+COLUMN_CLASS_NAME+" text not null,"+
            COLUMN_PHYLUM+" text not null,"+COLUMN_SCI_NAME+" integer primary key autoincrement, "+COLUMN_SPECIES_NAME+" text not null,"+COLUMN_WIKI_STR+" text not null,"
    +COLUMN_FAMILY+" text not null,"+COLUMN_GENUS+" text not null,"+COLUMN_LOC+" text not null"+")";

    public MySqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MySQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }*/

    }
}
