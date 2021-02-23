package raphel.bookie.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raphel.bookie.R;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.FragmentTargetCompletedBinding;
import raphel.bookie.ui.viewmodel.MainViewModel;

public class TargetCompletedFragment extends Fragment {

    private FragmentTargetCompletedBinding binding;
    private MainViewModel viewModel;

    public static TargetCompletedFragment newInstance(String param1, String param2) {
        return new TargetCompletedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel =  new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTargetCompletedBinding.inflate(inflater, container, false);
        ReadingSession session = viewModel.getSelectedSession().getValue();

        binding.targetCompltdTxtBookName.setText(session.book.title);
        binding.targetCompltdFirstPargph.setText(String.valueOf(session.book.length));
        binding.targetCompltdSecondPargph.setText(String.valueOf(session.book.userPosition));

        binding.targetCompltdBtnSave.setOnClickListener((View) -> {
            String pageText = binding.targetCompltdInputNmbr.getText().toString();
            if (pageText.isEmpty()) return;
            int pageReached = Integer.parseInt(pageText);
            if (pageReached == 0 || pageReached > session.book.length) return;

            session.dailyTarget.pageReached = pageReached;
            //viewModel.getRepository().updateDailyTarget(session.dailyTarget);

            getActivity().runOnUiThread(() -> {
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_targetCompletedFragment_to_todayFragment);
            });
        });

        return binding.getRoot();
    }
}