package com.sahin.isbulma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Ilansil extends AppCompatActivity {
    EditText ilanbaslıgı;
    String ilanbaslıgıforfirbase,ilansahibifromfirebase,ilansahibif,email;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilansil);
        ilanbaslıgı=findViewById(R.id.ilanbaslıksiltext);

        firestore=FirebaseFirestore.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

    }
    public void sil(View view){
        uyarıdialog();
    }
    private void uyarıdialog(){
       AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.AlertDialogTheme);
       View view= LayoutInflater.from(this).inflate(
               R.layout.layout_ilansilme_dialog
               ,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
       );
       builder.setView(view);
        ((TextView)view.findViewById(R.id.textTittle)).setText(getResources().getString(R.string.silme_title));
        ((TextView)view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.silme_text));
        ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.Evet));
        ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.Hayır));
        ((ImageView)view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_delete);
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
                email=firebaseUser.getEmail();
                ilanbaslıgıforfirbase= ilanbaslıgı.getText().toString();
                if(ilanbaslıgıforfirbase.equals("")){
                    Toast.makeText(Ilansil.this, "Gerekli Alanları Doldurunuz", Toast.LENGTH_SHORT).show();
                }else{
                    DocumentReference docRef=firestore.collection("İlanlar").document(ilanbaslıgıforfirbase);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document=task.getResult();
                            ilansahibifromfirebase= (String) document.get("İLAN SAHİBİ");
                            if(ilansahibifromfirebase==null){
                                Toast.makeText(Ilansil.this, "Böyle Bir İlan Yok", Toast.LENGTH_SHORT).show();
                            }else{
                                if(ilansahibifromfirebase.equals(email)){
                                    firestore.collection("İlanlar").document(ilanbaslıgıforfirbase)
                                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Ilansil.this, "İlanınız Başarılı Bir Şekilde Yayından Kaldırılmıştır", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(Ilansil.this,Secimekrani.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull  Exception e) {
                                            Toast.makeText(Ilansil.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(Ilansil.this, "Bu İlan Size Ait Değil", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                    });
                }
            }

        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

}