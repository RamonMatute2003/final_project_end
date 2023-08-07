package com.example.final_project.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project.Activity_main;
import com.example.final_project.R;
import com.example.final_project.Settings.Data;
import com.example.final_project.Settings.Message;
import com.example.final_project.Settings.Rest_api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_add_remove_companions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_add_remove_companions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_add_remove_companions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_add_remove_companions.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_add_remove_companions newInstance(String param1, String param2) {
        Fragment_add_remove_companions fragment = new Fragment_add_remove_companions();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    EditText txt_search_users;
    ListView list_view_users;
    Spinner sp_options;
    Message message=new Message();
    List<String> user_list2;
    ArrayAdapter<String> adapter;
    int value=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_add_remove_companions, container, false);

        txt_search_users=root.findViewById(R.id.txt_search_users);
        sp_options=root.findViewById(R.id.sp_options);
        list_view_users=root.findViewById(R.id.sp_list_users);

        sp_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, Collections.singletonList(""));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    list_view_users.setAdapter(adapter);
                    select_companions();
                }else{
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, Collections.singletonList(""));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    list_view_users.setAdapter(adapter);
                    select_add_people();
                }

                if(value==1){
                    search_text();
                }
                value=1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                select_add_people();
            }
        });

        list_view_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent new_window=new Intent(getContext(), Activity_main.class);
                String text = parent.getItemAtPosition(position).toString();
                int index=text.indexOf("-");
                new_window.putExtra("view", text.substring(0, index));
                new_window.putExtra("status", String.valueOf(sp_options.getSelectedItemId()));
                startActivity(new_window);
            }
        });

        txt_search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count){
                search_text();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    private void search_text(){
        List<String> list_copy=new ArrayList<>();

        for(int j=0; j<user_list2.toArray().length; j++){
            boolean search=false;
            String cadenaCompleta = user_list2.get(j);
            String parteBuscada = txt_search_users.getText().toString();

            Pattern pattern = Pattern.compile(parteBuscada);
            Matcher matcher = pattern.matcher(cadenaCompleta);

            while(matcher.find()){
                search=true;
                break;
            }

            if(search){
                list_copy.add(cadenaCompleta);
            }
        }

        if(list_copy!=null){
            if(list_copy.toArray().length>0){
                adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, list_copy);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                list_view_users.setAdapter(adapter);
            }else{
                List<String> list = new ArrayList<>();
                adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                list_view_users.setAdapter(adapter);
            }
        }else{
            adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, user_list2);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            list_view_users.setAdapter(adapter);
        }
    }

    private void select_companions(){
        String url=Rest_api.url_mysql+Rest_api.select_companions_compare+"?id_user="+Data.getId_user();
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);
                            user_list2 = new ArrayList<>();

                            for(int j=0; j<json_users.length(); j++){
                                JSONObject users_object=json_users.getJSONObject(j);

                                if(users_object.getInt("id_user")==Data.getId_user()){
                                    select_especific_user(users_object.getInt("id_companion"));
                                }else{
                                   if(users_object.getInt("id_companion")==Data.getId_user()){
                                       select_especific_user(users_object.getInt("id_user"));
                                   }
                                }
                            }
                        }catch(JSONException e){
                            message.message("Error", "datos "+e, getContext());
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message.message("Error", "datos erroneos "+error, getContext());
            }
        });

        queue.add(request2);
    }

    private void select_especific_user(int id){
        String url=Rest_api.url_mysql+Rest_api.select_specific_user+"?id_user="+id;
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);

                            if(json_users.length()>0){
                                JSONObject users_object=json_users.getJSONObject(0);

                                String id=users_object.getString("id_user");
                                String name=users_object.getString("name_user");
                                String account=users_object.getString("account");
                                String email=users_object.getString("email");
                                String user=id+"-"+name+"-"+account+"-"+email;
                                user_list2.add(user);
                                Log.e("d",""+user_list2);
                            }

                            Log.e("dif",""+user_list2);
                            adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, user_list2);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            list_view_users.setAdapter(adapter);

                        }catch(JSONException e){
                            message.message("Error", "datos "+e, getContext());
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message.message("Error", "datos erroneos "+error, getContext());
            }
        });

        queue.add(request2);
    }

    private void select_add_people(){
        String url=Rest_api.url_mysql+Rest_api.select_add_people+"?id_user="+Data.getId_user();
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);

                            if(json_users.length()>0){
                                select_not_companions(json_users);
                            }

                        }catch(JSONException e){
                            message.message("Error", "datos "+e, getContext());
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message.message("Error", "datos erroneos "+error, getContext());
            }
        });

        queue.add(request2);
    }

    private void select_not_companions(JSONArray json_users){
        String url=Rest_api.url_mysql+Rest_api.select_companions+"?id_user="+Data.getId_user();
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);
                            user_list2 = new ArrayList<>();

                            for(int i=0; i<json_users.length(); i++){

                                for(int j=0; j<jsonArray.length(); j++){

                                    JSONObject users_object=json_users.getJSONObject(i);
                                    JSONObject json2_object=jsonArray.getJSONObject(j);

                                    if(json2_object.getInt("id_user")==users_object.getInt("id_user") || json2_object.getInt("id_companion")==users_object.getInt("id_user")){
                                        json_users.remove(i);
                                    }
                                }
                            }

                            for(int j=0; j<json_users.length(); j++){

                                JSONObject users_object=json_users.getJSONObject(j);

                                String id=users_object.getString("id_user");
                                String name=users_object.getString("name_user");
                                String account=users_object.getString("account");
                                String email=users_object.getString("email");
                                String user=id+"-"+name+"-"+account+"-"+email;
                                user_list2.add(user);
                            }

                            adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, user_list2);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            list_view_users.setAdapter(adapter);
                        }catch(JSONException e){
                            message.message("Error", "datos "+e, getContext());
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message.message("Error", "datos erroneos "+error, getContext());
            }
        });

        queue.add(request2);
    }
}