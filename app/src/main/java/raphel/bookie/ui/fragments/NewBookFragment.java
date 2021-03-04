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

import raphel.bookie.data.room.Book;
import raphel.bookie.databinding.FragmentNewBookBinding;
import raphel.bookie.ui.viewmodel.SharedViewModel;
import raphel.bookie.R;

public class NewBookFragment extends Fragment implements View.OnClickListener {

    private FragmentNewBookBinding binding;
    private SharedViewModel viewModel;

    public static NewBookFragment newInstance(String param1, String param2) {
        return new NewBookFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel =  new ViewModelProvider(this).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewBookBinding.inflate(inflater);
        binding.newBookBtnAdd.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        String positionText = binding.newBookInputPosition.getText().toString();
        if (positionText.isEmpty()) return;
        int position = Integer.parseInt(positionText);

        String lengthText = binding.newBookInputLength.getText().toString();
        if (lengthText.isEmpty()) return;;
        int length = Integer.parseInt(lengthText);

        if (position < 0|| length < 0 || position > length) return;

        String title = binding.newBookInputTitle.getEditText().getText().toString();
        if (title.isEmpty()) return;;

        Book book = new Book();
        book.title = title;
        book.userPosition = position;
        book.length = length;
        viewModel.addBook(book);

        hideKeyboard();
        NavController navController = Navigation.findNavController(getView());
        Bundle bundle = new Bundle();
        bundle.putInt("index", 1);
        navController.navigate(R.id.action_newBookFragment_to_hostFragment, bundle);
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getContext());
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}