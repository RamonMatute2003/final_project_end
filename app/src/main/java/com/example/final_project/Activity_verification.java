package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class Activity_verification extends AppCompatActivity {

    Integer code=0;
    EditText txt_number1;
    TextView txt_time;
    Message message=new Message();
    int activity=0;
    private CountDownTimer count_down_timer;//count_down_timer=contador regresivo
    private long total_time_minutes=2*60*1000; //total_time_minutes=tiempo total en minutos
    private boolean is_running=false;//is_running=esta corriendo
    String main_email=null, new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        txt_number1=(EditText) findViewById(R.id.txt_number1);

        TextView link_resend_code=findViewById(R.id.link_resend_code);
        txt_time=findViewById(R.id.txt_time);
        Urderlined urderlined=new Urderlined();//urderline=subrayado
        urderlined.aesthetics_textView(link_resend_code, "Volver a enviar codigo");

        start_again();

        txt_number1.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(txt_number1.getText().toString().length()==6){
                    if(txt_number1.getText().toString().equals(String.valueOf(code))){
                        if(activity==0){
                            insert();
                        }else{
                            if(activity==1 || activity==2){
                                update_password();
                            }else{
                                update_data();
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
                    new_password=(getIntent().getExtras()).getString("new_password");
                    generate_code("");
                }else{
                    generate_code((getIntent().getExtras()).getString("email"));
                }
            }
        }

        link_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                start_again();
            }
        });
    }

    private void update_data(){
        RequestQueue queue= Volley.newRequestQueue(this);//queue=cola

        String url= Rest_api.url_mysql+Rest_api.update_data;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Data.setName((getIntent().getExtras()).getString("name_user"));
                        Data.setDni((getIntent().getExtras()).getString("dni"));
                        Data.setBirth_date((getIntent().getExtras()).getString("birth_date"));
                        Data.setAccount((getIntent().getExtras()).getString("account"));
                        Data.setPhone((getIntent().getExtras()).getString("phone"));
                        Data.setEmail((getIntent().getExtras()).getString("email"));
                        Data.setId_career(Integer.parseInt((getIntent().getExtras()).getString("id_career")));
                        Intent new_window=new Intent(getApplicationContext(), Activity_main.class);
                        new_window.putExtra("update", 2);
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
                parameters.put("name_user", (getIntent().getExtras()).getString("name_user"));
                parameters.put("dni", (getIntent().getExtras()).getString("dni"));
                parameters.put("birth_date", (getIntent().getExtras()).getString("birth_date"));
                parameters.put("account", (getIntent().getExtras()).getString("account"));
                parameters.put("phone", (getIntent().getExtras()).getString("phone"));
                parameters.put("email", (getIntent().getExtras()).getString("email"));
                parameters.put("id_career", (getIntent().getExtras()).getString("id_career"));

                return parameters;
            }
        };

        queue.add(request);
    }

    private void start_again(){//start_again=empezar de nuevo
        if(is_running==false){
            start_timer();//start_timer=temporizador de inicio
            if(main_email!=null){
                generate_code(main_email);
            }
        }else{
            message.message("Advertencia","Espera a que se acabe el tiempo para volver a enviar el codigo", Activity_verification.this);
        }
    }

    private void start_timer() {//start_timer=inicio temporizador
        count_down_timer = new CountDownTimer(total_time_minutes, 1000) {
            @Override
            public void onTick(long milliseconds_to_finish){//milliseconds_to_finish=milisegundos para terminar
                update_text_time(milliseconds_to_finish);
            }

            @Override
            public void onFinish() {
                update_text_time(0);
                is_running = false;
            }
        };

        count_down_timer.start();
        is_running = true;
    }

    private void update_text_time(long time_elapsed) {//update_text_time=actualizar texto de tiempo, time_elapsed=tiempo transcurrido
        int minutes = (int) (time_elapsed / 1000) / 60;
        int seconds = (int) (time_elapsed / 1000) % 60;
        String time_string = String.format("%02d:%02d", minutes, seconds);
        txt_time.setText(time_string);
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
        main_email=email;
        String url1=null;
        if(activity==0 || activity==2){
            url1=Rest_api.url_mysql+Rest_api.send_mail+"?email="+Data.getEmail();
            Log.e("0 y 2", url1);
        }else{
            if(activity==1 || activity==3){
                url1=Rest_api.url_mysql+Rest_api.send_mail+"?email="+email;
                Log.e("1", url1);
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

    public void update_password(){
        RequestQueue queue= Volley.newRequestQueue(this);//queue=cola

        String url= Rest_api.url_mysql+Rest_api.update_password;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent new_window = new Intent(getApplicationContext(), Activity_sign_in.class);//new_window=nueva ventana
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

                if(activity!=2){
                    parameters.put("password",Data.getPassword());
                }else{
                    parameters.put("password",new_password);
                    Data.setPassword(new_password);
                }

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
                        show_account();
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
                parameters.put("photo",Data.getPhoto());
                parameters.put("token",Activity_welcome.token);

                return parameters;
            }
        };

        queue.add(request);
    }

    private void show_account(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setMessage("Tu cuenta se ha creado exitosamente, por favor guarda tu numero de cuenta "+Data.getAccount()+" porque no se podra cambiar");
        builder2.setTitle("Adventencia");

        builder2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which2) {
                Intent new_window=new Intent(getApplicationContext(), Activity_main.class);//new_window=nueva ventana
                startActivity(new_window);
            }
        });

        AlertDialog dialog2 = builder2.create();
        dialog2.show();
    }
}