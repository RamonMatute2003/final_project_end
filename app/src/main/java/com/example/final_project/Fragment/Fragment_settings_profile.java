package com.example.final_project.Fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project.Activity_main;
import com.example.final_project.Activity_recover_account;
import com.example.final_project.Activity_sign_in;
import com.example.final_project.Activity_sign_up;
import com.example.final_project.Activity_verification;
import com.example.final_project.R;
import com.example.final_project.Settings.Data;
import com.example.final_project.Settings.Message;
import com.example.final_project.Settings.Rest_api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_settings_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_settings_profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_settings_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_settings_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_settings_profile newInstance(String param1, String param2) {
        Fragment_settings_profile fragment = new Fragment_settings_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    EditText txt_name1, txt_account1, txt_email2, birthdate1, txt_phone1, txt_dni1;
    Spinner sp_careers2;
    ImageView imageView5;
    TextView btn_delete_photo,btn_edit_photo, txt_friends,btn_edit_friends;
    Button btn_save_changes, btn_discard_changes,btn_update_password;
    ImageButton btn_calendar1;
    Message message=new Message();
    Integer position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_settings_profile, container, false);

        txt_name1=root.findViewById(R.id.txt_name1);
        txt_account1=root.findViewById(R.id.txt_account1);
        txt_email2=root.findViewById(R.id.txt_email2);
        sp_careers2=root.findViewById(R.id.sp_careers2);
        birthdate1=root.findViewById(R.id.birthdate1);
        txt_phone1=root.findViewById(R.id.txt_phone1);
        txt_dni1=root.findViewById(R.id.txt_dni1);
        imageView5=root.findViewById(R.id.imageView5);
        btn_delete_photo=root.findViewById(R.id.btn_delete_photo);
        btn_edit_photo=root.findViewById(R.id.btn_edit_photo);
        txt_friends=root.findViewById(R.id.txt_friends);
        btn_edit_friends=root.findViewById(R.id.btn_edit_friends);
        btn_save_changes=root.findViewById(R.id.btn_save_changes);
        btn_discard_changes=root.findViewById(R.id.btn_discard_changes);
        btn_update_password=root.findViewById(R.id.btn_update_password);
        btn_calendar1=root.findViewById(R.id.btn_calendar1);
        fill_career();
        fill_data();

        btn_calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate_calendar();
            }
        });

        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_security(btn_update_password.getText().toString());
            }
        });

        btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_security(btn_save_changes.getText().toString());
            }
        });

        btn_discard_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_message();
            }
        });

        btn_edit_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_window=new Intent(getContext(), Activity_main.class);
                new_window.putExtra("add_remove",2);
                startActivity(new_window);
            }
        });

        return root;
    }

    private void show_message(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¿Seguro que deseas descartar los cambios?");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fill_data();
                fill_career();
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

    private void
    show_security(String button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ingresa tu contraseña actual");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password=input.getText().toString();

                if(password.equals(Data.getPassword())){
                    Log.e("button",button);
                    if(button.equals("Guardar cambios")){
                        Intent new_window=new Intent(getContext(), Activity_verification.class);
                        new_window.putExtra("activity", 3);
                        new_window.putExtra("name_user", txt_name1.getText().toString());
                        new_window.putExtra("dni", txt_dni1.getText().toString());
                        new_window.putExtra("birth_date", birthdate1.getText().toString());
                        new_window.putExtra("account", txt_account1.getText().toString());
                        new_window.putExtra("phone", txt_phone1.getText().toString());
                        new_window.putExtra("email", txt_email2.getText().toString());
                        int index=(sp_careers2.getSelectedItem().toString()).indexOf("-");
                        new_window.putExtra("id_career", (sp_careers2.getSelectedItem().toString()).substring(0, index));
                        //new_window.putExtra("photo", Data.getEmail());
                        new_window.putExtra("sett_prof",1);
                        startActivity(new_window);
                    }else{
                        if(button.equals("Cambiar contraseña")){
                            update_password();
                        }
                    }

                }else{
                    message.message("Advertencia","Contraseña incorrecta", getContext());
                }
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

    private void update_password(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        builder2.setTitle("Ingresa tu nueva contraseña");

        final EditText input2 = new EditText(getContext());
        input2.setInputType(InputType.TYPE_CLASS_TEXT);
        builder2.setView(input2);

        builder2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which2) {
                String new_password = input2.getText().toString();
                Intent new_window=new Intent(getContext(), Activity_verification.class);
                new_window.putExtra("activity", 2);
                new_window.putExtra("new_password", new_password);
                startActivity(new_window);
            }
        });
        builder2.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog2 = builder2.create();
        dialog2.show();
    }

    private void fill_career(){//fill_career=llenar carreras
        String url= Rest_api.url_mysql+Rest_api.select_careers;
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            String[] careers=new String[jsonArray.length()];
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject career_object=jsonArray.getJSONObject(i);//career_object=objeto carrera
                                String id=career_object.getString("id_career");
                                String name=career_object.getString("career_name");
                                String career=id+"-"+name;
                                Log.e("A",career);
                                careers[i]=career;

                                if((i+1)==Data.getId_career()){
                                    position=i;
                                }
                            }

                            ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, careers);//adapter=adaptador
                            sp_careers2.setAdapter(adapter);
                            sp_careers2.setSelection(position, true);
                            Log.e("A",""+position);
                        }catch(JSONException e){
                            e.printStackTrace();
                            message.message("Error", "Revisa bien: "+e, getContext());
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message.message("Error", "Revisa bien: "+error, getContext());                    }
        });

        queue.add(request);
    }

    private void fill_data(){
        txt_name1.setText(Data.getName());
        txt_account1.setText(Data.getAccount());
        txt_email2.setText(Data.getEmail());
        birthdate1.setText(Data.getBirth_date());
        txt_phone1.setText(Data.getPhone());
        txt_dni1.setText(Data.getDni());
    }

    private void generate_calendar(){//generate_calendar=generar calendario
        Calendar calendar=Calendar.getInstance();

        int current_year=calendar.get(Calendar.YEAR);
        int current_month=calendar.get(Calendar.MONTH)+1;
        int current_day=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {//dialog=dialogo
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String day_format, month_format;//day_format=formato de dia, month_format=formato de mes

                if(dayOfMonth<10){
                    day_format="0"+String.valueOf(dayOfMonth);
                }else{
                    day_format=String.valueOf(dayOfMonth);
                }

                if(month<10){
                    month_format="0"+String.valueOf((month+1));
                }else{
                    month_format=String.valueOf((month+1));
                }

                Data.setBirth_date(year+"-"+month_format+"-"+day_format);
                birthdate1.setText(Data.getBirth_date());
            }
        }, current_year,current_month,current_day);
        dialog.show();
    }
}