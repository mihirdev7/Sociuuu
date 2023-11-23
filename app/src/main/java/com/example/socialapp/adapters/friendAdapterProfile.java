package com.example.socialapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.models.authInfoSignInModel;
import com.example.socialapp.models.profileFriendListModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class friendAdapterProfile extends RecyclerView.Adapter<friendAdapterProfile.viewHolder> {

    ArrayList<profileFriendListModel> list;
    Context context;

    public friendAdapterProfile(ArrayList<profileFriendListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.friendlist_layout_design,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        profileFriendListModel modal= list.get(position);
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(modal.getFollowdedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        authInfoSignInModel user=snapshot.getValue(authInfoSignInModel.class);
                        Picasso.get()
                                .load(user.getProfilePhoto())
                                .placeholder(R.drawable.profileicon)
                                .into(holder.friendid);
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

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView friendid;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            friendid=itemView.findViewById(R.id.friendid);

        }
    }
}
