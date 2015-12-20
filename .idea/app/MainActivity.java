package com.example.seryun.termproject;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //DB에서 쓰이는 변수들
    SQLiteDatabase db;
    String tableName = "Memo";
    String dbName = "list";
    String KEY_LONGITUDE = "longitude";
    String KEY_LATITUDE = "latitude";
    String KEY_EVENT = "Event";
    String KEY_EMOTION = "Emotion";
    String KEY_CATEGORY = "CATEGORY";

    //spinner 변수
    Spinner mSpCategoey;
    Spinner mSpEmotion;

    //버튼 변수
    Button mBtSave;
    Button mBtList;

    //맵 변수
    private GoogleMap map;
    static final LatLng SEOUL = new LatLng( 20, 30);

    String strLongitude;
    String strLatitude;

    EditText mEtEvent;
    String strEvent;

    final static int RESULT = 1000;

    ArrayList<String> nameList;



    int dbMode = Context.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create databases
        db = openOrCreateDatabase(dbName,dbMode,null);
        // create table;
        createTable();

        mSpCategoey = (Spinner) findViewById(R.id.Category);
        mSpCategoey.setPrompt("cotegory를 선택하세요.");

        mSpEmotion = (Spinner) findViewById(R.id.Emotion);

        //Map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.Map);
        map = mapFragment.getMap();

        //현재 위치로 가는 버튼 표시
        map.setMyLocationEnabled(true);
        Log.d("setmyloc ", "실행");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 30));



        mSpCategoey = (Spinner) findViewById(R.id.Category);
        mBtSave = (Button) findViewById(R.id.bt_save);
        mBtList = (Button) findViewById(R.id.bt_list);
        mEtEvent = (EditText) findViewById(R.id.et_event);


        //save버튼을 누르면 db에 저장된다.
        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEvent = mEtEvent.getText().toString();
                insertData();
                Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();

            }
        });
    }



    // Table 생성
    public void createTable() {
        try {
            String sql = "create table if not exists " + tableName + "(id integer primary key autoincrement, " + KEY_LONGITUDE + " text, "  + KEY_LATITUDE + " text, " + KEY_EVENT + " text, " + KEY_EMOTION + " text, " + KEY_CATEGORY + " text)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite","error: "+ e);
        }
    }

    // Data 추가
    public void insertData() {

        String sql = "insert into " + tableName + " values(NULL, '" + strLongitude + "',  '" + strLatitude + "', '" + strEvent +"', '" + mSpEmotion.getSelectedItem() + "', '" + mSpCategoey.getSelectedItem() + "');";
        db.execSQL(sql);
    }

    //마킹
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {

            Double double_longitude = Double.parseDouble(data.getStringExtra("long"));
            Double double_latitude = Double.parseDouble(data.getStringExtra("lat"));

            map.clear();
            LatLng latLng = new LatLng(double_latitude, double_longitude);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .snippet("Lat:" + double_latitude + "Lng:" + double_longitude)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("현재위치"));

        }

    }

}