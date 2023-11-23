package com.example.socialapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.socialapp.R;
import com.example.socialapp.adapters.NotifyAdapterNotification;
import com.example.socialapp.models.NotificationNotifyModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Notification2 extends Fragment {
RecyclerView recynotify;
ArrayList<NotificationNotifyModel> list;
FirebaseDatabase database;

    public Notification2() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view= inflater.inflate(R.layout.fragment_notification2, container, false);
        recynotify=view.findViewById(R.id.recynotify);
        database=FirebaseDatabase.getInstance();
        list=new ArrayList<>();

        NotifyAdapterNotification adapter=new NotifyAdapterNotification(list,getContext());
        LinearLayoutManager manager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recynotify.setLayoutManager(manager);
        recynotify.setNestedScrollingEnabled(false);
        recynotify.setAdapter(adapter);

        database.getReference().child("notification").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            NotificationNotifyModel n=dataSnapshot.getValue(NotificationNotifyModel.class);
                            n.setNotificationID(dataSnapshot.getKey());
                            list.add(n);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }
}