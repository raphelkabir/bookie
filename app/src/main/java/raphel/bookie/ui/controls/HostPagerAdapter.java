package raphel.bookie.ui.controls;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import raphel.bookie.ui.fragments.InfoFragment;
import raphel.bookie.ui.fragments.ListFragment;
import raphel.bookie.ui.fragments.TodayFragment;

public class HostPagerAdapter extends FragmentStateAdapter {

    public HostPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TodayFragment();
            case 1:
                return new ListFragment();
            case 2:
                return new InfoFragment();
        }

        throw new AssertionError("Unexpected position");
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
