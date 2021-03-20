package com.oamorales.myresume.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.PageSize;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.oamorales.myresume.R;
import com.oamorales.myresume.utils.HtmlToPdf;
import com.oamorales.myresume.viewmodels.GeneralDataViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1004;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private NavController navController;
    //private AppBarConfiguration appBarConfiguration;
    private MaterialToolbar toolbar;
    private Realm realm;
    private String currentImgPath;
    private GeneralDataViewModel generalDataViewModel;
    private final String KEY = "IMG_PATH";
    private final String PACKAGE_NAME = "package:com.oamorales.mycurriculum";
    //private final String FOLDER_NAME = getString(R.string.folder_name);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        currentImgPath = sharedPref.getString(KEY, null);
        configureGeneralDataViewModel();

        configureToolbar();
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navigationView);
        navController = Navigation.findNavController(this, R.id.navHostFragment);

        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawerLayout).build();
        /** Show and manage the drawer and back icon */
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);

        /** Use Navigation View and handle navigation clicks */
        NavigationUI.setupWithNavController(navView,navController);
    }

    private void configureGeneralDataViewModel() {
        generalDataViewModel = new ViewModelProvider(this).get(GeneralDataViewModel.class);
        /*generalDataViewModel.init();
        generalDataViewModel.getImgPath().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null)
                currentImgPath = s;
            }
        });*/
    }

    private void configureToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /** Habilitar función del botón de Navegación. Open Drawer */
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,drawerLayout);
    }

    /** Métodos para el menu del toolbar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.generatePdf){
            openMediaStore();
        }else {
            NavigationUI.navigateUp(navController,drawerLayout);
        }
        return true;
    }


    private void openMediaStore(){
        if (!isExternalStorageWritable()){
            Toast.makeText(this, getString(R.string.memory_access_denied), Toast.LENGTH_SHORT).show();
        }else{
            if (haveStoragePermission()){
                createPDFFile();
            }else{
                requestPermission();
            }
        }
    }

    /** Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /** Check if permission granted */
    private boolean haveStoragePermission(){
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /** Request permission */
    private void requestPermission(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createPDFFile();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (showRationale) {
                    Snackbar.make(toolbar, getString(R.string.rationale_file_message), Snackbar.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.grant_permission))
                            .setMessage(getString(R.string.grant_file_permission_message)).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goToSettings();
                        }
                    }).setNegativeButton(getString(R.string.no), null)
                            .show();
                }
            }
        }
    }

    /** Displays this app settings menu */
    private void goToSettings(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(PACKAGE_NAME));
        intent.addCategory(Intent.CATEGORY_DEFAULT).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /** Generate PDF File */
    private void createPDFFile(){
        if (currentImgPath == null){
            currentImgPath = generalDataViewModel.getImgPath().getValue();
            if (currentImgPath == null){
                Toast.makeText(this, getString(R.string.person_image_missing), Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            currentImgPath = sharedPref.getString(KEY, null);
        }

        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.LETTER);
        String fileName = getString(R.string.pdf_file_name);
            File file = createFile(fileName);
            FileOutputStream ficheroPDF = null;
            try {
                ficheroPDF = new FileOutputStream(file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                Toast.makeText(this, "FileOutputStream Error:  "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            /** Se asocia el flujo al documento */
            com.itextpdf.text.pdf.PdfWriter pdfWriter = null;
            try {
                pdfWriter = com.itextpdf.text.pdf.PdfWriter.getInstance(document, ficheroPDF);
            } catch (com.itextpdf.text.DocumentException e) {
                //e.printStackTrace();
                Toast.makeText(this, "PDFWriter instance Error:  "+e.getMessage(), Toast.LENGTH_LONG).show();
            }

        /** Se modifica el documento */
        document.open();
        document.setMargins(3,3,3,3);

        /** XML writer */
        XMLWorkerHelper workerHelper = XMLWorkerHelper.getInstance();
        String htmlToPdf = HtmlToPdf.getHtml(this, realm, currentImgPath);
        if (htmlToPdf == null)
            return;
        try {
            workerHelper.parseXHtml(pdfWriter, document, new StringReader(htmlToPdf));
        } catch (IOException e) {
            //e.printStackTrace();
            Toast.makeText(this, "XMLWorkerHelper Error:  "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        document.close();
        String message = getString(R.string.pdf_file_generated) + "Documents"+ getString(R.string.folder_name);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private File createFile(String fileName) {
        File path = getPath();
        File file = null;
        if (path != null){
            file = new File(path, fileName);
        }
        return file;
    }

    private File getPath() {
        File path = null;
        String mainPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ getString(R.string.folder_name);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path = new File(mainPath);
            if (path!=null){
                if (!path.mkdirs()){
                    if (!path.exists()){
                        return null;
                    }
                }
            }
        }
        return path;
    }

    @Override
    public void onBackPressed() {
        /** Verify BackStackEntryCounts to display alert dialog */
        if (drawerLayout.isOpen())
            drawerLayout.close();
        else if (Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.navHostFragment))
                .getChildFragmentManager().getBackStackEntryCount() > 0)
            super.onBackPressed();
        else
            exitAppAlert();
    }

    private void exitAppAlert() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(getString(R.string.exit_app))
                .setMessage(getString(R.string.exit_app_message))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}
