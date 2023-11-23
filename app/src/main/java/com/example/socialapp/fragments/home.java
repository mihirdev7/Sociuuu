package com.example.socialapp.fragments;

import static com.example.socialapp.R.id.commentid;
import static com.example.socialapp.R.id.imageView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialapp.R;
//import com.example.socialapp.adapters.feedAdapterHome;
import com.example.socialapp.adapters.PostAdapterHome;
import com.example.socialapp.adapters.storyAdapterHome;
//import com.example.socialapp.models.homeFeedModel;
import com.example.socialapp.models.PostModel;
import com.example.socialapp.models.UserStoryModel;
import com.example.socialapp.models.authInfoSignInModel;
import com.example.socialapp.models.storyModel;
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
import java.util.Date;


public class home extends Fragment {

    RecyclerView Recyid,recyfeed;
    ImageView imageView5,imageView2,profileicnfeed,imgcir;
    ArrayList<storyModel> llist;
   ArrayList<PostModel> feedlist;
    ProgressDialog dialog;

    FirebaseAuth auth;
    FirebaseStorage storage;

   FirebaseDatabase database;
   ActivityResultLauncher<String> gallerylauncher;
    authInfoSignInModel k;
    public home(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        imageView5=view.findViewById(R.id.imageView5);
        imageView2=view.findViewById(R.id.imageView2);
        profileicnfeed=view.findViewById(R.id.profileicnfeed);
        dialog=new ProgressDialog(getContext());

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //==========================profile image set===============================================

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         k=snapshot.getValue(authInfoSignInModel.class);
                        Picasso.get()
                                .load(k.getProfilePhoto())
                                .into(profileicnfeed);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        profileicnfeed.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Dialog dg=new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dg.setContentView(R.layout.profile_circleimg);
                 imgcir=dg.findViewById(R.id.imgcircle);
                Picasso.get().load(k.getProfilePhoto()).into(imgcir);
                imgcir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dg.dismiss();
                    }
                });
                dg.show();
                return true;
            }
        });


        //==============================================story show code=============================================
        Recyid=view.findViewById(R.id.Recyid);
        llist=new ArrayList<>();

        storyAdapterHome adapter=new storyAdapterHome(llist,getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        Recyid.setLayoutManager(linearLayoutManager);
        Recyid.setNestedScrollingEnabled(false);
        Recyid.setAdapter(adapter);
        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    llist.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        storyModel s=new storyModel();
                        s.setStoryBy(dataSnapshot.getKey());
                        s.setStoryAt(dataSnapshot.child("postedBy").getValue(Long.class));

                        ArrayList<UserStoryModel> storiess=new ArrayList<>();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.child("userStories").getChildren()){
                            UserStoryModel o=dataSnapshot1.getValue(UserStoryModel.class);
                            storiess.add(o);
                        }
                        s.setList(storiess);
                        llist.add(s);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //===============================================feed show code==================================================
        recyfeed=view.findViewById(R.id.recyfeed);
        feedlist=new ArrayList<>();
        PostAdapterHome adapterHome=new PostAdapterHome(feedlist,getContext());
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getContext());
        recyfeed.setLayoutManager(linearLayoutManager1);
        recyfeed.setAdapter(adapterHome);


        database.getReference().child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedlist.clear();
                for (DataSnapshot data:snapshot.getChildren())
                {       PostModel model=data.getValue(PostModel.class);
                    model.setPostId(data.getKey());
                    feedlist.add(model);
                }
                adapterHome.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallerylauncher.launch("image/*");
            }
        });
        gallerylauncher=registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageView2.setImageURI(result);
                        dialog.show();
                     final StorageReference reference=storage.getReference()
                             .child("stories").child(FirebaseAuth.getInstance().getUid())
                             .child(new Date().getTime()+"");
                     reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                     storyModel m=new storyModel();
                                     m.setStoryAt(new Date().getTime());
                                     database.getReference().child("stories")
                                             .child(FirebaseAuth.getInstance().getUid())
                                             .child("postedBy")
                                             .setValue(m.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void unused) {
                                                     UserStoryModel u=new UserStoryModel(uri.toString(),m.getStoryAt());
                                                     database.getReference().child("stories")
                                                             .child(FirebaseAuth.getInstance().getUid())
                                                             .child("userStories").push()
                                                             .setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                 @Override
                                                                 public void onSuccess(Void unused) {
                                                                     dialog.dismiss();
                                                                 }
                                                             });

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

}