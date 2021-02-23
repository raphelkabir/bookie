package raphel.bookie.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raphel.bookie.R;
import raphel.bookie.databinding.FragmentInfoBinding;

public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;

    public static InfoFragment newInstance(String param1, String param2) {
        return new InfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater);

        binding.infoTextView3.setMovementMethod(LinkMovementMethod.getInstance());
        binding.infoTextView4.setMovementMethod(LinkMovementMethod.getInstance());
        binding.infoTextView5.setMovementMethod(LinkMovementMethod.getInstance());
        binding.infoTextView6.setMovementMethod(LinkMovementMethod.getInstance());
        binding.infoTextView7.setMovementMethod(LinkMovementMethod.getInstance());

        return binding.getRoot();
    }
}