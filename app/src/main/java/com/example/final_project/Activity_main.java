package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project.Fragment.Fragment_add_remove_companions;
import com.example.final_project.Fragment.Fragment_groups;
import com.example.final_project.Fragment.Fragment_home;
import com.example.final_project.Fragment.Fragment_settings_profile;
import com.example.final_project.Fragment.Fragment_view_profile;
import com.example.final_project.Settings.Data;
import com.example.final_project.Settings.Message;
import com.example.final_project.Settings.Rest_api;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Activity_main extends AppCompatActivity {
    BottomNavigationView button_nav;//boton de navegar
    Message message=new Message();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_nav = findViewById(R.id.buttonNavigationContainer);

        //Manda a llamar el fragment_home
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new Fragment_home()).commit();
        button_nav.setSelectedItemId(R.id.itemHome);

        Intent received_intent=getIntent();

        select_id_user();

        if(received_intent.hasExtra("update")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new Fragment_settings_profile()).commit();
            button_nav.setSelectedItemId(R.id.itemSettings);
        }else{
            if(received_intent.hasExtra("add_remove")){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new Fragment_add_remove_companions()).commit();
                button_nav.setSelectedItemId(R.id.itemContacts);
            }else{
                if(received_intent.hasExtra("sett_prof")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new Fragment_settings_profile()).commit();
                    button_nav.setSelectedItemId(R.id.itemSettings);
                }else{
                    if(received_intent.hasExtra("view")){
                        button_nav.setSelectedItemId(R.id.itemContacts);
                        Fragment_view_profile fragment = new Fragment_view_profile();

                        if(received_intent.hasExtra("status")){
                            Bundle args=new Bundle();
                            args.putInt("id_user",Integer.parseInt((getIntent().getExtras()).getString("view")));
                            args.putInt("status",Integer.parseInt((getIntent().getExtras()).getString("status")));
                            fragment.setArguments(args);
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
                    }
                }
            }
        }

        button_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                if(item.getItemId()==R.id.itemExitToApp){
                    Intent welcome=new Intent(getApplicationContext(), Activity_welcome.class);
                    startActivity(welcome);
                    Data.setPhone("");
                    Data.setEmail("");
                    Data.setAccount("");
                    Data.setDni("");
                    Data.setId_career(0);
                    Data.setBirth_date("");
                    Data.setPassword("");
                    Data.setName("");
                    Data.setId_user(0);
                    Data.setCareer("");
                }else{
                    if(item.getItemId()==R.id.itemGroups){
                        fragment=new Fragment_groups();
                    }else{
                        if(item.getItemId()==R.id.itemContacts){
                            fragment=new Fragment_add_remove_companions();
                        }else{
                            if(item.getItemId()==R.id.itemSettings){
                                fragment=new Fragment_settings_profile();
                            }else{
                                if(item.getItemId() == R.id.itemHome){
                                    fragment=new Fragment_home();
                                }
                            }
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
                    item.setChecked(true);
                }

                return false;
            }
        });
    }

    private void select_id_user(){
        String url= Rest_api.url_mysql+Rest_api.select_id_user+"?account="+ Data.getAccount();
        RequestQueue queue= Volley.newRequestQueue(this);//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            if(jsonArray.length()>0){
                                JSONObject user_object=jsonArray.getJSONObject(0);//user_object=objeto usuario

                                Data.setId_user(Integer.parseInt(user_object.getString("id_user")));
                            }

                        }catch(JSONException e){
                            message.message("Error", "datos "+e, Activity_main.this);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message.message("Error", "datos erroneos "+error, Activity_main.this);            }
        });

        queue.add(request2);
    }
}