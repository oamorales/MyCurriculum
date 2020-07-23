package com.oamorales.myresume.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.oamorales.myresume.R;

public class DegreesActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degrees);
        drawerLayout = findViewById(R.id.drawerLayout);
        configureToolbar();
        //navView = findViewById(R.id.navigationView);
        //navView.getMenu().findItem(R.id.degreesMenu).setChecked(true);
        fab = findViewById(R.id.addDegreeFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //navView.setNavigationItemSelectedListener(this);

    }

    private void configureToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.degrees);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.personInfoMenu:
                drawerLayout.closeDrawers();
                i = new Intent(this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            case R.id.degreesMenu:
                drawerLayout.closeDrawers();
                break;
            case R.id.workExpMenu:
                changeActivity(this,WorkExpActivity.class);
                break;
            case R.id.languagesMenu:
                changeActivity(this,LanguagesActivity.class);
                break;
            case R.id.skillsMenu:
                changeActivity(this,SkillsActivity.class);
                break;
        }
        return true;
    }*/

    /*@Override
    public void onBackPressed() {
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }*/

    private void changeActivity(Context context,Class<?> anyClass){
        drawerLayout.closeDrawers();
        Intent i = new Intent(context,anyClass);
        startActivity(i);
    }

}
