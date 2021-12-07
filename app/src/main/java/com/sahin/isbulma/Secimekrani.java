package com.sahin.isbulma;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Secimekrani extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String email,dowloandurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secimekrani);
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();
        email=user.getEmail();
        basvuranlar();
    }
    public void isilanigörüntüle(View view){
        Intent intent=new Intent(Secimekrani.this,Ilanaramakriterleri.class);
        startActivity(intent);
    }
    public void isilaniver(View view){
        Intent intent=new Intent(Secimekrani.this,Ilanver.class);
        startActivity(intent);
    }
    public void cıkısyap(View view){
        firebaseAuth.signOut();
        Intent intent=new Intent(Secimekrani.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void isilanısil(View view){
        Intent intent=new Intent(Secimekrani.this,Ilansil.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.update_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.updateprofil:
                Intent intent=new Intent(Secimekrani.this,Profil.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void basvuranlar(){
      firestore.collection("İlanlar").whereEqualTo("İLAN SAHİBİ",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
          @Override
          public void onEvent(@Nullable  QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {
              if(error!=null){
                  Toast.makeText(Secimekrani.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
              }else{
                  if(value!=null){
                          for(DocumentSnapshot snapshot:value.getDocuments()){
                              Map<String,Object> basvurudata=snapshot.getData();
                              String basvuran= basvurudata.get("İlana Başvuran Kişiler").toString();
                              AlertDialog.Builder builder=new AlertDialog.Builder(Secimekrani.this,R.style.AlertDialogTheme);
                              View view= LayoutInflater.from(Secimekrani.this).inflate(
                                      R.layout.layout_basvuru_dialog
                                      ,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
                              );
                              builder.setView(view);
                              ((TextView)view.findViewById(R.id.textTittle)).setText(getResources().getString(R.string.basvuran_title));
                              ((TextView)view.findViewById(R.id.textMessage)).setText(basvuran+"maili ile kayıtlı kişi ilanınıza başvurmuştur.");
                              ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.Evet2));
                              ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.Hayır2));
                              ((ImageView)view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_done);
                              final AlertDialog alertDialog=builder.create();
                              view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      alertDialog.dismiss();
                                  }
                              });
                              view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      storageReference.child(basvuran).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                          @Override
                                          public void onSuccess(Uri uri) {
                                              dowloandurl=uri.toString();
                                              CVindir(Secimekrani.this,basvuran,".pdf",DIRECTORY_DOWNLOADS,dowloandurl);
                                              alertDialog.dismiss();
                                          }
                                      }).addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull  Exception e) {
                                              Toast.makeText(Secimekrani.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                          }
                                      });
                                  }

                              });
                              if(alertDialog.getWindow()!=null){
                                  alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                              }
                                  if(!basvuran.equals("[]")){
                                      alertDialog.show();
                                  }
                          }
                  }

              }
          }
      });
    }
    public void CVindir(Context context,String filename,String fileExtension,String destiantion,String url){
        DownloadManager downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destiantion,filename+fileExtension);
        downloadManager.enqueue(request);

    }
}