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
import com.example.socialapp.adapters.userListAdapterSearch;
import com.example.socialapp.models.authInfoSignInModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class searchh extends Fragment {
ArrayList<authInfoSignInModel> list=new ArrayList<>();
private FirebaseAuth auth;
private FirebaseDatabase database;
RecyclerView recysearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_searchh, container, false);
        recysearch=view.findViewById(R.id.recysearch);
        userListAdapterSearch adapter=new userListAdapterSearch(list,getContext());
        LinearLayoutManager lm=new LinearLayoutManager(getContext());
        recysearch.setLayoutManager(lm);
        recysearch.setAdapter(adapter);
        database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot datasnapshot:snapshot.getChildren())
                    {authInfoSignInModel m=datasnapshot.getValue(authInfoSignInModel.class);
                        m.setUserID(datasnapshot.getKey());
                        if (!datasnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                            list.add(m);
                        }
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