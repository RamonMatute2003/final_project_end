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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project.Settings.Data;
import com.example.final_project.Settings.Message;
import com.example.final_project.Settings.Rest_api;
import com.example.final_project.Settings.Urderlined;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Activity_sign_in extends AppCompatActivity {

    private EditText txt_account_number, txt_password;//txt_account_number=numero de cuenta, password=contraseña
    private Button btn_sign_in, btn_sign_up;//btn_sign_up=registrarse, btn_sign_in=iniciar sesion
    Message message=new Message();

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
                if(txt_account_number.getText().toString().isEmpty() || txt_password.getText().toString().isEmpty()){
                    message.message("No dejar campos vacios", "Alerta", Activity_sign_in.this);
                }else{
                    select_user();
                }
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
                new_window.putExtra("activity",1);
                startActivity(new_window);
            }
        });
    }

    private void select_user(){//consulta de usuario
        String url= Rest_api.url_mysql+Rest_api.select_user+"?account="+txt_account_number.getText().toString()+"&password="+txt_password.getText().toString();
        RequestQueue queue=Volley.newRequestQueue(this);//queue=cola

        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            if(jsonArray.length()>0){
                                JSONObject user_object=jsonArray.getJSONObject(0);//user_object=objeto usuario
                                Data data=new Data(user_object.getString("email"),
                                        user_object.getString("password"),
                                        user_object.getString("career_name"),
                                        user_object.getString("name_user"),
                                        user_object.getString("phone"),
                                        user_object.getString("dni"),
                                        user_object.getString("birth_date"));

                                Data.setId_career(user_object.getInt("id_career"));
                                Data.setAccount(user_object.getString("account"));
                                Intent new_window=new Intent(getApplicationContext(), Activity_main.class);//new_window=nueva ventana
                                startActivity(new_window);
                            }else{
                                message.message("Alerta","Numero de cuenta o usuario incorrecto", Activity_sign_in.this);
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error:"+e, Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
    }
}