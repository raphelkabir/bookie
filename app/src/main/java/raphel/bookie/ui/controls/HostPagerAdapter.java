package raphel.bookie.ui.controls;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import raphel.bookie.ui.fragments.HostNavigation;
import raphel.bookie.ui.fragments.ListFragment;
import raphel.bookie.ui.fragments.ListItemFragment;
import raphel.bookie.ui.fragments.TodayFragment;

public class HostPagerAdapter extends FragmentStateAdapter {

    private HostNavigation hostNavigation;

    public HostPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                TodayFragment todayFragment = new TodayFragment();
                return todayFragment;

            case 1:
                ListFragment listFragment = new ListFragment();
                listFragment.setHostNavigation(hostNavigation);
                return listFragment;

            case 2:
                ListItemFragment listItemFragment = new ListItemFragment();
                listItemFragment.setHostNavigation(hostNavigation);
                return listItemFragment;
        }

        throw new AssertionError("Unexpected position");
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setHostNavigation(HostNavigation hostNavigation) {
        this.hostNavigation = hostNavigation;
    }
}
