package raphel.bookie.data.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    public boolean areItemsTheSame(@Nullable Object obj) {
        Book book = (Book) obj;
        if (book == null) return false;

        return this.id == book.id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Book book = (Book) obj;
        if (book == null) return false;

        if (this.title.equals(book.title)
                && this.userPosition == book.userPosition
                && this.length == book.length) return true;

        return  false;
    }
}
