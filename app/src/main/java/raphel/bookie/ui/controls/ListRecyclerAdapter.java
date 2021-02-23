package raphel.bookie.ui.controls;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import raphel.bookie.R;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.ItemBookBinding;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {

    private List<ReadingSession> data;
    private ItemListener itemListener;

    @NonNull
    @Override
    public ListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = ItemBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecyclerAdapter.ViewHolder holder, int position) {
        ReadingSession session = data.get(position);
        holder.binding.itmbkTitle.setText(session.book.title);
        if (session.book.userPosition == 0
                || session.book.userPosition == session.book.length) holder.binding.itemBookIcon.setImageResource(R.drawable.ic_book);
        holder.binding.listItmFirstPargph.setText(String.valueOf(session.book.length));
        holder.binding.listItmSecondPargph.setText(String.valueOf(session.book.userPosition));

        holder.itemView.setOnClickListener((View) -> {
            if (itemListener != null) itemListener.itemClicked(session);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<ReadingSession> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemBookBinding binding;

        public ViewHolder(@NonNull ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemListener {
        void itemClicked(ReadingSession session);
    }
}
