package com.websarva.wings.android.carendersample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class Search extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



    }
    public void onBackButtonClick(View view) {
        Intent intent = new Intent(Search.this, MainActivity.class);
        startActivity(intent);
    }
    public void onTimetable(View view) {
        Intent intent = new Intent(Search.this, Timetable.class);
        startActivity(intent);
    }
    public void onSchedule(View view) {
        Intent intent = new Intent(Search.this, Schedule_Add.class);
        startActivity(intent);
    }

    public void onSearch(View view){
        SearchStart();
    }



    public void SearchStart(){

        //仮
        String b = findViewById(R.id.textView2).toString();

        //format変換する為の記述
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh");
        java.util.Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp a = new java.sql.Timestamp(parsedDate.getTime());

        db.collection("Calender")
                .whereGreaterThan("Start", a)
                .get()

                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView textViews = findViewById(R.id.textView3);

                                textViews.setText( textViews.getText()+ document.get("Subject").toString());

                            }
                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public void SearchEnd(){

        //仮
        int a = 0;
        db.collection("Calender")
                .whereLessThanOrEqualTo("End", a)
                .get()

                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TextView textViews = findViewById(R.id.textView3);

                                textViews.setText( textViews.getText()+ "\n" + document.get("Subject").toString());

                            }
                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }


    public void SearchSubject(){

        //仮
        String a = findViewById(R.id.textView2).toString();

        Query query = db.collection("Calender").where(Filter.or(
                Filter.lessThanOrEqualTo("Subject", a),
                Filter.greaterThanOrEqualTo("Subject", a+"んんんんんんんんんん"+"んんんんん")));

        query.get()

                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                TextView textViews = findViewById(R.id.textView3);
                                Log.d(TAG, document.getId() + " => " + document.get("Start"));
                                textViews.setText( textViews.getText()+ document.get("Subject").toString());

                            }
                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }


    public void SearchNotes(){

        //仮
        int a = 0;

        Query query = db.collection("Calender").where(Filter.or(
                Filter.lessThanOrEqualTo("Notes", a),
                Filter.greaterThanOrEqualTo("Notes", a+"んんんんんんんんんん"+"んんんんん")));

        query.get()
                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }




}