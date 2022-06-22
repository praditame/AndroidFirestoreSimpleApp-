package com.example.firestoresimpleapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InputActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText nama;
    Button submit;
    ProgressBar progressBar;
    String abc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        nama = findViewById(R.id.textNama);
        submit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.pb);
        progressBar.setVisibility(View.INVISIBLE);


        submit.setOnClickListener(view -> {
            abc = nama.getText().toString();
            insert(abc);
        });

    }


    void insert(String n) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", n);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.incrementProgressBy(5);

        db.collection("mahasiswa").add(user).addOnSuccessListener(documentReference -> {
            Toast.makeText(InputActivity.this, "OK", Toast.LENGTH_SHORT).show();
            nama.getText().clear();
            progressBar.setVisibility(View.GONE);
            finish();
        })

                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Log.w(TAG, "Error adding document", e);
                });


    }
}