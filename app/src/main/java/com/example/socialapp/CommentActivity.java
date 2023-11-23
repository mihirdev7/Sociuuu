package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.socialapp.adapters.commentsShowAdapter;
import com.example.socialapp.models.NotificationNotifyModel;
import com.example.socialapp.models.PostModel;
import com.example.socialapp.models.authInfoSignInModel;
import com.example.socialapp.models.commentsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {
    EditText writecmtid;
    ImageView cmtimg,cmtprofileimg,imageView7;
    TextView cmtnametxt,cmtdisctxt,cmtlikeid,commentid,cmtshareid;
    androidx.appcompat.widget.Toolbar toolbar2;
    RecyclerView recycmt;
    Intent intent;
    String postId;
    String postBy;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<commentsModel> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        writecmtid=findViewById(R.id.writecmtid);
        cmtimg=findViewById(R.id.cmtimg);
        cmtprofileimg=findViewById(R.id.cmtprofileimg);
        cmtnametxt=findViewById(R.id.cmtnametxt);
        cmtdisctxt=findViewById(R.id.cmtdisctxt);
        cmtlikeid=findViewById(R.id.cmtlikeid);
        commentid=findViewById(R.id.commentid);
        cmtshareid=findViewById(R.id.cmtshareid);
        recycmt=findViewById(R.id.recycmt);
        imageView7=findViewById(R.id.imageView7);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        intent=getIntent();
        toolbar2=findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar2);
        CommentActivity.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        postId=intent.getStringExtra("Namepost");
        postBy=intent.getStringExtra("Nameby");

        database.getReference().child("Users").child(postBy)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                authInfoSignInModel m=snapshot.getValue(authInfoSignInModel.class);
                               Picasso.get().load(m.getProfilePhoto())
                                        .into(cmtprofileimg);
                                cmtnametxt.setText(m.getName());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

        //==============data for post img and discription====================================
        database.getReference().child("posts").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PostModel model=snapshot.getValue(PostModel.class);
                        Picasso.get()
                                .load(model.getPostImg())
                                .placeholder(R.drawable.profileicon)
                                .into(cmtimg);
                        cmtdisctxt.setText(model.getPostDescription());
                        cmtlikeid.setText(model.getPostLikes()+"");
                        commentid.setText(model.getCommentsCount()+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsModel n=new commentsModel();
                n.setCommentBody(writecmtid.getText().toString());
                n.setCommentBy(FirebaseAuth.getInstance().getUid());
                n.setCommentedAt(new Date().getTime());

                database.getReference().child("posts").child(postId)
                        .child("comments").push().setValue(n).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            database.getReference().child("posts")
                                    .child(postId).child("commentsCount")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int commentCount=0;
                                            if (snapshot.exists()){
                                                commentCount=snapshot.getValue(Integer.class);
                                            }
                                            database.getReference().child("posts")
                                                    .child(postId).child("commentsCount")
                                                    .setValue(commentCount+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            writecmtid.setText("");
                                                            Toast.makeText(CommentActivity.this, "commented", Toast.LENGTH_SHORT).show();
                                                            NotificationNotifyModel n=new NotificationNotifyModel();
                                                            n.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                            n.setPostID(postId);
                                                            n.setPostedBy(postBy);
                                                            n.setType("comment");
                                                            n.setNotificationAt(new Date().getTime());
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("notification").child(postBy)
                                                                    .push().setValue(n);
                                                        }
                                                    });
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                            }
                        });
            }
        });
        commentsShowAdapter adapter=new commentsShowAdapter(list,this);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recycmt.setLayoutManager(manager);
        recycmt.setNestedScrollingEnabled(false);
        recycmt.setAdapter(adapter);
        database.getReference().child("posts").child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            commentsModel comment=dataSnapshot.getValue(commentsModel.class);
                            list.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}