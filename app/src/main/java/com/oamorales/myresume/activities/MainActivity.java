package com.oamorales.myresume.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.oamorales.myresume.R;
import com.oamorales.myresume.models.Degree;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    //private AppBarLayout toolbar;
    private MaterialToolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureToolbar();
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navigationView);
        navController = Navigation.findNavController(this, R.id.navHostFragment);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(drawerLayout).build();
        /** Show and manage the drawer and back icon */
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);

        /** Use Navigation View and handle navigation clicks */
        NavigationUI.setupWithNavController(navView,navController);

        /** Abrir drawer al iniciar activity */
        //NavigationUI.navigateUp(navController,drawerLayout);
        /** Mostrar el fragment de acuerdo al item del menú indicado */
        //NavigationUI.onNavDestinationSelected(navView.getMenu().getItem(2),navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() != R.id.generalDataFragment){
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }
        });
    }

    private void configureToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_lateral_menu);
        //Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_lateral_menu);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /** Habilitar función del botón de Navegación. Open Drawer */
    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();

        return NavigationUI.navigateUp(navController,drawerLayout);
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
        public void onBackPressed() {
            //super.onBackPressed();
        drawerLayout.closeDrawers();
        *//*if (getSupportFragmentManager().findFragmentById(R.id.personInfoFragment).getView() == null){
            Toast.makeText(this, "VA A SALIR", Toast.LENGTH_SHORT).show();
        }*//*
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Confirmar Salida")
                    .setMessage("¿Desea salir de la aplicación?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finishAndRemoveTask();
                            finish();
                        }
                    })
                    .setNegativeButton("No",null);
            dialog.show();
        }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        navView.getMenu().getItem(0).setChecked(true);
    }*/
}
