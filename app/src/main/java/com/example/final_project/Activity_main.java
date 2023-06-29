package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.final_project.Fragment.Fragment_add_remove_companions;
import com.example.final_project.Fragment.Fragment_groups;
import com.example.final_project.Fragment.Fragment_home;
import com.example.final_project.Fragment.Fragment_settings_profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Activity_main extends AppCompatActivity {
    BottomNavigationView button_nav;//boton de navegar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_nav = findViewById(R.id.buttonNavigationContainer);

        //Manda a llamar el fragment_home
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new Fragment_home()).commit();
        button_nav.setSelectedItemId(R.id.itemHome);

        button_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                if(item.getItemId() == R.id.itemHome){
                    fragment=new Fragment_home();
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
                                if(item.getItemId()==R.id.itemExitToApp){
                                    Intent welcome=new Intent(getApplicationContext(), Activity_welcome.class);
                                    startActivity(welcome);
                                }
                            }
                        }
                    }
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
                item.setChecked(true);
                return false;
            }
        });
    }
}