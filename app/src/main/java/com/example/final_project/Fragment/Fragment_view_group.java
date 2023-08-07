package com.example.final_project.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Notification;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.final_project.Activity_sign_in;
import com.example.final_project.Activity_welcome;
import com.example.final_project.R;
import com.example.final_project.Settings.Adapter_messages;
import com.example.final_project.Settings.Data;
import com.example.final_project.Settings.DownloadHelper;
import com.example.final_project.Settings.Message;
import com.example.final_project.Settings.Messages_groups;
import com.example.final_project.Settings.Rest_api;
import com.example.final_project.Settings.UriToFileConverter;
import com.example.final_project.Settings.Validation_field;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_view_group#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_view_group extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_view_group() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_view_group.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_view_group newInstance(String param1, String param2) {
        Fragment_view_group fragment = new Fragment_view_group();
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

    TextView btn_exit, txt_name_group_conf, btn_add_members;
    ImageButton btn_take_photo, take_video, btn_other_files, imgbtn_send, btn_cancel;
    EditText txt_name_file2;
    ImageView image_group;
    private Adapter_messages adapter_messages;
    private ImageView image_view_photo, image_send;
    private TextView txt_name_view;
    private RecyclerView recyclerView;
    ScrollView scrollView;
    private FirebaseDatabase database;
    private DatabaseReference database_reference;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    static final int REQUEST_VIDEO_CAPTURE=3;
    private static final int REQUEST_CODE_PICK_DOCUMENT = 4;
    Message message=new Message();
    String url, id_amphi;
    VideoView videoView;
    String id_group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_view_group, container, false);

        btn_exit=root.findViewById(R.id.btn_exit);
        txt_name_group_conf=root.findViewById(R.id.txt_name_group_conf);
        btn_add_members=root.findViewById(R.id.btn_add_members);
        btn_take_photo=root.findViewById(R.id.btn_take_photo);
        take_video=root.findViewById(R.id.take_video);
        btn_other_files=root.findViewById(R.id.btn_other_files);
        image_group=root.findViewById(R.id.image_group);
        image_view_photo=root.findViewById(R.id.image_view_photo);
        txt_name_view=root.findViewById(R.id.txt_name_view);
        recyclerView=root.findViewById(R.id.recyclerView);
        imgbtn_send=root.findViewById(R.id.imgbtn_send);
        txt_name_file2=root.findViewById(R.id.txt_name_file2);
        image_send=root.findViewById(R.id.image_send);
        videoView=root.findViewById(R.id.videoView);
        btn_cancel=root.findViewById(R.id.btn_cancel);
        adapter_messages=new Adapter_messages(getContext());

        LinearLayoutManager layout=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);

        adapter_messages.setOnItemClickListener(new Adapter_messages.OnItemClickListener() {
            @Override
            public void onItemButtonClick(int position, List<Messages_groups> list_message){
                showMenu(position,list_message);
            }
        });

        recyclerView.setAdapter(adapter_messages);

        Bundle args=getArguments();
        if(args!=null){
            String data=args.getString("data");
            int index=data.indexOf("-");
            txt_name_group_conf.setText(data.substring((index+1), data.length()));
            database=FirebaseDatabase.getInstance();
            id_group=data.substring(0, index);
            database_reference=database.getReference(txt_name_group_conf.getText().toString()+"_"+data.substring(0, index));
            select_group_amphitryon();
        }

        database_reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages_groups messages_groups=snapshot.getValue(Messages_groups.class);
                adapter_messages.add_message(messages_groups);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String deletedMessageKey = snapshot.getKey();
                Log.e("snapshot.getKey()", "snapshot.getKey()" + snapshot.getValue()+" "+snapshot.getKey());
                Log.e("Adapter_messages", "onChildRemoved: DataSnapshot - " + snapshot.getValue());
                int position = adapter_messages.findPositionByKey(deletedMessageKey);
                if (position != -1) {
                    adapter_messages.removeMessage(position);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgbtn_send.setOnClickListener(act->{
            if(url!=null && !txt_name_file2.getText().toString().equals("")){
                if(Validation_field.isValidNameText(txt_name_file2.getText().toString())){
                    new_data();
                    Toast.makeText(getContext(), "Se ha enviado con exito", Toast.LENGTH_LONG).show();
                }else{
                    message.message("Alerta", "Caracteres no permitidos, revisa nuestro manual de usuario", getContext());
                }
            }else{
                Toast.makeText(getContext(), "Se requiere algun archivo y tambien nombre del archivo", Toast.LENGTH_LONG).show();
            }
        });

        adapter_messages.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                set_scroll();
            }
        });

        btn_exit.setOnClickListener(act->{
            options(0);
        });

        txt_name_group_conf.setOnClickListener(act->{
            options(1);
        });

        image_group.setOnClickListener(act->{
            options(1);
        });

        btn_add_members.setOnClickListener(act->{
            options(2);
        });

        btn_take_photo.setOnClickListener(act->{
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
        });

        take_video.setOnClickListener(act->{
            permisos();
        });

        btn_other_files.setOnClickListener(act->{
            showDocumentPicker();
        });

        btn_other_files.setOnClickListener(act->{
            showDocumentPicker();
        });

        btn_cancel.setOnClickListener(act->{
            image_send.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            txt_name_file2.setText("");
            url=null;
        });

        return root;
    }

    private void showMenu(int position, List<Messages_groups> list_message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elija una opción");
        builder.setItems(new CharSequence[]{"Descargar archivo", "Eliminar mensaje", "Cancelar"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                DownloadHelper.downloadFile(getContext(), list_message.get(position).getImage_file(), "my_downloaded_image"+list_message.get(position).getExtension());
                                Toast.makeText(getContext(), "Se ha descargado con exito", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                if(Integer.parseInt(list_message.get(position).getId_user())==Data.getId_user()){
                                    String mensajeId = list_message.get(position).getId();
                                    DatabaseReference mensajeRef = database_reference.child(mensajeId);
                                    mensajeRef.removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                Toast.makeText(getActivity(), "Mensaje eliminado correctamente", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Error al eliminar el mensaje", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    message.message("Solicitud denegada","No puedes borrar un archivo que no lo mandaste tu", getContext());
                                }

                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void showDocumentPicker(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_DOCUMENT);
    }

    private class NetworkTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            int imageSize = 0;
            try {
                URL url2 = new URL(params[0]);
                URLConnection connection = url2.openConnection();
                imageSize = connection.getContentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return imageSize;
        }

        @Override
        protected void onPostExecute(Integer imageSize) {
            int lastSlashIndex = url.lastIndexOf("/");
            String fileNameAndExtension = url.substring(lastSlashIndex + 1);
            int lastDotIndex = fileNameAndExtension.lastIndexOf(".");
            String fileExtension = fileNameAndExtension.substring(lastDotIndex);
            int index=fileExtension.indexOf("?");
            String ext=fileExtension.substring(0, index);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("America/Tegucigalpa"));
            String dateTimeString = sdf.format(date);

            DatabaseReference newMessageRef = database_reference.push();
            String messageId = newMessageRef.getKey();
            Messages_groups message = new Messages_groups(Data.getName(), dateTimeString, txt_name_file2.getText().toString(), imageSize + "KB", ext, Data.getPhoto(), url, messageId, String.valueOf(Data.getId_user()));

            newMessageRef.setValue(message, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        image_send.setVisibility(View.GONE);
                        videoView.setVisibility(View.GONE);
                        url = null;
                        txt_name_file2.setText("");
                        select_token_members();
                        select_group_amphitryon();
                    }else{
                        Toast.makeText(getActivity(), "Error al guardar el mensaje", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void select_group_amphitryon(){
        String url= Rest_api.url_mysql+Rest_api.select_groups_chat+"?id_group="+id_group;
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            if(jsonArray.length()>0){
                                JSONObject career_object=jsonArray.getJSONObject(0);//career_object=objeto carrera
                                id_amphi=career_object.getString("id_amphitryon");

                                if(Data.getId_user()!=Integer.parseInt(career_object.getString("id_amphitryon"))){
                                    send_message(career_object.getString("token"));
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

    private void select_token_members(){
        String url= Rest_api.url_mysql+Rest_api.select_members+"?id_user="+Data.getId_user()+"&id_group="+id_group;
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

        StringRequest request2=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray jsonArray=new JSONArray(response);

                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject career_object=jsonArray.getJSONObject(i);//career_object=objeto carrera
                                String token=career_object.getString("token");

                                send_message(token);
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

    private void send_message(String token){
        String url= Rest_api.url_mysql+Rest_api.send_message;
        RequestQueue queue= Volley.newRequestQueue(getContext());//queue=cola

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
                    }
                }) {
            @NonNull
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters=new HashMap<String,String>();//parameters=parametros
                parameters.put("token", token);
                parameters.put("name_group",txt_name_group_conf.getText().toString());

                return parameters;
            }
        };

        queue.add(request);
    }

    private void new_data(){
        new NetworkTask().execute(url);
    }

    private void set_scroll(){
        recyclerView.scrollToPosition(adapter_messages.getItemCount());
    }

    private void permisos(){
        if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA},REQUEST_VIDEO_CAPTURE);
        }else{
            dispatchTakeVideoIntent();
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        image_send.setVisibility(View.GONE);
    }

    private void options(int i){
        FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        if(i==0){
            fragmentTransaction.replace(R.id.frameContainer, new Fragment_groups());
        }else{
            if(i==1){
                Fragment_group_settings fragment=new Fragment_group_settings();
                Bundle args=new Bundle();
                args.putString("data", id_group);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frameContainer, fragment);
            }else{
                if(i==2){
                    if(Integer.parseInt(id_amphi)!=Data.getId_user()){
                        message.message("Permiso denegado", "Solo el anfitrion puede agregar", getContext());
                    }else{
                        Fragment_add_member fragment=new Fragment_add_member();
                        Bundle args=new Bundle();
                        args.putString("data", id_group);
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.frameContainer, fragment);
                    }
                }
            }
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

                case REQUEST_VIDEO_CAPTURE:
                    if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
                        Uri videoUri = data.getData();

                        videoView.setVisibility(View.VISIBLE);
                        videoView.setVideoURI(videoUri);
                        MediaController mediaController = new MediaController(getContext());
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                        videoView.start();
                        String fileType=getMimeType(getContext(), videoUri);

                        try{
                            File file= UriToFileConverter.convertUriToFile(getContext(), videoUri, fileType);
                            uploadFileToFirebase(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                break;

                case REQUEST_CODE_PICK_DOCUMENT:
                    if(requestCode == REQUEST_CODE_PICK_DOCUMENT && resultCode == Activity.RESULT_OK){
                        if(data != null){
                            Uri uri=data.getData();
                            File file=null;

                            String fileType=(getMimeType(getContext(), uri));
                            try{
                                file=UriToFileConverter.convertUriToFile(getContext(), uri, fileType);
                                uploadFileToFirebase(file);
                            }catch(IOException e){
                                throw new RuntimeException(e);
                            }
                            image_send.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.GONE);
                            Glide.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/final-project-d3437.appspot.com/o/Message_files%2Ffile.png?alt=media&token=fbad2126-e500-4576-bfce-372efce40636").diskCacheStrategy(DiskCacheStrategy.ALL).into(image_send);
                        }
                    }
                    break;

            }
            message.message("Exitoso", "Archivo cargado con exito", getContext());
        }
    }

    public static String getMimeType(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return "."+mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImageToFirebase(Bitmap imageBitmap, Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("Message_files/"+generate_name()+"_"+Data.getId_user()+".jpg");

        if(imageBitmap != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(exception -> {
                Log.e("error", ""+exception);
                message.message("Error", "Error al "+exception, getContext());
            }).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    url=String.valueOf(uri);
                    image_send.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(image_send);
                });
            });
        } else if (imageUri != null) {
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnFailureListener(exception -> {
                Log.e("error", ""+exception);
                message.message("Error", "Error al "+exception, getContext());
            }).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    url=String.valueOf(uri);
                    image_send.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(image_send);
                });
            });
        }
    }

    private void uploadFileToFirebase(File file) throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String fileName = generate_name() + "_" + Data.getId_user() + "_" + file.getName();
        StorageReference fileRef = storageRef.child("Message_files/" + fileName);

        if (file != null && file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fileInputStream.read(data);

            UploadTask uploadTask = fileRef.putBytes(data);
            uploadTask.addOnFailureListener(exception -> {
                Log.e("error", "" + exception);
                message.message("Error", "Error al "+exception, getContext());
            }).addOnSuccessListener(taskSnapshot -> {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    url = String.valueOf(uri);
                });
            });
        } else {
            Log.e("error", "Archivo inválido o no encontrado");
            message.message("Error", "Archivo inválido o no encontrado", getContext());
        }
    }

    private String generate_name(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        String dateTimeString = sdf.format(date);

        return dateTimeString;
    }
}