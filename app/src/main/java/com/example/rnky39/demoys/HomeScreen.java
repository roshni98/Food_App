package com.example.rnky39.demoys;

import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;

public class HomeScreen extends AppCompatActivity {
    private Button camera;
    private String username;
    private TextView name;
    private TextView cweight;
    private TextView gweight;
    private ProgressBar spinner;
    private ArrayList<String> user_data;
    private ArrayList<String> user_info;
    private TextView calories;
    private ListView lv;
    private int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (TextView)findViewById(R.id.nameText);
        this.camera = (Button) findViewById(R.id.camera);
        this.lv = (ListView) findViewById(R.id.listView);
        this.cweight = (TextView) findViewById(R.id.currentWeight);
        this.gweight = (TextView) findViewById(R.id.goalWeight);
        this.calories = (TextView) findViewById(R.id.caloriesGoal);
        Bundle bundle = getIntent().getExtras();
        this.username = bundle.getString("username");
        this.user_data = bundle.getStringArrayList("user_data");
        this.user_info = bundle.getStringArrayList("user_info");
        this.sum = 0;
        //name.setText(this.username);
        if(this.user_data!=null) {
            Log.v("com.example.rnky39.demo", "" + this.user_data.size());
        }
        setSupportActionBar(toolbar);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        if(user_data!=null && user_data.size()>0) {
            formatAndShowArray();
        }
        if(user_info!=null && user_info.size()>0){
            formatUserInfo();
        }
//        BackgroundTask backgroundTask=new BackgroundTask(this,spinner);
//        backgroundTask.execute("get_data",this.username);
    }
    public void formatAndShowArray(){
        ArrayList<String> arr = new ArrayList<String>();
        for(String s: this.user_data) {
            HashMap<String, String> map = new HashMap<String, String>();
            try {
                JSONObject jObject = new JSONObject(s);
                Iterator<?> keys = jObject.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String value = jObject.getString(key);
                    map.put(key, value);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String t  = "";
                try {
                    Date date = sdf.parse(map.get("timestamp"));
                    t = date.toLocaleString();
                    Log.v("com.example.rnky39.demo",date.toString());
                    Calendar today = Calendar.getInstance();
                    today.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date td = today.getTime();


                    if(date.getDay() == td.getDay()){
                        this.sum+=Integer.parseInt(map.get("calories"));
                    }

                }catch (ParseException e){
                    e.printStackTrace();
                }
                arr.add(t+"\t: "+map.get("food")+" , "+map.get("calories")+" cal");

            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arr);
        lv.setAdapter(arrayAdapter);
    }

    public void formatUserInfo(){

        for(String s: this.user_info) {
            HashMap<String, String> map = new HashMap<String, String>();
            try {
                JSONObject jObject = new JSONObject(s);
                Iterator<?> keys = jObject.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String value = jObject.getString(key);
                    map.put(key, value);
                }
                this.cweight.setText("Current Weight: "+map.get("current_weight"));
                this.gweight.setText("Goal Weight :"+map.get("goal_weight"));
                this.name.setText(map.get("name"));
                int diff = 2000 - this.sum;
                this.calories.setText("Calories Left For Today:" +diff);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void cameraClass(View view){
        Intent intent=new Intent(this,ClassifierActivity.class);
        intent.putExtra("username",this.username);
        System.out.println("username:"+this.username);
        startActivity(intent);

    }

}
