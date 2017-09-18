package com.github.nf1213.camera;
import android.app.Activity;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by avinaash on 2/4/17.
 */

public class ReadJson extends Activity{
    Butterfly butterfly;
    DatawaladataSource database;
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("final2.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    public ReadJson () throws JSONException {
                JSONObject jsonObj = new JSONObject(loadJSONFromAsset());

                // Getting data JSON Array nodes
                JSONArray data  = jsonObj.getJSONArray("butterfly");

                // looping through All nodes
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);


                    butterfly.setKingdom(c.getString("kingdom"));
                    butterfly.setCommon_name(c.getString("cmname"));
                    butterfly.setOrder(c.getString("order"));
                    butterfly.setClass_name(c.getString("class"));
                    butterfly.setPhylum(c.getString("phylum"));
                    butterfly.setSci_name(c.getString("stname"));
                    butterfly.setSpecies_name(c.getString("species"));
                    butterfly.setWiki_str(c.getString("Wikipedia"));
                    butterfly.setFamily(c.getString("family"));
                    butterfly.setGenus(c.getString("genus"));

                    database.createButterfly(butterfly);

                    //use >  int id = c.getInt("duration"); if you want get an int





                    // do what do you want on your interface
                }
        }
    }
