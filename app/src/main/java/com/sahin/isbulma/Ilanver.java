package com.sahin.isbulma;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ilanver extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    Spinner sehirspinner,kategorispinner;
    String issehri,ilanıverenkişi,katerogi,ilanbaslıgıfromfirebase;
    EditText firmaadi,ilanbaslıgı,kriterler;
    FirebaseFirestore firestore;
    ArrayList baslıklist;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilanver);
        sehirspinner=findViewById(R.id.ilanverspinner);
        kategorispinner=findViewById(R.id.ilanverkategorispinner);
        firestore=FirebaseFirestore.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        baslıklist=new ArrayList();
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.Şehirler, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> arrayAdapter2=ArrayAdapter.createFromResource(this,R.array.Kategoriler, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sehirspinner.setAdapter(arrayAdapter);
        kategorispinner.setAdapter(arrayAdapter2);
        kategorispinner.setOnItemSelectedListener(this);
        sehirspinner.setOnItemSelectedListener(this);
        firmaadi=findViewById(R.id.firmatext);
        ilanbaslıgı=findViewById(R.id.baslıktext);
        kriterler=findViewById(R.id.kritertext);
        ilankontrol();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        issehri=sehirspinner.getSelectedItem().toString();
        katerogi=kategorispinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Konum Belirtmediniz", Toast.LENGTH_SHORT).show();
    }
    public void ilanver(View view){
        ilanıverenkişi=firebaseUser.getEmail();
        String firmadiforfirebase=firmaadi.getText().toString();
        String ilanbaslıgıforfirebase=ilanbaslıgı.getText().toString();
        String kriterlerforfirebase=kriterler.getText().toString();
        HashMap<String,Object> ilanbilgileri=new HashMap<>();

        if(firmadiforfirebase.matches("")||ilanbaslıgıforfirebase.matches("")||kriterlerforfirebase.matches("")){
            Toast.makeText(this, "Gerekli Alanları Lütfen Doldurunuz", Toast.LENGTH_SHORT).show();
        }else{
            if(baslıklist.contains(ilanbaslıgıforfirebase)){
                Toast.makeText(this, "Bu başlıkla başka bir ilan mevcut başlığı değiştiriniz", Toast.LENGTH_SHORT).show();
            }else{
                ilanbilgileri.put("FİRMA ADI",firmadiforfirebase);
                ilanbilgileri.put("İLAN BAŞLIĞI",ilanbaslıgıforfirebase);
                ilanbilgileri.put("ADAY KRİTERLERİ",kriterlerforfirebase);
                ilanbilgileri.put("İŞ KONUMU",issehri);
                ilanbilgileri.put("İLAN SAHİBİ",ilanıverenkişi);
                ilanbilgileri.put("İLAN KATEGORİSİ",katerogi);
                ilanbilgileri.put("İlana Başvuran Kişiler","[]");

                firestore.collection("İlanlar").document(ilanbaslıgıforfirebase).set(ilanbilgileri)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Ilanver.this, "İlanınız Başarılı Bir Şekilde Oluşturuldu", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Ilanver.this,Secimekrani.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(Ilanver.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }
    public void ilankontrol(){
        firestore.collection("İlanlar").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(Ilanver.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    for(DocumentSnapshot snapshot:value.getDocuments()){
                        Map<String,Object> data=snapshot.getData();
                        ilanbaslıgıfromfirebase=(String) data.get("İLAN BAŞLIĞI");
                        baslıklist.add(ilanbaslıgıfromfirebase);
                    }
                }
            }
        });
    }

}