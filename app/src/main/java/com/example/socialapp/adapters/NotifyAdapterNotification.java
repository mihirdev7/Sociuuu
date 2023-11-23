package com.example.socialapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.CommentActivity;
import com.example.socialapp.R;
import com.example.socialapp.models.NotificationNotifyModel;
import com.example.socialapp.models.authInfoSignInModel;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotifyAdapterNotification extends RecyclerView.Adapter<NotifyAdapterNotification.viewHolder> {

ArrayList<NotificationNotifyModel> list;
Context context;

    public NotifyAdapterNotification(ArrayList<NotificationNotifyModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification_layout_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NotificationNotifyModel model=list.get(position);
        String type= model.getType();
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getNotificationBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        authInfoSignInModel users=snapshot.getValue(authInfoSignInModel.class);
                        Picasso.get().load(users.getProfilePhoto()).into(holder.profileicnfeed);
                        String text = TimeAgo.using(model.getNotificationAt());
                        holder.timeidnotify.setText(text);
                        if (type.equals("Follow")){
                            holder.textView16.setText(Html.fromHtml("<b>"+users.getName()+"</b>"+" started following you!"));
                        } else if (type.equals("Like")) {
                            holder.textView16.setText(Html.fromHtml("<b>"+users.getName()+"</b>"+" liked your post!"));
                        } else if(type.equals("comment")){
                            holder.textView16.setText(Html.fromHtml("<b>"+users.getName()+"</b>"+" commented your post!"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.openfriendid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!type.equals("Follow")){
                    FirebaseDatabase.getInstance().getReference().child("notification")
                                    .child(model.getPostedBy())
                                            .child(model.getNotificationID())
                                                    .child("checkOpen")
                                                            .setValue(true);
                    holder.openfriendid.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent=new Intent(context, CommentActivity.class);
                    intent.putExtra("Namepost",model.getPostID());
                    intent.putExtra("Nameby",model.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });
        Boolean checkOpen=model.isCheckOpen();
        if(checkOpen){
            holder.openfriendid.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{}
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView profileicnfeed;
        TextView textView16;
        TextView timeidnotify;
        ConstraintLayout openfriendid;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profileicnfeed=itemView.findViewById(R.id.profileicnfeed);
            textView16=itemView.findViewById(R.id.textView16);
            timeidnotify=itemView.findViewById(R.id.timeidnotify);
            openfriendid=itemView.findViewById(R.id.openfriendid);
        }
    }
}
