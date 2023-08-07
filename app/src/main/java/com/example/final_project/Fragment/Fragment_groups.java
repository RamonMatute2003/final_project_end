package com.example.final_project.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_groups#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_groups extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_groups() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_groups.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_groups newInstance(String param1, String param2) {
        Fragment_groups fragment = new Fragment_groups();
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

    TextView btn_create_group_link;
    ListView list_my_groups;
    Message message=new Message();
    List<String> group_list2;
    EditText txt_search_group;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_groups, container, false);
        btn_create_group_link=root.findViewById(R.id.btn_add_members);
        list_my_groups=root.findViewById(R.id.list_my_groups);
        txt_search_group=root.findViewById(R.id.txt_search_members);

        btn_create_group_link.setOnClickListener(v->{
            options(1);
        });

        list_my_groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                Fragment_view_group fragment_view_group=new Fragment_view_group();
                fragmentTransaction.replace(R.id.frameContainer, fragment_view_group);
                Bundle args=new Bundle();
                args.putString("data", parent.getItemAtPosition(position).toString());
                fragment_view_group.setArguments(args);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        select_groups_amphitryon();

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

        for(int j=0; j<group_list2.toArray().length; j++){
            boolean search=false;
            String cadenaCompleta = group_list2.get(j);
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
            }else{
                List<String> list = new ArrayList<>();
                adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, list);
            }
        }else{
            adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, group_list2);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_my_groups.setAdapter(adapter);
    }

    private void options(int i){
        FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        if(i==1){
            fragmentTransaction.replace(R.id.frameContainer, new Fragment_create_group());
        }else{

        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void select_groups(){
        String url= Rest_api.url_mysql+Rest_api.select_id_group+"?id_user="+Data.getId_user();
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);

                            for(int j=0; j<json_users.length(); j++){
                                JSONObject users_object=json_users.getJSONObject(j);

                                String id=users_object.getString("id_group");
                                String name=users_object.getString("group_name");
                                String user=id+"-"+name;
                                group_list2.add(user);
                            }

                            adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_layout, group_list2);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            list_my_groups.setAdapter(adapter);

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

    private void select_groups_amphitryon(){
        String url=Rest_api.url_mysql+Rest_api.select_group_amphitryon+"?id_user="+Data.getId_user();
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);
                            group_list2 =new ArrayList<>();

                            for(int j=0; j<json_users.length(); j++){
                                JSONObject users_object=json_users.getJSONObject(j);

                                String id=users_object.getString("id_group");
                                String name=users_object.getString("group_name");
                                String group=id+"-"+name;
                                group_list2.add(group);
                            }

                            select_groups();

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