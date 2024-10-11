package com.example.milk_store_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userName;
    EditText password;
    TextView create;
    Button loginButton;

    private final String REQUIRE = "Required!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        create = findViewById(R.id.create);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    private boolean checkInput() {
        if (userName.getText().toString().isEmpty()) {
            userName.setError(REQUIRE);
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError(REQUIRE);
            return false;
        }
        return true;
    }
    private void signIn(){
        if(!checkInput()){
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void signUp(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginButton) {
            signIn();
        } else if (view.getId() == R.id.create) {
            signUp();
        }
    }
}