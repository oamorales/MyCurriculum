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
    private Realm realm;
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


        /** PARAMETROS INICIALES BD */
        realm = Realm.getDefaultInstance();
        Degree usbDegree = new Degree(R.drawable.usb_logo,"T.S.U en Electrónica","USB",
                "TECNOLOGIA", 2004, 2008, 5);
        Degree iutomsDegree = new Degree(R.drawable.iutoms_logo,"Ingeniero en Informática", "IUTOMS",
                "TECNOLOGIA", 2010, 2013, 19);
        Degree usbDegree2 = new Degree(R.drawable.usb_logo,"T.S.U en Administración","USB",
                "TECNOLOGIA", 2008, 2010, 4);
        Degree iutomsDegree2 = new Degree(R.drawable.iutoms_logo,"Ingeniero Eléctrico", "IUTOMS",
                "TECNOLOGIA", 2013, 2015, 18);
        realm.beginTransaction();
        realm.deleteAll();
        realm.copyToRealmOrUpdate(usbDegree);
        realm.copyToRealmOrUpdate(iutomsDegree);
        realm.copyToRealmOrUpdate(usbDegree2);
        realm.copyToRealmOrUpdate(iutomsDegree2);
        realm.commitTransaction();

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

    /*private void changeFragment(Fragment fragment, MenuItem item){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout,fragment)
                .addToBackStack(null).setReorderingAllowed(true)
                .commit();
        getSupportActionBar().setTitle(item.getTitle());
        item.setChecked(true);
    }*/

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        boolean fragmentTransaction = false;
        Intent i;
        NavDirections action;
        switch (item.getItemId()){
            case R.id.personInfoMenu:
                fragment = new PersonInfoFragment();
                fragmentTransaction = true;
                break;
            case R.id.degreesFragment:
                //fragment = new DegreesFragment();
                //fragmentTransaction = true;
                drawerLayout.closeDrawers();
                *//*i = new Intent(this,DegreesActivity.class);
                startActivity(i);*//*
                action = PersonInfoFragmentDirections.actionPersonInfoFragmentToDegreesFragment();
                Navigation.findNavController(this, R.id.navHostFragment).navigate(action);
                break;
            case R.id.workExpMenu:
                //fragment = new WorkExpFragment();
                //fragmentTransaction = true;
                drawerLayout.closeDrawers();
                *//*action = PersonInfoFragmentDirections.actionPersonInfoFragmentToWorkExpActivity();
                Navigation.findNavController(this, R.id.navHostFragment).navigate(action);*//*
                *//*i = new Intent(this,WorkExpActivity.class);
                startActivity(i);*//*
                break;
            case R.id.languagesMenu:
                //fragment = new LanguagesFragment();
                fragmentTransaction = true;
                drawerLayout.closeDrawers();
                *//*action = PersonInfoFragmentDirections.actionPersonInfoFragmentToLanguagesActivity();
                Navigation.findNavController(this, R.id.navHostFragment).navigate(action);*//*
                //i = new Intent(this,LanguagesActivity.class);
                //startActivity(i);
                break;
            case R.id.skillsMenu:
                fragment = new SkillsFragment();
                //fragmentTransaction = true;
                drawerLayout.closeDrawers();
                *//*action = PersonInfoFragmentDirections.actionPersonInfoFragmentToSkillsActivity();
                Navigation.findNavController(this, R.id.navHostFragment).navigate(action);*//*
                *//*i = new Intent(this,SkillsActivity.class);
                startActivity(i);*//*
                break;
        }
        if (fragmentTransaction){
            //changeFragment(fragment,item);
            drawerLayout.closeDrawers();
        }
        return true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        navView.getMenu().getItem(0).setChecked(true);
    }*/
}
