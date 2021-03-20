package com.oamorales.myresume.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.oamorales.myresume.R;
import com.oamorales.myresume.databinding.FragmentGeneralDataBinding;
import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.models.PersonInfo;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.utils.EditImage;
import com.oamorales.myresume.utils.Levels;
import com.oamorales.myresume.viewmodels.GeneralDataViewModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GeneralDataFragment extends Fragment implements View.OnClickListener {

    private FragmentGeneralDataBinding binding;
    private final int GALLERY_REQUEST_CODE = 1000;
    private final int CAMERA_REQUEST_CODE = 1001;
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1000;
    /** Variable para la ruta de la imagen a guardar */
    private String currentPhotoPath, currentImagePath;
    private List<String> photoToDiscardPath = new ArrayList<>();
    private Uri imageUri;
    private GeneralDataViewModel viewModel;

    public GeneralDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        currentImagePath = sharedPref.getString("IMG_PATH", null);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGeneralDataBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(GeneralDataViewModel.class);
        viewModel.init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (currentImagePath!=null){
            imageUri = Uri.fromFile(new File(currentImagePath));
            Picasso.get().load(imageUri).fit().into(binding.generalPersonImage);
        }
        setUI();
        binding.generalPersonImageButton.setOnClickListener(this);
    }

    /** Set UI and all observers */
    private void setUI() {
        viewModel.getPersonInfo().observe(getViewLifecycleOwner(), new Observer<PersonInfo>() {
            @Override
            public void onChanged(PersonInfo personInfo) {
                if (personInfo!=null)
                    binding.generalPersonName.setText(personInfo.getName());
            }
        });
        viewModel.getDegree().observe(getViewLifecycleOwner(), new Observer<Degree>() {
            @Override
            public void onChanged(Degree degree) {
                if (degree != null)
                    binding.generalLastDegree.setText(degree.getDegreeTittle());
            }
        });

        viewModel.getWorkExp().observe(getViewLifecycleOwner(), new Observer<WorkExp>() {
            @Override
            public void onChanged(WorkExp workExp) {
                if (workExp != null)
                    binding.generalCurrentJob.setText(workExp.getPosition());
            }
        });

        viewModel.getLanguage().observe(getViewLifecycleOwner(), new Observer<Language>() {
            @Override
            public void onChanged(Language language) {
                if (language == null){
                    return;
                }
                String lang = language.getLanguage();
                String level = "";
                switch (language.getLevel()){
                    case 1:
                        level = getString(Levels.LANGUAGE_LEVEL_1);
                        break;
                    case 2:
                        level = getString(Levels.LANGUAGE_LEVEL_2);
                        break;
                    case 3:
                        level = getString(Levels.LANGUAGE_LEVEL_3);
                        break;
                    case 4:
                        level = getString(Levels.LANGUAGE_LEVEL_4);
                        break;
                    case 5:
                        level = getString(Levels.LANGUAGE_LEVEL_5);
                        break;
                }
                lang = lang + ": " + level;
                binding.generalPersonLanguage.setText(lang);
            }
        });
    }

    /** Edit image event */
    @Override
    public void onClick(View v) {
        openMediaStore();
    }

    /** newDegreeLogo Click event */
    private void openMediaStore(){
        if (!isExternalStorageWritable()){
            Toast.makeText(requireContext(), getString(R.string.memory_access_denied), Toast.LENGTH_SHORT).show();
        }else{
            if (havePermission()){
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
    private boolean havePermission(){
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /** Request permission */
    private void requestPermission(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
        requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED && permissions[1].equals(Manifest.permission.CAMERA)){
                        /** Permission Granted */
                        selectAction();
                    }else{
                        boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                        if (showRationale){
                            Snackbar.make(binding.generalPersonImage,getString(R.string.rationale_message), Snackbar.LENGTH_LONG)
                                    .show();
                        }else {
                            managePermissions();
                        }
                    }
                }else {
                    boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (showRationale){
                        Snackbar.make(binding.generalPersonImage,getString(R.string.rationale_message), Snackbar.LENGTH_LONG)
                                .show();
                    }else {
                        managePermissions();
                    }
                }
        }
    }

    private void managePermissions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.grant_permission))
                .setMessage(getString(R.string.grant_permission_message)).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToSettings();
            }
        }).setNegativeButton(getString(R.string.no),null)
                .show();
    }

    /** Displays this app settings menu */
    private void goToSettings(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.oamorales.mycurriculum"));
        intent.addCategory(Intent.CATEGORY_DEFAULT).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /** Select between gallery and camera action  */
    private void selectAction(){
        final CharSequence gallery = getText(R.string.gallery), camera = getText(R.string.camera), cancel = getText(R.string.cancel);
        final CharSequence[] items = {gallery, camera, cancel};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getText(R.string.select_option))
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
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
        in.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        in.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(in, getText(R.string.select_app)), GALLERY_REQUEST_CODE);
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
                Snackbar.make(binding.generalPersonImage, "Error while creating image", Snackbar.LENGTH_LONG);
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
            /** Select from gallery result */
            case GALLERY_REQUEST_CODE:
                if (resultCode== AppCompatActivity.RESULT_OK){
                    /** call image cropper after image selected */
                    assert data != null;
                    launchImageCrop(data.getData());
                }else{
                    Toast.makeText(requireContext(), getText(R.string.select_img_error), Toast.LENGTH_SHORT).show();
                }
                return;
            /** Take picture result */
            case CAMERA_REQUEST_CODE:
                if (resultCode== AppCompatActivity.RESULT_OK){
                    /** Imagen tomada, se envía al cropper */
                    launchImageCrop(this.imageUri);
                }else{
                    currentPhotoPath = null;
                    Toast.makeText(requireContext(), getText(R.string.take_picture_error), Toast.LENGTH_SHORT).show();
                }
                return;
            /** Image cropper result */
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode== AppCompatActivity.RESULT_OK){
                    imageUri = Objects.requireNonNull(result).getUri();
                    Picasso.get().load(imageUri).fit().into(binding.generalPersonImage);
                    /** Se guarda la imagen cortada y se obtiene la ruta */
                    currentImagePath = EditImage.storeFile(requireContext(), imageUri, requireActivity().getContentResolver());
                    if (currentPhotoPath != null){
                        photoToDiscardPath.add(currentPhotoPath);
                        currentPhotoPath = null;
                    }
                    viewModel.setImgPath(currentImagePath);
                    savePreferences();
                }else {
                    if (currentPhotoPath != null){
                        photoToDiscardPath.add(currentPhotoPath);
                        currentPhotoPath = null;
                    }
                    Toast.makeText(requireActivity(), getText(R.string.select_img_error), Toast.LENGTH_SHORT).show();
                }
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void launchImageCrop(Uri data) {
        CropImage.activity(data)
                .setGuidelines(CropImageView.Guidelines.ON)
                //.setAspectRatio(1920, 1080)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(requireContext(),this);
    }

    private void savePreferences(){
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("IMG_PATH", currentImagePath);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        EditImage.discardImages(photoToDiscardPath);
        /** Save main image path */
        savePreferences();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}