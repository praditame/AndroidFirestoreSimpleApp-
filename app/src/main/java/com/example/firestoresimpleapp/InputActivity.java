package com.example.firestoresimpleapp;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText nama;
    Button submit;
    ImageView foto;
    ProgressBar progressBar;
    String abc;
    ActivityResultLauncher<Intent> activityResultLauncherTakePhoto;
    ActivityResultLauncher<String> getImage;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        nama = findViewById(R.id.textNama);
        submit = findViewById(R.id.btnSubmit);
        foto = findViewById(R.id.foto);

        activityResultLauncherTakePhoto =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    Thread thread = new Thread(() -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null){
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            foto.post(() -> foto.setImageBitmap(bitmap));
                        }
                    });
                    thread.start();
                });

        getImage =
                registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
                    if (result != null){
                        foto.setImageURI(result);
                    }

                });
        foto.setOnClickListener(view -> selectImage());
        submit.setOnClickListener(view -> {
            abc = nama.getText().toString();
//            insert(abc);
            uploadImage(abc);
        });





    }




    void insert(String n, String foto) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", n);
        user.put("foto", foto);

       db.collection("mahasiswa").add(user).addOnSuccessListener(documentReference -> {
            Toast.makeText(InputActivity.this, "OK", Toast.LENGTH_SHORT).show();
            nama.getText().clear();
            finish();
        })

                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Log.w(TAG, "Error adding document", e);
                });


    }

    void selectImage(){
        final CharSequence[] itemsImage = {"Take a photo", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(InputActivity.this);
        builder.setItems(itemsImage, (DialogInterface dialogInterface, int i) -> {
            if ( itemsImage[i].equals("Take a photo")) {
                Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncherTakePhoto.launch(intentPhoto);
            } else if (itemsImage[i].equals("Choose from gallery")) {
                getImage.launch("image/*");
            } else {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    void uploadImage(String nama){
        foto.setDrawingCacheEnabled(true);
        foto.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("images").child("IMG" + new Date().getTime()+ ".jpeg");
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(e -> {
            Toast.makeText(InputActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }).addOnSuccessListener(taskSnapshot -> {
            if (taskSnapshot.getMetadata()!=null){
                if (taskSnapshot.getMetadata().getReference()!=null){
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(task -> {
                        if (task.getResult()!= null){
                            insert(nama, task.getResult().toString());
                        }

                    });
                } else {
                    Toast.makeText(InputActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(InputActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

}