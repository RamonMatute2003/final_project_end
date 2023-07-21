package com.example.final_project.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project.Activity_main;
import com.example.final_project.Activity_verification;
import com.example.final_project.R;
import com.example.final_project.Settings.Data;
import com.example.final_project.Settings.Rest_api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_view_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_view_profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_view_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_view_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_view_profile newInstance(String param1, String param2) {
        Fragment_view_profile fragment = new Fragment_view_profile();
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

    TextView txt_friends_view, txt_dni_view, txt_phone_view, txt_birth_date_view, txt_career_view, txt_email_view, txt_account_view, txt_name_view;
    ImageView image_view;
    Button btn_action;
    int id_user, id_friend, status;
    final String text_add ="Agregar amigo", text_del="Eliminar amigo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_profile, container, false);

        txt_account_view = root.findViewById(R.id.txt_account_view);
        txt_friends_view = root.findViewById(R.id.txt_friends_view);
        txt_dni_view = root.findViewById(R.id.txt_dni_view);
        txt_phone_view = root.findViewById(R.id.txt_phone_view);
        txt_birth_date_view = root.findViewById(R.id.txt_birth_date_view);
        txt_career_view = root.findViewById(R.id.txt_career_view);
        txt_email_view = root.findViewById(R.id.txt_email_view);
        txt_name_view = root.findViewById(R.id.txt_name_view);
        btn_action = root.findViewById(R.id.btn_action);
        image_view = root.findViewById(R.id.image_view);

        Bundle args=getArguments();
        if(args!=null){
            id_user=args.getInt("id_user");
            status=args.getInt("status");
        }

        if(status==0){
            btn_action.setText(text_del);
            load_data();
        }else{
            btn_action.setText(text_add);
            add_friends();
        }

        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action();
            }
        });

        return root;
    }

    private void action(){
        if(btn_action.getText().toString().equals(text_add)){
            insert_friend();
        }else{
            show_message();
        }
    }

    private void insert_friend(){
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        String url= Rest_api.url_mysql+Rest_api.insert_friend;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        Log.e("exitoso", response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("Volley Error", errorMessage);
                    }
                }) {
            @NonNull
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters=new HashMap<String,String>();//parameters=parametros
                parameters.put("id_user", String.valueOf(Data.getId_user()));
                parameters.put("id_companion", String.valueOf(id_user));

                return parameters;
            }
        };

        queue.add(request);
    }

    private void show_message(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¿Seguro que lo deseas eliminar de tus amigos?");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete_friend();
            }
        });
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void delete_friend(){
        String url=Rest_api.url_mysql+Rest_api.delete_friend;
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);

                            if(json_users.length()>0){
                                btn_action.setText(text_add);
                            }

                        }catch(JSONException e){
                            Log.e("Error", "datos erroneos "+e);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "datos erroneos "+error);
            }
        }){
            @NonNull
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters=new HashMap<String,String>();//parameters=parametros
                parameters.put("id_friend", String.valueOf(id_friend));

                return parameters;
            }
        };;

        queue.add(request2);
    }

    private void load_data(){
        String url= Rest_api.url_mysql+Rest_api.select_friend+"?id_user="+id_user;
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            if(jsonArray.length()>0){
                                JSONObject user_object=jsonArray.getJSONObject(0);//user_object=objeto usuario

                                txt_account_view.setText("N° cuenta: "+user_object.getString("account"));
                                txt_friends_view.setText("Amigos: 0");
                                txt_dni_view.setText("DNI: "+user_object.getString("dni"));
                                txt_phone_view.setText("Telefono: "+user_object.getString("phone"));
                                txt_birth_date_view.setText("Fecha de nacimiento: "+user_object.getString("birth_date"));
                                txt_career_view.setText("Carrera: "+user_object.getString("career_name"));
                                txt_email_view.setText("Correo: "+user_object.getString("email"));
                                txt_name_view.setText("Nombre: "+user_object.getString("name_user"));
                                id_friend=user_object.getInt("id_friend");
                            }

                        }catch(JSONException e){
                            Log.e("e",""+e);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("e",""+error);
            }
        });

        queue.add(request2);
    }

    private void add_friends(){
        String url= Rest_api.url_mysql+Rest_api.select_add_friends+"?id_user="+id_user;
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            if(jsonArray.length()>0){
                                JSONObject user_object=jsonArray.getJSONObject(0);//user_object=objeto usuario

                                txt_account_view.setText("N° cuenta: "+user_object.getString("account"));
                                txt_friends_view.setText("Amigos: 0");
                                txt_dni_view.setText("DNI: "+user_object.getString("dni"));
                                txt_phone_view.setText("Telefono: "+user_object.getString("phone"));
                                txt_birth_date_view.setText("Fecha de nacimiento: "+user_object.getString("birth_date"));
                                txt_career_view.setText("Carrera: "+user_object.getString("career_name"));
                                txt_email_view.setText("Correo: "+user_object.getString("email"));
                                txt_name_view.setText("Nombre: "+user_object.getString("name_user"));
                            }

                        }catch(JSONException e){
                            Log.e("e",""+e);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("e",""+error);
            }
        });

        queue.add(request2);
    }
}