package com.oamorales.myresume.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.oamorales.myresume.R;
import com.oamorales.myresume.models.Degree;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;

public class NewDegreeFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private Realm realm;
    private TextInputEditText newDegreeTittle, newDegreeUniversity,
            newDegreeDiscipline, newDegreeAverage ;
    private AutoCompleteTextView newDegreeYearEnd, newDegreeYearBegin;
    private MaterialButton saveBtn;
    private AppCompatImageView newDegreeLogo;
    private int yearB, yearE;

    private final String MEDIA_DIRECTORY = "curriculumFiles/" + "media";
    private final String TEMPORAL_PICTURE_NAME = "temporal.jpg";
    private final int GALLERY_REQUEST_CODE = 1000;
    private final int CAMERA_REQUEST_CODE = 1001;

    private List<Integer> years = new ArrayList<>();
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1000;

    private String absolutePath;
    private Uri imageUri;

    public NewDegreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_degree, container, false);
        setID(view);
        int year = 2020;
        for (int i = 0; i<15; i++){
            years.add(year);
            year--;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(requireContext(), R.layout.degrees_list_years, years);
        newDegreeYearBegin.setAdapter(adapter);
        newDegreeYearEnd.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        saveBtn.setOnClickListener(this);
        newDegreeDiscipline.setOnEditorActionListener(this);
        newDegreeTittle.setOnEditorActionListener(this);
        newDegreeUniversity.setOnEditorActionListener(this);
        newDegreeAverage.setOnEditorActionListener(this);

        newDegreeYearBegin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newDegreeYearBegin.setError(null);
                yearB = years.get(position);
            }
        });

        newDegreeYearEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newDegreeYearEnd.setError(null);
                yearE = years.get(position);
            }
        });

        newDegreeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMediaStore();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void setID(View view){
        newDegreeLogo = view.findViewById(R.id.newDegreeLogo);
        newDegreeTittle = view.findViewById(R.id.newDegreeTitle);
        newDegreeUniversity = view.findViewById(R.id.newDegreeUniversity);
        newDegreeDiscipline = view.findViewById(R.id.newDegreeDiscipline);
        newDegreeYearBegin = view.findViewById(R.id.newDegreeYearBegin);
        newDegreeYearEnd = view.findViewById(R.id.newDegreeYearEnd);
        newDegreeAverage = view.findViewById(R.id.newDegreeGradeAverage);
        saveBtn = view.findViewById(R.id.saveBtn);

    }

    /** Evento botón guardar */
    @Override
    public void onClick(View v) {
        String tittle = Objects.requireNonNull(newDegreeTittle.getText()).toString();
        String university = Objects.requireNonNull(newDegreeUniversity.getText()).toString();
        String discipline = Objects.requireNonNull(newDegreeDiscipline.getText()).toString();
        String gradeAverage = Objects.requireNonNull(newDegreeAverage.getText()).toString();

        if (!tittle.isEmpty() && !university.isEmpty() && !discipline.isEmpty()
                && !gradeAverage.isEmpty() && yearB!=0 && yearE!=0){
            if (yearE >= yearB){

                Degree newDegree = new Degree(R.drawable.usb_logo,tittle, university, discipline, yearB, yearE, Double.parseDouble(gradeAverage));
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(newDegree);
                realm.commitTransaction();
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }else{
                newDegreeYearBegin.setError("invalid");
                newDegreeYearEnd.setError("invalid");
            }

        }else{
            newDegreeTittle.setError(tittle.isEmpty() ? "required" : null);
            newDegreeUniversity.setError(university.isEmpty() ? "required" : null);
            newDegreeDiscipline.setError(discipline.isEmpty() ? "required" : null);
            newDegreeAverage.setError(gradeAverage.isEmpty() ? "required" : null);
            newDegreeYearBegin.setError((yearB == 0) ? "required" : null);
            newDegreeYearEnd.setError((yearE == 0) ? "required" : null);
        }
    }

    /** Eliminar icono de error al escribir en los EditText */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        v.setError(null);
        return true;
    }

    /** newDegreeLogo Click event */
    private void openMediaStore(){
        if (!isExternalStorageWritable()){
            Snackbar.make(newDegreeLogo, "Memory Access Denied", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
        }else{
            if (haveStoragePermission()){
                pickImage();
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

    /** Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    /** Check if permission granted */
    private boolean haveStoragePermission(){
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /** Request permission */
    private void requestPermission(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImage();
                }else {
                    boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (showRationale){
                        Snackbar.make(newDegreeLogo,"Debe habilitar permiso al almacenamiento para poder cargar el logo", Snackbar.LENGTH_LONG)
                                .show();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("HABILITAR PERMISO")
                                .setMessage("Para poder cargar el logo es necesario que habilite el permiso de acceso al almacenamiento.\n" +
                                "¿Desea habilitar el permiso mencionado?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goToSettings();
                            }
                        }).setNegativeButton("No",null)
                        .show();
                    }
                }
        }
    }

    /** Displays this app settings menu */
    private void goToSettings(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.oamorales.mycurriculum"));
        intent.addCategory(Intent.CATEGORY_DEFAULT).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /** Pick image logo */
    private void pickImage(){
        final String gallery = "Galería", camera = "Cámara", cancel = "Cancel";
        final CharSequence[] items = {gallery, camera, cancel};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Seleccione Opción")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals(gallery)){
                            selectFromGallery();
                        }else if (items[which].equals(camera)){
                            takePicture();
                        }else{
                            dialog.dismiss();
                        }
                    }
                }).show();
    }

    private void selectFromGallery(){
        Intent in = new Intent(Intent.ACTION_PICK);
        in.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(Intent.createChooser(in, "Seleccione aplicación"), GALLERY_REQUEST_CODE);
    }


    private void takePicture() {
        //SE CREA LA CARPETA PARA ALMACENAR LAS IMÁGENES
        String mainPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/curriculum_images";
        long suffix = System.currentTimeMillis()/1000;
        absolutePath = mainPath + File.separator + "img_" + suffix + ".png";
        File newFolder = new File(mainPath);
        if (!newFolder.exists()){
            newFolder.mkdir();
        }
        File imageFile = new File(absolutePath);
        imageUri = FileProvider.getUriForFile(requireContext(), "com.oamorales.myresume.provider", imageFile);


        /*SimpleDateFormat sdf =  (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        String dt = sdf.format(new Date());*/

        //RUTA DONDE SE ALMACENA LA IMAGEN TOMADA
        //File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //absolutePath = path.getAbsolutePath() + "/curriculum_images/" + "img_prueba_"+ dt +".png";
        //File imageFile = new File(path.getAbsolutePath() + "/curriculum_images", "img_prueba_"+ dt +".png");
        //Uri imageUri = FileProvider.getUriForFile(requireContext(), "com.oamorales.myresume.provider", imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent in = new Intent(Intent.ACTION_GET_CONTENT);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null)
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        else
            Snackbar.make(newDegreeLogo,"Imposible tomar foto", Snackbar.LENGTH_SHORT).show();

        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null)
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        else
            Snackbar.make(newDegreeLogo,"Imposible tomar foto", Snackbar.LENGTH_SHORT).show();*/
    }

    /** Executed after select or capture image */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case GALLERY_REQUEST_CODE:
                if (resultCode== AppCompatActivity.RESULT_OK){
                    assert data != null;
                    //Picasso.get().load(data.getData()).fit().into(newDegreeLogo);
                    Picasso.get().load(data.getDataString()).fit().into(newDegreeLogo);
                }else{
                    Snackbar.make(newDegreeLogo, "FALLO AL OBTENER IMAGEN", Snackbar.LENGTH_SHORT);
                }
                return;
            case CAMERA_REQUEST_CODE:
                if (resultCode== AppCompatActivity.RESULT_OK){
                    Picasso.get().load(imageUri).fit().into(newDegreeLogo);
                }else{
                    Snackbar.make(newDegreeLogo,"Error al tomar foto",Snackbar.LENGTH_SHORT);
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void decodeBitmap(Bundle bundle) {
        Bitmap imageBitmap = (Bitmap) bundle.get("data");
        newDegreeLogo.setBackground(null);
        newDegreeLogo.setImageBitmap(imageBitmap);
    }

    private void loadImage(){
        //File newPicture = new File(Environment.getExternalStorageDirectory().getPath() + "image1" + ".png");
        //MEDIA_DIRECTORY = "curriculumFiles/" + "media";
        // TEMPORAL_PICTURE_NAME = "temporal.jpg";

        File imgFile = new File(Environment.getExternalStorageDirectory() + MEDIA_DIRECTORY);
        /*File file = new File(Environment.getExternalStoragePublicDirectory(), MEDIA_DIRECTORY);
        file.mkdirs();*/
        //dirección temporal donde se almacena la foto tomada
        String path = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                + File.separator + TEMPORAL_PICTURE_NAME;

        File newFile = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
}