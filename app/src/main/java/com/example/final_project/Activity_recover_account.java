package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.final_project.Settings.Data;
import com.example.final_project.Settings.Message;

public class Activity_recover_account extends AppCompatActivity {
    EditText txt_account_recover, txt_new_password, txt_repeat_new_password;
    Button btn_change_password;
    Message message=new Message();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_account);
        txt_account_recover=findViewById(R.id.txt_account_recover);
        txt_new_password=findViewById(R.id.txt_new_password);
        txt_repeat_new_password=findViewById(R.id.txt_repeat_new_password);
        btn_change_password=findViewById(R.id.btn_save_changes);

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_account_recover.getText().toString().isEmpty() && txt_new_password.getText().toString().isEmpty() && txt_repeat_new_password.getText().toString().isEmpty()){
                    message.message("Advertencia", "No dejar campos vacios", Activity_recover_account.this);
                }else{
                    Intent new_window=new Intent(getApplicationContext(), Activity_verification.class);//new_window=nueva ventana
                    new_window.putExtra("activity",1);
                    Data.setAccount(txt_account_recover.getText().toString());
                    Data.setPassword(txt_new_password.getText().toString());
                    startActivity(new_window);
                }
            }
        });
    }

}