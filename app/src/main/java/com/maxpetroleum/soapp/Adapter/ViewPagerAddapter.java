package com.maxpetroleum.soapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.maxpetroleum.soapp.Fragments.AcceptedPOFragment;
import com.maxpetroleum.soapp.Fragments.ClosedPOFragment;
import com.maxpetroleum.soapp.Fragments.PendingPOFragment;

public class ViewPagerAddapter extends FragmentPagerAdapter {
    public ViewPagerAddapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PendingPOFragment pendingPOFragment=new PendingPOFragment();
                return pendingPOFragment;
            case 2:
                ClosedPOFragment closedPOFragment=new ClosedPOFragment();
                return closedPOFragment;
            case 1:
                AcceptedPOFragment poFragment=new AcceptedPOFragment();
                return poFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Pending";
            case 2:
                return "Closed";
            case 1:
                return "Accepted";
        }
        return null;
    }
}
