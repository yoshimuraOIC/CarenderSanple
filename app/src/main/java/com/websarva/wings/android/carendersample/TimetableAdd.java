package com.websarva.wings.android.carendersample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;





public class TimetableAdd extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private final String[] spinnerItems = {
            "背景色を選択してください。",
            "赤色",
            "紫色",
            "青色",
            "緑色",
            "黄色",
            "橙色"
    };
    //赤(FFCDD2)
    //紫(E1BEE7)
    //青(BBDEFB)
    //緑(69F0AE)
    //黄(FFF9C4)
    //橙(FFCC80)
    private final String[] spinnerClassItems = {
            "教室を選択してください。",
            "2-C", "3-A", "3-B", "3-C", "4-A", "4-B", "4-C", "4-D", "5-A", "5-B", "5-C", "5-D1", "5-D2",
            "6-A", "6-B", "6-C", "6-D", "7-A", "7-B", "7-C", "7-D", "8-A", "8-B", "8-C", "8-D", "9-D1", "9-D2"
    };
    private final String[] spinnerClassregist1 = {
            "を登録してください",
            "月-1限", "月-2限", "月-3限", "月-4限", "月-5限", "火-1限", "火-2限", "火-3限", "火-4限", "火-5限", "水-1限", "水-2限", "水-3限",
            "水-4限", "水-5限", "木-1限", "木-2限", "木-3限", "木-4限", "木-5限", "金-1限", "金-2限", "金-3限", "金-4限", "金-5限"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetableadd);

        Spinner spinner = findViewById(R.id.ba_co);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerItems
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //この↓からタイトルのスピナー（spinnerの変数は使っちゃってるのでべつのでよろしく)

        Spinner spinnerClass = findViewById(R.id.spinnerClass);
        ArrayAdapter<String> adapterClass = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerClassItems
                );
                Spinner regist1 = findViewById(R.id.regist1);
        ArrayAdapter<String> adapterRegist = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerClassregist1
        );

        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerClass.setAdapter(adapterClass);

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public void onMain(View view) {
        Intent intent = new Intent(TimetableAdd.this, MainActivity.class);
        startActivity(intent);
    }

    public void onTimetable(View view) {
        Intent intent = new Intent(TimetableAdd.this, Timetable.class);
        startActivity(intent);
    }

    public void onSearch(View view) {
        Intent intent = new Intent(TimetableAdd.this, Search.class);
        startActivity(intent);
    }

    public void onSchedule(View view) {
        Intent intent = new Intent(TimetableAdd.this, Schedule_Add.class);
        startActivity(intent);
    }

    public void onAdd(View view) {
        // Intent intent = new Intent(Schedule_Add.this, AddSchedule.class);
        // startActivity(intent);
        // AddScheduleメソッドを呼び出す
        AddTimetable();
    }




    public void AddTimetable() {

        //データ取得
        TextView title1 = findViewById(R.id.title1);
        TextView Memo = findViewById(R.id.memo);
        Spinner Regist = findViewById(R.id.regist1);
        Spinner ba_co = findViewById(R.id.ba_co);
        Spinner spinnerClass = findViewById(R.id.spinnerClass);


        // Create a new user with a first and last name
        Map<String, Object> Events = new HashMap<>();
        Events.put("Regist",Regist.getSelectedItem());
        Events.put("title1", title1.getText().toString());
        Events.put("Memo", Memo.getText().toString());
        Events.put("ba_co", ba_co.getSelectedItem().toString());
        Events.put("spinnerClass", spinnerClass.getSelectedItem().toString());


        db.collection("Timetable").add(Events)
                //登録成功時の挙動
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                //登録失敗時の挙動
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding", e);
                    }
                });


    }

    public void UpdateTimetable(){
        //データ取得
        //日付を取得する
        // TextView OldTitle = findViewById(R.id.title1);
        String OldTitle  = "1";

        TextView title1 = findViewById(R.id.title1);
        TextView Memo = findViewById(R.id.memo);
        Spinner ba_co = findViewById(R.id.ba_co);
        Spinner spinnerClass = findViewById(R.id.spinnerClass);



        db.collection("Timetable").document(OldTitle)
                .update(
                        "title1", title1.getText().toString(),
                        "Memo", Memo.getText().toString(),
                        "ba_co", ba_co.getSelectedItem().toString(),
                        "spinnerClass", spinnerClass.getSelectedItem().toString()
                );


    }


    //プレビュー
    public void DisplayTimetable(String Setregist) {



        TextView title1 = findViewById(R.id.title1);
        TextView Memo = findViewById(R.id.memo);
        Spinner ba_co = findViewById(R.id.ba_co);
        Spinner spinnerClass = findViewById(R.id.spinnerClass);
        // Spinner regist = findViewById(R.id.regist1);


        db.collection("Timetable").whereEqualTo("regist1", Setregist)
                .get()

                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //プレビューさせる
                                title1.setText(document.get("title1").toString());
                                Memo.setText(document.get("Memo").toString());
                                setSelection(ba_co,document.get("ba_co").toString());
                                setSelection(spinnerClass,document.get("spinnerClass").toString());
           //                     setSelection(regist,document.get("regist1").toString());
                            }
                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public static void setSelection(Spinner spinner, String item) {
        SpinnerAdapter adapter = spinner.getAdapter();
        int index = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(item)) {
                index = i; break;
            }
        }
        spinner.setSelection(index);
    }

    public void DeleteTimetable(){

        //日付を取得する
        // TextView OldTitle = findViewById(R.id.title1);
        String OldTitle  = "1";
        db.collection("Timetable").document(OldTitle)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

}