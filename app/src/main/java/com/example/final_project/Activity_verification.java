package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class Activity_verification extends AppCompatActivity {

    Integer code=0;
    EditText txt_number1;
    Message message=new Message();
    int activity=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        txt_number1=(EditText) findViewById(R.id.txt_number1);

        TextView text_view=findViewById(R.id.link_resend_code);
        Urderlined urderlined=new Urderlined();//urderline=subrayado
        urderlined.aesthetics_textView(text_view, "Volver a enviar codigo");

        txt_number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(txt_number1.getText().toString().length()==6){
                    if(txt_number1.getText().toString().equals(String.valueOf(code))){
                        if(activity==0){
                            insert();
                            Intent intent = new Intent(getApplicationContext(), Activity_main.class);
                            startActivity(intent);
                        }else{
                            if(activity==1){
                                update_password();
                            }else{
                                if(activity==2){

                                }
                            }
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"AQUI",Toast.LENGTH_LONG).show();
                    }
               }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity=(getIntent().getExtras()).getInt("activity");
        if(activity==0){
            generate_code("");
        }else{
            if(activity==1){
                select_email();
            }else{
                if(activity==2){

                }
            }
        }







    }

    private void select_email(){
        String url=Rest_api.url_mysql+Rest_api.select_recover+"?account="+Data.getAccount();
        RequestQueue queue=Volley.newRequestQueue(this);//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            if(jsonArray.length()>0){
                                JSONObject user_object=jsonArray.getJSONObject(0);//user_object=objeto usuario

                                generate_code(user_object.getString("email"));
                            }

                        }catch(JSONException e){
                            message.message("Error", "datos "+e, Activity_verification.this);
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        message.message("Error", "datos erroneos "+error, Activity_verification.this);            }
                });

        queue.add(request2);
    }


    private void generate_code(String email){

        String url1=null;
        if(activity==0){
            url1=Rest_api.url_mysql+Rest_api.send_mail+"?email="+Data.getEmail();
        }else{
            if(activity==1){
                url1=Rest_api.url_mysql+Rest_api.send_mail+"?email="+email;
            }else{

            }
        }

        RequestQueue queue=Volley.newRequestQueue(this);//queue=cola

        StringRequest request=new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            if(jsonArray.length()>0){
                                JSONObject code_object=jsonArray.getJSONObject(0);//code_object=objeto codigo
                                code=code_object.getInt("Code");
                            }else{
                                message.message("Alerta","Numero de cuenta o usuario incorrecto", Activity_verification.this);
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
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

    public void update_password(){//insert=insertar
        RequestQueue queue= Volley.newRequestQueue(this);//queue=cola

        String url= Rest_api.url_mysql+Rest_api.update_password;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        Intent new_window=new Intent(getApplicationContext(), Activity_sign_in.class);//new_window=nueva ventana
                        startActivity(new_window);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("Volley Error", errorMessage);
                    }
                }) {
            @NonNull
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters=new HashMap<String,String>();//parameters=parametros
                parameters.put("account", Data.getAccount());
                parameters.put("password",Data.getPassword());

                return parameters;
            }
        };

        queue.add(request);
    }

    public void insert(){//insert=insertar
        RequestQueue queue= Volley.newRequestQueue(this);//queue=cola

        String url= Rest_api.url_mysql+Rest_api.insert_user;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        message.message("Alerta","Tu cuenta se ha creado exitosamente, por favor guarda tu numero de cuenta "+Data.getAccount()+" porque no se podra cambiar"+response, Activity_verification.this);
                        Intent new_window=new Intent(getApplicationContext(), Activity_main.class);//new_window=nueva ventana
                        startActivity(new_window);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("Volley Error", errorMessage);
                    }
                }) {
            @NonNull
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters=new HashMap<String,String>();//parameters=parametros
                parameters.put("name_user", Data.getName());
                parameters.put("dni",Data.getDni());
                parameters.put("birth_date",String.valueOf(Data.getBirth_date()));
                parameters.put("account",Data.getAccount());
                parameters.put("password",Data.getPassword());
                parameters.put("phone",Data.getPhone());
                parameters.put("email",Data.getEmail());
                parameters.put("id_career",String.valueOf(Data.getId_career()));
                /*parameters.put("photo","");*/

                return parameters;
            }
        };

        queue.add(request);
    }
}