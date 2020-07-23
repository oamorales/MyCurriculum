package com.oamorales.myresume.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.oamorales.myresume.R;

public class LanguagesActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private NavigationView navView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);
        configureToolbar();
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navigationView);
        //navView.getMenu().findItem(R.id.languagesMenu).setChecked(true);
        //navView.setNavigationItemSelectedListener(this);
    }

    private void configureToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.languages);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_lateral_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


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
            case R.id.degreesFragment:
                changeActivity(this, DegreesActivity.class);
                break;
            case R.id.workExpMenu:
                changeActivity(this,WorkExpActivity.class);
                break;
            case R.id.languagesMenu:
                drawerLayout.closeDrawers();
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

    private void changeActivity(Context context, Class<?> anyClass){
        drawerLayout.closeDrawers();
        Intent i = new Intent(context,anyClass);
        startActivity(i);
    }

}
