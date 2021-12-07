package com.sahin.isbulma;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UyeOl extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    EditText isim,soyisim,email,sifre,telefon;
    String emailforfirebase,sifreforfirebase,userid,isimforfirebase,soyisimforfirebase,telefonforfirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_ol);
        firestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        isim=findViewById(R.id.uyeolisim);
        soyisim=findViewById(R.id.uyeolsoyisim);
        email=findViewById(R.id.uyeolemail);
        sifre=findViewById(R.id.uyeolpassword);
        telefon=findViewById(R.id.uyeoltelefon);

    }
    public void kayıtislemi(View view){
        isimforfirebase = isim.getText().toString();
        soyisimforfirebase=soyisim.getText().toString();
        emailforfirebase=email.getText().toString();
        sifreforfirebase=sifre.getText().toString();
        telefonforfirebase=telefon.getText().toString();
        if(emailforfirebase.matches("") || sifreforfirebase.matches("")|| telefonforfirebase.matches("")|| isimforfirebase.matches("")|| soyisimforfirebase.matches("")){
            Toast.makeText(this, "Lütfen Bütün Gerekli Bilgileri Giriniz", Toast.LENGTH_SHORT).show();
        }else{
            firebaseAuth.createUserWithEmailAndPassword(emailforfirebase,sifreforfirebase).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    userid=firebaseAuth.getCurrentUser().getUid();
                    Map<String,Object> userdata=new HashMap<>();
                    userdata.put("İsim",isimforfirebase);
                    userdata.put("Soyisim",soyisimforfirebase);
                    userdata.put("Email",emailforfirebase);
                    userdata.put("Telefon",telefonforfirebase);
                    firestore.collection("Üyeler").document(userid)
                            .set(userdata);
                    Toast.makeText(UyeOl.this, "Üyelik Oluşturuldu", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(UyeOl.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UyeOl.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}