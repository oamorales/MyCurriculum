package com.oamorales.myresume.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentNewDegreeBinding;
import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.utils.DBManager;
import com.oamorales.myresume.utils.EditImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewDegreeFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{

    private FragmentNewDegreeBinding binding;
    private int yearB, yearE;
    private final int GALLERY_REQUEST_CODE = 1000;
    private final int CAMERA_REQUEST_CODE = 1001;

    private List<Integer> years = new ArrayList<>();
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1000;

    /** Variable para la ruta de la imagen a guardar */
    private String currentPhotoPath, currentGalleryPath;
    private List<String> photoToDiscardPath = new ArrayList<>();
    private Uri imageUri;

    public NewDegreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Recuperar imagen tomada
        if (savedInstanceState != null) {
            currentPhotoPath = savedInstanceState.getString("PHOTO_PATH");
            currentGalleryPath = savedInstanceState.getString("GALLERY_PATH");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewDegreeBinding.inflate(inflater, container,false);
        View view = binding.getRoot();
        int year = 2020;
        for (int i = 0; i<15; i++){
            years.add(year);
            year--;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), R.layout.degrees_list_years, years);
        binding.newDegreeYearBegin.setAdapter(adapter);
        binding.newDegreeYearEnd.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (currentPhotoPath!=null){
            Uri uri = Uri.fromFile(new File(currentPhotoPath));
            Picasso.get().load(uri).fit().into(binding.newDegreeLogo);
        }else if (currentGalleryPath!=null){
            //Uri uri = Uri.fromFile(new File(currentGalleryPath));
            Picasso.get().load(currentGalleryPath).fit().into(binding.newDegreeLogo);
        }
        binding.saveBtn.setOnClickListener(this);
        binding.newDegreeDiscipline.setOnEditorActionListener(this);
        binding.newDegreeTitle.setOnEditorActionListener(this);
        binding.newDegreeUniversity.setOnEditorActionListener(this);
        binding.newDegreeGradeAverage.setOnEditorActionListener(this);
        binding.newDegreeYearBegin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.newDegreeYearBegin.setError(null);
                yearB = years.get(position);
            }
        });
        binding.newDegreeYearEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.newDegreeYearEnd.setError(null);
                yearE = years.get(position);
            }
        });
        binding.newDegreeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMediaStore();
            }
        });
    }

    /** Evento botón guardar */
    @Override
    public void onClick(View v) {
        String tittle = Objects.requireNonNull(binding.newDegreeTitle.getText()).toString();
        String university = Objects.requireNonNull(binding.newDegreeUniversity.getText()).toString();
        String discipline = Objects.requireNonNull(binding.newDegreeDiscipline.getText()).toString();
        String gradeAverage = Objects.requireNonNull(binding.newDegreeGradeAverage.getText()).toString();

        //Se guardan los datos si no hay campos vacíos
        if (!tittle.isEmpty() && !university.isEmpty() && !discipline.isEmpty()
                && !gradeAverage.isEmpty() && yearB!=0 && yearE!=0 ){
            if (yearE >= yearB){
                String path = null;
                if (currentGalleryPath != null)
                    path = EditImage.storeFile(requireContext(), imageUri, requireActivity().getContentResolver());
                else if (currentPhotoPath != null)
                    path = currentPhotoPath;
                Degree newDegree = new Degree(path,tittle, university, discipline, yearB, yearE, Double.parseDouble(gradeAverage));
                //Se guarda el nuevo degree
                DBManager.insert(newDegree, requireContext());
                //Evitar que la imagen de camara sea eliminada
                currentPhotoPath = null;
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }else{
                binding.newDegreeYearBegin.setError("invalid");
                binding.newDegreeYearEnd.setError("invalid");
            }
        }else{
            binding.newDegreeTitle.setError(tittle.isEmpty() ? "required" : null);
            binding.newDegreeUniversity.setError(university.isEmpty() ? "required" : null);
            binding.newDegreeDiscipline.setError(discipline.isEmpty() ? "required" : null);
            binding.newDegreeGradeAverage.setError(gradeAverage.isEmpty() ? "required" : null);
            binding.newDegreeYearBegin.setError((yearB == 0) ? "required" : null);
            binding.newDegreeYearEnd.setError((yearE == 0) ? "required" : null);
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
            Snackbar.make(binding.newDegreeLogo, "Memory Access Denied", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
        }else{
            if (haveStoragePermission()){
                selectAction();
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
                    selectAction();
                }else {
                    boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (showRationale){
                        Snackbar.make(binding.newDegreeLogo,"Debe habilitar permiso al almacenamiento para poder cargar el logo", Snackbar.LENGTH_LONG)
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

    /** Select between gallery and camera action  */
    private void selectAction(){
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

    /** Pick an image from gallery */
    private void selectFromGallery(){
        Intent in = new Intent(Intent.ACTION_GET_CONTENT);
        in.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(Intent.createChooser(in, "Seleccione aplicación"), GALLERY_REQUEST_CODE);
    }

    /** Take a picture using camera app */
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null){
            File photoFile = null;
            try {
                /** Obtain an empty image file instance */
                photoFile = EditImage.createImageFile(requireContext());
                /** Si se había tomado una foto se agrega a la lista de descarte */
                if (currentPhotoPath != null)
                    photoToDiscardPath.add(currentPhotoPath);
                /** Se obtiene la ruta de la nueva imagen */
                currentPhotoPath = photoFile.getAbsolutePath();
            }catch (IOException e){
                Snackbar.make(binding.newDegreeLogo, "Error while creating image", Snackbar.LENGTH_LONG);
            }
            if (photoFile != null){
                imageUri = FileProvider.getUriForFile(requireContext(), "com.oamorales.myresume.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /** Executed after select or capture image */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case GALLERY_REQUEST_CODE:
                if (resultCode== AppCompatActivity.RESULT_OK){
                    /** Si se había tomado una foto se agrega a la lista de descarte */
                    if (currentPhotoPath != null){
                        photoToDiscardPath.add(currentPhotoPath);
                        currentPhotoPath = null;
                    }
                    assert data != null;
                    Picasso.get().load(data.getDataString()).fit().into(binding.newDegreeLogo);
                    currentGalleryPath = Objects.requireNonNull(data.getDataString());
                    imageUri = data.getData();
                }else{
                    Toast.makeText(requireContext(), "FALLO AL OBTENER IMAGEN", Toast.LENGTH_SHORT).show();
                }
                return;
            case CAMERA_REQUEST_CODE:
                if (resultCode== AppCompatActivity.RESULT_OK){
                    currentGalleryPath = null;
                    Picasso.get().load(this.imageUri).fit().into(binding.newDegreeLogo);
                }else{
                    currentPhotoPath = null;
                    Toast.makeText(requireContext(), "Error al tomar foto", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /** Delete images not used */
    private boolean discardImage(String path){
        File file = new File(path);
        return file.delete();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("GALLERY_PATH", currentGalleryPath);
        outState.putString("PHOTO_PATH", currentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        for (String path: photoToDiscardPath) {
            discardImage(path);
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}