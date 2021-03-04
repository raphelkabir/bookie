package raphel.bookie.ui.controls;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import raphel.bookie.R;
import raphel.bookie.data.room.Book;
import raphel.bookie.data.room.Deadline;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.ItemBookBinding;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {

    private final AsyncListDiffer<ReadingSession> listDiffer = new AsyncListDiffer(this, new DiffUtil.ItemCallback<ReadingSession>() {
        @Override
        public boolean areItemsTheSame(@NonNull ReadingSession oldItem, @NonNull ReadingSession newItem) {
            return oldItem.areItemsTheSame(newItem);
        }
        @Override
        public boolean areContentsTheSame(@NonNull ReadingSession oldItem, @NonNull ReadingSession newItem) {
            return oldItem.equals(newItem);
        }
    });
    private ItemListener itemListener;

    @NonNull
    @Override
    public ListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = ItemBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecyclerAdapter.ViewHolder holder, int position) {
        ReadingSession session = listDiffer.getCurrentList().get(position);

        holder.binding.itmbkTitle.setText(session.book.title);
        holder.binding.listItemFirstParagraph.setText(String.valueOf(session.book.length));
        holder.binding.listItemSecondParagraph.setText(String.valueOf(session.book.userPosition));

        holder.itemView.setOnClickListener((View) -> {
            if (itemListener != null) itemListener.itemClicked(holder.getAdapterPosition());
        });;
    }

    @Override
    public int getItemCount() {
        return listDiffer.getCurrentList().size();
    }

    public void submitData(List<ReadingSession> data) {
        listDiffer.submitList(data);
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ItemBookBinding binding;

        public ViewHolder(@NonNull ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemListener {
        void itemClicked(int position);
    }
}
