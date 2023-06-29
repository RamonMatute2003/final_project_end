package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.final_project.Settings.Urderlined;


public class Activity_sign_in extends AppCompatActivity {

    private EditText txt_account_number, txt_password;//txt_account_number=numero de cuenta, password=contraseña
    private Button btn_sign_in, btn_sign_up;//btn_sign_up=registrarse, btn_sign_in=iniciar sesion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txt_account_number=(EditText) findViewById(R.id.txt_account_number);
        txt_password=(EditText) findViewById(R.id.txt_password);
        btn_sign_in=(Button) findViewById(R.id.btn_sign_in2);
        btn_sign_up=(Button) findViewById(R.id.btn_sign_up2);
        TextView text_view1=(TextView) findViewById(R.id.link_recover_password);
        TextView text_view2=(TextView) findViewById(R.id.link_create_account);

        Urderlined urderlined=new Urderlined();//urderline=subrayado

        urderlined.aesthetics_textView(text_view1, "¿Se te olvido la contraseña?");
        urderlined.aesthetics_textView(text_view2, "¿No tienes una cuenta?");

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window=new Intent(getApplicationContext(), Activity_main.class);//new_window=nueva ventana
                startActivity(new_window);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window= new Intent(getApplicationContext(), Activity_sign_up.class);//new_window=nueva ventana
                startActivity(new_window);
            }
        });

        text_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window=new Intent(getApplicationContext(), Activity_recover_account.class);
                startActivity(new_window);
            }
        });
    }
}