package com.example.milk_store_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milk_store_app.models.request.LoginRequest;
import com.example.milk_store_app.repository.AuthRepository;
import com.example.milk_store_app.services.AuthService;
import com.example.milk_store_app.session.SessionManager;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userName;
    EditText password;
    TextView create;
    Button loginButton;
    AuthService authService;
    SessionManager sessionManager;

    private final String REQUIRE = "Required!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        sessionManager = new SessionManager(this);
        authService = AuthRepository.getAuthService(this);
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
        // this will run asynchronously
        handeLoginApi();
    }

    private void signUp(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginButton) {
            signIn();
        } else if (view.getId() == R.id.create) {
            signUp();
        }
    }

    private void handeLoginApi() {
        LoginRequest loginRequest = new LoginRequest(userName.getText().toString(), password.getText().toString());
        Call<ResponseBody> call = authService.login(loginRequest);
        // this will run asynchronously
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody == null) {
                            showErrorDialog(SignInActivity.this, "Something went wrong");
                            return;
                        }
                        String token = responseBody.string();
                        sessionManager.saveAuthToken(token);

                        if (sessionManager.isDeliveryStaff()) {
                            Intent intent = new Intent(SignInActivity.this, DeliveryViewActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }

                        if (sessionManager.isShopStaff()) {
                            Intent intent = new Intent(SignInActivity.this, StaffViewOrderActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }

                        if(sessionManager.isCustomer()){
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        } else if(sessionManager.fetchUserId().equals("5f3e80fd-3aeb-4480-872a-21db9b93b5ec")){
                            Intent intent = new Intent(SignInActivity.this, MessageListActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }

                    } catch (Exception e) {
                        showErrorDialog(SignInActivity.this, "Something went wrong");
                    }
                }
                showErrorDialog(SignInActivity.this, "Invalid username or password");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                showErrorDialog(SignInActivity.this, "Something went wrong");
            }
        });
    }


    private void showErrorDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}