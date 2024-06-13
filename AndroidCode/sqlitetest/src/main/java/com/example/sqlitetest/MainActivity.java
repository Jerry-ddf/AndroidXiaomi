package com.example.sqlitetest;


import com.example.sqlitetest.database.DatabaseHelper;
import com.example.sqlitetest.entry.Userinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String JSON_URL = "https://jsonplaceholder.typicode.com/posts/3";
    private Button buttonA;
    private Button buttonB;
    private TextView textView1, textView2, textView3, textView4;
    private Handler handler;
    private DatabaseHelper dbHelper;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        handler = new Handler(Looper.getMainLooper());
        dbHelper = new DatabaseHelper(this);

        executorService = Executors.newSingleThreadExecutor();

        buttonA = findViewById(R.id.btn_request);
        buttonB = findViewById(R.id.btn_read);
        textView1 = findViewById(R.id.tv_contentUserId);
        textView2 = findViewById(R.id.tv_contentId);
        textView3 = findViewById(R.id.tv_contentTitle);
        textView4 = findViewById(R.id.tv_contentBody);


        buttonA.setOnClickListener(this);
        buttonB.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_request) {
//           // Get writable database
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//            // Execute SQL statement to delete table
//            db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_NAME);
            fetchJson(JSON_URL);


        } else if (v.getId() == R.id.btn_read) {
            displayJsonData(3);
        }
    }

    private void fetchJson(String urlString) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
//                Log.v("465", "第一次");

                String json = null;
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) builder.append(line);
                    reader.close();
                    json = builder.toString();
//                    Log.v("json", json);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "JSON获取失败", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MainActivity.this, "123123", Toast.LENGTH_SHORT).show();

                String finalJson = json;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (finalJson != null) {
                            Gson gson = new Gson();
                            Userinfo userinfo = gson.fromJson(finalJson, Userinfo.class);
//                            Log.i("mesg", userinfo.toString());
                            dbHelper.insertData(userinfo);
                        }
                    }
                });
            }
        });
    }

    private void displayJsonData(int id) {
//        Log.v("123", "第一次");
        executorService.execute(new Runnable() {
            @Override
            public void run() {
//                Log.v("123", "第er次");

                Userinfo userinfo = dbHelper.getJsonData(id);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (userinfo != null) {
                            textView1.setText("userID: " + userinfo.getUserId());
                            textView2.setText("id: " + userinfo.getId());
                            textView3.setText("title: " + userinfo.getTitle());
                            textView4.setText("body: " + userinfo.getBody());
                        } else {
                            textView1.setText("No data found for ID: " + id);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

}