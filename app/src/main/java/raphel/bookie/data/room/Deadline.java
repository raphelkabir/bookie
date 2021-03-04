package raphel.bookie.data.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_deadline")
public class Deadline {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "book_id")
    public long bookId;

    @ColumnInfo(name = "name")
    public String name;

    @Embedded
    public Date date;

    @ColumnInfo(name = "page_to_reach")
    public int pageToReach;

    @ColumnInfo(name = "is_active")
    public boolean isActive;

    public boolean areItemsTheSame(@Nullable Object obj) {
        Deadline deadline = (Deadline) obj;
        if (deadline == null) return false;

        return this.id == deadline.id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Deadline deadline = (Deadline) obj;
        if (deadline == null) return false;

        if (this.bookId == deadline.bookId
                && this.name.equals(deadline.name)
                && this.date.equals(deadline.date)
                && this.pageToReach == deadline.pageToReach) return true;

        return  false;
    }
}
