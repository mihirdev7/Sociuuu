package com.example.socialapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.socialapp.fragments.Notification2;
import com.example.socialapp.fragments.Notification2view;

public class vwpagerAdapterNotification extends FragmentStateAdapter {


    public vwpagerAdapterNotification(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Notification2();
            case 1:
                return new Notification2view();
            default:
                return new Notification2();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }



}
