package raphel.bookie.ui.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.time.LocalDate;

import raphel.bookie.R;
import raphel.bookie.data.room.Date;
import raphel.bookie.data.room.Deadline;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.FragmentNewDeadlineBinding;
import raphel.bookie.ui.viewmodel.MainViewModel;

public class NewDeadlineFragment extends Fragment {

    private FragmentNewDeadlineBinding binding;
    private MainViewModel viewModel;

    public static NewDeadlineFragment newInstance(String param1, String param2) {
        return new NewDeadlineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel =  new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewDeadlineBinding.inflate(inflater, container, false);

        binding.newDeadlineBtnAdd.setOnClickListener((View) -> {
            ReadingSession session = viewModel.getSelectedSession().getValue();

            String positionText = binding.newDeadlineInputPosition.getText().toString();
            if (positionText.isEmpty()) return;
            int position = Integer.parseInt(positionText);
            if (position <= session.book.userPosition
                    && position >= session.book.length) return;

            int day = binding.newDeadlineDatePicker.getDayOfMonth();
            int month = binding.newDeadlineDatePicker.getMonth()+1;
            int year = binding.newDeadlineDatePicker.getYear();

            LocalDate today = LocalDate.now();
            LocalDate deadlineDate = LocalDate.of(year, month, day);
            if (deadlineDate.isBefore(today)) return;

            Deadline deadline = new Deadline();
            deadline.bookId = session.book.id;
            deadline.date = new Date();
            deadline.date.day = day;
            deadline.date.month = month;
            deadline.date.year = year;
            deadline.pageToReach = position;
            viewModel.addDeadline(session, deadline);

            hideKeyboard();
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_newDeadlineFragment_to_listItemFragment);
        });

        return binding.getRoot();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getContext());
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}