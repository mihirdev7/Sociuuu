package com.example.socialapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialapp.MainActivity;
import com.example.socialapp.R;
import com.example.socialapp.adapters.friendAdapterProfile;
import com.example.socialapp.loginActivity;
import com.example.socialapp.models.authInfoSignInModel;
import com.example.socialapp.models.profileFriendListModel;
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
import java.util.ArrayList;

public class profile extends Fragment {
    RecyclerView recyprofilefriends;
    ArrayList<profileFriendListModel> list;
    ImageView coversetimg,imageView,prosetimg2,imageView3;
    TextView textView6,textView10;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    public profile() {}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        coversetimg =view.findViewById(R.id.coversetimg);
        imageView=view.findViewById(R.id.imageView);
        prosetimg2=view.findViewById(R.id.prosetimg2);
        imageView3=view.findViewById(R.id.imageView3);
        textView6=view.findViewById(R.id.textView6);
        textView10=view.findViewById(R.id.textView10);
        recyprofilefriends=view.findViewById(R.id.recyprofilefriends);
        list=new ArrayList<>();

        database.getReference().child("Users").child(auth.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){


                                    authInfoSignInModel model=snapshot.getValue(authInfoSignInModel.class);
                                    Picasso.get()
                                            .load(model.getCoverPhoto())
                                            .into(imageView);
                                    Picasso.get().load(model.getProfilePhoto())
                                                    .placeholder(R.drawable.profileicon)
                                                            .into(imageView3);
                                    textView6.setText(model.getName());
                                    textView10.setText(String.valueOf(model.getFollowerCount()));
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });


        friendAdapterProfile adapter=new friendAdapterProfile(list,getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyprofilefriends.setLayoutManager(layoutManager);
        recyprofilefriends.setNestedScrollingEnabled(false);
        recyprofilefriends.setAdapter(adapter);

        database.getReference().child("Users").child(auth.getUid())
                        .child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot data:snapshot.getChildren()) {
                            profileFriendListModel mm=data.getValue(profileFriendListModel.class);
                            list.add(mm);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        coversetimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder abc = new AlertDialog.Builder(getContext());
                abc.setTitle("Change cover picture?");
                abc.setMessage("Are you sure want to change?");
                abc.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,1);
                       }
                });
                abc.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });

        prosetimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder abcd = new AlertDialog.Builder(getContext());
                abcd.setTitle("Change Profile picture?");
                abcd.setMessage("Are you sure want to change?");
                abcd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent();
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        i.setType("image/*");
                        startActivityForResult(i,2);  }
                });
                abcd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            Toast.makeText(getContext(), "Please select an image!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode==1){
            if (data.getData()!=null){
                Uri uri=data.getData();
                imageView.setImageURI(uri);
                final StorageReference reference=storage.getReference().child("cover_photo")
                        .child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .child("coverPhoto").setValue(uri.toString());
                            }
                        });
                    }
                });
             }
        }else{
            if (data.getData()!=null) {
                Uri uri = data.getData();
                imageView3.setImageURI(uri);
                final StorageReference reference = storage.getReference().child("profile_photo")
                        .child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .child("profilePhoto").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        }
    }
}