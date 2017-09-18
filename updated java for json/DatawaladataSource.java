package com.github.nf1213.camera;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by avinaash on 2/4/17.
 */

public class DatawaladataSource extends Activity {
    // Database fields
    private SQLiteDatabase database;
    private MySqlHelper dbHelper;
    private String[] allColumns = {
            MySqlHelper.COLUMN_KINGDOM,MySqlHelper.COLUMN_COMMON_NAME,MySqlHelper.COLUMN_ORDER,MySqlHelper.COLUMN_CLASS_NAME
    ,MySqlHelper.COLUMN_PHYLUM,MySqlHelper.COLUMN_SCI_NAME,MySqlHelper.COLUMN_SPECIES_NAME,MySqlHelper.COLUMN_WIKI_STR
    ,MySqlHelper.COLUMN_FAMILY,MySqlHelper.COLUMN_GENUS,MySqlHelper.COLUMN_LOC};

    public DatawaladataSource(Context context) throws JSONException {
        dbHelper = new MySqlHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createButterfly(Butterfly butterfly) {
        ContentValues values = new ContentValues();
        values.put(MySqlHelper.COLUMN_KINGDOM, butterfly.getKingdom());
        values.put(MySqlHelper.COLUMN_COMMON_NAME, butterfly.getCommon_name());
        values.put(MySqlHelper.COLUMN_ORDER, butterfly.getOrder());
        values.put(MySqlHelper.COLUMN_CLASS_NAME, butterfly.getClass_name());
        values.put(MySqlHelper.COLUMN_PHYLUM, butterfly.getPhylum());
        values.put(MySqlHelper.COLUMN_SCI_NAME, butterfly.getSci_name());
        values.put(MySqlHelper.COLUMN_SPECIES_NAME, butterfly.getSpecies_name());
        values.put(MySqlHelper.COLUMN_WIKI_STR, butterfly.getWiki_str());
        values.put(MySqlHelper.COLUMN_FAMILY, butterfly.getFamily());
        values.put(MySqlHelper.COLUMN_GENUS, butterfly.getGenus());
        values.put(MySqlHelper.COLUMN_LOC, butterfly.getLoc());

        Cursor cursor = database.query(MySqlHelper.TABLE_NAME,
                allColumns, MySqlHelper.COLUMN_KINGDOM + "," + MySqlHelper.COLUMN_COMMON_NAME+ "," + MySqlHelper.COLUMN_ORDER+ "," + MySqlHelper.COLUMN_CLASS_NAME
                        + "," + MySqlHelper.COLUMN_PHYLUM+ "," + MySqlHelper.COLUMN_SCI_NAME+ "," + MySqlHelper.COLUMN_SPECIES_NAME+ "," + MySqlHelper.COLUMN_WIKI_STR+ "," +
        MySqlHelper.COLUMN_FAMILY+ "," + MySqlHelper.COLUMN_GENUS+ "," + MySqlHelper.COLUMN_LOC,null,null,null,null);
        cursor.moveToLast();
        Butterfly newComment = cursorToComment(cursor);
        cursor.close();
        return ;
    }

    public List<Butterfly> getAllButterflies() {
        List<Butterfly> comments = new ArrayList<Butterfly>();

        Cursor cursor =  database.query(MySqlHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Butterfly comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Butterfly cursorToComment(Cursor cursor) {
        Butterfly comment = new Butterfly();
        comment.setKingdom(cursor.getString(0));
        comment.setCommon_name(cursor.getString(1));
        comment.setOrder(cursor.getString(2));
        comment.setClass_name(cursor.getString(3));
        comment.setPhylum(cursor.getString(4));
        comment.setSci_name(cursor.getString(5));
        comment.setSpecies_name(cursor.getString(6));
        comment.setWiki_str(cursor.getString(7));
        comment.setFamily(cursor.getString(8));
        comment.setGenus(cursor.getString(9));
        comment.setLoc(cursor.getString(10));
        return comment;

    }

}

