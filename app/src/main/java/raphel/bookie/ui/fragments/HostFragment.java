package raphel.bookie.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import raphel.bookie.R;
import raphel.bookie.databinding.FragmentHostBinding;
import raphel.bookie.ui.controls.HostPagerAdapter;

public class HostFragment extends Fragment {

    private Holder holder;
    private FragmentHostBinding binding;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHostBinding.inflate(inflater, container, false);

        Holder holder = new Holder();

        HostPagerAdapter pagerAdapter = new HostPagerAdapter(getActivity());
        binding.mainViewPager.setAdapter(pagerAdapter);
        binding.mainViewPager.registerOnPageChangeCallback(holder);
        binding.mainBottomMenu.setOnNavigationItemSelectedListener(holder);

        if (index > 0) {
            binding.mainViewPager.setCurrentItem(index, false);
        }

        return binding.getRoot();
    }

    class Holder extends ViewPager2.OnPageChangeCallback implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_today:
                    binding.mainViewPager.setCurrentItem(0);
                    return true;

                case R.id.menu_books:
                    binding.mainViewPager.setCurrentItem(1);
                    return true;
                case R.id.menu_info:
                    binding.mainViewPager.setCurrentItem(2);
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
                    binding.mainBottomMenu.setSelectedItemId(R.id.menu_info);
                    break;
            }
        }
    }
}