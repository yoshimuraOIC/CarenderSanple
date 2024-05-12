package com.websarva.wings.android.carendersample;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class Schedule_Add extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);
    }
    public void main(View view) {
        Intent intent = new Intent(Schedule_Add.this, MainActivity.class);
        startActivity(intent);
    }
    public void zikanwari(View view) {
        Intent intent = new Intent(Schedule_Add.this, Timetable.class);
        startActivity(intent);
    }
    public void onSearch(View view) {
        Intent intent = new Intent(Schedule_Add.this, Search.class);
        startActivity(intent);
    }

    public void onAdd(View view) {
        // Intent intent = new Intent(Schedule_Add.this, AddSchedule.class);
        // startActivity(intent);
        // AddScheduleメソッドを呼び出す
        AddSchedule();
        //DisplaySchredule();
    }
    public void AddSchedule(){

        //データ取得
        TextView Start = findViewById(R.id.Start);
        TextView End = findViewById(R.id.End);
        TextView Subject = findViewById(R.id.subjectname);
        TextView Notes = findViewById(R.id.notes);


        // Create a new user with a first and last name
        Map<String, Object> Events = new HashMap<>();
        Events.put("Start", Start.getText().toString());
        Events.put("End", End.getText().toString());
        Events.put("Subject", Subject.getText().toString());
        Events.put("Notes", Notes.getText().toString());


        db.collection("Calender").add(Events)
                //登録成功時の挙動
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Log.d(TAG, "seikousimasita" );
                    }
                })
                //登録失敗時の挙動
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }

    public void UpdateSchedule(){
        //データ取得
        //日付を取得する
        // TextView Today = findViewById(R.id.title1);
        String Today  = "1";

        TextView Start = findViewById(R.id.Start);
        TextView End = findViewById(R.id.End);
        TextView Subject = findViewById(R.id.subjectname);
        TextView Duedate = findViewById(R.id.notes);



        db.collection("Calender").document(Today)
                .update(
                        "Start", Start.getText().toString(),
                        "End", End.getText().toString(),
                        "Subject", Subject.getText().toString(),
                        "Duedate", Duedate.getText().toString()
                );


    }


    public void DisplaySchredule(){

        //日付を取得する
        // TextView Today = findViewById(R.id.title1);
        int Today  = 1;
        db.collection("Calender").whereEqualTo("Start", Today)
                .get()

                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.get("Subject"));
                            }
                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    //document.get("Start")で　一部だけデータ取れる
    //


    //削除
    public void DeleteSchredule(){

        String Today  = "1";
        db.collection("Calender").document(Today)
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