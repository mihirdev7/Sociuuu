package com.example.socialapp.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.models.authInfoSignInModel;
import com.example.socialapp.models.commentsModel;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class commentsShowAdapter extends RecyclerView.Adapter<commentsShowAdapter.viewHolder> {
    ArrayList<commentsModel> list;
    Context context;

    public commentsShowAdapter(ArrayList<commentsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.comment_show_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        commentsModel mode=list.get(position);
        String text = TimeAgo.using(mode.getCommentedAt());
        holder.cmtshowdate.setText(text);
        //holder.cmtshowdisc.setText(mode.getCommentBody());

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mode.getCommentBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        authInfoSignInModel model=snapshot.getValue(authInfoSignInModel.class);
                        Picasso.get().load(model.getProfilePhoto())
                                .into(holder.cmtshowprofileimg);
                        holder.cmtshowdisc.setText(Html.fromHtml("<b>"+model.getName()+"</b>"+" "+mode.getCommentBody()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView cmtshowprofileimg;
        TextView cmtshowdate,cmtshowdisc;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cmtshowprofileimg=itemView.findViewById(R.id.cmtshowprofileimg);
            cmtshowdisc=itemView.findViewById(R.id.cmtshowdisc);
            cmtshowdate=itemView.findViewById(R.id.cmtshowdate);

        }
    }
}
