package com.example.final_project;

import static android.content.ContentValues.TAG;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.final_project.Settings.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class Activity_welcome extends AppCompatActivity {

    private Button btn_sign_in, btn_sign_up;//btn_sign_in=iniciar sesion, btn_sign_up=registrarse
    public static String token;//Device ID o token de firebase messagin
    Message message=new Message();//clase message de settings para mensajes de alert builder
    private boolean hasNotificationPermissionGranted = false;//variable para permiso de notificaciones

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_sign_in=(Button) findViewById(R.id.btn_sign_in1);
        btn_sign_up=(Button) findViewById(R.id.btn_sign_up1);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window= new Intent(getApplicationContext(), Activity_sign_in.class);//new_window=nueva ventana
                startActivity(new_window);
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Log.w(TAG, "Error al obtener el token de registro de FCM", task.getException());
                            Toast.makeText(getApplicationContext(), "Error token", Toast.LENGTH_SHORT).show();
                            message.message("Error", "Error al obtener token", getApplicationContext());
                            return;
                        }

                        token=task.getResult();

                        Log.e(TAG, token);
                    }
                });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window= new Intent(getApplicationContext(), Activity_sign_up.class);//new_window=nueva ventana
                startActivity(new_window);
            }
        });

        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
        } else {
            hasNotificationPermissionGranted = true;
        }
    }

    private ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                hasNotificationPermissionGranted = isGranted;
                if (!isGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Build.VERSION.SDK_INT >= 33) {
                            if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                                showNotificationPermissionRationale();
                            } else {
                                showSettingDialog();
                            }
                        }
                    }
                }
            });

    private void showSettingDialog() {
        new AlertDialog.Builder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
                .setTitle("Notificacion de permiso")
                .setMessage("Se requiere permiso de notificación, verifique el permiso de notificación en la configuración")
                .setPositiveButton("Ok", (dialog, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showNotificationPermissionRationale() {
        new AlertDialog.Builder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
                .setTitle("Alert")
                .setMessage("Se requiere permiso de notificación para mostrar la notificación.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    if (Build.VERSION.SDK_INT >= 33) {
                        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}