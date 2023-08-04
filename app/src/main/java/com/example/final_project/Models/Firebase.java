package com.example.final_project.Models;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Firebase extends AppCompatActivity {
    public FirebaseStorage storage;
    public StorageReference storageRef;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String email="ramonmatute2003@gmail.com";
    String password="Matute10";

    public void get_token(String file){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.e("iniciar sesión", "iniciar sesión exitoso ");
                    }else{
                        Exception exception = task.getException();
                        if (exception != null) {
                            Log.e("LoginError", "Error al iniciar sesión: " + exception.getMessage());
                        }
                    }
                });
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child(file);
    }
}
