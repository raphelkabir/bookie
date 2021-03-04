package raphel.bookie.data.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_daily_target")
public class DailyTarget {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "book_id")
    public long bookId;

    @Embedded
    public Date validFor;

    @ColumnInfo(name = "page_to_reach")
    public int pageToReach;

    @ColumnInfo(name = "page_reached")
    public int pageReached;

    public boolean areItemsTheSame(@Nullable Object obj) {
        DailyTarget dailyTarget = (DailyTarget) obj;
        if (dailyTarget == null) return false;

        return this.id == dailyTarget.id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        DailyTarget dailyTarget = (DailyTarget) obj;
        if (dailyTarget == null) return false;

        if (this.bookId == dailyTarget.bookId
                && this.validFor.equals(dailyTarget.validFor)
                && this.pageToReach == dailyTarget.pageToReach
                && this.pageReached == dailyTarget.pageReached) return true;

        return false;
    }
}
