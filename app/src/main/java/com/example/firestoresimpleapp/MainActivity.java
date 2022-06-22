package com.example.firestoresimpleapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.firestoresimpleapp.adapter.adapterMhs;
import com.example.firestoresimpleapp.model.modelMhs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private FloatingActionButton fab;
    FirebaseFirestore db;
    private List<modelMhs> list;
    private adapterMhs adapter;
    private modelMhs model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.floating_action_button);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapter = new adapterMhs(MainActivity.this, list);
        rv.setAdapter(adapter);

//        displayData();

        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, InputActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayData();
    }

    void displayData() {
                db.collection("mahasiswa").get().addOnCompleteListener(task -> {
                    list.clear();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            model = new modelMhs(document.getString("name"));
                            list.add(model);

                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}