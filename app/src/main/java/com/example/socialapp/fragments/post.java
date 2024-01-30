package com.example.socialapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialapp.R;
import com.example.socialapp.models.PostModel;
import com.example.socialapp.models.authInfoSignInModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.Date;

public class post extends Fragment {
    EditText discpost;
    ImageView imageView8,uploadpost,imageView6;
    TextView namepost;
    Button postbtn;
    Uri uri;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    ProgressDialog dialog;


    public post() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   auth=FirebaseAuth.getInstance();
    database=FirebaseDatabase.getInstance();
    storage=FirebaseStorage.getInstance();
    dialog=new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_post, container, false);
        discpost=view.findViewById(R.id.discpost);
        namepost=view.findViewById(R.id.namepost);
        postbtn=view.findViewById(R.id.postbtn);
        uploadpost=view.findViewById(R.id.uploadpost);
        imageView6=view.findViewById(R.id.imageView6);
        imageView8=view.findViewById(R.id.imageView8);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        database.getReference().child("Users")
                .child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        authInfoSignInModel model=snapshot.getValue(authInfoSignInModel.class);
                        Picasso.get().load(model.getProfilePhoto())
                                .placeholder(R.drawable.profileicon)
                                .into(imageView6);
                        namepost.setText(model.getName());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        //===========================below is for add description and image for post===================

           discpost.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

               }

               @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {
                   String desc=discpost.getText().toString();
                   if(!desc.isEmpty()){
                       postbtn.setBackgroundResource(R.color.red);
                       postbtn.setText("Post");
                       postbtn.setEnabled(true);
                       postbtn.setTextColor(getContext().getColor(R.color.white));
                   }else{
                       if(desc.isEmpty()){
                           postbtn.setBackgroundResource(R.drawable.circle_iconborder);
                           postbtn.setText("Post");
                           postbtn.setTextColor(getContext().getColor(R.color.grey));
                           postbtn.setEnabled(false);
                       }}}

               @Override
               public void afterTextChanged(Editable s) {

               }
           });

           uploadpost.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent();
                   intent.setAction(Intent.ACTION_GET_CONTENT);
                   intent.setType("image/*");
                   startActivityForResult(intent,3);
               }
           });

           postbtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.show();
                   final StorageReference reference= storage.getReference().child("Posts")
                           .child(FirebaseAuth.getInstance().getUid()).child(String.valueOf(new Date().getTime()));
                   reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    PostModel model=new PostModel();
                                    model.setPostImg(uri.toString());
                                    model.setPostedBy(FirebaseAuth.getInstance().getUid());
                                    model.setPostDescription(discpost.getText().toString());
                                    model.setPostedAt(new Date().getTime());


                                  database.getReference().child("posts").push().setValue(model)
                                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void unused) {
                                                  Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                                  dialog.dismiss();
                                                  discpost.setText(null);
                                                  imageView8.setImageDrawable(null);

                                              }
                                          });
                                }
                            });
                       }
                   });

               }
           });


            return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
        uri=data.getData();
        imageView8.setImageURI(uri);
            imageView8.setVisibility(View.VISIBLE);
            postbtn.setBackgroundResource(R.color.red);
            postbtn.setText("Post");
            postbtn.setEnabled(true);
            postbtn.setTextColor(getContext().getColor(R.color.white));


        }
    }
}