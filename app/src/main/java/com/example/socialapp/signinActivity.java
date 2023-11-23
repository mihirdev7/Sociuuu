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

import com.example.socialapp.models.authInfoSignInModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class signinActivity extends AppCompatActivity {
    EditText reg_email,reg_phone,reg_pass,reg_name;
    Button regstrAct_btn;
    TextView signtext;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    boolean b=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        reg_name=findViewById(R.id.reg_name);
        reg_phone=findViewById(R.id.reg_phone);
        reg_pass=findViewById(R.id.reg_pass);
        reg_email=findViewById(R.id.reg_email);
        regstrAct_btn=findViewById(R.id.regstrAct_btn);
        signtext=findViewById(R.id.signtext);
        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        signtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(signinActivity.this, loginActivity.class);
                startActivity(j);
            }
        });


        regstrAct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Emails=reg_email.getText().toString();
                String Passs=reg_pass.getText().toString();
                String Phones=reg_phone.getText().toString();
                String Names=reg_name.getText().toString();

                auth.createUserWithEmailAndPassword(Emails,Passs)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            authInfoSignInModel m = new authInfoSignInModel(Names, Emails, Passs,Phones);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(m);
                            Toast.makeText(signinActivity.this, "Success", Toast.LENGTH_SHORT).show();

                            Intent d = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(d);
                        } else{
                            Toast.makeText(signinActivity.this, "Failedddd", Toast.LENGTH_SHORT).show();
                           /* reg_email.setText("");
                            reg_name.setText("");
                            reg_pass.setText("");
                            reg_phone.setText("");*/
                        }
                    }
                });
            }
        });
    }
}