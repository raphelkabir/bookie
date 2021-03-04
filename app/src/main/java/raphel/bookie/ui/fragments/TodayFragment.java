package raphel.bookie.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import raphel.bookie.R;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.FragmentTodayBinding;
import raphel.bookie.databinding.PopupCompleteTargetBinding;
import raphel.bookie.ui.controls.TodayRecyclerAdapter;
import raphel.bookie.ui.viewmodel.SharedViewModel;

public class TodayFragment extends Fragment implements TodayRecyclerAdapter.ItemListener {

    private FragmentTodayBinding binding;
    private SharedViewModel viewModel;
    private TodayRecyclerAdapter recyclerAdapter;

    public static TodayFragment newInstance(String param1, String param2) {
        return new TodayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new TodayRecyclerAdapter();
        viewModel =  new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSessions().observe(requireActivity(), sessions -> {
            recyclerAdapter.submitData(filter(sessions));
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTodayBinding.inflate(inflater);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.todayRecyclerView.setLayoutManager(layout);
        recyclerAdapter.setItemListener(this);
        binding.todayRecyclerView.setAdapter(recyclerAdapter);
        return binding.getRoot();
    }

    @Override
    public void checked(TodayRecyclerAdapter.ViewHolder holder, boolean isChecked) {
        if (isChecked) {
            getActivity().runOnUiThread(() -> {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                PopupCompleteTargetBinding binding = PopupCompleteTargetBinding.inflate(inflater);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(binding.getRoot(), width, height, true);
                binding.targetCompltdBtnSave.setOnClickListener((View) -> {
                    String pageText = binding.targetCompltdInputNmbr.getText().toString();
                    if (pageText.isEmpty()) return;
                    int page = Integer.parseInt(pageText);
                    if (page <= 0 || page > holder.getData().book.length) return;

                    holder.getData().dailyTarget.pageReached = page;
                    viewModel.updateDailyTarget(holder.getData().dailyTarget);
                    popupWindow.dismiss();
                    holder.binding.itmTargetIcon.setImageResource(R.drawable.ic_check);
                });

                popupWindow.setElevation(20);
                popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
                popupWindow.setOnDismissListener(holder::updateVisual);
            });
        }
        else {
            holder.getData().dailyTarget.pageReached = 0;
            viewModel.updateDailyTarget(holder.getData().dailyTarget);
            holder.updateVisual();
        }
    }

    private List<ReadingSession> filter(List<ReadingSession> sessions) {
        List<ReadingSession> filtered = new ArrayList<>();
        for (ReadingSession session : sessions) {
            if (session.dailyTarget != null
                    && session.dailyTarget.pageToReach != 0) {
                filtered.add(session);
            }
        }

        return filtered.size() > 0 ? filtered : null;
    }
}