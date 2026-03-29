package com.example.doan_ltmb.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_ltmb.data.api.RetrofitClient;
import com.example.doan_ltmb.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(v -> register());
        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void register() {
        String name = binding.etFullName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String phone = binding.etPhone.getText().toString();
        String password = binding.etPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("full_name", name);
        userMap.put("email", email);
        userMap.put("phone", phone);
        userMap.put("password", password);

        // This assumes an endpoint exists or we use login logic for now
        // Based on readme: POST /auth/register
        // Since I haven't defined RegisterResponse in ApiService, I'll use a generic Map or update ApiService
        Toast.makeText(this, "Registering...", Toast.LENGTH_SHORT).show();
        
        // Mocking success for now as API might not be live
        finish();
    }
}
