package com.sahin.isbulma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sahin.isbulma.databinding.ActivityIlanlarBinding;

import java.util.ArrayList;
import java.util.Map;

public class Ilanlar extends AppCompatActivity  {
    String ilansehiri,ilankategorisi,ilansahibifromfirebase,
            ilanbaslıgıfromfirebase,firmaadifromfirebase,kriterfromfirebase;
    FirebaseFirestore firestore;
    ArrayList<Ilan> ilanArraylist;
    private  ActivityIlanlarBinding binding;
    Ilanadapter ilanadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityIlanlarBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        Intent intent=getIntent();
        ilansehiri=intent.getStringExtra("Sehir");
        ilankategorisi=intent.getStringExtra("Kategori");
        firestore=FirebaseFirestore.getInstance();
        ilanArraylist=new ArrayList<>();
        ilanlarılistele();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ilanadapter=new Ilanadapter(ilanArraylist);
        binding.recyclerView.setAdapter(ilanadapter);

    }
    private void ilanlarılistele(){
        ilanArraylist.clear();
        firestore.collection("İlanlar").whereEqualTo("İLAN KATEGORİSİ",ilankategorisi)
                .whereEqualTo("İŞ KONUMU",ilansehiri).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable  QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(Ilanlar.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    for(DocumentSnapshot snapshot:value.getDocuments()){
                        Map<String,Object> data=snapshot.getData();
                        firmaadifromfirebase=(String) data.get("FİRMA ADI");
                        ilanbaslıgıfromfirebase=(String) data.get("İLAN BAŞLIĞI");
                        kriterfromfirebase= (String) data.get("ADAY KRİTERLERİ");
                        Ilan ilan=new Ilan(firmaadifromfirebase,ilanbaslıgıfromfirebase,kriterfromfirebase);
                        ilanArraylist.add(ilan);
                    }
                    ilanadapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.basvurma,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.basvurma:
                Intent intent=new Intent(Ilanlar.this,Basvurma.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}