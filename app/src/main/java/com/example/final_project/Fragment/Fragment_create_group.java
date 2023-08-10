package com.example.final_project.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
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
import android.widget.Toast;
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
import com.example.final_project.Settings.Validation_field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_create_group#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_create_group extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_create_group() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_create_group.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_create_group newInstance(String param1, String param2) {
        Fragment_create_group fragment = new Fragment_create_group();
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

    Button btn_create_group;
    EditText txt_name_group;
    ListView list_users_group;
    List<String> user_list2;
    Message message=new Message();
    List<String> selected_items=new ArrayList<>();
    EditText txt_search_group;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_create_group, container, false);

        btn_create_group=root.findViewById(R.id.btn_create_group);
        txt_name_group=root.findViewById(R.id.txt_name_group);
        list_users_group=root.findViewById(R.id.list_users_group);
        txt_search_group=root.findViewById(R.id.txt_search_members);
        select_companions();

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

        list_users_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        btn_create_group.setOnClickListener(v -> {
            if(selected_items.toArray().length>0){
                if(!txt_name_group.getText().toString().isEmpty()){
                    if(Validation_field.isValidNameText(txt_name_group.getText().toString())){
                        insert_group();
                    }else{
                        message.message("Alerta", "Caracteres invalidos en nombre de grupo", getContext());
                    }

                }else{
                    message.message("Alerta", "No dejar vacio el nombre del grupo", getContext());
                }
            }else{
                message.message("Alerta", "Selecciona al menos un integrante", getContext());
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
                adapter = new ArrayAdapter<>(getContext(), R.layout.list_check, list_copy);
            }else{
                List<String> list = new ArrayList<>();
                adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, list);
            }
        }else{
            adapter = new ArrayAdapter<>(getContext(), R.layout.list_check, user_list2);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_users_group.setAdapter(adapter);
    }

    public void insert_group(){
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        String url=Rest_api.url_mysql+Rest_api.insert_group;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            if(jsonArray.length()>0){
                                JSONObject user_object=jsonArray.getJSONObject(0);//user_object=objeto usuario

                                for(int j=0; j<selected_items.toArray().length; j++){
                                    insert_member(user_object.getString("id_group"), selected_items.get(j));
                                }

                                FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                Fragment_view_group fragment_view_group=new Fragment_view_group();
                                fragmentTransaction.replace(R.id.frameContainer, fragment_view_group);
                                Bundle args=new Bundle();
                                args.putString("data", user_object.getString("id_group")+"-"+txt_name_group.getText().toString());
                                fragment_view_group.setArguments(args);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }

                        }catch(JSONException e){
                            message.message("Error", "datos "+e, getContext());
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("Volley Error", errorMessage);
                        message.message("Error", errorMessage, getContext());
                    }
                }){
            @NonNull
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters=new HashMap<String,String>();//parameters=parametros
                parameters.put("id_amphitryon", String.valueOf(Data.getId_user()));
                parameters.put("group_name", txt_name_group.getText().toString());

                return parameters;
            }
        };

        queue.add(request);
    }

    public void insert_member(String id, String id_user){
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        String url=Rest_api.url_mysql+Rest_api.insert_member;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("Volley Error", errorMessage);
                        message.message("Error", errorMessage, getContext());
                    }
                }) {
            @NonNull
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters=new HashMap<String,String>();//parameters=parametros
                parameters.put("id_user", id_user);
                parameters.put("id_group", id);

                return parameters;
            }
        };

        queue.add(request);
    }

    private void select_companions(){
        String url= Rest_api.url_mysql+Rest_api.select_companions_compare+"?id_user="+Data.getId_user();
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

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
                            }

                            Log.e("dif",""+user_list2);
                            adapter = new ArrayAdapter<>(getContext(), R.layout.list_check, user_list2);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            list_users_group.setAdapter(adapter);

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