package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.final_project.Fragment.Fragment_home;
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
                    fragment = new Fragment_home();
                }else{
                    Toast.makeText(getApplicationContext(), "Item Selccionado: " + item.getItemId(), Toast.LENGTH_LONG).show();
                    fragment = new Fragment_home();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
                item.setChecked(true);
                return false;
            }
        });
    }
}