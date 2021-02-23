package raphel.bookie.data.room;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_deadline")
public class Deadline {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "book_id")
    public long bookId;

    @Embedded
    public Date date;

    @ColumnInfo(name = "page_to_reach")
    public int pageToReach;
}
