package com.websarva.wings.android.carendersample;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Setting extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    //初期起動か否か
    boolean FirstApp = false;

    private static final String PREF_NAME = "MyPrefs";
    private static final String DARK_MODE_KEY = "darkModeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ActionBar actionBar = getSupportActionBar();
        //アクションバーに戻るボタンを追加
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        //スイッチを取得
        Switch darkModeSwitch = findViewById(R.id.bl_wh_switch);

        //保存されたダークモードの状態を読み込む
        boolean isDarkModeEnabled = getDarkModeState();

        //スイッチの状態をセット
        darkModeSwitch.setChecked(isDarkModeEnabled);

        //スイッチの変更リスナーを設定
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //ダークモードの状態を保存
                saveDarkModeState(isChecked);

                //ダークモードを切り替え
                setDarkMorde(isChecked);

//                // 大阪の天気情報を表示
//                String urlFullOsaka = WEATHERINFO_URL + "&q=Osaka&appid=" + APP_ID;
//                receiveWeatherInfo(urlFullOsaka);

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId() ) {
            case android.R.id.home:
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDarkMorde(boolean isDarkModeEnabled){
        //ダークモードの設定を変更
        if (isDarkModeEnabled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        //アクティビティを再作成して変更を反映
        recreate();
    }

    private boolean getDarkModeState(){
        //SharedPreferencesからダークモードの状態を読み込む
        SharedPreferences preferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getBoolean(DARK_MODE_KEY,false);
    }

    private void saveDarkModeState(boolean isDarkModeEnabled){
        //SharedPreferencesにダークモードの状態を保存
        SharedPreferences preferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DARK_MODE_KEY,isDarkModeEnabled);
        editor.apply();
    }

//    private List<Map<String, String>> createList() {
//        List<Map<String, String>> list = new ArrayList<>();
//
//        // 大阪の情報だけを持つリストを作成
//        Map<String, String> osakaMap = new HashMap<>();
//        osakaMap.put("name", "大阪");
//        osakaMap.put("q", "Osaka");
//        list.add(osakaMap);
//
//        return list;
//    }
//    private void receiveWeatherInfo(final String urlFull){
//        Log.i(DEBUG_TAG,urlFull);
//
//        //新しくワーカースレッドを作成(P283)
//        WeatherInfoBackgroundReceiver backgroundReceiver = new WeatherInfoBackgroundReceiver(urlFull);
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Future<String> future = executorService.submit(backgroundReceiver);
//        //ワーカースレッドの処理を実行させる(P287)
//        String result = "";
//        try{
//            Log.i(DEBUG_TAG,"future.get()呼び出し開始");
//            result = future.get(); //WeatherInfoBackgroundReveiverのcallが呼ばれ、結果を待つ
//            Log.i(DEBUG_TAG,"future.get()呼び出し完了");
//        }catch(ExecutionException ex){
//            Log.w(DEBUG_TAG,"非同期処理結果の取得で例外発生",ex);
//        }catch(InterruptedException ex) {
//            Log.w(DEBUG_TAG, "非同期処理結果の取得で例外発生", ex);
//        }
//
//        //取得されたJSONデータを解析してUI状に表示
//        showWeatherInfo(result);
//    }
//
//    private void showWeatherInfo(String result) {
//        String cityName = "";
//        String weather = "";
//        String latitude = "";
//        String longitude = "";
//        try{
//            JSONObject rootJSON = new JSONObject(result);
//            cityName = rootJSON.getString("name");
//            JSONObject coordJSON = rootJSON.getJSONObject("coord");
//            JSONArray weatherJSONArray = rootJSON.getJSONArray("weather");
//            JSONObject weatherJSON = weatherJSONArray.getJSONObject(0);
//            weather = weatherJSON.getString("description");
//        }catch(JSONException ex){
//            Log.e(DEBUG_TAG,"JSON解析失敗",ex);
//        }
//        String telop = cityName + "の天気";
//        String desc  = "現在は" + weather + "です。";
//        TextView tvWeatherTelop = findViewById(R.id.weather_FC);
//        TextView tvWeatherDesc  = findViewById(R.id.weather_FC);
//        tvWeatherTelop.setText(telop);
//        tvWeatherDesc.setText(desc);
//    }
//
//    private class WeatherInfoBackgroundReceiver implements Callable<String> {
//
//        private final String _urlFull;
//
//        //コンストラクタ追加(P290)
//        public WeatherInfoBackgroundReceiver(String urlFull)
//        {
//            _urlFull = urlFull;
//        }
//
//
//        @WorkerThread //バックグラウンド処理であることを保証(P288)
//        @Override
//        public String call() throws Exception {
//            Log.i(DEBUG_TAG,"WeatherInfoBackgroundReceiver.callを実行");
//
//            //HTTP通信(P291)
//            String result = "";
//            HttpURLConnection con = null;   //接続オブジェクト
//            InputStream is = null;          //読み取り用オブジェクト
//            try{
//                URL url = new URL(_urlFull);
//                con = (HttpURLConnection) url.openConnection();
//                con.setConnectTimeout(1000); //接続タイムアウト
//                con.setReadTimeout(1000);    //読み取りタイムアウト
//                con.setRequestMethod("GET"); //GETかPOSTか
//                con.connect();               //実際に接続
//                is = con.getInputStream();   //取得したデータを読み取るオブジェクトを取得
//                result = is2String(is);      //文字列に変換(P292のプログラム)
//                Log.i(DEBUG_TAG,result);
//                Thread.sleep(5000); //５０００ミリ秒（＝5秒）停止させる
//            }catch (MalformedURLException ex){   Log.e(DEBUG_TAG,"URL変換失敗",ex);
//            }catch (SocketTimeoutException ex){  Log.e(DEBUG_TAG,"通信タイムアウト",ex);
//            }catch (IOException ex){             Log.e(DEBUG_TAG,"通信失敗",ex);
//            }
//            finally{ //try～catch文の最後に必ず実行する命令
//                if(con != null) { con.disconnect(); } //接続していたら切断をする
//                if(is != null)  { try{ is.close(); } catch(IOException ex){Log.e(DEBUG_TAG,"InputStream解放失敗",ex);} }
//            }
//            return result;
//        }
//
//        private String is2String(InputStream is) throws IOException {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
//            StringBuffer sb = new StringBuffer();
//            char[] b = new char[1024];
//            int line;
//            while(0 <= (line = reader.read(b))){ sb.append(b,0,line); }
//            return sb.toString();
//        }
//    }
}
