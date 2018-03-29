package com.example.shadow.csf11;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int count=0;

    Time time;
    ListView listView;
    TextView txt;
    Button tue, wed, thu, fri, CW;
    ImageView im;

    String[] sTypeOfWeek = {"Числитель", "Знаменатель"};
    String[] sNameDay = {"","","Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье"};

    String jsonChisl = "timetable.json";
    String jsonZnam = "timetablez.json";

    /*
    String jsonChisl = "any_chis.json";
    String jsonZnam = "any_zn.json";
    */

    String debug = "MyLog";
    int typeOfWeek, dayOfWeek, chekDay, chekWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.lv);
        tue = (Button)findViewById(R.id.Tue);
        wed = (Button)findViewById(R.id.Wed);
        thu = (Button)findViewById(R.id.Thu);
        fri = (Button)findViewById(R.id.Fri);
        CW = (Button)findViewById(R.id.CW);
        txt = (TextView)findViewById(R.id.textView3) ;
        im = (ImageView)findViewById(R.id.imageView);

        im.setVisibility(View.INVISIBLE);

        time = new Time(12, 2, 2018);
        typeOfWeek = time.getTypeOfWeek();
        chekWeek = typeOfWeek;
        Log.v(debug,"tow="+typeOfWeek);

        dayOfWeek=time.getDayOfWeek();
        chekDay=dayOfWeek;

        setAll(chekWeek,chekDay);
        Log.v(debug, "dow="+dayOfWeek);

        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);
        CW.setOnClickListener(this);
        txt.setOnClickListener(this);
        //get_JSON(todaynumber);
    }

    public void setAll(int week, int day){
        setText();
        getSubject(week,day);

        if(day==2) {
            voenka();
        }
    }

    public void voenka(){
        im.setImageResource(R.drawable.popov);
        listView.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);
        im.setVisibility(View.VISIBLE);
        CW.setVisibility(View.INVISIBLE);
    }

    public void setAll(int week, int day, int a){
        setText();
        getSubject(typeOfWeek,dayOfWeek);
    }

    public void setText(){
        setTitle(sNameDay[chekDay]);
        txt.setText(sTypeOfWeek[chekWeek]);
    }

    public void setText(int day, int week){
        setTitle(sNameDay[day]);
        txt.setText(sTypeOfWeek[week]);
    }

    public void getSubject(int typOfWeek, int countDay){

        Log.v(debug, "dow_get_sub="+chekDay+" "+countDay);

        String json = null;
        String name = "";
        String place = "";
        String time = "";
        String prof="";
        long day;

        try {
            InputStream is = null;
            if(chekWeek==0) {
                 is = getAssets().open(jsonChisl);
           }
            if(chekWeek==1) {
                 is = getAssets().open(jsonZnam);
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            //  List<String> listname = new ArrayList<String>();
            ArrayList<HashMap<String, String>> subjectList;
            subjectList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                name = object.getString("name");
                place = object.getString("place");
                time = object.getString("startTime");
                day = object.getLong("day");


                if (day == chekDay) {
                    HashMap<String, String> subject = new HashMap<>();
                    subject.put("name", name);
                    subject.put("place", place);
                    subject.put("startTime", time);
                    subjectList.add(subject);
                }
            }

            ListAdapter adapter = new SimpleAdapter(MainActivity.this, subjectList, R.layout.listview,
                    new String[]{"name", "place", "startTime"}, new int[]{R.id.textView2, R.id.textView, R.id.textView4});
            //ArrayAdapter adapter1 = new ArrayAdapter<String>(this,R.layout.listview, R.id.textView2 ,listplace);
            ListView listView = (ListView) findViewById(R.id.lv);
            listView.setAdapter(adapter);
        }

        catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    /*
    public String tOfWeek(){
        if (typeOfWeek==0)
            return "Числитель";
        if (typeOfWeek==1)
            return "Знаменатель";
        return "";
    }
    */

    @Override
    public void onClick(View view){
        Resources res = getResources();
        Log.v(debug,"тип недели в обработчике="+typeOfWeek);

        if(view.getId()==R.id.imageView){
            Log.v(debug," Нажата картикна");
            //setAll(time.getDayOfWeek(),time.getDayOfWeek(),1);

        }

        if (view.getId()==R.id.CW){
            if(count==5){
                listView.setVisibility(View.INVISIBLE);
                txt.setVisibility(View.INVISIBLE);
                im.setImageResource(R.drawable.lsls);
                im.setVisibility(View.VISIBLE);
                CW.setVisibility(View.INVISIBLE);

            }
            boolean b=true;
            if ((chekWeek==0) & (b)) {
                chekWeek = 1;
                setAll(chekWeek,chekDay,1);
//                getSubject(1,chekDay);
//                setText(chekDay,1);
                count++;
                b=!b;

            }
            else if ((chekWeek==1) & (b)) {
                chekWeek=0;
                setAll(chekWeek,chekDay);
//                getSubject(0,chekDay);
//                setText(chekDay,0);
                count++;
                b=!b;
            }

        }
        Log.v(debug,"тип недели в обработчике изменили="+typeOfWeek);


        switch (view.getId()){
            case R.id.Tue:
                chekDay=3;
                setAll(chekWeek,3);
//            setTitle(res.getString(R.string.tue));
//            getSubject(typeOfWeek,2);
                count=0;
            break;
            case R.id.Wed:
                chekDay=4;
                setAll(chekWeek,chekDay);
//            setTitle(res.getString(R.string.wen));
//            getSubject(chekWeek,4);
                count=0;
            break;
            case R.id.Thu:
                chekDay=5;
                setAll(chekWeek,chekDay);
//            setTitle(res.getString(R.string.thu));
//            getSubject(chekWeek,5);
                count=0;
            break;
            case  R.id.Fri:
                chekDay=6;
                setAll(chekWeek,chekDay);
//            setTitle(res.getString(R.string.fri));
//            getSubject(chekWeek,6);
                count=0;
            break;
        }
    }
}


