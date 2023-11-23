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
import com.example.socialapp.models.UserStoryModel;
import com.example.socialapp.models.authInfoSignInModel;
import com.example.socialapp.models.storyModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class storyAdapterHome extends RecyclerView.Adapter<storyAdapterHome.viewHolder> {

    ArrayList<storyModel> list;
    Context context;

    public storyAdapterHome(ArrayList list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.story_layout_design,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        storyModel model= list.get(position);
        if(model.getList().size()>0) {

            UserStoryModel laststory = model.getList().get(model.getList().size() - 1);
            Picasso.get().load(laststory.getImage()).into(holder.imageView2);
            FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(model.getStoryBy()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            authInfoSignInModel user = snapshot.getValue(authInfoSignInModel.class);
                            Picasso.get().load(user.getProfilePhoto()).into(holder.profidStory);
                            holder.textView4.setText(user.getName());

                            holder.imageView2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView2,profidStory,liveid;
        TextView textView4;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView2=itemView.findViewById(R.id.imageView2);
            profidStory=itemView.findViewById(R.id.profidStory);
            liveid=itemView.findViewById(R.id.liveid);
            textView4=itemView.findViewById(R.id.textView4);
        }
    }
}
