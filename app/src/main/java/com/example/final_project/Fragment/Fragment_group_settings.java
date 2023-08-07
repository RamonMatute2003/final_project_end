package com.example.final_project.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project.R;
import com.example.final_project.Settings.Data;
import com.example.final_project.Settings.Message;
import com.example.final_project.Settings.Rest_api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_group_settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_group_settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_group_settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_group_settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_group_settings newInstance(String param1, String param2) {
        Fragment_group_settings fragment = new Fragment_group_settings();
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

    EditText txt_name_group;
    ListView list_view;
    TextView btn_add_members;
    Button btn_delete_members, btn_delete_group;
    String id_group;
    List<String> selected_items=new ArrayList<>();
    List<String> user_list2;
    Message message=new Message();
    String id_amphi;
    EditText txt_search_group;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_group_settings, container, false);

        list_view=root.findViewById(R.id.list_view);
        txt_name_group=root.findViewById(R.id.txt_name_group);
        btn_add_members=root.findViewById(R.id.btn_add_members);
        btn_delete_members=root.findViewById(R.id.btn_create_group);
        btn_delete_group=root.findViewById(R.id.btn_delete_group);
        txt_search_group=root.findViewById(R.id.txt_search_members);

        Bundle args=getArguments();
        if(args!=null){
            id_group=args.getString("data");
        }

        load_data(0);


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = (String) parent.getItemAtPosition(position);
                String id_member = selected_item.substring(0, selected_item.indexOf("-"));

                if(!selected_items.contains(id_member)){
                    selected_items.add(id_member);
                    Log.e("e", selected_items.toString());
                }else{
                    Iterator<String> iterator=selected_items.iterator();
                    while(iterator.hasNext()){
                        String memberId = iterator.next();
                        if(memberId.equals(id_member)){
                            iterator.remove();
                            break;
                        }
                    }
                }
                Log.e("e", selected_items.toString());
            }
        });

        btn_delete_members.setOnClickListener(act->{
            Log.e("id_amphi",id_amphi);
            if(Integer.parseInt(id_amphi)!=Data.getId_user()){
                message.message("Permiso denegado", "Solo el anfitrion puede eliminar", getContext());
            }else{
                if(selected_items.toArray().length>0){
                    show_message();
                }else{
                    message.message("Alerta", "Selecciona al menos un integrante", getContext());
                }
            }
        });

        btn_delete_group.setOnClickListener(act->{
            Log.e("id_amphi",id_amphi);
            if(Integer.parseInt(id_amphi)!=Data.getId_user()){
                message.message("Permiso denegado", "Solo el anfitrion puede eliminar", getContext());
            }else{
                show_message1();
            }
        });

        btn_add_members.setOnClickListener(act->{
            FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            Fragment_add_member fragment=new Fragment_add_member();
            Bundle args1=new Bundle();
            args1.putString("data", id_group);
            fragment.setArguments(args1);
            fragmentTransaction.replace(R.id.frameContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        txt_search_group.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
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
            String parteBuscada = txt_search_group.getText().toString();

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

                list_view.setAdapter(adapter);
            }else{
                List<String> list = new ArrayList<>();
                adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                list_view.setAdapter(adapter);
            }
        }else{
            adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, user_list2);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            list_view.setAdapter(adapter);
        }
    }

    private void show_message1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¿Seguro que deseas eliminar este grupo?");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete_group();
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

    private void delete_group(){
        String url=Rest_api.url_mysql+Rest_api.delete_group;
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameContainer, new Fragment_groups());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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
                parameters.put("id_group", id_group);

                return parameters;
            }
        };;

        queue.add(request2);
    }

    private void show_message(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¿Seguro que lo deseas eliminar de tu grupo?");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int j=0; j<selected_items.toArray().length ;j++){
                    delete_members(selected_items.get(j));
                }
                load_data(23);
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

    private void delete_members(String id_member){
        String url=Rest_api.url_mysql+Rest_api.delete_member;
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){

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
                parameters.put("id_member", id_member);

                return parameters;
            }
        };;

        queue.add(request2);
    }

    private void load_data(int index){
        if(index==0){
            load_amphitryon();
        }

        String url= Rest_api.url_mysql+Rest_api.select_members+"?id_user="+Data.getId_user()+"&id_group="+id_group;
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);

                            for(int i=0; i<json_users.length(); i++){
                                JSONObject users_object=json_users.getJSONObject(i);

                                String id=users_object.getString("id_member");
                                String name=users_object.getString("name_user");
                                String account=users_object.getString("account");
                                String email=users_object.getString("email");
                                String user=id+"-"+name+"-"+account+"-"+email;

                                user_list2 = new ArrayList<>();
                                if(Integer.parseInt(id)!=Data.getId_user()){
                                    user_list2.add(user);
                                }
                            }

                            adapter = new ArrayAdapter<>(getContext(), R.layout.list_check, user_list2);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            list_view.setAdapter(adapter);

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

    private void load_amphitryon(){
        String url= Rest_api.url_mysql+Rest_api.select_groups_chat+"?id_group="+id_group;
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);

                            if(json_users.length()>0){
                                JSONObject users_object=json_users.getJSONObject(0);

                                String id=users_object.getString("id_amphitryon");
                                String name=users_object.getString("name_user");
                                String account=users_object.getString("account");
                                String email=users_object.getString("email");
                                String user=id+"-"+name+"-"+account+"-"+email;
                                txt_name_group.setText(users_object.getString("group_name"));
                                id_amphi=id;

                                if(Integer.parseInt(users_object.getString("id_user"))!=Data.getId_user()){
                                    user_list2.add(user);
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


}