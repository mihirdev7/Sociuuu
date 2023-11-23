package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    EditText editTextTextEmailAddress,editTextTextPassword;
    Button button;
    TextView txtvw;
    String email,pass;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextTextEmailAddress=findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword=findViewById(R.id.editTextTextPassword);
        button=findViewById(R.id.button);
        txtvw=findViewById(R.id.txtvw);
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();

        button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               email=editTextTextEmailAddress.getText().toString();
               pass=editTextTextPassword.getText().toString();

               if(email.equals("")||pass.equals("")){
                   Toast.makeText(loginActivity.this, "Enter Details", Toast.LENGTH_SHORT).show();
               }else{
                   auth.signInWithEmailAndPassword(email,pass)
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                               startActivity(intent);
                           }
                           else{
                               Toast.makeText(loginActivity.this, "Not matching", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });

               }

           }
       });

        txtvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b=new Intent(getApplicationContext(), signinActivity.class);
                startActivity(b);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!= null){
            Intent intent=new Intent(loginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}