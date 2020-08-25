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
import androidx.lifecycle.ViewModelProvider;

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
import com.oamorales.myresume.activities.MyViewModel;
import com.oamorales.myresume.databinding.FragmentEditDegreeBinding;
import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.utils.DBManager;
import com.oamorales.myresume.utils.EditImage;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditDegreeFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private MyViewModel model;
    private FragmentEditDegreeBinding binding;
    private EditDegreeFragmentArgs args;
    private List<Integer> years = new ArrayList<>();
    private List<String> photoToDiscardPath = new ArrayList<>();
    /** Request codes */
    private final int GALLERY_REQUEST_CODE = 1000;
    private final int CAMERA_REQUEST_CODE = 1001;
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1000;
    /** Variables para la ruta de la imagen a guardar */
    private String currentPhotoPath, currentGalleryPath;
    private int yearB, yearE, id;
    private Uri imageUri;

    public EditDegreeFragment() {
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
        args = EditDegreeFragmentArgs.fromBundle(requireArguments());
        id = args.getDegreeId();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditDegreeBinding.inflate(inflater, container,false);
        View view = binding.getRoot();
        int year = 2020;
        for (int i = 0; i<15; i++){
            years.add(year);
            year--;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), R.layout.list_years, years);
        binding.editDegreeYearBegin.setAdapter(adapter);
        binding.editDegreeYearEnd.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        Degree degree = (Degree) DBManager.getObjectById(Degree.class, id);
        if (currentPhotoPath!=null){
            imageUri = model.getImgPath().getValue();
            Picasso.get().load(imageUri).fit().into(binding.editDegreeLogo);
        }else if (currentGalleryPath!=null){
            imageUri = model.getImgPath().getValue();
            Picasso.get().load(imageUri).fit().into(binding.editDegreeLogo);
        }else{
            /** Se obtiene ruta de la imagen recibida */
            currentPhotoPath = degree.getImageLogo();
            imageUri = Uri.fromFile(new File(currentPhotoPath));
            model.setImgPath(imageUri);
            Picasso.get().load(imageUri).fit().into(binding.editDegreeLogo);
        }
        setContentUI(degree);
        setListeners();
    }

    /** Set UI content */
    private void setContentUI(Degree degree){
        binding.editDegreeTitle.setText(degree.getDegreeTittle());
        binding.editDegreeUniversity.setText(degree.getUniversity());
        binding.editDegreeDiscipline.setText(degree.getDiscipline());
        yearB = degree.getYearBegin();
        binding.editDegreeYearBegin.setText(String.valueOf(yearB));
        yearE = degree.getYearEnd();
        binding.editDegreeYearEnd.setText(String.valueOf(yearE));
        binding.editDegreeGradeAverage.setText(String.valueOf(degree.getGradeAverage()));
    }

    /** Listeners events configs */
    private void setListeners(){
        binding.editDegreeSaveBtn.setOnClickListener(this);
        binding.editDegreeDiscipline.setOnEditorActionListener(this);
        binding.editDegreeTitle.setOnEditorActionListener(this);
        binding.editDegreeUniversity.setOnEditorActionListener(this);
        binding.editDegreeGradeAverage.setOnEditorActionListener(this);
        binding.editDegreeYearBegin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.editDegreeYearBegin.setError(null);
                binding.editDegreeYearBegin.setText(String.valueOf(years.get(position)));
                //yearB = years.get(position);
            }
        });
        binding.editDegreeYearEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.editDegreeYearEnd.setError(null);
                binding.editDegreeYearBegin.setText(String.valueOf(years.get(position)));
                //yearE = years.get(position);
            }
        });
        binding.editDegreeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMediaStore();
            }
        });
    }

    /** Evento botón guardar */
    @Override
    public void onClick(View v) {
            String tittle = Objects.requireNonNull(binding.editDegreeTitle.getText()).toString();
            String university = Objects.requireNonNull(binding.editDegreeUniversity.getText()).toString();
            String discipline = Objects.requireNonNull(binding.editDegreeDiscipline.getText()).toString();
            String gradeAverage = Objects.requireNonNull(binding.editDegreeGradeAverage.getText()).toString();
            String yearBegin = binding.editDegreeYearBegin.getText().toString();
            String yearEnd = binding.editDegreeYearEnd.getText().toString();
        if (fieldsAreValid(tittle, university, discipline, gradeAverage, yearBegin, yearEnd)){
            //Se guardan los datos si no hay campos vacíos
            String path = "";
            if (currentGalleryPath != null)
                path = EditImage.storeFile(requireContext(), imageUri, requireActivity().getContentResolver());
            else if (currentPhotoPath != null)
                path = currentPhotoPath;
            Degree newDegree = new Degree(path,tittle, university, discipline, yearB, yearE, Float.parseFloat(gradeAverage));
            //Se guarda el nuevo degree
            DBManager.updateDegree(newDegree, args.getDegreeId(), requireContext());
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
    }

    private boolean fieldsAreValid(String value1, String value2, String value3, String value4, String value5, String value6){
        /** Se valida que no hay campos vacíos */
        if (!value1.isEmpty() && !value2.isEmpty() && !value3.isEmpty()
                && !value4.isEmpty() && isNumeric(value5) && isNumeric(value6) &&
                (currentPhotoPath != null || currentGalleryPath != null)){
            yearB = Integer.parseInt(value5);
            yearE = Integer.parseInt(value6);
            if (yearE >= yearB && yearB >= years.get(0)-years.size() && yearB <= years.get(0)
                    && yearE >= years.get(0)-years.size() && yearE <= years.get(0)){
                return true;
            }else{
                binding.editDegreeYearBegin.setError("invalid");
                binding.editDegreeYearEnd.setError("invalid");
                return false;
            }
        }else{
            binding.editDegreeTitle.setError(value1.isEmpty() ? "required" : null);
            binding.editDegreeUniversity.setError(value2.isEmpty() ? "required" : null);
            binding.editDegreeDiscipline.setError(value3.isEmpty() ? "required" : null);
            binding.editDegreeGradeAverage.setError(value4.isEmpty() ? "required" : null);
            binding.editDegreeYearBegin.setError((isNumeric(value5)) ? null : "required");
            binding.editDegreeYearEnd.setError((isNumeric(value6)) ? null : "required");
            return false;
        }
    }

    private boolean isNumeric(String string){
        try {
            Integer.parseInt(string);
            return true;
        }catch (NumberFormatException e){
            return false;
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
            Snackbar.make(binding.editDegreeLogo, "Memory Access Denied", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
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
                        Snackbar.make(binding.editDegreeLogo,"Debe habilitar permiso al almacenamiento para poder cargar el logo", Snackbar.LENGTH_LONG)
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
                Snackbar.make(binding.editDegreeLogo, "Error while creating image", Snackbar.LENGTH_LONG);
            }
            if (photoFile != null){
                imageUri = FileProvider.getUriForFile(requireContext(), "com.oamorales.myresume.provider", photoFile);
                model.setImgPath(imageUri);
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
                    Picasso.get().load(data.getDataString()).fit().into(binding.editDegreeLogo);
                    currentGalleryPath = Objects.requireNonNull(data.getDataString());
                    imageUri = data.getData();
                    model.setImgPath(imageUri);
                }else{
                    Toast.makeText(requireContext(), "FALLO AL OBTENER IMAGEN", Toast.LENGTH_SHORT).show();
                }
                return;
            case CAMERA_REQUEST_CODE:
                if (resultCode== AppCompatActivity.RESULT_OK){
                    /** Imagen tomada/generada */
                    currentGalleryPath = null;
                    Picasso.get().load(this.imageUri).fit().into(binding.editDegreeLogo);
                }else{
                    /** Imagen no fue tomada/generada */
                    currentPhotoPath = null;
                    Toast.makeText(requireContext(), "Error al tomar foto", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("GALLERY_PATH", currentGalleryPath);
        outState.putString("PHOTO_PATH", currentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        EditImage.discardImages(photoToDiscardPath);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}