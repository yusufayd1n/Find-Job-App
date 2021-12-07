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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Basvurma extends AppCompatActivity {
    EditText aramatext,kriter;
    private TextView baslık,firma;
    FirebaseFirestore firestore;
    String ilanbaslıgı;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basvurma);
        aramatext=findViewById(R.id.basvuraramatext);
        baslık=findViewById(R.id.basvurbaslıktext);
        firma=findViewById(R.id.basvurfirmatext);
        kriter=findViewById(R.id.basvurkritertext);
        firebaseAuth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        baslık.setVisibility(View.INVISIBLE);
        firma.setVisibility(View.INVISIBLE);
        kriter.setVisibility(View.INVISIBLE);
    }
    public void basvurmakicinara(View view){
        ilanbaslıgı=aramatext.getText().toString();
        if(ilanbaslıgı.matches("")){
            Toast.makeText(this, "Başlık Girilmesi Zorunludur", Toast.LENGTH_SHORT).show();
        }else{
            kriter.setFocusable(false);
            baslık.setVisibility(View.VISIBLE);
            firma.setVisibility(View.VISIBLE);
            kriter.setVisibility(View.VISIBLE);
            DocumentReference docRef=firestore.collection("İlanlar").document(ilanbaslıgı);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot=task.getResult();
                        String baslıkfortext=(String)documentSnapshot.get("İLAN BAŞLIĞI");
                        String firmafortext=(String)documentSnapshot.get("FİRMA ADI");
                        String kriterfortext=(String)documentSnapshot.get("ADAY KRİTERLERİ");
                        baslık.setText(baslıkfortext);
                        firma.setText(firmafortext);
                        kriter.setText(kriterfortext);
                    }

                }
            });
        }
    }
    public void basvur(View view){
        basvurudialog();
    }
    private void basvurudialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        View view= LayoutInflater.from(this).inflate(
                R.layout.layout_basvuru_dialog
                ,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView)view.findViewById(R.id.textTittle)).setText(getResources().getString(R.string.basvuru_title));
        ((TextView)view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.basvuru_text));
        ((Button)view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.Evet));
        ((Button)view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.Hayır));
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
                String email=user.getEmail();
                HashMap<String,Object> basvurubilgileri=new HashMap<>();
                basvurubilgileri.put("İlana Başvuran Kişiler", FieldValue.arrayUnion(email));
                firestore.collection("İlanlar").document(ilanbaslıgı).update(basvurubilgileri).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Basvurma.this, "Başvurunuz Başarılı Bir Şekilde Yapılmıştır", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Basvurma.this,Secimekrani.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(Basvurma.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}