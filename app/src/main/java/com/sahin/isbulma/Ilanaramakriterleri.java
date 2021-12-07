package com.sahin.isbulma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Ilanaramakriterleri extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner sehirspinner,kategorispinner;
    String issehriforsearch,katerogiforsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilanaramakriterleri);
        sehirspinner=findViewById(R.id.ilanaraspinner);
        kategorispinner=findViewById(R.id.ilanarakategorispinner);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.Şehirler, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> arrayAdapter2=ArrayAdapter.createFromResource(this,R.array.Kategoriler, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sehirspinner.setAdapter(arrayAdapter);
        kategorispinner.setAdapter(arrayAdapter2);
        kategorispinner.setOnItemSelectedListener(this);
        sehirspinner.setOnItemSelectedListener(this);
    }
    public void ilanara(View view){
        Intent intent=new Intent(Ilanaramakriterleri.this,Ilanlar.class);
        intent.putExtra("Sehir",issehriforsearch);
        intent.putExtra("Kategori",katerogiforsearch);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        issehriforsearch=sehirspinner.getSelectedItem().toString();
        katerogiforsearch=kategorispinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}