package raphel.bookie.data.room;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_book")
public class Book {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "book_title")
    public String title;

    @ColumnInfo(name = "book_length")
    public int length;

    @ColumnInfo(name = "book_user_position")
    public int userPosition;
}
