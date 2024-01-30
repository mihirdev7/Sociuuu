package com.example.socialapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.models.NotificationNotifyModel;
import com.example.socialapp.models.authInfoSignInModel;
import com.example.socialapp.models.profileFriendListModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class userListAdapterSearch extends RecyclerView.Adapter<userListAdapterSearch.viewHolder> {
    ArrayList<authInfoSignInModel> list;
    Context context;

    public userListAdapterSearch(ArrayList<authInfoSignInModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_list_search_design,parent,false);
        return new viewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        authInfoSignInModel model=list.get(position);
        Picasso.get().load(model.getProfilePhoto())
                .placeholder(R.drawable.profileicon).into(holder.imageView3);
        holder.textView17.setText(model.getName());

        //Below code is for following btn set, check if user have any followers if yes then set following btn or set follow btn
        FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(model.getUserID()).child("followers")
                        .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.followbtn.setBackgroundColor(context.getColor(R.color.white));
                            holder.followbtn.setText("Following");
                            holder.followbtn.setTextColor(context.getColor(R.color.black));
                            holder.followbtn.setEnabled(false);
                        } else {
                            holder.followbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    profileFriendListModel mode=new profileFriendListModel();
                                    mode.setFollowdedBy(FirebaseAuth.getInstance().getUid());
                                    mode.setTime(new Date().getTime());
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users").child(model.getUserID()).child("followers")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(mode).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Users").child(model.getUserID()).child("FollowerCount")
                                                            .setValue(model.getFollowerCount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(context, "You followed "+model.getName(), Toast.LENGTH_SHORT).show();
                                                                    holder.followbtn.setBackgroundColor(context.getColor(R.color.white));
                                                                    holder.followbtn.setText("Following");
                                                                    holder.followbtn.setTextColor(context.getColor(R.color.black));
                                                                    holder.followbtn.setEnabled(false);

                                                                    NotificationNotifyModel n=new NotificationNotifyModel();
                                                                    n.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    n.setNotificationAt(new Date().getTime());
                                                                    n.setType("Follow");
                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification").child(model.getUserID())
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


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView3;
        TextView textView17;
        Button followbtn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView3=itemView.findViewById(R.id.imageView3);
            textView17=itemView.findViewById(R.id.textView17);
            followbtn=itemView.findViewById(R.id.followbtn);


        }
    }
}
