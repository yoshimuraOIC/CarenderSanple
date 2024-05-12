package com.websarva.wings.android.carendersample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
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


public class Timetable extends AppCompatActivity {

   // String device_id = DeviceInfoUtil.getDeviceId(this);

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
    }
    @Override
    protected void onStart() {
        super.onStart();
        OnStartDisplayTimetable();
    }

    public void onBackButtonClick(View view){
        Intent intent = new Intent(Timetable.this,MainActivity.class);
        startActivity(intent);
    }

    public void onTimeRegi(View view){
        Intent intent = new Intent(Timetable.this,TimetableAdd.class);
        startActivity(intent);
    }

    public void onSearch(View view){
        Intent intent = new Intent(Timetable.this,Search.class);
        startActivity(intent);
    }

    public void onSchedule(View view){
        Intent intent = new Intent(Timetable.this,Schedule_Add.class);
        startActivity(intent);
    }

    //onTimetable○○ の　○○をSelectedTimetableに入れる

    public void DisplayAllTimetable(String Class ,String FlontBack){

        //日付を取得する
        // TextView OldTitle = findViewById(R.id.title1);
        // String OldTitle  = "1";

        //前期後期かを判別する必要があり
        db.collection("Timetable" + FlontBack).whereEqualTo("クラス", Class)
                .orderBy("曜日を識別するやつ").get()

                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //それぞれ対応するスピンに表示させる。
                            }
                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    //タイトルと色を両方
    public void OnStartDisplayTimetable(){

        //日付を取得する
        // TextView OldTitle = findViewById(R.id.title1);
        String OldTitle  = "1";
        db.collection("Timetable").get()

                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d(TAG,"kokomade1");
                                String comame= document.get("regist1").toString();
                                String backcolor = document.get("ba_co").toString();
                                String title = document.get("title1").toString();
                                 //SelectComa　でコマ捜索
                                  TextView textviews = SelectComa(comame, title);
                                // デバイスを識別するAndroidIDを取得 先頭でやるため要らないかも
                                //これでコマに色を塗る
                                ColoredComa(textviews,backcolor);
                                textviews.setText(title);

         }
                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public TextView SelectComa(String comame, String title){

        TextView ComaArea = findViewById(R.id.btn_mon1);
        // comameと等しいコマを探す。  ここから
        if(comame.equals("月-1限")){
            ComaArea = findViewById(R.id.btn_mon1);
        }
        //ここまでを　コマ数分作る
        if(comame.equals("月-2限")){
            ComaArea = findViewById(R.id.btn_mon2);
        }
        if(comame.equals("月-3限")){
            ComaArea = findViewById(R.id.btn_mon3);
        }
        if(comame.equals("月-4限")){
            ComaArea = findViewById(R.id.btn_mon4);
        }
        if(comame.equals("月-5限")){
            ComaArea = findViewById(R.id.btn_mon5);
        }

        if(comame.equals("火-1限")){
            ComaArea = findViewById(R.id.btn_tue1);
        }
        if(comame.equals("火-2限")){
            ComaArea = findViewById(R.id.btn_tue2);
        }
        if(comame.equals("火-3限")){
            ComaArea = findViewById(R.id.btn_tue3);
        }
        if(comame.equals("火-4限")){
            ComaArea = findViewById(R.id.btn_tue4);
        }
        if(comame.equals("火-5限")){
            ComaArea = findViewById(R.id.btn_tue5);
        }

        if(comame.equals("水-1限")){
            ComaArea = findViewById(R.id.btn_wed1);
        }
        if(comame.equals("水-2限")){
            ComaArea = findViewById(R.id.btn_wed2);
        }
        if(comame.equals("水-3限")){
            ComaArea = findViewById(R.id.btn_wed3);
        }
        if(comame.equals("水-4限")){
            ComaArea = findViewById(R.id.btn_wed4);
        }
        if(comame.equals("水-5限")){
            ComaArea = findViewById(R.id.btn_wed5);
        }

        if(comame.equals("木-1限")){
            ComaArea = findViewById(R.id.btn_thu1);
        }
        if(comame.equals("木-2限")){
            ComaArea = findViewById(R.id.btn_thu2);
        }
        if(comame.equals("木-3限")){
            ComaArea = findViewById(R.id.btn_thu3);
        }
        if(comame.equals("木-4限")){
            ComaArea = findViewById(R.id.btn_thu4);
        }
        if(comame.equals("木-5限")){
            ComaArea = findViewById(R.id.btn_thu5);
        }

        if(comame.equals("金-1限")){
            ComaArea = findViewById(R.id.btn_fri1);
        }
        if(comame.equals("金-2限")){
            ComaArea = findViewById(R.id.btn_fri2);
        }
        if(comame.equals("金-3限")){
            ComaArea = findViewById(R.id.btn_fri3);
        }
        if(comame.equals("金-4限")){
            ComaArea = findViewById(R.id.btn_fri4);
        }
        if(comame.equals("金-5限")){
            ComaArea = findViewById(R.id.btn_fri5);
        }

        return ComaArea;
    }


    public void ColoredComa(TextView textviews, String backcolor){
        if(backcolor.equals("赤")==true){
            textviews.setBackgroundResource(R.color.red);
        }

        if(backcolor.equals("紫")==true){
            textviews.setBackgroundResource(R.color.purple);
        }

        if(backcolor.equals("青")==true){
            textviews.setBackgroundResource(R.color.blue);
        }

        if(backcolor.equals("緑")==true){
            textviews.setBackgroundResource(R.color.green);
        }

        if(backcolor.equals("黄")==true){
            textviews.setBackgroundResource(R.color.yellow);
        }

        if(backcolor.equals("橙")==true){
            textviews.setBackgroundResource(R.color.orange);
        }

    }

    //ボタンを押す機構を作る
    //これで初期設定を行う
    public void DisplayTimetable(String Class){


        //端末情報をいれないこと
        db.collection("Timetable").whereEqualTo("spin", Class)
                .get()

                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                AddTimetable(
                                        document.get("Title").toString(),
                                        document.get("Memo").toString(),
                                        document.get("Ba_co").toString(),
                                        document.get("spin").toString()
                                );
                            }
                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    //初期設定の登録
    public void AddTimetable(String Title, String Memo, String Ba_co,String spinnerClass) {

        //データ取得



        // Create a new user with a first and last name
        Map<String, Object> Events = new HashMap<>();
        Events.put("Title", Title);
        Events.put("Memo", Memo);
        Events.put("Ba_co", Ba_co);
        Events.put("spinnerClass", spinnerClass);



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


}