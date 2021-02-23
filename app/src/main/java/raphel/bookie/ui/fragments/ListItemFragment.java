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

import raphel.bookie.R;
import raphel.bookie.data.room.Deadline;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.FragmentListItemBinding;
import raphel.bookie.ui.controls.ListItemRecyclerAdapter;
import raphel.bookie.ui.viewmodel.MainViewModel;

public class ListItemFragment extends Fragment implements ListItemRecyclerAdapter.ItemListener {

    private FragmentListItemBinding binding;
    private ListItemRecyclerAdapter recyclerAdapter;
    private MainViewModel viewModel;

    public static ListItemFragment newInstance(String param1, String param2) {
        return new ListItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel =  new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.getSelectedSession().observe(requireActivity(), session -> {
            if (recyclerAdapter != null) recyclerAdapter.setData(session);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListItemBinding.inflate(inflater, container, false);

        ReadingSession session = viewModel.getSelectedSession().getValue();

        binding.listItmDelete.setOnClickListener((View) -> {
            viewModel.deleteReadingSession(session);
            Navigation.findNavController(getView()).navigate(R.id.action_listItemFragment_to_hostFragment);
        });

        binding.listItmTxtTitle.setText(session.book.title);
        if (session.book.userPosition == 0) binding.listItmIcon.setImageResource(R.drawable.ic_book);
        binding.listItmFirstPargph.setText(String.valueOf(session.book.length));
        binding.listItmSecondPargph.setText(String.valueOf(session.book.userPosition));

        binding.listItmBtnAdd.setOnClickListener((View) -> {
            Navigation.findNavController(getView()).navigate(R.id.newDeadlineFragment);
        });


        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listItmRecyclerView.setLayoutManager(layout);
        recyclerAdapter = new ListItemRecyclerAdapter();
        recyclerAdapter.setItemListener(this);
        recyclerAdapter.setData(viewModel.getSelectedSession().getValue());
        binding.listItmRecyclerView.setAdapter(recyclerAdapter);

        return binding.getRoot();
    }

    @Override
    public void delete(ReadingSession session, Deadline deadline) {
        viewModel.deleteDeadline(session, deadline);
    }
}