package raphel.bookie.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import raphel.bookie.R;
import raphel.bookie.databinding.FragmentHostBinding;
import raphel.bookie.ui.controls.HostPagerAdapter;

public class HostFragment extends Fragment implements HostNavigation {

    private NavigationUIListener navigationUIListener;
    private FragmentHostBinding binding;
    private HostPagerAdapter pagerAdapter;

    private int index;

    public static HostFragment newInstance(String param1, String param2) {
        return new HostFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt("index");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHostBinding.inflate(inflater);

        pagerAdapter = new HostPagerAdapter(getActivity());
        pagerAdapter.setHostNavigation(this);
        binding.mainViewPager.setAdapter(pagerAdapter);

        navigationUIListener = new NavigationUIListener();
        binding.mainViewPager.registerOnPageChangeCallback(navigationUIListener);
        binding.mainBottomMenu.setOnNavigationItemSelectedListener(navigationUIListener);

        if (index > 0) {
            binding.mainViewPager.setCurrentItem(index, false);
        }

        return binding.getRoot();
    }

    @Override
    public void navigateTo(int index) {
        if (index < 0 || index > 2) throw new AssertionError("Out of bounds");
        binding.mainViewPager.setCurrentItem(index);
        if (index == 2) deselectAll();
    }

    public void deselectAll() {
        binding.mainBottomMenu.getMenu().setGroupCheckable(0, true, false);
        binding.mainBottomMenu.getMenu().getItem(0).setChecked(false);
        binding.mainBottomMenu.getMenu().getItem(1).setChecked(false);
        binding.mainBottomMenu.getMenu().setGroupCheckable(0, true, true);
    }

    class NavigationUIListener extends ViewPager2.OnPageChangeCallback implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_today:
                    binding.mainViewPager.setCurrentItem(0);
                    return true;

                case R.id.menu_books:
                    binding.mainViewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    binding.mainBottomMenu.setSelectedItemId(R.id.menu_today);
                    break;

                case 1:
                    binding.mainBottomMenu.setSelectedItemId(R.id.menu_books);
                    break;
                case 2:
                    deselectAll();
                    break;
            }
        }
    }
}