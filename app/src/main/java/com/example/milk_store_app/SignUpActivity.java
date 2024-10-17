package com.example.milk_store_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.milk_store_app.models.error.ValidationError;
import com.example.milk_store_app.models.request.RegisterRequest;
import com.example.milk_store_app.models.response.RegisterResponse;
import com.example.milk_store_app.repository.AuthRepository;
import com.example.milk_store_app.services.AuthService;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userName;
    EditText password;
    EditText confirmPassword;
    TextView signIn;
    Button signUpButton;
    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        signIn = findViewById(R.id.signIn);
        signUpButton = findViewById(R.id.signUpButton);

        authService = AuthRepository.getAuthService(this);

        signIn.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    private boolean checkInput() {
        String REQUIRE = "Required!";
        if (userName.getText().toString().isEmpty()) {
            userName.setError(REQUIRE);
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError(REQUIRE);
            return false;
        }
        if (confirmPassword.getText().toString().isEmpty()) {
            confirmPassword.setError(REQUIRE);
            return false;
        }
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signUp() {
        if (!checkInput()) {
            return;
        }
        handleRegisterApi();
    }

    private void signIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signUpButton) {
            signUp();
        } else if (view.getId() == R.id.signIn) {
            signIn();
        }
    }

    private void handleRegisterApi() {
        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setUsername(userName.getText().toString());
        registerRequest.setPassword(password.getText().toString());

        Call<RegisterResponse> registerResponseCall = authService.register(registerRequest);

        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    // Show success dialog
                    assert response.body() != null;
                    showSuccessDialog(response.body().getMessage());
                } else {
                    // Show error dialog
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Register success");
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void handleError(Response<RegisterResponse> response) {
        try (ResponseBody responseBody = response.errorBody()) {
            // Convert the error body to the ErrorResponse model
            if (responseBody == null) {
                showErrorDialog("An unknown error occurred.");
                return;
            }
            String errorBody = responseBody.string();
            Gson gson = new Gson();
            ValidationError errorResponse = gson.fromJson(errorBody, ValidationError.class);

            // Extract the first error message for the PhoneNumber field (if it exists)
            List<String> errorMessages = errorResponse.getErrors()
                    .values()
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());


            String errorMessage = errorMessages.isEmpty()
                    ? "An unknown error occurred."
                    : String.join("\n", errorMessages);

            // Show the error dialog
            showErrorDialog(errorMessage);
        } catch (Exception e) {
            showErrorDialog("An unknown error occurred.");
        }
    }
}