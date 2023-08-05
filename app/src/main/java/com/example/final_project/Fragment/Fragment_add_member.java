package com.example.final_project.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_add_member#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_add_member extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_add_member() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_add_member.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_add_member newInstance(String param1, String param2) {
        Fragment_add_member fragment = new Fragment_add_member();
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

    String id_group;
    Button btn_add_members1;
    ListView list_add_members;
    Message message=new Message();
    List<String> user_list2;
    List<String> selected_items=new ArrayList<>();
    EditText txt_search_members;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_add_member, container, false);

        Bundle args=getArguments();
        if(args!=null){
            id_group=args.getString("data");
        }

        btn_add_members1=root.findViewById(R.id.btn_add_members1);
        list_add_members=root.findViewById(R.id.list_add_members);
        txt_search_members=root.findViewById(R.id.txt_search_members);
        select_companions();

        btn_add_members1.setOnClickListener(act->{

        });

        list_add_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        txt_search_members.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    private void select_companions(){
        String url= Rest_api.url_mysql+Rest_api.select_companions_compare+"?id_user="+ Data.getId_user();
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

                            Log.e("dif",""+user_list2);
                            adapter = new ArrayAdapter<>(getContext(), R.layout.list_check, user_list2);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            list_add_members.setAdapter(adapter);
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
                                select_insert_group(id, user);
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

    private void select_insert_group(String id, String user){
        String url=Rest_api.url_mysql+Rest_api.select_insert_group+"?id_user="+id+"&id_group="+id_group;
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);

                            if(json_users.length()>0){
                                Log.e("ya insertado","ya insertado");
                            }else{
                                user_list2.add(user);
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