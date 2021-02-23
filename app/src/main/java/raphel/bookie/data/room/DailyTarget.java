package raphel.bookie.data.room;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
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
}
