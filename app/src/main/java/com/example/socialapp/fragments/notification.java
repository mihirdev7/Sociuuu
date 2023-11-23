package com.example.socialapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.socialapp.R;
import com.example.socialapp.adapters.vwpagerAdapterNotification;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class notification extends Fragment {
TabLayout tabLayout;
ViewPager2 vwpgr;

    public notification() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        tabLayout=view.findViewById(R.id.tablyout);
        vwpgr=view.findViewById(R.id.vwpgr);
        vwpagerAdapterNotification m= new vwpagerAdapterNotification(getActivity());
        vwpgr.setAdapter(m);

        new TabLayoutMediator(tabLayout,vwpgr,(tab, position) ->{
            switch (position) {
                case 0:
                    tab.setText("Notification");
                    break;
                case 1:
                    tab.setText("Follow");
                    break;
            }
        }).attach();

        return view;
    }
}