package com.example.socialapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.socialapp.fragments.home;
import com.example.socialapp.fragments.notification;
import com.example.socialapp.fragments.post;
import com.example.socialapp.fragments.profile;
import com.example.socialapp.fragments.searchh;

public class vwpagerAdapter extends FragmentStateAdapter {


    public vwpagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return new home();
        }else if(position==1){
            return new searchh();
        }else if(position==2){
            return new post();
        }else if(position==3){
            return new notification();
        }else{
            return new profile();
        }

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
