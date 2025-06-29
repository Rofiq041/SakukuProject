package com.example.sakuku;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class YourPagerAdapter extends FragmentStateAdapter {

    public YourPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a new fragment instance for the given position
        switch (position) {
            case 0:
                return new PlaceholderFragment(); // For "Struk"
            case 1:
                return new PlaceholderFragment(); // For "Tagihan"
            case 2:
                return new PlaceholderFragment(); // For "QR Code"
            default:
                return new PlaceholderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs: Struk, Tagihan, QR Code
    }
}