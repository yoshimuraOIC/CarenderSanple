package com.websarva.wings.android.carendersample;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.view.View;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.security.auth.Subject;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "AsyncSample";
    private static final String WEATHERINFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja";
    private static final String APP_ID = "2498560399a72d494a8889303d181368";
    private List<Map<String,String>> _list;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //初期起動か否か
    boolean FirstApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendar = findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                try {
                String SelectedDate = year + "/" + (month + 1) + "/" + (dayOfMonth + 1);
             //   String SelectedBeforeDate = year + "/" + (month + 2) + "/" + dayOfMonth;

                    Log.d(TAG, SelectedDate);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date SelectedCalender = sdf.parse(SelectedDate);
              //  Date SelectedBeforeCalender = sdf.parse(SelectedBeforeDate);

                DisplaySchredule(SelectedCalender);
            }catch (ParseException e) {
                    //例外処理  正しくない日付
                    Log.d(TAG, "日付エラー");
                }
            }
        });

        // 大阪の天気情報を表示
        String urlFullOsaka = WEATHERINFO_URL + "&q=Osaka&appid=" + APP_ID;
        receiveWeatherInfo(urlFullOsaka);

    }
    public void onSearch(View view) {
        Intent intent = new Intent(MainActivity.this, Search.class);
        startActivity(intent);
    }
    public void onSetting(View view) {
        Intent intent = new Intent(MainActivity.this, Setting.class);
        startActivity(intent);
    }
    public void onTimetable(View view){
        Intent intent = new Intent(MainActivity.this, Timetable.class);
        startActivity(intent);
    }
    public void onSchedule(View view){
        Intent intent = new Intent(MainActivity.this, Schedule_Add.class);
        startActivity(intent);
    }

    public void SelectedCalender(){

    }

    //表示
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


    //選択されたときのやつ
    public void DisplaySchredule(Date SelectDay){



        //日付を取得する
        // TextView Today = findViewById(R.id.title1);
      //  db.collection("Calender").whereEqualTo("Start", SelectDay).get()

        CollectionReference Calenders =db.collection("Calender");


        //エラーが発生した　INVALID_ARGUMENT: 不等式フィルター プロパティと最初の並べ替え順序は同じである必要があります: Start と `End`
       //Query query = Calenders.whereGreaterThanOrEqualTo("Start", SelectDay).whereLessThanOrEqualTo("End", SelectDay).orderBy("Start").orderBy("End");



    //   Query query = Calenders.whereGreaterThanOrEqualTo("Start", SelectDay).orderBy("Start");



       Query query = Calenders.whereGreaterThan("End", SelectDay).orderBy("End");
          //     .whereGreaterThan("End",SelectBeforeDay).orderBy("End");


       // Query combinedQuery = query1; // 例えば query1 が null でないことを確認してください
       // if (query2 != null) {combinedQuery = query1.whereLessThanOrEqualTo("End", SelectDay).orderBy("End");}

        Log.d(TAG, "12345");

    query.get()
    //combinedQuery.get()

                //検索にエラーが起きるかどうか
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //エラー起きない
                        if (task.isSuccessful()) {
                            String Displaydata = "";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              Log.d(TAG, document.getId() + " => " + document.get("Subject"));






                                                        // タイムスタンプ型のデータを取得
                                                        Object timestampObject = document.get("Start");

                                                        // タイムスタンプ型をDate型に変換
                                                        if (timestampObject instanceof com.google.firebase.Timestamp) {
                                                            com.google.firebase.Timestamp timestamp = (com.google.firebase.Timestamp) timestampObject;
                                                            Date date = timestamp.toDate();
                                                            Log.d(TAG, "bat");
                                                            if(SelectDay.after(date)) {
                                                                Displaydata += document.get("Subject") + "\n";
                                                                Log.d(TAG, "succes");
                                                            }
                                                            // ここで date を使って何か処理
                                                        } else {
                                                    // 失敗時の処理
                                                    Exception e = task.getException();
                                                    if (e != null) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                //指定された日より前である

                            }
                            Log.d(TAG, "OnCrea");

                            TextView textView = findViewById(R.id.subjectmain);
                            // テキストを設定して表示
                            textView.setText(Displaydata);


                            //エラー
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });

    }

    private List<Map<String, String>> createList() {
        List<Map<String, String>> list = new ArrayList<>();

        // 大阪の情報だけを持つリストを作成
        Map<String, String> osakaMap = new HashMap<>();
        osakaMap.put("name", "大阪");
        osakaMap.put("q", "Osaka");
        list.add(osakaMap);

        return list;
    }
    private void receiveWeatherInfo(final String urlFull){
        Log.i(DEBUG_TAG,urlFull);

        //新しくワーカースレッドを作成(P283)
        WeatherInfoBackgroundReceiver backgroundReceiver = new WeatherInfoBackgroundReceiver(urlFull);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(backgroundReceiver);
        //ワーカースレッドの処理を実行させる(P287)
        String result = "";
        try{
            Log.i(DEBUG_TAG,"future.get()呼び出し開始");
            result = future.get(); //WeatherInfoBackgroundReveiverのcallが呼ばれ、結果を待つ
            Log.i(DEBUG_TAG,"future.get()呼び出し完了");
        }catch(ExecutionException ex){
            Log.w(DEBUG_TAG,"非同期処理結果の取得で例外発生",ex);
        }catch(InterruptedException ex) {
            Log.w(DEBUG_TAG, "非同期処理結果の取得で例外発生", ex);
        }

        //取得されたJSONデータを解析してUI状に表示
        showWeatherInfo(result);
    }

    private void showWeatherInfo(String result) {
        String cityName = "";
        String weather = "";
        String latitude = "";
        String longitude = "";
        try{
            JSONObject rootJSON = new JSONObject(result);
            cityName = rootJSON.getString("name");
            JSONObject coordJSON = rootJSON.getJSONObject("coord");
            JSONArray weatherJSONArray = rootJSON.getJSONArray("weather");
            JSONObject weatherJSON = weatherJSONArray.getJSONObject(0);
            weather = weatherJSON.getString("description");
        }catch(JSONException ex){
            Log.e(DEBUG_TAG,"JSON解析失敗",ex);
        }
        String telop = cityName + "の天気";
        String desc  = "現在は" + weather + "です。";
        TextView tvWeatherTelop = findViewById(R.id.weather_FC);
        TextView tvWeatherDesc  = findViewById(R.id.weather_FC);
        tvWeatherTelop.setText(telop);
        tvWeatherDesc.setText(desc);
    }

    private class WeatherInfoBackgroundReceiver implements Callable<String> {

        private final String _urlFull;

        //コンストラクタ追加(P290)
        public WeatherInfoBackgroundReceiver(String urlFull)
        {
            _urlFull = urlFull;
        }


        @WorkerThread //バックグラウンド処理であることを保証(P288)
        @Override
        public String call() throws Exception {
            Log.i(DEBUG_TAG,"WeatherInfoBackgroundReceiver.callを実行");

            //HTTP通信(P291)
            String result = "";
            HttpURLConnection con = null;   //接続オブジェクト
            InputStream is = null;          //読み取り用オブジェクト
            try{
                URL url = new URL(_urlFull);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(10000); //接続タイムアウト
                con.setReadTimeout(10000);    //読み取りタイムアウト
                con.setRequestMethod("GET"); //GETかPOSTか
                con.connect();               //実際に接続
                is = con.getInputStream();   //取得したデータを読み取るオブジェクトを取得
                result = is2String(is);      //文字列に変換(P292のプログラム)
                Log.i(DEBUG_TAG,result);
                Thread.sleep(5000); //５０００ミリ秒（＝5秒）停止させる
            }catch (MalformedURLException ex){   Log.e(DEBUG_TAG,"URL変換失敗",ex);
            }catch (SocketTimeoutException ex){  Log.e(DEBUG_TAG,"通信タイムアウト",ex);
            }catch (IOException ex){             Log.e(DEBUG_TAG,"通信失敗",ex);
            }
            finally{ //try～catch文の最後に必ず実行する命令
                if(con != null) { con.disconnect(); } //接続していたら切断をする
                if(is != null)  { try{ is.close(); } catch(IOException ex){Log.e(DEBUG_TAG,"InputStream解放失敗",ex);} }
            }
            return result;
        }

        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while(0 <= (line = reader.read(b))){ sb.append(b,0,line); }
            return sb.toString();
        }
    }

public void  a(){

}


}