package com.example.week2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    private EditText editusername;
    private EditText editpassword;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editusername = findViewById(R.id.editTextUsername);
        editpassword = findViewById(R.id.editTextPassword);
        button = findViewById(R.id.buttonLogin);
        button.setOnClickListener(v -> {
            String username = editusername.getText().toString();
            String password = editpassword.getText().toString();
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                // 使用Toast显示警告信息
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Login.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 当用户名和密码都不为空时，才跳转到下一个Activity
                Intent intent = new Intent(Login.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("password", password);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}