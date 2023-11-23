package com.example.socialapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.CommentActivity;
import com.example.socialapp.R;
import com.example.socialapp.models.NotificationNotifyModel;
import com.example.socialapp.models.PostModel;
import com.example.socialapp.models.authInfoSignInModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapterHome extends RecyclerView.Adapter<PostAdapterHome.viewHolder>{
    ArrayList<PostModel> list;
    Context context;
    authInfoSignInModel m;

    public PostAdapterHome(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.feed_home_design,parent,false);
        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PostModel model = list.get(position);
        Picasso.get().load(model.getPostImg())
                .placeholder(R.drawable.profileicon)
                .into(holder.postImgFeed);
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         m=snapshot.getValue(authInfoSignInModel.class);
                        Picasso.get().load(m.getProfilePhoto())
                                .placeholder(R.drawable.profileicon)
                                .into(holder.profileicnfeed);
                        holder.nameFeed.setText(m.getName());
                        holder.likeid.setText(model.getPostLikes()+"");
                        holder.commentid.setText(model.getCommentsCount()+"");

                        String dscr=model.getPostDescription();
                        if(dscr.equals("")){
                            holder.aboutFeedHome.setVisibility(View.GONE);
                        }else{
                            holder.aboutFeedHome.setVisibility(View.VISIBLE);
                            holder.aboutFeedHome.setText(model.getPostDescription());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(model.getPostId()).child("likes").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.likeid.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hhome,0,0,0);
                        }else{
                            holder.likeid.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference().child("posts")
                                            .child(model.getPostId()).child("likes").child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference().child("posts")
                                                            .child(model.getPostId()).child("postLikes")
                                                            .setValue(model.getPostLikes()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.likeid.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hhome,0,0,0);

                                                                    NotificationNotifyModel n=new NotificationNotifyModel();
                                                                    n.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    n.setNotificationAt(new Date().getTime());
                                                                    n.setPostID(model.getPostId());
                                                                    n.setPostedBy(model.getPostedBy());
                                                                    n.setType("Like");
                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification").child(model.getPostedBy())
                                                                            .push().setValue(n);
                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.commentid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("Namepost",model.getPostId());
                intent.putExtra("Nameby",model.getPostedBy());
                intent.putExtra("img",m.getProfilePhoto());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView profileicnfeed,postImgFeed,editFeedHome,postSaveimgId;
        TextView nameFeed,aboutFeedHome,likeid,commentid,shareid;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profileicnfeed=itemView.findViewById(R.id.profileicnfeed);
            postImgFeed=itemView.findViewById(R.id.postImgFeed);
            editFeedHome=itemView.findViewById(R.id.editFeedHome);
            postSaveimgId=itemView.findViewById(R.id.postSaveimgId);
            nameFeed=itemView.findViewById(R.id.nameFeed);
            aboutFeedHome=itemView.findViewById(R.id.aboutFeedHome);
            likeid=itemView.findViewById(R.id.likeid);
            commentid=itemView.findViewById(R.id.commentid);
            shareid=itemView.findViewById(R.id.shareid);
        }
    }

}
