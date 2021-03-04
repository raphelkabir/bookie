package raphel.bookie.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.FragmentListBinding;
import raphel.bookie.ui.controls.ListRecyclerAdapter;
import raphel.bookie.ui.viewmodel.SharedViewModel;
import raphel.bookie.R;

public class ListFragment extends Fragment implements ListRecyclerAdapter.ItemListener {

    private HostNavigation hostNavigation;

    private FragmentListBinding binding;
    private SharedViewModel viewModel;
    private ListRecyclerAdapter recyclerAdapter;

    public static ListFragment newInstance(String param1, String param2) {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new ListRecyclerAdapter();
        recyclerAdapter.setItemListener(this);

        viewModel =  new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSessions().observe(requireActivity(),
                sessions -> recyclerAdapter.submitData(sessions));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listRecyView.setLayoutManager(layout);
        binding.listRecyView.setAdapter(recyclerAdapter);

        binding.listBtnAdd.setOnClickListener((View) -> {
            Navigation.findNavController(getView()).navigate(R.id.newBookFragment);
        });

        return binding.getRoot();
    }
    
    @Override
    public void itemClicked(int position) {
        viewModel.getSelectedIndex().setValue(position);
        if (hostNavigation != null) hostNavigation.navigateTo(2);
    }

    public void setHostNavigation(HostNavigation hostNavigation) {
        this.hostNavigation = hostNavigation;
    }
}