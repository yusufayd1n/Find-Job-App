package com.sahin.isbulma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sahin.isbulma.R;

public class MainActivity extends AppCompatActivity {
    EditText emailtext,passwordtext;
    FirebaseAuth firebaseAuth;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailtext=findViewById(R.id.emailtext);
        passwordtext=findViewById(R.id.passwordtext);
        firebaseAuth=FirebaseAuth.getInstance();
    }
    public void giris(View view){
        email=emailtext.getText().toString();
        password=passwordtext.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent=new Intent(MainActivity.this,Secimekrani.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void kayıt(View view){
        Intent intent=new Intent(MainActivity.this,UyeOl.class);
        startActivity(intent);
    }
}