package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;


public class Activity_sign_in extends AppCompatActivity {

    EditText txt_email, txt_password;//email=correo, password=contraseña
    Button btn_sign_in, btn_login;//sign_in=registrarse, login=iniciar sesion
    ImageView image_view;
    Uri svg_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        image_view=findViewById(R.id.imageView4);
        svg_uri=Uri.parse("android.resource://" + getPackageName() + "/drawable/image_login");

        txt_email=(EditText) findViewById(R.id.txt_email);
        txt_password=(EditText) findViewById(R.id.txt_password);
        btn_sign_in=(Button) findViewById(R.id.btn_sign_in);

        adapt_image();
    }

    private void adapt_image(){
        //Glide.with(this).load(R.drawable.image_login).into(image_view);
    }
}