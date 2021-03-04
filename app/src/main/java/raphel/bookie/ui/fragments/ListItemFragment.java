package raphel.bookie.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import raphel.bookie.R;
import raphel.bookie.data.room.Deadline;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.FragmentListItemBinding;
import raphel.bookie.ui.controls.ListItemRecyclerAdapter;
import raphel.bookie.ui.viewmodel.SharedViewModel;

public class ListItemFragment extends Fragment implements ListItemRecyclerAdapter.ItemListener {

    private HostNavigation hostNavigation;

    private FragmentListItemBinding binding;
    private ListItemRecyclerAdapter recyclerAdapter;
    private SharedViewModel viewModel;

    public String title;

    public static ListItemFragment newInstance(String param1, String param2) {
        return new ListItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentListItemBinding.inflate(getLayoutInflater());

        recyclerAdapter = new ListItemRecyclerAdapter();
        recyclerAdapter.setItemListener(this);

        viewModel =  new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSessions().observe(requireActivity(), sessions -> updateDataUI());
        viewModel.getSelectedIndex().observe(requireActivity(), index -> updateDataUI());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listItmRecyclerView.setLayoutManager(layout);
        binding.listItmRecyclerView.setAdapter(recyclerAdapter);

        binding.listItmDelete.setOnClickListener((View) -> {
            binding.listItmContentLayout.setVisibility(View.INVISIBLE);
            binding.listItmStandbyText.setVisibility(View.VISIBLE);

            viewModel.deleteReadingSession(viewModel.getSelected());
            if (hostNavigation != null) hostNavigation.navigateTo(1);
        });

        binding.listItmBtnAdd.setOnClickListener((View)
                -> Navigation.findNavController(getView()).navigate(R.id.newDeadlineFragment));

        return binding.getRoot();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, R.id.menu_delete, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                Deadline deadline = recyclerAdapter.getLongPressedDeadline();
                viewModel.deleteDeadline(viewModel.getSelected(), deadline);
                return true;

            default:
                return false;
        }
    }

    @Override
    public void itemCreated(View view) {
        registerForContextMenu(view);
    }

    @Override
    public void itemSwitchChanged(Deadline deadline) {
        viewModel.updateDeadline(viewModel.getSelected(), deadline);
    }

    public void setHostNavigation(HostNavigation hostNavigation) {
        this.hostNavigation = hostNavigation;
    }

    public void showData(boolean show) {
        if (show) {
            binding.listItmContentLayout.setVisibility(View.VISIBLE);
            binding.listItmStandbyText.setVisibility(View.INVISIBLE);
        }
        else {
            binding.listItmContentLayout.setVisibility(View.INVISIBLE);
            binding.listItmStandbyText.setVisibility(View.VISIBLE);
        }
    }

    public void updateDataUI() {
        ReadingSession session = viewModel.getSelected();
        if (session == null) {
            showData(false);
            return;
        }

        binding.listItmTxtTitle.setText(session.book.title);
        binding.listItemFirstParagraph.setText(String.valueOf(session.book.length));
        binding.listItemSecondParagraph.setText(String.valueOf(session.book.userPosition));
        recyclerAdapter.submitData(session.deadlines);
        showData(true);
    }
}