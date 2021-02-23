package raphel.bookie.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.FragmentListBinding;
import raphel.bookie.ui.controls.ListRecyclerAdapter;
import raphel.bookie.ui.viewmodel.MainViewModel;
import raphel.bookie.R;

public class ListFragment extends Fragment implements ListRecyclerAdapter.ItemListener {

    private FragmentListBinding binding;
    private MainViewModel viewModel;
    private ListRecyclerAdapter recyclerAdapter;

    public static ListFragment newInstance(String param1, String param2) {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel =  new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.getSessions().observe(requireActivity(), sessions -> {
            if (recyclerAdapter != null) recyclerAdapter.setData(sessions);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listRecyView.setLayoutManager(layout);

        recyclerAdapter = new ListRecyclerAdapter();
        recyclerAdapter.setItemListener(this);
        recyclerAdapter.setData(viewModel.getSessions().getValue());
        binding.listRecyView.setAdapter(recyclerAdapter);

        binding.listBtnAdd.setOnClickListener((View) -> {
            Navigation.findNavController(getView()).navigate(R.id.newBookFragment);
        });

        return binding.getRoot();
    }

    @Override
    public void itemClicked(ReadingSession session) {
        viewModel.getSelectedSession().setValue(session);
        Navigation.findNavController(getView()).navigate(R.id.listItemFragment);
    }
}