package com.example.final_project.Fragment;

import static android.app.Activity.RESULT_OK;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.final_project.Activity_main;
import com.example.final_project.Activity_sign_up;
import com.example.final_project.Activity_verification;
import com.example.final_project.Models.Firebase;
import com.example.final_project.R;
import com.example.final_project.Settings.Data;
import com.example.final_project.Settings.Message;
import com.example.final_project.Settings.Rest_api;
import com.example.final_project.Settings.Validation_field;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    Firebase firebase=new Firebase();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings_profile, container, false);

        txt_name1 = root.findViewById(R.id.txt_name1);
        txt_account1 = root.findViewById(R.id.txt_account1);
        txt_email2 = root.findViewById(R.id.txt_email2);
        sp_careers2 = root.findViewById(R.id.sp_careers2);
        birthdate1 = root.findViewById(R.id.birthdate1);
        txt_phone1 = root.findViewById(R.id.txt_phone1);
        txt_dni1 = root.findViewById(R.id.txt_dni1);
        imageView5 = root.findViewById(R.id.imageView5);
        btn_delete_photo = root.findViewById(R.id.btn_delete_photo);
        btn_edit_photo = root.findViewById(R.id.btn_edit_photo);
        txt_friends = root.findViewById(R.id.txt_friends);
        btn_edit_friends = root.findViewById(R.id.btn_edit_friends);
        btn_save_changes = root.findViewById(R.id.btn_save_changes);
        btn_discard_changes = root.findViewById(R.id.btn_discard_changes);
        btn_update_password = root.findViewById(R.id.btn_update_password);
        btn_calendar1 = root.findViewById(R.id.btn_calendar1);
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
            public void onClick(View v){
                if(txt_name1.getText().toString().isEmpty() || txt_email2.getText().toString().isEmpty() || txt_phone1.getText().toString().isEmpty() || txt_dni1.getText().toString().isEmpty() || birthdate1.getText().toString().isEmpty()){
                    message.message("Alerta", "No dejar campos vacios",getContext());
                }else{
                    if(Validation_field.isValidName(txt_name1.getText().toString())){
                        if(Validation_field.isValidEmail(txt_email2.getText().toString())){
                            if(Validation_field.isValidPhoneNumber(txt_phone1.getText().toString())){
                                if(Validation_field.isValidDni(txt_dni1.getText().toString())){
                                    if(Validation_field.isValidBirthdate(birthdate1.getText().toString())){
                                        show_security(btn_save_changes.getText().toString());
                                    }else{
                                        message.message("Alerta", "Fecha no es valida, revisa nuestro manual de usuario", getContext());
                                    }
                                }else{
                                    message.message("Alerta", "Caracteres incorrectos en DNI, revisa nuestro manual de usuario",getContext());
                                }
                            }else{
                                message.message("Alerta", "Caracteres incorrectos en Telefono, revisa nuestro manual de usuario",getContext());
                            }
                        }else{
                            message.message("Alerta", "Caracteres incorrectos en correo electronico, revisa nuestro manual de usuario",getContext());
                        }
                    }else{
                        message.message("Alerta", "Caracteres incorrectos en nombre, revisa nuestro manual de usuario",getContext());
                    }
                }
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
                Intent new_window = new Intent(getContext(), Activity_main.class);
                new_window.putExtra("add_remove", 2);
                startActivity(new_window);
            }
        });

        btn_edit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog();
            }
        });

        btn_delete_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_photo();
            }
        });

        firebase.get_token("Profile_pictures");
        select_count_companions();

        return root;
    }

    private void select_count_companions(){
        String url=Rest_api.url_mysql+Rest_api.select_count_companions+"?id_user="+Data.getId_user();
        RequestQueue queue=Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray json_users=new JSONArray(response);

                            if(json_users.length()>0){
                                JSONObject users_object=json_users.getJSONObject(0);

                                txt_friends.setText("Amigos: "+users_object.getString("counter"));
                            }else{
                                txt_friends.setText("Amigos: 0");
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

    private void showMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elija una opción");
        builder.setItems(new CharSequence[]{"Tomar foto", "Elegir de galería"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{android.Manifest.permission.CAMERA},
                                            CAMERA_PERMISSION_REQUEST_CODE);
                                } else {
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }
                                break;
                            case 1:
                                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_FROM_GALLERY);
                                break;
                        }
                    }
                });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    uploadImageToFirebase(bitmap, null);
                    break;
                case REQUEST_IMAGE_FROM_GALLERY:
                    Uri selectedImageUri = data.getData();
                    try {
                        Bitmap galleryBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                        uploadImageToFirebase(galleryBitmap, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void show_message(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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

    private void show_security(String button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Ingresa tu contraseña actual");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
        AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext());
        builder2.setTitle("Ingresa tu nueva contraseña");

        final EditText input2 = new EditText(getContext());
        input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder2.setView(input2);

        builder2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which2) {
                String new_password = input2.getText().toString();
                if(Validation_field.isValidPassword(new_password)){
                    Intent new_window=new Intent(getContext(), Activity_verification.class);
                    new_window.putExtra("activity", 2);
                    new_window.putExtra("new_password", new_password);
                    startActivity(new_window);
                }else{
                    update_password();
                    Toast.makeText(getContext(),"Caracteres no permitidos en contraseña, revisa nuestro manual de usuario", Toast.LENGTH_LONG).show();
                }
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
        RequestQueue queue= Volley.newRequestQueue(requireContext());//queue=cola

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
        Glide.with(this).load(Data.getPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView5);
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

    private void uploadImageToFirebase(Bitmap imageBitmap, Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("Profile_pictures/photo_user_"+Data.getId_user()+".jpg");

        if (imageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(exception -> {
               Log.e("error", ""+exception);
            }).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    update_photo(uri);
                    Data.setPhoto(String.valueOf(uri));
                    Glide.with(this).load(Data.getPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView5);
                });
            });
        } else if (imageUri != null) {
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnFailureListener(exception -> {
                Log.e("error", ""+exception);
            }).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    update_photo(uri);
                    Data.setPhoto(String.valueOf(uri));
                    Glide.with(this).load(Data.getPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView5);
                });
            });
        }
    }

    private void update_photo(Uri uri){
        RequestQueue queue=Volley.newRequestQueue(requireContext());//queue=cola

        String url= Rest_api.url_mysql+Rest_api.update_photo;
        StringRequest request=new StringRequest(Request.Method.POST, url,//request=peticion
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Data.setPhoto(String.valueOf(uri));
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
                parameters.put("photo", String.valueOf(uri));

                return parameters;
            }
        };

        queue.add(request);
    }

    private void delete_photo(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext());
        builder2.setTitle("¿Deseas borrar tu foto?");

        builder2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog2, int which2) {
                update_photo(Uri.parse("https://firebasestorage.googleapis.com/v0/b/final-project-d3437.appspot.com/o/Profile_pictures%2Fsin_foto.jpg?alt=media&token=87e00569-a4d5-41be-adcb-7e1a443ce40f"));
                Glide.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/final-project-d3437.appspot.com/o/Profile_pictures%2Fsin_foto.jpg?alt=media&token=87e00569-a4d5-41be-adcb-7e1a443ce40f").diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView5);
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

}