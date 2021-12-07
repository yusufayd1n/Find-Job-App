package com.sahin.isbulma;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sahin.isbulma.databinding.ActivityProfilBinding;

public class Profil extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    Uri CVDATA;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissonLauncher;
    private ActivityProfilBinding binding;
    Bitmap selectedbitmap;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfilBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        registerLauncher();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        email=firebaseUser.getEmail();
    }
    public void sec(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Dosyalara Erişim İçin İzin Gerekiyor",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         permissonLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                permissonLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else{
            Intent intenttoFiles=new Intent(Intent.ACTION_OPEN_DOCUMENT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intenttoFiles);
        }
    }
    public void yukle(View view){
        if(CVDATA!=null){
            storageReference.child("["+email+"]").putFile(CVDATA).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Profil.this, "CV'niz Sisteme Başarılı Bir Şekilde Yüklenmiştir.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profil.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public void registerLauncher(){
      activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
          @Override
          public void onActivityResult(ActivityResult result) {
              if(result.getResultCode()==RESULT_OK){
                  Intent intentfromresult=result.getData();
                  if(intentfromresult!=null){
                      Toast.makeText(Profil.this, "Dosya Seçilmiştir Yüklemek için Yükle Butonuna Tıklayınız", Toast.LENGTH_SHORT).show();
                      CVDATA=intentfromresult.getData();
                      binding.imageView.setImageURI(CVDATA);
                      /*
                      try{
                          if(Build.VERSION.SDK_INT>=28){
                              ImageDecoder.Source source=ImageDecoder.createSource(Profil.this.getContentResolver(),CVDATA);
                              selectedbitmap=ImageDecoder.decodeBitmap(source);
                              binding.imageView.setImageBitmap(selectedbitmap);
                          }else{
                              selectedbitmap=MediaStore.Images.Media.getBitmap(Profil.this.getContentResolver(),CVDATA);
                              binding.imageView.setImageBitmap(selectedbitmap);
                          }
                      }catch (Exception e){
                          e.printStackTrace();
                      }*/
                  }
              }
          }
      });
      permissonLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
          @Override
          public void onActivityResult(Boolean result) {
              if (result) {
                  Intent intenttoFiles=new Intent(Intent.ACTION_OPEN_DOCUMENT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  activityResultLauncher.launch(intenttoFiles);
              } else{
                  Toast.makeText(Profil.this, "İzin Gerekiyor!", Toast.LENGTH_SHORT).show();
              }
          }
      });
    }
}